package com.kathir.BlogApi.dao;

import java.sql.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
    @Column(nullable = false)
    private String title;
    private String image="https://www.blogtyrant.com/wp-content/uploads/2017/02/how-to-write-a-good-blog-post.png";
    private String category = "uncategorized";
    @Column(unique = true,nullable = false)
    private String slug;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

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
