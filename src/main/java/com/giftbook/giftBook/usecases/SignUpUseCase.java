package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.RegisterException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.requests.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SignUpUseCase {
    private static final Logger log = LoggerFactory.getLogger(SignUpUseCase.class);

    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationRepository authRepository;
    private final UserRepository userRepository;
    private final SignUpRequest request;

    public SignUpUseCase(PasswordEncoder passwordEncoder,
                         UserAuthenticationRepository authRepository,
                         UserRepository userRepository,
                         SignUpRequest request
    ) {
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.request = request;
    }

    public String execute() throws RegisterException {
        UserAuthentication userAuthentication = authRepository.findByEmail(request.getEmail());

        if (userAuthentication != null) {
            log.error("User Already exist with same email: {}", request.getEmail());
            throw new RegisterException("User already exist with same email");
        }

        User user = User
                .builder()
                .fullName(request.getName())
                .email(request.getEmail())
                .build();

        userRepository.save(user);

        UserAuthentication newEntity = UserAuthentication
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .loginAttempts(0)
                .passwordResetKey(null)
                .user(user)
                .build();

        authRepository.save(newEntity);

        return "User Registered successfully";
    }
}
