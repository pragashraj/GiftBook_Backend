package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.Item;
import com.giftbook.giftBook.entities.Merchant;
import com.giftbook.giftBook.pageable.core.CoreItem;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreItem;
import com.giftbook.giftBook.repositories.ItemRepository;
import com.giftbook.giftBook.repositories.MerchantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

public class GetItemsByMerchantUseCase {
    private final MerchantRepository merchantRepository;
    private final ItemRepository itemRepository;
    private final String merchantName;
    private final int page;

    public GetItemsByMerchantUseCase(MerchantRepository merchantRepository, ItemRepository itemRepository, String merchantName, int page) {
        this.merchantRepository = merchantRepository;
        this.itemRepository = itemRepository;
        this.merchantName = merchantName;
        this.page = page;
    }

    public PageableCoreItem execute() {
        Merchant merchant = merchantRepository.findByName(merchantName);
        Page<Item> itemPage = itemRepository.findAllByMerchant(merchant, PageRequest.of(page, 10));

        return new PageableCoreItem(
                itemPage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
                itemPage.getTotalPages(),
                itemPage.getNumber()
        );
    }

    private CoreItem convertToCoreEntity(Item item) {
        return new CoreItem(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getMerchant()
        );
    }
}
