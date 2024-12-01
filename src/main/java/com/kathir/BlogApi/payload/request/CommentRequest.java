package com.kathir.BlogApi.payload.request;

import lombok.Data;

@Data
public class CommentRequest {
    
    private String comment;
    private long userId;
    private long postId;
}
