package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendPasswordResetCodeUseCase {
    private static final Logger log = LoggerFactory.getLogger(SendPasswordResetCodeUseCase.class);

    private final UserAuthenticationRepository authRepository;
    private final String email;

    public SendPasswordResetCodeUseCase(UserAuthenticationRepository authRepository, String email) {
        this.authRepository = authRepository;
        this.email = email;
    }

    public String execute() throws EntityNotFoundException {
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", email);
            throw new EntityNotFoundException("User not found, please enter correct email address");
        }

        String passwordResetKey = RandomStringUtils.randomAlphanumeric(6);

        userAuthentication.setPasswordResetKey(passwordResetKey);
        authRepository.save(userAuthentication);

        log.info("Password reset code has been sent to email : {} & reset-code: {}", email, passwordResetKey);

        return "Reset code has been sent, please check your email";
    }
}
