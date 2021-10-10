package com.giftbook.giftBook.repositories;

import com.giftbook.giftBook.entities.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SenderRepository extends JpaRepository<Sender, Long> {
}
