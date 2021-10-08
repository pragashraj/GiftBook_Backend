package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.exceptions.MismatchException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmResetCodeUseCase {
    private static final Logger log = LoggerFactory.getLogger(ConfirmResetCodeUseCase.class);

    private final UserAuthenticationRepository authRepository;
    private final String code;
    private final String email;

    public ConfirmResetCodeUseCase(UserAuthenticationRepository authRepository, String code, String email) {
        this.authRepository = authRepository;
        this.code = code;
        this.email = email;
    }

    public String execute() throws EntityNotFoundException, MismatchException {
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", email);
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        String passwordResetKey = userAuthentication.getPasswordResetKey();

        if (passwordResetKey != null) {
            if (!passwordResetKey.equals(code)) {
                throw new MismatchException("Incorrect reset code, please try again");
            }
        } else {
            log.error("Password reset key not found for email address : {}", email);
            throw new EntityNotFoundException("Password reset key not found");
        }

        return "Reset code is confirmed";
    }
}
