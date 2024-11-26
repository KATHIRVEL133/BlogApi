package com.kathir.BlogApi.payload.request;

import lombok.Data;

@Data
public class UpdatePostRequest {
    private int userId;
    private String title;
    private String content;
    private String category;
    private String image;
}
