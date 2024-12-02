package com.kathir.BlogApi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kathir.BlogApi.models.Comment;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
  List<Comment> findByPostIdOrderByCreatedAtDesc(long postId);

  Page<Comment> findAll(Pageable pageable);
 
  long count();

  long countByCreatedAtAfter(LocalDateTime date);

  long countByCreatedAtBefore(LocalDateTime date);
}
