package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ForgotPasswordUseCase {
    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordUseCase.class);
    private final UserAuthenticationRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String password;

    public ForgotPasswordUseCase(UserAuthenticationRepository authRepository,
                                 PasswordEncoder passwordEncoder,
                                 String email,
                                 String password
    ) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.password = password;
    }

    public String execute() throws EntityNotFoundException {
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", email);
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        userAuthentication.setPassword(passwordEncoder.encode(password));
        userAuthentication.setPasswordResetKey(null);
        authRepository.save(userAuthentication);

        return "Password changed successfully";
    }
}
