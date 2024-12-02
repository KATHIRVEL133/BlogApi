package com.kathir.BlogApi.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private String comment;
    private long userId;
    private long postId;
    private List<Long> likes;
    private long numberOfLikes;
    
     private LocalDateTime createdAt;
    

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Comment(String comment,long userId,long postId)
    {
    this.comment = comment;
    this.userId = userId;
    this.postId = postId;
    }

}
