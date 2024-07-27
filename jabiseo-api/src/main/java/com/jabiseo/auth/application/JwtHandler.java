package com.jabiseo.auth.application;

import com.jabiseo.auth.exception.AuthenticationBusinessException;
import com.jabiseo.auth.exception.AuthenticationErrorCode;
import com.jabiseo.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtHandler {

    private final Key accessKey;
    private final Key refreshKey;
    private final Integer accessExpiredMin;
    private final Integer refreshExpiredDay;
    private final String APP_ISSUER = "jabiseo";

    public JwtHandler(JwtProperty jwtProperty) {
        byte[] accessEncodeByte = Base64.getEncoder().encode((jwtProperty.getAccessKey().getBytes()));
        byte[] refreshEncodeByte = Base64.getEncoder().encode(jwtProperty.getRefreshKey().getBytes());
        this.accessExpiredMin = jwtProperty.getAccessExpiredMin();
        this.refreshExpiredDay = jwtProperty.getRefreshExpiredDay();
        this.accessKey = Keys.hmacShaKeyFor(accessEncodeByte);
        this.refreshKey = Keys.hmacShaKeyFor(refreshEncodeByte);
    }


    public String createAccessToken(Member member) {
        System.out.println(this.accessExpiredMin);
        Instant accessExpiredTime = Instant.now()
                .plus(this.accessExpiredMin, ChronoUnit.MINUTES);
        System.out.println(accessExpiredTime);
        System.out.println(Instant.now());
        Map<String, Object> payload = new HashMap<>();

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuer(APP_ISSUER)
                .setExpiration(Date.from(accessExpiredTime))
                .addClaims(payload)
                .signWith(accessKey)
                .compact();
    }

    public String createRefreshToken() {
        Instant refreshExpiredTime = Instant.now()
                .plus(this.refreshExpiredDay, ChronoUnit.DAYS);
        System.out.println("refreshExpiredTime: " + refreshExpiredTime);
        return Jwts.builder()
                .setExpiration(Date.from(refreshExpiredTime))
                .signWith(refreshKey)
                .compact();
    }


    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(refreshKey)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.EXPIRED_APP_JWT);
        } catch (Exception e) {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.INVALID_APP_JWT);
        }
    }


    public Claims getClaimFromExpiredAccessToken(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.INVALID_APP_JWT);
        }
    }

    public Claims getClaimsFromAccessToken(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.EXPIRED_APP_JWT);
        } catch (Exception e) {
            throw new AuthenticationBusinessException(AuthenticationErrorCode.INVALID_APP_JWT);
        }
    }
}
