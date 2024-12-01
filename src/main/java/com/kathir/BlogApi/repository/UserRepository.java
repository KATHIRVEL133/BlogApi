package com.kathir.BlogApi.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kathir.BlogApi.models.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Page<User> findAll(Pageable pageable);

  long count();

  long countByCreatedAtAfter(LocalDateTime date);

  long countByCreatedAtBefore(LocalDateTime date);

}