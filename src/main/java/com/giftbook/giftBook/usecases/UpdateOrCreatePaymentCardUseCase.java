package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.PaymentCard;
import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.PaymentCardRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import com.giftbook.giftBook.requests.UpdatePaymentCardRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateOrCreatePaymentCardUseCase {
    private static final Logger log = LoggerFactory.getLogger(GetProfileDetailsUseCase.class);

    private final UserRepository userRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final UpdatePaymentCardRequest request;

    public UpdateOrCreatePaymentCardUseCase(UserRepository userRepository,
                                            PaymentCardRepository paymentCardRepository,
                                            UpdatePaymentCardRequest request
    ) {
        this.userRepository = userRepository;
        this.request = request;
        this.paymentCardRepository = paymentCardRepository;
    }

    public String execute() throws EntityNotFoundException {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            log.error("User not found for email: {}", request.getEmail());
            throw new EntityNotFoundException("User not found");
        }

        PaymentCard paymentCard = paymentCardRepository.findByUser(user);

        if (paymentCard == null) {
            PaymentCard newPayment = PaymentCard
                    .builder()
                    .cardType(request.getCardType())
                    .cardNo(request.getCardNo())
                    .user(user)
                    .build();

            paymentCardRepository.save(newPayment);
        } else {
            paymentCard.setCardType(request.getCardType());
            paymentCard.setCardNo(request.getCardNo());

            paymentCardRepository.save(paymentCard);
        }

        return "Payment card updated successfully";
    }
}
