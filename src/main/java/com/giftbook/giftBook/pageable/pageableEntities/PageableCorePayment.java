package com.giftbook.giftBook.pageable.pageableEntities;

import com.giftbook.giftBook.pageable.core.CorePayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCorePayment {
    private List<CorePayment> paymentList = new ArrayList<>();
    private int total;
    private int current;
}
