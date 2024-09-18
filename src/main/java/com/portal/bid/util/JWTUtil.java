package com.portal.bid.util;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

    @Value("${app.secret.key}")
    private String secret_key;

    // code to generate Token with permissions
    public String generateToken(String subject, List<String> permissions) {
        String tokenId = String.valueOf(new Random().nextInt(10000));

        return Jwts.builder()
                .setId(tokenId)
                .setSubject(subject)
                .setIssuer("ABC_Ltd")
                .setAudience("XYZ_Ltd")
                .claim("permissions", permissions)  // Adding permissions as a claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(secret_key.getBytes()))
                .compact();
    }

    // code to get Claims from token
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secret_key.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    // Fetch permissions from the token
    public List<String> getPermissionsFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("permissions", List.class);  // Extracting permissions claim
    }

    // code to check if token is valid
    public boolean isValidToken(String token) {
        return getClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));
    }

    // code to check if token is valid for a specific username
    public boolean isValidToken(String token, String username) {
        String tokenUserName = getSubject(token);
        return (username.equals(tokenUserName) && !isTokenExpired(token));
    }

    // code to check if token is expired
    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date(System.currentTimeMillis()));
    }

    // code to get expiration date
    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    // code to get subject (username) from token
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }
}
