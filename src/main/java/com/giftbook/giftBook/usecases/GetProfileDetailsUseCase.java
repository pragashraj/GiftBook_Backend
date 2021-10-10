package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.PaymentCard;
import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.PaymentCardRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.responses.GetProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetProfileDetailsUseCase {
    private static final Logger log = LoggerFactory.getLogger(GetProfileDetailsUseCase.class);

    private final UserRepository userRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final String email;

    public GetProfileDetailsUseCase(UserRepository userRepository, PaymentCardRepository paymentCardRepository, String email) {
        this.userRepository = userRepository;
        this.email = email;
        this.paymentCardRepository = paymentCardRepository;
    }

    public GetProfileResponse execute() throws EntityNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User not found for email: {}", email);
            throw new EntityNotFoundException("User not found");
        }

        GetProfileResponse getProfileResponse = GetProfileResponse
                .builder()
                .email(user.getEmail())
                .name(user.getFullName())
                .address(user.getAddress())
                .contact(user.getContact())
                .district(user.getDistrict())
                .build();

        PaymentCard paymentCard = paymentCardRepository.findByUser(user);

        if (paymentCard != null) {
            getProfileResponse.setCardType(paymentCard.getCardType());
            getProfileResponse.setCardNo(paymentCard.getCardNo());
        }

        log.info("User profile for email: {}:: name: {}, address: {}, contact: {}",
                user.getEmail(),
                user.getFullName(),
                user.getAddress(),
                user.getContact()
        );

        return getProfileResponse;
    }
}
