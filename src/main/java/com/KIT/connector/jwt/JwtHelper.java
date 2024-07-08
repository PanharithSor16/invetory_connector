package com.KIT.connector.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtHelper {
    private SecretKey secretKey;
    @Value("${jwt.expiration}")
    private long accessTokenExpirationMs;
    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpirationMs;
    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    private void init(){
        if (secret.length() < 32) {
            throw new IllegalArgumentException("Jwt secrete key must be at least 256 bite (32 bytes) long.");
        }
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
    private Claims extractAllClaims (String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token) {
        return  extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token){ return extractClaim(token, Claims::getExpiration);}
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    private String createToken(Map<String, Objects> claims, String subject, long accessTokenExpirationMs){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(UserDetails userDetails){
        Map<String, Objects> claims =new HashMap<>();
        return createToken(claims, userDetails.getUsername(), accessTokenExpirationMs);
    }
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Objects> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshTokenExpirationMs);
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
