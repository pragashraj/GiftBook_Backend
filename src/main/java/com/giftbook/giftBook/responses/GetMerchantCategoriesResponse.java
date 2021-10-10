package com.giftbook.giftBook.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMerchantCategoriesResponse {
    private Long id;
    private String title;
    private byte[] src;
}
