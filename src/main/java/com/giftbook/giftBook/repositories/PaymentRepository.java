package com.giftbook.giftBook.repositories;

import com.giftbook.giftBook.entities.Payment;
import com.giftbook.giftBook.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAllByUserOrderByPaymentAt(User user, Pageable pageable);

    Page<Payment> findAllByUserAndPaymentAtGreaterThanEqualAndPaymentAtLessThanEqualOrderByPaymentAt(
            User user, LocalDateTime start, LocalDateTime end, Pageable pageable
    );
}