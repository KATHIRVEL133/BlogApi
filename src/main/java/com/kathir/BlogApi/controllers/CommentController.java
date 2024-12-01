package com.kathir.BlogApi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kathir.BlogApi.payload.request.CommentEditRequest;
import com.kathir.BlogApi.payload.request.CommentRequest;
import com.kathir.BlogApi.security.services.CommentService;
import com.kathir.BlogApi.security.services.UserDetailsImpl;

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

    @PostMapping("/likeComment/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> likeComment(@PathVariable long commentId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null&&authentication.getPrincipal() instanceof UserDetailsImpl)
        {
            UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
            return commentService.likeComment(commentId, user.getId());
        }
        return ResponseEntity.status(400).body("Authentication required");
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editComment(@RequestBody CommentEditRequest req)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null&&authentication.getPrincipal() instanceof UserDetailsImpl)
        {
            UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
            return commentService.editComment(req, user.getId());
        }
        return ResponseEntity.status(400).body("Authentication required");
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("deleteComment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null&&authentication.getPrincipal() instanceof UserDetailsImpl)
        {
            UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
            return commentService.deleteComment(commentId,user.getId());
        }
        return ResponseEntity.status(400).body("Authentication required");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("deleteComment2/{commentId}")
    public ResponseEntity<?> deleteComment2(@PathVariable long commentId)
    {
        
        return commentService.deleteComment2(commentId);
    }
}
