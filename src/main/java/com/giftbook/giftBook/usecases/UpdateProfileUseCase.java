package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.entities.UserAuthentication;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.UserAuthenticationRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.requests.UpdateProfileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateProfileUseCase {
    private static final Logger log = LoggerFactory.getLogger(UpdateProfileUseCase.class);

    private final UserRepository userRepository;
    private final UserAuthenticationRepository authRepository;
    private final UpdateProfileRequest request;
    private final String authenticatedEmail;

    public UpdateProfileUseCase(UserRepository userRepository,
                                UserAuthenticationRepository authRepository,
                                UpdateProfileRequest request,
                                String authenticatedEmail
    ) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.request = request;
        this.authenticatedEmail = authenticatedEmail;
    }

    public String execute() throws EntityNotFoundException {
        User user = userRepository.findByEmail(authenticatedEmail);

        if (user == null) {
            log.error("User not found for email: {}", authenticatedEmail);
            throw new EntityNotFoundException("User not found");
        }

        user.setFullName(request.getName());
        user.setAddress(request.getAddress());
        user.setDistrict(request.getDistrict());
        user.setContact(request.getContact());

        if (!request.getEmail().equals(authenticatedEmail)) {
            UserAuthentication userAuthentication = authRepository.findByEmail(authenticatedEmail);
            userAuthentication.setEmail(request.getEmail());
            authRepository.save(userAuthentication);

            user.setEmail(request.getEmail());
            log.info("User updated with new email address: {}", request.getEmail());
        }

        userRepository.save(user);

        return "Profile updated successfully";
    }
}
