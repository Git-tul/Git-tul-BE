package io.gittul.infra.auth.jwt;

import io.gittul.infra.auth.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;


    /**
     * JWT 토큰 생성
     */
    public String createToken(TokenUserInfo userInfo) {
        Date now = new Date();
        Date accessExpiration = new Date(now.getTime() + this.expirationTime);

        return Jwts.builder()
                .subject(userInfo.userName())
                .claim("userId", userInfo.userId())
                .claim("email", userInfo.email())
                .issuedAt(now)
                .expiration(accessExpiration)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    public TokenUserInfo validateToken(String token) {
        try {
            return parseToken(token);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                throw AuthenticationException.EXPIRED_TOKEN;
            }

            throw AuthenticationException.INVALID_TOKEN;
        }
    }

    private TokenUserInfo parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        System.out.println(claims);

        return new TokenUserInfo(
                claims.get("userId", Long.class),
                claims.getSubject(), // userName
                claims.get("email", String.class)
        );
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
