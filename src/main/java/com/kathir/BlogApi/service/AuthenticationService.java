package com.kathir.BlogApi.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kathir.BlogApi.dao.User;
import com.kathir.BlogApi.dto.LoginUserDto;
import com.kathir.BlogApi.dto.RegisterUserDto;
import com.kathir.BlogApi.dto.VerifyUserDto;
import com.kathir.BlogApi.repository.UserRepository;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AuthenticationService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    public User signup(RegisterUserDto input)
    {
    User user = new User(input.getUsername(),input.getEamil(),passwordEncoder.encode(input.getPassword()));
    user.setVerificationCode(getVerificationCode());
    user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
    user.setEnabled(false);
    sendVerificationEmail(user);
    return userRepository.save(user);
    }
    public User authenticateUser(LoginUserDto input)
    {
     User user = userRepository.findByemail(input.getEmail()).orElseThrow(()-> new RuntimeException("User not found"));
     if(!user.isEnabled())
     {
         throw new RuntimeException("Account is not verified. Please verify your code ");
     }
     authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
     );
     return user;
    }
    public void verifyUser(VerifyUserDto input)
    {
    Optional<User> optionalUser = userRepository.findByemail(input.getEmail());
    if(optionalUser.isPresent())
    {
    User user = optionalUser.get();
    if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now()))
    {
        throw new RuntimeException("Verification code expired");
    }
    if(user.getVerificationCode().equals(input.getVerificationCode()))
    {
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }
    else 
    {
        throw new RuntimeException("Verification code mismatched");
    }
    }
    else
    {
        throw new RuntimeException("User not found");
    }
    }
    public void resendVerificationCode(String email)
    {
    Optional<User> optionalUser = userRepository.findByemail(email);
    if(optionalUser.isPresent())
    {
        User user = optionalUser.get();
        if(user.isEnabled())
        {
            throw new RuntimeException("Account already expired");
        }
        user.setVerificationCode(getVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(user);
        userRepository.save(user);
    }
    else
    {
        throw new RuntimeException("User not found");
    }
    }
    public void sendVerificationEmail(User user)
    {
        String email = user.getEmail();
        String subject = "Email Verification";
        String httpMessage = "<html>" +
                "<head>" +
                    "<style>" +
                        "body { font-family: Arial, sans-serif; color: #333; background-color: #f9f9f9; padding: 20px; }" +
                        "h2 { color: #4CAF50; }" +
                        "p { font-size: 16px; line-height: 1.5; }" +
                        ".code { font-size: 24px; font-weight: bold; color: #2d88ff; padding: 10px; border: 2px dashed #2d88ff; display: inline-block; }" +
                        ".footer { font-size: 12px; color: #888; margin-top: 20px; }" +
                    "</style>" +
                "</head>" +
                "<body>" +
                    "<h2>Email Verification</h2>" +
                    "<p>Thank you for signing up! Please use the verification code below to verify your email address:</p>" +
                    "<div class='code'>" + user.getVerificationCode() + "</div>" +
                    "<p>If you did not request this, please ignore this email.</p>" +
                    "<div class='footer'>This is an automated message. Please do not reply.</div>" +
                "</body>" +
            "</html>";
            try 
            { 
            emailService.sendVerificationEmail(email,subject,httpMessage);
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
    }
    public String getVerificationCode()
    {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

      
}
