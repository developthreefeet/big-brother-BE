package com.example.bigbrotherbe.global.jwt.component;

import static com.example.bigbrotherbe.global.jwt.entity.TokenDto.ACCESS_TOKEN;
import static com.example.bigbrotherbe.global.jwt.entity.TokenDto.REFRESH_TOKEN;

import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.enums.ErrorCode;
import com.example.bigbrotherbe.global.jwt.entity.JwtToken;
import com.example.bigbrotherbe.global.jwt.entity.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private static final long ACCESS_TIME = 10 * 60 * 1000L; // 10분
    private static final long REFRESH_TIME = 30 * 60 * 1000L; //30분
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // Member 정보를 가지고 Access Token, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication){
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
            Collectors.joining(","));

        TokenDto tokenDto = createAllToken(authentication.getName(),authorities);

        return JwtToken.builder()
            .grantType("Bearer")
            .accessToken(tokenDto.getAccessToken())
            .refreshToken(tokenDto.getRefreshToken())
            .build();
    }

    private String createToken(String email,String authorities,String type) {
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

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        log.info("Authorities from token: {}", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token){

        try{
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return true;
        }  catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            // 토큰 타입 확인 (예: "type" 클레임에 저장된 값을 확인)
            String tokenType = claims.get("type", String.class);

            if (ACCESS_TOKEN.equals(tokenType)) {
                throw new BusinessException(ErrorCode.ACCESS_TOKEN_EXPIRED);
            } else if (REFRESH_TOKEN.equals(tokenType)) {
                throw new BusinessException(ErrorCode.REFRESH_Token_Expired);
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

    public boolean checkTokenType(String token){
        String tokenType = (String) parseClaims(token).get("type");
        return ACCESS_TOKEN.equals(tokenType);
    }


    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
    public String createTokenByRefreshToken(String refreshToken) {
        Claims claims = getAllClaimsFromToken(refreshToken);
        Date now = new Date();
        Date expiraion = new Date(now.getTime() + ACCESS_TIME);
        return Jwts.builder()
            .setSubject(claims.getSubject())
            .claim("auth",claims.get("auth"))
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
            throw new BusinessException(ErrorCode.REFRESH_Token_Expired);
    }
    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new BusinessException(ErrorCode.ILLEGAL_HEADER_PATTERN);
    }
}
