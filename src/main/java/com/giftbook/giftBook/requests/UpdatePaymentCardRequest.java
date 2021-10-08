package com.giftbook.giftBook.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentCardRequest {
    private String email;
    private String cardType;
    private String cardNo;
}
