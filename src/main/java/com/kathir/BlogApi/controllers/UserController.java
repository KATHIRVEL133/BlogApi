package com.kathir.BlogApi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kathir.BlogApi.payload.request.UpdateUser;
import com.kathir.BlogApi.security.services.UserDetailsImpl;
import com.kathir.BlogApi.security.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService)
    {
    this.userService = userService;
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser,@PathVariable Long userId)
    {
     return userService.updateUser(updateUser,userId);
    }
    
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null&&authentication.getPrincipal() instanceof UserDetailsImpl)
        {
           UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
           long currUserId = user.getId();
           if(currUserId!=userId)
           {
            return ResponseEntity.status(400).body("you can only delete your account");
           }
           return userService.deleteUser(userId);
        }
        return ResponseEntity.status(400).body("authencticate fisrt");
    }
 }
