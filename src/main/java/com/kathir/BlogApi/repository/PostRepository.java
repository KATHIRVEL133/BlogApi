package com.kathir.BlogApi.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kathir.BlogApi.models.Post;



public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("""
        SELECT p FROM Post p
        WHERE (:userId IS NULL OR p.userId = :userId)
        AND (:category IS NULL OR p.category = :category)
        AND (:slug IS NULL OR p.slug = :slug)
        AND (:postId IS NULL OR p.id = :postId)
        AND (:searchTerm IS NULL OR 
            (LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
                )
                ORDER BY p.updatedAt DESC
                """)
        List<Post> findPosts(
                @Param("userId") String userId,
                @Param("category") String category,
                @Param("slug") String slug,
                @Param("postId") Long postId,
                @Param("searchTerm") String searchTerm
        );  


        @Query(value = "SELECT COUNT(*) FROM post", nativeQuery = true)
        long countAllPosts();

        @Query(value = "SELECT COUNT(*) FROM post WHERE created_at < DATE_SUB(:currentDate, INTERVAL 1 MONTH)", nativeQuery = true)
        long countPostsExcludingLastMonth(java.util.Date currentDate);

        
        Optional<Post> getPostById(long postId);
}