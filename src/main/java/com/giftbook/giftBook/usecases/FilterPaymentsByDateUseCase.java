package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.Payment;
import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.pageable.core.CorePayment;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCorePayment;
import com.giftbook.giftBook.repositories.PaymentRepository;
import com.giftbook.giftBook.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class FilterPaymentsByDateUseCase {
    private static final Logger log = LoggerFactory.getLogger(FilterPaymentsByDateUseCase.class);

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final String start;
    private final String end;
    private final String email;
    private final int page;

    public FilterPaymentsByDateUseCase(PaymentRepository paymentRepository,
                                       UserRepository userRepository,
                                       String start,
                                       String end,
                                       String email,
                                       int page
    ) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.start = start;
        this.end = end;
        this.email = email;
        this.page = page;
    }

    public PageableCorePayment execute() throws EntityNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User not found");
            throw new EntityNotFoundException("User not found");
        }

        Page<Payment> paymentPage = paymentRepository.findAllByUserAndPaymentAtGreaterThanEqualAndPaymentAtLessThanEqualOrderByPaymentAt(
                user,
                parseDate(start),
                parseDate(end),
                PageRequest.of(page, 10)
        );

        return new PageableCorePayment(
                paymentPage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
                paymentPage.getTotalPages(),
                paymentPage.getNumber()
        );
    }

    private CorePayment convertToCoreEntity(Payment payment) {
        return new CorePayment(
                payment.getId(),
                payment.getPaymentAt(),
                payment.getValue(),
                payment.getSenderType(),
                payment.getMerchant().getName(),
                payment.getItem().getName(),
                payment.getReceiver(),
                payment.getPaymentCard()
        );
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date);
    }
}
