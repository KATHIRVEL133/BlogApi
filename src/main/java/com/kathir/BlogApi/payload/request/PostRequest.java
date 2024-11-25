package com.kathir.BlogApi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank
    private int userId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String category;
    private String image;
}
