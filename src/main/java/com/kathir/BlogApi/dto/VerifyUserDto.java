package com.kathir.BlogApi.dto;

import lombok.Data;

@Data
public class VerifyUserDto {
    String email;
    String verificationCode;
}
