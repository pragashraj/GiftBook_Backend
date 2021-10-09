package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.*;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.repositories.*;
import com.giftbook.giftBook.requests.CreateNewPaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class CreateNewPaymentUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateNewPaymentUseCase.class);

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ReceiverRepository receiverRepository;
    private final MerchantRepository merchantRepository;
    private final ItemRepository itemRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final CreateNewPaymentRequest request;

    public CreateNewPaymentUseCase(PaymentRepository paymentRepository,
                                   UserRepository userRepository,
                                   ReceiverRepository receiverRepository,
                                   MerchantRepository merchantRepository,
                                   ItemRepository itemRepository,
                                   PaymentCardRepository paymentCardRepository,
                                   CreateNewPaymentRequest request
    ) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.receiverRepository = receiverRepository;
        this.merchantRepository = merchantRepository;
        this.itemRepository = itemRepository;
        this.paymentCardRepository = paymentCardRepository;
        this.request = request;
    }

    public String execute() throws EntityNotFoundException {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            log.error("User not found");
            throw new EntityNotFoundException("User not found");
        }

        Merchant merchant = merchantRepository.findByName(request.getMerchantName());

        if (merchant == null) {
            log.error("Merchant not found");
            throw new EntityNotFoundException("Merchant not found");
        }

        Item item = itemRepository.findByName(request.getItemName());

        if (item == null) {
            log.error("Item not found");
            throw new EntityNotFoundException("Item not found");
        }

        Receiver receiver = Receiver
                .builder()
                .name(request.getReceiverName())
                .address(request.getReceiverAddress())
                .district(request.getReceiverDistrict())
                .build();

        receiverRepository.save(receiver);

        PaymentCard paymentCard = paymentCardRepository.findByUser(user);

        Payment payment = Payment
                .builder()
                .paymentAt(LocalDateTime.now())
                .value(request.getValue())
                .senderType(request.getSenderType())
                .receiver(receiver)
                .paymentCard(paymentCard)
                .merchant(merchant)
                .item(item)
                .user(user)
                .build();

        paymentRepository.save(payment);

        return "New payment made successfully";
    }
}
