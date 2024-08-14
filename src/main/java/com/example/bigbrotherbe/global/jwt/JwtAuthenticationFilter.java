package com.example.bigbrotherbe.global.jwt;
import com.example.bigbrotherbe.global.exception.BusinessException;
import com.example.bigbrotherbe.global.exception.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            String token = resolveToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)
                && jwtTokenProvider.checkTokenType(token)) {
                // 유효한 엑세스 토큰이 있는 경우
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch (BusinessException e) {
            handleException((HttpServletResponse) servletResponse, e);
            // 예외 처리 로직
        }

    }
    private void handleException(HttpServletResponse response, BusinessException e) throws IOException {

        if (e != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = new ObjectMapper().writeValueAsString(ApiResponse.error(e.getErrorCode()));
            response.getWriter().write(jsonResponse);  // ApiResponse의 내용을 JSON으로 변환하여 작성
        } else {
            response.getWriter().write("{\"error\": \"An unexpected error occurred.\"}");
        }
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}