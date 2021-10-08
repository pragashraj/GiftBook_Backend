package com.giftbook.giftBook.repositories;

import com.giftbook.giftBook.entities.Merchant;
import com.giftbook.giftBook.entities.MerchantCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Page<Merchant> findMerchantsByMerchantCategory(MerchantCategory merchantCategory, Pageable pageable);

    Page<Merchant> findAllByNameContaining(String name, Pageable pageable);

    Merchant findByName(String name);
}
