package com.kathir.BlogApi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kathir.BlogApi.payload.request.UpdateUser;
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
}
