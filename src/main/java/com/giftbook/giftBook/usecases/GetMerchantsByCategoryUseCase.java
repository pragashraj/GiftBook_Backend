package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.Merchant;
import com.giftbook.giftBook.entities.MerchantCategory;
import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.pageable.core.CoreMerchant;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreMerchant;
import com.giftbook.giftBook.repositories.MerchantCategoryRepository;
import com.giftbook.giftBook.repositories.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

public class GetMerchantsByCategoryUseCase {
    private static final Logger log = LoggerFactory.getLogger(GetMerchantsByCategoryUseCase.class);

    private final MerchantRepository merchantRepository;
    private final MerchantCategoryRepository merchantCategoryRepository;
    private final int page;
    private final String category;

    public GetMerchantsByCategoryUseCase(MerchantRepository merchantRepository,
                                         MerchantCategoryRepository merchantCategoryRepository,
                                         int page,
                                         String category
    ) {
        this.merchantRepository = merchantRepository;
        this.merchantCategoryRepository = merchantCategoryRepository;
        this.page = page;
        this.category = category;
    }

    public PageableCoreMerchant execute() throws EntityNotFoundException {
        MerchantCategory merchantCategory = merchantCategoryRepository.findByTitle(category);

        if (merchantCategory == null) {
            log.error("Merchant category not found for title: {}", category);
            throw new EntityNotFoundException("Merchant category not found");
        }

        Page<Merchant> merchantPage = merchantRepository.findMerchantsByMerchantCategory(merchantCategory, PageRequest.of(page, 10));

        return new PageableCoreMerchant(
                merchantPage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
                merchantPage.getTotalPages(),
                merchantPage.getNumber()
        );
    }

    private CoreMerchant convertToCoreEntity(Merchant merchant) {
        return new CoreMerchant(
                merchant.getId(),
                merchant.getName(),
                merchant.getLocation(),
                merchant.getDescription(),
                merchant.getMerchantCategory()
        );
    }
}
