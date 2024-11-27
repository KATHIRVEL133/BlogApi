package com.kathir.BlogApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kathir.BlogApi.models.Post;



public interface PostRepository extends JpaRepository<Post,Long> {
Optional<Post> getPostById(long postId);
}