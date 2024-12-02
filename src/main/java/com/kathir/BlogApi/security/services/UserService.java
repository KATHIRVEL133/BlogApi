package com.kathir.BlogApi.security.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kathir.BlogApi.models.User;
import com.kathir.BlogApi.payload.request.UpdateUser;
import com.kathir.BlogApi.payload.response.UserResponse;
import com.kathir.BlogApi.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<?> updateUser(UpdateUser updateUser,Long userId)
    {
     if(updateUser.getPassword()!=null)
     {
     updateUser.setPassword(encoder.encode(updateUser.getPassword()));
     }
     if(updateUser.getUsername()!=null)
     {
        CharSequence charSequence  = " ";
        if(updateUser.getUsername().length()<7||updateUser.getUsername().length()>20)
        {
            return ResponseEntity.status(400).body("Username must contain between 7 to 20 characters");
        }
        if(updateUser.getUsername().contains(charSequence))
        {
            return ResponseEntity.status(400).body("It must not contains white spaces");
        }
        if(!updateUser.getUsername().equals(updateUser.getUsername().toLowerCase()))
        {
            return ResponseEntity.status(400).body("User name must be in lowercase");
        }
       
     }
     try
     {
     Optional<User> Existinguser = userRepository.findById(userId);
     if(Existinguser.isPresent())
     {
     User user  = Existinguser.get();
     if(updateUser.getUsername()!=null)
     {
        user.setUsername(updateUser.getUsername());
     }
     if(updateUser.getPassword()!=null)
     {
     user.setPassword(updateUser.getPassword());
     }
     if(updateUser.getEmail()!=null)
     {
        user.setEmail(updateUser.getEmail());
     }
     User updatedUser= userRepository.save(user);
     User sendUser = new User();
     sendUser.setEmail(updatedUser.getEmail());
     sendUser.setUsername(updatedUser.getUsername());
     sendUser.setRoles(updatedUser.getRoles());
     return ResponseEntity.status(200).body(sendUser);
     }
     else
     {
        return ResponseEntity.status(400).body("User not found");
     }
     } 
     catch(Exception e)
     {
        return ResponseEntity.status(400).body(e);
     }

    }
    public ResponseEntity<?> deleteUser(long userId)
    { 
    try
    {
    userRepository.deleteById(userId);
    }
    catch(Exception e)
    {
      return ResponseEntity.status(400).body(e);
    }
    return ResponseEntity.status(200).body("User Deleted Successfully");
    }
    public ResponseEntity<?> getUser(long userId)
    {
      Optional<User> user = userRepository.findById(userId);
      if(user.isPresent())
      {
      return ResponseEntity.status(200).body(user.get());
      }
      return ResponseEntity.status(400).body("User not found");
    }
     public ResponseEntity<?> getUsers(int startIndex, int limit, String sort) {
       
        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageable = PageRequest.of(startIndex / limit, limit, Sort.by(direction, "createdAt"));

        List<User> users = userRepository.findAll(pageable).getContent();
        List<UserResponse> usersWithoutPassword = users.stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt()))
                .collect(Collectors.toList());
        long totalUsers = userRepository.count();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        long lastMonthUsers = userRepository.countByCreatedAtBefore(oneMonthAgo);
        HashMap<String, Object> response = new HashMap<>();
        response.put("users", usersWithoutPassword);
        response.put("totalUsers", totalUsers);
        response.put("lastMonthUsers", lastMonthUsers);
        return ResponseEntity.status(200).body(response);
    }
}
