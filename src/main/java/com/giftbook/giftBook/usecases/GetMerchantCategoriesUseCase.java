package com.giftbook.giftBook.usecases;

import com.giftbook.giftBook.entities.MerchantCategory;
import com.giftbook.giftBook.exceptions.FileStorageException;
import com.giftbook.giftBook.repositories.MerchantCategoryRepository;
import com.giftbook.giftBook.responses.GetMerchantCategoriesResponse;
import com.giftbook.giftBook.util.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetMerchantCategoriesUseCase {
    private static final Logger log = LoggerFactory.getLogger(GetMerchantCategoriesUseCase.class);

    private final MerchantCategoryRepository merchantCategoryRepository;

    public GetMerchantCategoriesUseCase(MerchantCategoryRepository merchantCategoryRepository) {
        this.merchantCategoryRepository = merchantCategoryRepository;
    }

    public List<GetMerchantCategoriesResponse> execute() throws FileStorageException, IOException {
        List<MerchantCategory> merchantCategoryList = merchantCategoryRepository.findAll();

        List<GetMerchantCategoriesResponse> getMerchantCategoriesResponses = new ArrayList<>();

        for (MerchantCategory category : merchantCategoryList) {
            GetMerchantCategoriesResponse response = new GetMerchantCategoriesResponse(
                    category.getId(),
                    category.getTitle(),
                    getImage(category.getFileName())
            );
            getMerchantCategoriesResponses.add(response);
        }

        log.info("Total number of categories found: {}", merchantCategoryList.size());
        return getMerchantCategoriesResponses;
    }

    private byte[] getImage(String fileName) throws IOException, FileStorageException {
        if (fileName != null) {
            FileStorageService fileStorageService = new FileStorageService("MerchantCategories");
            return fileStorageService.convert(fileName);
        }
        return null;
    }
}
