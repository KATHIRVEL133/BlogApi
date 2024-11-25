package com.kathir.BlogApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kathir.BlogApi.models.Post;



public interface PostRepository extends JpaRepository<Post,Long> {

}