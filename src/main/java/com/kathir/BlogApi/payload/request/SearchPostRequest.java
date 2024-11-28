package com.kathir.BlogApi.payload.request;

import lombok.Data;

@Data
public class SearchPostRequest {
    
    private String userId;
    private String category;
    private String slug;
    private Long postId;
    private String searchTerm;
    private int startIndex = 0;
    private int limit = 9;
    private String sort = "desc";
}
