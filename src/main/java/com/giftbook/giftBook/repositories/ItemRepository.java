package com.giftbook.giftBook.repositories;

import com.giftbook.giftBook.entities.Item;
import com.giftbook.giftBook.entities.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByMerchant(Merchant merchant, Pageable pageable);

    Page<Item> findAllByMerchantAndName(Merchant merchant, String name, Pageable pageable);

    Item findByName(String name);
}