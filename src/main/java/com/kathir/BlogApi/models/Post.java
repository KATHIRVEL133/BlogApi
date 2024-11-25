package com.kathir.BlogApi.models;


import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    @Column(nullable=false)
    private long userId;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false,unique = true)
    private String title;
    private String image="https://www.blogtyrant.com/wp-content/uploads/2017/02/how-to-write-a-good-blog-post.png";
    private String category = "uncategorized";
    @Column(unique = true,nullable = false)
    private String slug;

    @CreatedDate
    @Column(nullable = false)
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

    public Post(long userId,String content,String title,String image,String category,String slug)
    {
    this.userId = userId;
    this.content = content;
    this.title = title;
    this.image = image;
    this.category = category;
    this.slug = slug;
    }
    public Post()
    {

    }
}