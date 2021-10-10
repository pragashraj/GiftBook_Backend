package com.giftbook.giftBook.pageable.core;

import com.giftbook.giftBook.entities.PaymentCard;
import com.giftbook.giftBook.entities.Receiver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorePayment {
    private Long id;
    private LocalDateTime paymentAt;
    private BigDecimal value;
    private String senderType;
    private String merchantName;
    private String itemName;
    private Receiver receiver;
    private PaymentCard paymentCard;
}