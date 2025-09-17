package org.example.uberprojectauthservice.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService implements CommandLineRunner {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;


    private String generateToken(Map<String, Object> payload, String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(payload)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    };

    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("email","a@b.c" );
        payload.put("username","a@b.c");
        payload.put("password","a123");
        String jwtToken = generateToken(payload,"Shivam");
        System.out.println("JWT Token: " + jwtToken);
    }
}
