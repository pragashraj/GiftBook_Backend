package com.giftbook.giftBook.pageable.core;

import com.giftbook.giftBook.entities.MerchantCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoreMerchant {
    private Long id;
    private String name;
    private String location;
    private String description;
    private MerchantCategory merchantCategory;
}
