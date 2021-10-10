package com.giftbook.giftBook.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String email;
    private String name;
    private String token;
    private int expiration;
}
