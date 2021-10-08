package com.giftbook.giftBook.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileResponse {
    private String email;
    private String name;
    private String address;
    private String district;
    private String contact;
    private String cardType;
    private String cardNo;
}
