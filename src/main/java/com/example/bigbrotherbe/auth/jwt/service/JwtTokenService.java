package com.example.bigbrotherbe.auth.jwt.service;

import static com.example.bigbrotherbe.common.exception.enums.ErrorCode.ACCESS_TOKEN_EXPIRED;
import static com.example.bigbrotherbe.common.exception.enums.ErrorCode.REFRESH_TOKEN_EXPIRED;
import static com.example.bigbrotherbe.auth.jwt.dto.TokenDto.ACCESS_TOKEN;
import static com.example.bigbrotherbe.auth.jwt.dto.TokenDto.REFRESH_TOKEN;

import com.example.bigbrotherbe.common.exception.BusinessException;
import com.example.bigbrotherbe.common.exception.enums.ErrorCode;
import com.example.bigbrotherbe.auth.jwt.entity.JwtToken;
import com.example.bigbrotherbe.auth.jwt.dto.TokenDto;
import com.example.bigbrotherbe.auth.custom.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenService {
    private final Key key;
    private static final long ACCESS_TIME = 10 * 60 * 1000L; // 10분
    private static final long REFRESH_TIME = 30 * 60 * 1000L; //30분

    private final CustomUserDetailsService customerDetailsService;

    public JwtTokenService(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customerDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customerDetailsService = customerDetailsService;
    }

    // Member 정보를 가지고 Access Token, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
                Collectors.joining(","));

        TokenDto tokenDto = createAllToken(authentication.getName(), authorities);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }

    private String createToken(String email, String authorities, String type) {
        long now = new Date().getTime();
        long expiration = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;
        return Jwts.builder()
                .setSubject(email)
                .claim("auth", authorities)
                .claim("type", type)
                .setExpiration(new Date(now + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        log.info("Parsed claims: {}", claims);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        UserDetails principal = customerDetailsService.loadUserByUsername(
            claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    public boolean validateToken(String token) {

        try {
        Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            // 토큰 타입 확인 (예: "type" 클레임에 저장된 값을 확인)
            String tokenType = claims.get("type", String.class);

            if (ACCESS_TOKEN.equals(tokenType)) {
                throw new BusinessException(ACCESS_TOKEN_EXPIRED);
            } else if (REFRESH_TOKEN.equals(tokenType)) {
                throw new BusinessException(REFRESH_TOKEN_EXPIRED);
            } else {
                log.info("알 수 없는 토큰 타입입니다.");
            }
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않은 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public boolean checkTokenType(String token) {
        String tokenType = (String) parseClaims(token).get("type");
        return ACCESS_TOKEN.equals(tokenType);
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String createTokenByRefreshToken(String refreshToken) {
        Claims claims = getAllClaimsFromToken(refreshToken);
        Date now = new Date();
        Date expiraion = new Date(now.getTime() + ACCESS_TIME);
        return Jwts.builder()
                .setSubject(claims.getSubject())
                .claim("auth", claims.get("auth"))
                .claim("type", ACCESS_TOKEN)
                .setExpiration(expiraion)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenDto createAllToken(String email, String role) {
        return new TokenDto(createToken(email, role, ACCESS_TOKEN), createToken(email, role, REFRESH_TOKEN));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public TokenDto refreshAccessToken(String refreshToken) {

        String resolveToken = resolveToken(refreshToken);

        if (validateToken(resolveToken)) {
            String newAccessToken = createTokenByRefreshToken(resolveToken);
            Authentication authentication = getAuthentication(newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return TokenDto
                    .builder()
                    .accessToken(newAccessToken)
                    .refreshToken(resolveToken)
                    .build();
        } else
            throw new BusinessException(REFRESH_TOKEN_EXPIRED);
    }

    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new BusinessException(ErrorCode.ILLEGAL_HEADER_PATTERN);
    }

}
