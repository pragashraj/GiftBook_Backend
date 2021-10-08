package com.giftbook.giftBook.pageable.pageableEntities;

import com.giftbook.giftBook.pageable.core.CoreMerchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreMerchant {
    private List<CoreMerchant> merchantList = new ArrayList<>();
    private int total;
    private int current;
}
