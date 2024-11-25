package com.kathir.BlogApi.security.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.kathir.BlogApi.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${security.jwt.secret-key}")
  private String jwtSecret;

  @Value("${security.jwt.expiration-time}")
  private int jwtExpirationMs;

  @Value("${security.app.jwtCookieName}")
  private String jwtCookie;

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
    return cookie;
  }

  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }
 
  public String getUserNameFromJwtToken(String token) {
      return extractClaims(token,Claims::getSubject);
  }
  public <T> T extractClaims(String token,Function<Claims,T> claimsResolver)
  {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
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
     private SecretKey getKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().verifyWith(getKey()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  public String generateTokenFromUsername(String username) {   
     return Jwts
    .builder()
    .claims()
    .subject(username)
    .issuedAt(new Date(System.currentTimeMillis()))
    .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
    .and()
    .signWith(getKey())
    .compact();
  }
}
