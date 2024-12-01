package com.kathir.BlogApi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kathir.BlogApi.payload.request.CommentRequest;
import com.kathir.BlogApi.security.services.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {
    
    private CommentService commentService;

    public CommentController(CommentService commentService)
    {
    this.commentService = commentService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest req)
    {
        return commentService.createComment(req);
    }

    @GetMapping("/getPostComments/{postId}")
    public ResponseEntity<?> getPostComments(@PathVariable long postId)
    {
        return commentService.getPostComments(postId);
    }
}
