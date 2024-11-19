package com.kathir.BlogApi.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    public String username;
    public String email;
    public String password;
}
