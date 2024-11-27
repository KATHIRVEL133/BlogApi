package com.kathir.BlogApi.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kathir.BlogApi.payload.request.PostRequest;
import com.kathir.BlogApi.payload.request.UpdatePostRequest;
import com.kathir.BlogApi.security.services.PostService;
import com.kathir.BlogApi.security.services.UserDetailsImpl;

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
    @PostMapping("/update/{userId}/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePost(@PathVariable long userId,@PathVariable long postId,@RequestBody  UpdatePostRequest requestData)
    {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     if(authentication!=null && authentication.getPrincipal() instanceof UserDetailsImpl)
     {
        UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
        long currentUserId = user.getId();
        if(currentUserId!=userId)
        {
            return ResponseEntity.status(400).body("You are not allowed to update");
        }
       return postService.updatePost(requestData, postId);
     }
     
    return ResponseEntity.status(400).body("Authentication details failed");
     
    }
    @DeleteMapping("/delete/{postId}/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable long postId,@PathVariable long userId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetailsImpl)
        {
           UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
           long currentUserId = user.getId();
           if(currentUserId!=userId)
           {
               return ResponseEntity.status(400).body("You are not allowed to delete");
           }
          return postService.deletePost(postId);
        }
        
       return ResponseEntity.status(400).body("Authentication details failed");
    }
    @GetMapping("/getPost/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getPostById(@PathVariable long postId)
    {
     return postService.getPostById(postId);
    }
}
