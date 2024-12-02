package com.kathir.BlogApi.payload.request;

import lombok.Data;

@Data
public class CommentEditRequest {
    private long commentId;
    private String comment;
}
