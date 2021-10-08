package com.giftbook.giftBook.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeNewPasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
