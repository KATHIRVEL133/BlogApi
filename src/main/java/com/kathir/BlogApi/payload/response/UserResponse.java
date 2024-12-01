package com.kathir.BlogApi.payload.response;


import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String email;
    private String username;
    private LocalDateTime createdAt;
    public UserResponse(Long id, String username2, String email2, LocalDateTime createdAt2) {
     this.userId = id;
     this.username = username2;
     this.email = email2;
     this.createdAt = createdAt2;
    }
}
