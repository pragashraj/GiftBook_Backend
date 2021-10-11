package com.giftbook.giftBook.repositories;

import com.giftbook.giftBook.entities.User;
import com.giftbook.giftBook.entities.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Page<Voucher> findAllByUserOrderByCreatedAt(User user, Pageable pageable0);

    Page<Voucher> findAllByUserAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualOrderByCreatedAt(
            User user, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    Page<Voucher> findAllByUserAndStatusOrderByCreatedAt(User user, String status, Pageable pageable);

    List<Voucher> findAllByUser(User user);
}