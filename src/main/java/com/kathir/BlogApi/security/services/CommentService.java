package com.kathir.BlogApi.security.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kathir.BlogApi.models.Comment;
import com.kathir.BlogApi.payload.request.CommentEditRequest;
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
    public ResponseEntity<?> likeComment(long commentId,long userId)
    {
     Optional<Comment> comment = commentRepository.findById(commentId);
     try
     {

         if(!comment.isPresent())
         {
            return ResponseEntity.status(400).body("comment not found");
         }
         Comment com = comment.get();
         if(com.getLikes()==null)
         {
           com.setNumberOfLikes(com.getNumberOfLikes()+1); 
           List<Long> list = new ArrayList<>();
           list.add(userId);
           com.setLikes(list);
         }
         else if(!com.getLikes().contains(userId))
         {
            com.setNumberOfLikes(com.getNumberOfLikes()+1);
            List<Long> li = com.getLikes();
            li.add(userId);
         }
         else
         {
            com.setNumberOfLikes(com.getNumberOfLikes()-1);
            List<Long> li = com.getLikes();
            li.remove(Long.valueOf(userId));
            com.setLikes(li);
         }
         Comment savedComment = commentRepository.save(com);
         return ResponseEntity.status(200).body(savedComment);
     }
     catch(Exception e)
     {
        return ResponseEntity.status(400).body(e.toString());
     }
    }
    public ResponseEntity<?> editComment(CommentEditRequest req,Long userId)
    {
    try
    {
    Optional<Comment> comment = commentRepository.findById(req.getCommentId());
    if(!comment.isPresent())
    {
        return ResponseEntity.status(400).body("Oops comment not found");
    }
    if(comment.get().getUserId()!=userId)  return ResponseEntity.status(400).body("You are not allowed to edit");
    Comment com = comment.get();
    com.setComment(req.getComment());
    Comment savedComment = commentRepository.save(com);
    return ResponseEntity.status(200).body(savedComment);
    } 
    catch(Exception e)
    {
        return ResponseEntity.status(400).body(e.toString());
    }
    }

    public ResponseEntity<?> deleteComment(long commentId,long userId)
    {
        Optional<Comment> comment = commentRepository.findById(commentId);
        try
        {
            if(!comment.isPresent())
            {
                return ResponseEntity.status(400).body("Oops comment not found");
            }
            if(comment.get().getUserId()!=userId)  return ResponseEntity.status(400).body("You are not allowed to edit");
            commentRepository.deleteById(commentId);
            return ResponseEntity.status(200).body("Comment deleted successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(400).body(e.toString());
        }
    }
    public ResponseEntity<?> deleteComment2(long commentId)
    {
        Optional<Comment> comment = commentRepository.findById(commentId);
        try
        {
            if(!comment.isPresent())
            {
                return ResponseEntity.status(400).body("Oops comment not found");
            }
            commentRepository.deleteById(commentId);
            return ResponseEntity.status(200).body("Comment deleted successfully");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(400).body(e.toString());
        }
        
    }
    public ResponseEntity<?> getComments(int startIndex,int limit,String sort)
    {
     
        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageable = PageRequest.of(startIndex / limit, limit, Sort.by(direction, "createdAt"));

        List<Comment> comments = commentRepository.findAll(pageable).getContent();
        long totalComments = commentRepository.count();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        long lastMonthComments = commentRepository.countByCreatedAtBefore(oneMonthAgo);
        HashMap<String,Object> res = new HashMap<>();
        res.put("comments",comments);
        res.put("totalComments",totalComments);
        res.put("lastMonthComments",lastMonthComments);
        return ResponseEntity.status(200).body(res);

    }
}
