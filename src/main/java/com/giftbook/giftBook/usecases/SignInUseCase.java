package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.exceptions.UserLoginException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import com.giftbook.giftBook.requests.SignInRequest;
import com.giftbook.giftBook.responses.AuthenticationResponse;
import com.giftbook.giftBook.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SignInUseCase {
    private static final Logger log = LoggerFactory.getLogger(SignInUseCase.class);
    private final UserAuthenticationRepository authRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final SignInRequest request;

    public SignInUseCase(UserAuthenticationRepository authRepository,
                         JwtUtil jwtUtil,
                         AuthenticationManager authenticationManager,
                         SignInRequest request) {
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.request = request;
    }

    public AuthenticationResponse execute(int expiration) throws EntityNotFoundException, UserLoginException {
        UserAuthentication userAuthentication = authRepository.findByEmail(request.getEmail());

        if (userAuthentication == null) {
            log.error("User not found for email: {}", request.getEmail());
            throw new EntityNotFoundException("Incorrect email or password, please try again");
        }

        int tries = userAuthentication.getLoginAttempts();

        try {
            if (tries < 5) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtil.generate(authentication);
                return new AuthenticationResponse(
                        request.getEmail(),
                        userAuthentication.getUser().getFullName(),
                        jwt,
                        expiration
                );
            } else {
                throw new UserLoginException("Account blocked, cause:: 5 failure login attempts");
            }
        } catch (AuthenticationException e) {
            if (tries < 5) {
                userAuthentication.setLoginAttempts(tries + 1);
                authRepository.save(userAuthentication);
            }
            log.error("Incorrect email or password, login attempts: {}, for email: {}", tries, request.getEmail());
            throw new UserLoginException("Incorrect email or password, please try again");
        }
    }
}
