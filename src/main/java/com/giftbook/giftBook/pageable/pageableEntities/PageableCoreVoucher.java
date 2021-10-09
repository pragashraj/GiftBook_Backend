package com.giftbook.giftBook.pageable.pageableEntities;

import com.giftbook.giftBook.pageable.core.CoreVoucher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreVoucher {
    private List<CoreVoucher> voucherList = new ArrayList<>();
    private int total;
    private int current;
}
