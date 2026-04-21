///*
//package com.gym.app.config;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtils {
//
//    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
//
//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${app.jwt.expiration-ms}")
//    private int jwtExpirationMs;
//
//    private Key key() {
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(
//                java.util.Base64.getEncoder().encodeToString(jwtSecret.getBytes())
//        ));
//    }
//
//    public String generateToken(Authentication authentication) {
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
//        return Jwts.builder()
//                .setSubject(principal.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        return Jwts.parserBuilder().setSigningKey(key()).build()
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
//            return true;
//        } catch (MalformedJwtException e)  { log.error("Invalid JWT: {}", e.getMessage()); }
//        catch (ExpiredJwtException e)      { log.error("JWT expired: {}", e.getMessage()); }
//        catch (UnsupportedJwtException e)  { log.error("Unsupported JWT: {}", e.getMessage()); }
//        catch (IllegalArgumentException e) { log.error("JWT claims empty: {}", e.getMessage()); }
//        return false;
//    }
//}
//*/
