package com.kathir.BlogApi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("security.jwt.secret-key")
    private String secretkey;

    @Value("security.jwt.expiration-time")
    private long expiration;

    public String extractUsername(String token)
    {
        return extractClaims(token,Claims::getSubject);
    }
    public <T> T extractClaims(String token,Function<Claims,T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails)
    {
        return generateToken(new HashMap<>(),userDetails);
    }
    private String generateToken(Map<String,Object> claims,UserDetails userDetails)
    {
    return buildToken(claims,userDetails,expiration);
    }
    private String buildToken(Map<String,Object> claims,UserDetails userDetails,long exp)
    {
        return Jwts
        .builder()
        .claims()
        .add(claims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis()*expiration))
        .and()
        .signWith(getKey())
        .compact();

    }
    public long getExpirationTime()
    {
        return expiration;
    }
    private SecretKey getKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaims(String token)
    {
       return Jwts.
       parser()
       .verifyWith(getKey())
       .build()
       .parseSignedClaims(token)
       .getPayload();
    }
    public boolean validateToken(String token,UserDetails userDetails)
    {
    final String userName = extractUsername(token);
    return userName.equals(userDetails.getUsername())&&(!isTokenExpired(token));
    }
    public boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token)
    {
        return extractClaims(token, Claims::getExpiration);
    }
}
