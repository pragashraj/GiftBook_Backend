package com.giftbook.giftBook.controllers;

import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreItem;
import com.giftbook.giftBook.repositories.ItemRepository;
import com.giftbook.giftBook.repositories.MerchantRepository;
import com.giftbook.giftBook.usecases.GetItemsByMerchantUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/item/")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemRepository itemRepository;
    private final MerchantRepository merchantRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository, MerchantRepository merchantRepository) {
        this.itemRepository = itemRepository;
        this.merchantRepository = merchantRepository;
    }

    @GetMapping("getItemsByMerchant")
    public ResponseEntity<?> getItems(@RequestParam String merchant, @RequestParam int page) {
        try {
            GetItemsByMerchantUseCase useCase = new GetItemsByMerchantUseCase(
                    merchantRepository,
                    itemRepository,
                    merchant,
                    page
            );
            PageableCoreItem pageableCoreItem = useCase.execute();
            return ResponseEntity.ok(pageableCoreItem);
        } catch (Exception e) {
            log.error("Unable to get items, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}
