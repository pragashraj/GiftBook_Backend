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
    private final VoucherRepository voucherRepository;
    private final SenderRepository senderRepository;
    private final CreateNewPaymentRequest request;

    public CreateNewPaymentUseCase(PaymentRepository paymentRepository,
                                   UserRepository userRepository,
                                   ReceiverRepository receiverRepository,
                                   MerchantRepository merchantRepository,
                                   ItemRepository itemRepository,
                                   PaymentCardRepository paymentCardRepository,
                                   VoucherRepository voucherRepository,
                                   SenderRepository senderRepository,
                                   CreateNewPaymentRequest request
    ) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.receiverRepository = receiverRepository;
        this.merchantRepository = merchantRepository;
        this.itemRepository = itemRepository;
        this.paymentCardRepository = paymentCardRepository;
        this.voucherRepository = voucherRepository;
        this.senderRepository = senderRepository;
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

        Receiver receiver = createReceiver();
        receiverRepository.save(receiver);

        Sender sender = null;
        if (request.getSenderType().equals("Own")) {
            sender = createSender();
            senderRepository.save(sender);
        }

        PaymentCard paymentCard = paymentCardRepository.findByUser(user);

        Payment payment = Payment
                .builder()
                .paymentAt(LocalDateTime.now())
                .value(request.getValue())
                .senderType(request.getSenderType())
                .receiver(receiver)
                .sender(sender)
                .paymentCard(paymentCard)
                .merchant(merchant)
                .item(item)
                .user(user)
                .build();
        paymentRepository.save(payment);

        Voucher voucher = createVoucher(payment, user);
        voucherRepository.save(voucher);

        return "New payment made successfully";
    }

    private Receiver createReceiver() {
        return Receiver
                .builder()
                .name(request.getReceiverName())
                .address(request.getReceiverAddress())
                .district(request.getReceiverDistrict())
                .build();
    }

    private Sender createSender() {
        return Sender
                .builder()
                .name(request.getSenderName())
                .address(request.getSenderAddress())
                .contact(request.getSenderContact())
                .build();
    }

    private Voucher createVoucher(Payment payment, User user) {
        return Voucher
                .builder()
                .createdAt(LocalDateTime.now())
                .value(request.getValue())
                .status("Active")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.")
                .payment(payment)
                .user(user)
                .build();
    }
}
