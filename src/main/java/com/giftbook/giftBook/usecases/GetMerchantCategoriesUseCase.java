package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.MerchantCategory;
import com.giftbook.giftBook.repositories.MerchantCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetMerchantCategoriesUseCase {
    private static final Logger log = LoggerFactory.getLogger(GetMerchantCategoriesUseCase.class);

    private final MerchantCategoryRepository merchantCategoryRepository;

    public GetMerchantCategoriesUseCase(MerchantCategoryRepository merchantCategoryRepository) {
        this.merchantCategoryRepository = merchantCategoryRepository;
    }

    public List<MerchantCategory> execute() {
        List<MerchantCategory> merchantCategoryList = merchantCategoryRepository.findAll();
        log.info("Total number of categories found: {}", merchantCategoryList.size());
        return merchantCategoryList;
    }
}
