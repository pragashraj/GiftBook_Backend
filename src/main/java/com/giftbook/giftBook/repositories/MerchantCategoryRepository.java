package com.giftbook.giftBook.repositories;

import com.giftbook.giftBook.entities.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, Long> {
    MerchantCategory findByTitle(String title);
}