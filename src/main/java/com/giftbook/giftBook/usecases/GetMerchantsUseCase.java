package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.Merchant;
import com.giftbook.giftBook.pageable.core.CoreMerchant;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreMerchant;
import com.giftbook.giftBook.repositories.MerchantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

public class GetMerchantsUseCase {
    private final MerchantRepository merchantRepository;
    private final int page;

    public GetMerchantsUseCase(MerchantRepository merchantRepository, int page) {
        this.merchantRepository = merchantRepository;
        this.page = page;
    }

    public PageableCoreMerchant execute() {
        Page<Merchant> merchantPage = merchantRepository.findAll(PageRequest.of(page, 10));

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
