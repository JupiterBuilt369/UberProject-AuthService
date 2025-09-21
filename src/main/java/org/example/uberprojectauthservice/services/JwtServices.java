package org.example.uberprojectauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServices implements CommandLineRunner {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    private Key getSignKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private String generateToken(Map<String, Object> payload, String email) {

        return Jwts.builder()
                .setClaims(payload)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    };


    private Claims extractAllPayloads(String token) {
        return Jwts.parserBuilder()
                            .setSigningKey(getSignKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllPayloads(token);
        return claimsResolver.apply(claims);
    }

    private Object extractPayload(String token, String payloadKey){
        Claims claim = (Claims) extractAllPayloads(token);
        return (Object)claim.get(payloadKey);
    }

    private String extractEmail(String token) {
        return extractClaim(token,Claims::getSubject);
    }


    public boolean isTokenValid(String token,  String email) {
        final String userEmailFetchedFromToken = extractEmail(token);
        return (userEmailFetchedFromToken.equals(email) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token,Claims :: getExpiration).before(new Date());
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("email","a@b.c ");
        payload.put("username","jaiShreeRam");
        payload.put("password","a123");
        payload.put("phoneNumber","123456789");
        String jwtToken = generateToken(payload,"a@b.c ");
        System.out.println(isTokenValid(jwtToken,"a@b.c "));
        System.out.println("JWT Token: " + jwtToken);
        System.out.println(extractPayload(jwtToken,"phoneNumber"));
        System.out.println(extractPayload(jwtToken,"email"));
        System.out.println(extractClaim(jwtToken,Claims::getSubject));
    }
}
