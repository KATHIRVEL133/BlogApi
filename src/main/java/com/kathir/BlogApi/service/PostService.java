package com.kathir.BlogApi.service;

import org.springframework.stereotype.Service;

import com.kathir.BlogApi.repository.PostRepository;

@Service
public class PostService {
    public PostRepository postRepository;
    public PostService(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    } 
    
}
