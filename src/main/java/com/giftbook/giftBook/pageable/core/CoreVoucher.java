package com.giftbook.giftBook.pageable.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoreVoucher {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal value;
    private String owner;
    private String status;
    private String description;
    private String merchantName;
    private String itemName;
}
