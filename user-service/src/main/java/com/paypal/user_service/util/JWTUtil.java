package com.paypal.user_service.util;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {
    private static final String SECRET = "secret123secret123secret123secret123secret123secret123";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractEmail(String token) {
        return Jwts.parser()                        // ✅ replaces parserBuilder()
                .verifyWith(getSigningKey())         // ✅ replaces setSigningKey()
                .build()
                .parseSignedClaims(token)           // ✅ replaces parseClaimsJws()
                .getPayload()                       // ✅ replaces getBody()
                .getSubject();
    }

    public String extractUsername(String token) {
        return extractEmail(token);                 // same logic, no need to duplicate
    }

    public boolean validateToken(String token, String username) {
        try {
            extractEmail(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)                     // ✅ replaces setClaims()
                .subject(email)                     // ✅ replaces setSubject()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey())           // ✅ no need to specify algorithm
                .compact();
    }
    public String extractRole(String token){
        return (String) Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }
}
//public class JWTUtil {
//    private static  final String SECRET = "secret123secret123secret123secret123secret123secret123";
//
//    private Key getSigningKey(){
//        return Keys.hmacShaKeyFor(SECRET.getBytes());
//    }
//    public String extractEmail(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token, String username){
//        try{
//            extractEmail(token);
//            return true;
//        }catch(Exception e){
//            return false;
//        }
//    }
//
//    public String extractUsername(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public String generateToken(Map<String, Object> claims, String email){
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(email)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 866400000))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();//build
//    }
//}
