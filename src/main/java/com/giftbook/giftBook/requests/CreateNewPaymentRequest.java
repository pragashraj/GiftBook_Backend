package com.giftbook.giftBook.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewPaymentRequest {
    private String email;
    private BigDecimal value;
    private String senderType;
    private String merchantName;
    private String itemName;
    private String receiverName;
    private String receiverAddress;
    private String receiverDistrict;
    private String senderName;
    private String senderAddress;
    private String senderContact;
}