package com.kathir.BlogApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kathir.BlogApi.dao.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
 
}
