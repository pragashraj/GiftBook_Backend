package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.Item;
import com.giftbook.giftBook.entities.Merchant;
import com.giftbook.giftBook.pageable.core.CoreItem;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreItem;
import com.giftbook.giftBook.repositories.ItemRepository;
import com.giftbook.giftBook.repositories.MerchantRepository;
import com.giftbook.giftBook.util.FileStorageService;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

public class GetItemByNameAndMerchantUseCase {
    private final ItemRepository itemRepository;
    private final MerchantRepository merchantRepository;
    private final String itemName;
    private final String merchantName;
    private final int page;

    public GetItemByNameAndMerchantUseCase(ItemRepository itemRepository,
                                           MerchantRepository merchantRepository,
                                           String itemName,
                                           String merchantName,
                                           int page
    ) {
        this.itemRepository = itemRepository;
        this.merchantRepository = merchantRepository;
        this.itemName = itemName;
        this.merchantName = merchantName;
        this.page = page;
    }

    public PageableCoreItem execute() {
        Merchant merchant = merchantRepository.findByName(merchantName);
        Page<Item> itemPage = itemRepository.findAllByMerchantAndName(merchant, itemName, PageRequest.of(page, 10));

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
                item.getMerchant(),
                getImage(item.getFileName())
        );
    }

    @SneakyThrows
    private byte[] getImage(String fileName) {
        if (fileName != null) {
            FileStorageService fileStorageService = new FileStorageService("Items");
            return fileStorageService.convert(fileName);
        }
        return null;
    }
}
