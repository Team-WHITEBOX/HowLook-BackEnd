package org.whitebox.howlook.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.whitebox.howlook.global.config.security.exception.TokenException;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Component
@Log4j2
public class JWTUtil {
    private Key key;

    public JWTUtil(@Value("${org.whitebox.jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String userName,Collection<? extends GrantedAuthority> authorities) {
        // 권한 가져오기
        String authority = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(userName)
                .claim("auth", authority)
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(1).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public String generateRefreshToken(String username) {
        long now = (new Date()).getTime();
        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(15).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }


    // 토큰의 정보로 Authentication 가져옴
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new TokenException(JWT_INVALID);
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        log.info(authorities);

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new TokenException(JWT_MALFORM);
        } catch (IllegalArgumentException e) {
            throw new TokenException(JWT_MALFORM);
        } catch (ExpiredJwtException e) {
            throw new TokenException(JWT_EXPIRED);
        }
    }
    
    // JWT 토큰에 들어있는 정보를 꺼냄
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public HashMap<Object,String> parseClaimsByExpiredToken(String accessToken){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
        } catch (ExpiredJwtException e) {
            try {
                String[] splitJwt = accessToken.split("\\.");
                Base64.Decoder decoder = Base64.getDecoder();
                String payload = new String(decoder.decode(splitJwt[1].getBytes()));

                return new ObjectMapper().readValue(payload, HashMap.class);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    public String getUsernameFromJWT(String accessToken) {
        try {
            String[] splitJwt = accessToken.split("\\.");
            Base64.Decoder decoder = Base64.getDecoder();
            String payload = new String(decoder.decode(splitJwt[1].getBytes()));

            HashMap<Object, String> claims = new ObjectMapper().readValue(payload, HashMap.class);
            return claims.get("sub");
        }catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}