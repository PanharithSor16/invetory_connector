package com.KIT.connector.jwt;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j
public class JwtHelper {
    @Value("${jwt.expiration}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpirationMs;
    @Value("${jwt.secret}")
    private String secret;

}
