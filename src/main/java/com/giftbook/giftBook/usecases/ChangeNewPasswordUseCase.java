package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.exceptions.MismatchException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import com.giftbook.giftBook.requests.ChangeNewPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ChangeNewPasswordUseCase {
    private static final Logger log = LoggerFactory.getLogger(ChangeNewPasswordUseCase.class);

    private final UserAuthenticationRepository authRepository;
    private final ChangeNewPasswordRequest request;
    private final PasswordEncoder passwordEncoder;

    public ChangeNewPasswordUseCase(UserAuthenticationRepository authRepository,
                                    ChangeNewPasswordRequest request,
                                    PasswordEncoder passwordEncoder
    ) {
        this.authRepository = authRepository;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
    }

    public String execute() throws EntityNotFoundException, MismatchException {
        UserAuthentication userAuthentication = authRepository.findByEmail(request.getEmail());

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", request.getEmail());
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        if (passwordEncoder.matches(request.getOldPassword(), userAuthentication.getPassword())) {
            userAuthentication.setPassword(passwordEncoder.encode(request.getNewPassword()));
            authRepository.save(userAuthentication);
        } else {
            log.error("Incorrect old Password");
            throw new MismatchException("Incorrect old Password");
        }

        return "Password changed successfully";
    }
}
