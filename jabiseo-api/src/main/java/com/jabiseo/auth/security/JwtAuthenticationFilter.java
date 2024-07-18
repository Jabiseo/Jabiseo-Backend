package com.jabiseo.auth.security;

import com.jabiseo.auth.application.JwtClaim;
import com.jabiseo.auth.application.JwtHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String HEADER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtHandler.validateAccessToken(token)) {
            JwtClaim jwtClaim = jwtHandler.getClaimsFromAccessToken(token);
            setAuthenticationToContext(jwtClaim);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(JwtClaim jwtClaim) {
        String memberId = jwtClaim.getSubject();
        Set<GrantedAuthority> authorities = new HashSet<>();
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, "",
                authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(HEADER_PREFIX.length());
        }
        return null;
    }
}
