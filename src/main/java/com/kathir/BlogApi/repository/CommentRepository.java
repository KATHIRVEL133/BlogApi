package com.kathir.BlogApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kathir.BlogApi.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
 List<Comment> findByPostIdOrderByCreatedAtDesc(long postId);
}
