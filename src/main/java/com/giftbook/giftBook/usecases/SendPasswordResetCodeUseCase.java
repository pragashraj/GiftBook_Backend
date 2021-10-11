package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.ContentCreationException;
import com.giftbook.giftBook.exceptions.DispatcherException;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import com.giftbook.giftBook.transport.EmailService;
import com.giftbook.giftBook.transport.templates.ForgotPasswordTemplate;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendPasswordResetCodeUseCase {
    private static final Logger log = LoggerFactory.getLogger(SendPasswordResetCodeUseCase.class);

    private final UserAuthenticationRepository authRepository;
    private final ForgotPasswordTemplate forgotPasswordTemplate;
    private final EmailService emailService;
    private final String email;

    public SendPasswordResetCodeUseCase(UserAuthenticationRepository authRepository,
                                        ForgotPasswordTemplate forgotPasswordTemplate,
                                        EmailService emailService,
                                        String email
    ) {
        this.authRepository = authRepository;
        this.email = email;
        this.forgotPasswordTemplate = forgotPasswordTemplate;
        this.emailService = emailService;
    }

    public String execute() throws EntityNotFoundException, DispatcherException, ContentCreationException {
        UserAuthentication userAuthentication = authRepository.findByEmail(email);

        if (userAuthentication == null) {
            log.error("Incorrect email address : {}", email);
            throw new EntityNotFoundException("User not found, Incorrect email address");
        }

        String passwordResetKey = RandomStringUtils.randomAlphanumeric(6);

        dispatchEmail(userAuthentication.getUser(), email, passwordResetKey);

        userAuthentication.setPasswordResetKey(passwordResetKey);
        authRepository.save(userAuthentication);

        log.info("Password reset code has been sent to email : {} & reset-code: {}", email, passwordResetKey);

        return "Reset code has been sent, please check your email";
    }

    private void dispatchEmail(User user, String email, String key) throws ContentCreationException, DispatcherException {
        String subject = "Password reset Code";
        String content = forgotPasswordTemplate.getContent(
                user.getFullName(),
                key
        );
        emailService.sendEmail(email, subject, content);
    }
}
