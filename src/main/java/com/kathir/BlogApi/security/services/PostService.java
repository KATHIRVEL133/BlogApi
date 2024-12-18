package com.kathir.BlogApi.security.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

//import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kathir.BlogApi.models.Post;
import com.kathir.BlogApi.payload.request.PostRequest;
import com.kathir.BlogApi.payload.request.SearchPostRequest;
import com.kathir.BlogApi.payload.request.UpdatePostRequest;
import com.kathir.BlogApi.repository.PostRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;

@Service
public class PostService {
    public PostRepository postRepository;
     @PersistenceContext
    private EntityManager entityManager;

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
    public ResponseEntity<?> updatePost(UpdatePostRequest updatePostRequest,long postId)
    {
    try
    {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent())
        {
        Post post = existingPost.get();    
        if(updatePostRequest.getTitle()!=null)
        {
        post.setTitle(updatePostRequest.getTitle());
        String slug = updatePostRequest.getTitle()
        .toLowerCase()
        .replaceAll("[^a-zA-Z0-9\\s]", "")
        .replaceAll("\\s+", "-");
        post.setSlug(slug);
        }
        if(updatePostRequest.getCategory()!=null)
        {
            post.setCategory(updatePostRequest.getCategory());
        }
        if(updatePostRequest.getContent()!=null)
        {
            post.setContent(updatePostRequest.getContent());
        }
        if(updatePostRequest.getImage()!=null)
        {
            post.setImage(updatePostRequest.getImage());
        }
        Post savedPost = postRepository.save(post);
        ResponseEntity.status(200).body(savedPost);
        }
        else
        {
            return ResponseEntity.status(400).body("Post not present");
        }
    }
    catch(Exception e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }
    return ResponseEntity.ok().body("ok");
    }
    public ResponseEntity<?> deletePost(long postId)
    {
     Optional<Post> exitsPost = postRepository.findById(postId);
     if(exitsPost.isPresent())
     {
     postRepository.deleteById(postId);
     return ResponseEntity.status(200).body("post deleted successfully");
     }
     return ResponseEntity.status(400).body("post not present");
    }
    public ResponseEntity<?> getPostById(long postId)
    {
    Optional<Post> post = postRepository.getPostById(postId);
    if(post.isPresent())
    {
        return ResponseEntity.status(200).body(post);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("post not present");
    }

    public ResponseEntity<?> getAllPosts(SearchPostRequest request)
    {
    HashMap<String,Object> res = new HashMap<>();     
    int startIndex = request.getStartIndex();
        int limit = request.getLimit();
        //Sort.Direction sortDirection = request.getSort().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Fetch posts
        List<Post> posts = postRepository.findPosts(
                request.getUserId(),
                request.getCategory(),
                request.getSlug(),
                request.getPostId(),
                request.getSearchTerm()
        );

        // Apply pagination manually
        int fromIndex = Math.min(startIndex, posts.size());
        int toIndex = Math.min(fromIndex + limit, posts.size());
        //List<Post> paginatedPosts = posts.subList(fromIndex, toIndex);


   
    long totalPosts = postRepository.countAllPosts();
    Date currentDate = new Date();
    long lastMonthPosts = postRepository.countPostsExcludingLastMonth(currentDate);
    res.put("posts",posts);
    res.put("totalPosts",totalPosts);
    res.put("currentDate", currentDate);
    res.put("lastMonthPosts", lastMonthPosts);
    return ResponseEntity.status(200).body(res);
    }
}