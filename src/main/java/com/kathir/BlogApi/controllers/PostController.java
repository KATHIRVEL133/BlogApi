package com.kathir.BlogApi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kathir.BlogApi.payload.request.PostRequest;
import com.kathir.BlogApi.security.services.PostService;

@RestController
@RequestMapping("/post")
public class PostController {
    
    private PostService postService;
   
    PostController(PostService postService)
    {
        this.postService=postService;
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createPost(@RequestBody  PostRequest requestData)
    {
     return postService.createPost(requestData);
    }
}
