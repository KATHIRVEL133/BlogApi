package com.kathir.BlogApi.security.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kathir.BlogApi.models.Comment;
import com.kathir.BlogApi.payload.request.CommentRequest;
import com.kathir.BlogApi.repository.CommentRepository;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    public ResponseEntity<?> createComment(CommentRequest req)
    {
    Comment newComment = new Comment(req.getComment(),req.getUserId(),req.getPostId());    
    try
    {
    Comment savedComment = commentRepository.save(newComment);
    return ResponseEntity.status(201).body(savedComment);
    }
    catch(Exception e)
    {
        return ResponseEntity.status(400).body(e);
    }
    }
    public ResponseEntity<?> getPostComments(long postId)
    {
    try
    {
    List<Comment> comment = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    if(comment.size()==0)
    {
        return ResponseEntity.status(200).body("No comments found");
    }
    return ResponseEntity.status(200).body(comment);
    }
    catch(Exception e)
    {
       return ResponseEntity.status(400).body(e);
    }
    }
}
