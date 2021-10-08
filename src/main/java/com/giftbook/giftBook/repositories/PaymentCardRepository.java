package com.giftbook.giftBook.repositories;

import com.giftbook.giftBook.entities.PaymentCard;
import com.giftbook.giftBook.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
    PaymentCard findByUser(User user);
}
