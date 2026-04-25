package com.example.Blog.Hub.service.impl;

import com.example.Blog.Hub.dto.UserDTO;
import com.example.Blog.Hub.utills.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTTokenGenerationService {

    public static String generateJWTToken(UserDTO userDTO){
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

            String jwt = Jwts.builder()
                    .setIssuer("Blog Hub")
                    .setSubject("JWT Token")
                    .claim("username",userDTO.getEmail())
                    .claim("userId",userDTO.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
                    .signWith(key).compact();

            return jwt;
    }
}
