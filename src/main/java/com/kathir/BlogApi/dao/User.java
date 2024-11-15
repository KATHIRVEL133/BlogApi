package com.kathir.BlogApi.dao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table
@Data
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(unique = true , nullable = false)
    private String username;
    @Column(unique = true , nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    
    private boolean enabled;
    private String profilepicture;
    private boolean isAdmin;
    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verification_expires")
    private LocalDateTime verificationCodeExpiresAt;

    public User(String username,String email,String password)
    {
    this.username = username;
    this.email = email;
    this.password = password;
    }
    public User()
    {
        
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
     }

     @Override
     public boolean isAccountNonLocked() {
        return true;
     }

     @Override
     public boolean isCredentialsNonExpired() {
        return true;
     }
     
     @Override
     public boolean isEnabled() {
        return enabled;
     }

    @Override
    public String getPassword() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }

    @Override
    public String getUsername() {
     
        throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
    }
   
    
}
