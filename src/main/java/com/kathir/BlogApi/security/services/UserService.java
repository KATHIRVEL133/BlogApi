package com.kathir.BlogApi.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kathir.BlogApi.models.User;
import com.kathir.BlogApi.payload.request.UpdateUser;
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
}
