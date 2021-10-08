package com.giftbook.giftBook.controllers;

import com.giftbook.giftBook.exceptions.EntityNotFoundException;
import com.giftbook.giftBook.pageable.pageableEntities.PageableCoreMerchant;
import com.giftbook.giftBook.repositories.MerchantCategoryRepository;
import com.giftbook.giftBook.repositories.MerchantRepository;
import com.giftbook.giftBook.usecases.GetMerchantByNameUseCase;
import com.giftbook.giftBook.usecases.GetMerchantCategoriesUseCase;
import com.giftbook.giftBook.usecases.GetMerchantsByCategoryUseCase;
import com.giftbook.giftBook.usecases.GetMerchantsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/merchant/")
public class MerchantController {
    private static final Logger log = LoggerFactory.getLogger(MerchantController.class);

    private final MerchantRepository merchantRepository;
    private final MerchantCategoryRepository merchantCategoryRepository;

    @Autowired
    public MerchantController(MerchantRepository merchantRepository, MerchantCategoryRepository merchantCategoryRepository) {
        this.merchantRepository = merchantRepository;
        this.merchantCategoryRepository = merchantCategoryRepository;
    }

    @GetMapping("getMerchants/{page}")
    public ResponseEntity<?> getMerchants(@PathVariable int page) {
        try {
            GetMerchantsUseCase useCase = new GetMerchantsUseCase(
                    merchantRepository,
                    page
            );
            PageableCoreMerchant pageableCoreMerchant = useCase.execute();
            return ResponseEntity.ok(pageableCoreMerchant);
        } catch (Exception e) {
            log.error("Unable to get merchants, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("getMerchantsByCategory")
    public ResponseEntity<?> getMerchantsByCategory(@RequestParam String category, @RequestParam int page) {
        try {
            GetMerchantsByCategoryUseCase useCase = new GetMerchantsByCategoryUseCase(
                    merchantRepository,
                    merchantCategoryRepository,
                    page,
                    category
            );
            PageableCoreMerchant pageableCoreMerchant = useCase.execute();
            return ResponseEntity.ok(pageableCoreMerchant);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get merchants by category, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get merchants by category, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("getMerchantByName")
    public ResponseEntity<?> getMerchantByName(@RequestParam String name, @RequestParam int page) {
        try {
            GetMerchantByNameUseCase useCase = new GetMerchantByNameUseCase(
                    merchantRepository,
                    name,
                    page
            );
            PageableCoreMerchant pageableCoreMerchant = useCase.execute();
            return ResponseEntity.ok(pageableCoreMerchant);
        } catch (Exception e) {
            log.error("Unable to get merchant by name, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("getMerchantCategories")
    public ResponseEntity<?> getMerchantCategories() {
        try {
            GetMerchantCategoriesUseCase useCase = new GetMerchantCategoriesUseCase(merchantCategoryRepository);
            return ResponseEntity.ok(useCase.execute());
        } catch (Exception e) {
            log.error("Unable to get merchant categories, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}
