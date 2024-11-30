package com.kathir.BlogApi.payload.request;

import lombok.Data;

@Data
public class UpdateUser {
   
    private String username;
    private String email;
    private String password;
}
