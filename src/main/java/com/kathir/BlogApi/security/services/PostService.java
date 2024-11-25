package com.kathir.BlogApi.security.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kathir.BlogApi.models.Post;
import com.kathir.BlogApi.payload.request.PostRequest;
import com.kathir.BlogApi.repository.PostRepository;

@Service
public class PostService {
    public PostRepository postRepository;
    public PostService(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    } 
    public ResponseEntity<?> createPost(PostRequest requestdata)
    {
    if(requestdata.getTitle()==null||requestdata.getContent()==null||requestdata.getTitle()==""||requestdata.getContent()=="")
    {
        return ResponseEntity.status(400).body("Required all fields");
    }
    String slug = requestdata.getTitle()
    .toLowerCase()
    .replaceAll("[^a-zA-Z0-9\\s]", "")
    .replaceAll("\\s+", "-");
    Post post = new Post();
    post.setUserId(requestdata.getUserId());
    post.setContent(requestdata.getContent());
    post.setTitle(requestdata.getTitle());
    post.setSlug(slug);
    if(requestdata.getImage()!=null)
    {
        post.setImage(requestdata.getImage());
    }
    if(requestdata.getCategory()!=null)
    {
        post.setCategory(requestdata.getCategory());
    }
    try
    {
     postRepository.save(post);
    }
    catch(Exception e)
    {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}