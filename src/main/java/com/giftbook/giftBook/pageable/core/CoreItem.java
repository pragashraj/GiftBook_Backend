package com.giftbook.giftBook.pageable.core;

import com.giftbook.giftBook.entities.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoreItem {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private Merchant merchant;
    private byte[] src;
}
