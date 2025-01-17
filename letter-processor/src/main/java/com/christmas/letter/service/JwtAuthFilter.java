package com.christmas.letter.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
//@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth")
                || request.getServletPath().startsWith("/sender");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String username = null;
            String token = null;
            String authHeader = request.getHeader(AUTH_HEADER_NAME);

            if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null || authentication == null) {
                authenticate(username, token, request);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
        }
    }

    private void authenticate(String username, String token, HttpServletRequest request) {
        UserDetails userDetails = userInfoService.loadUserByUsername(username);

        if (jwtService.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null,
                    userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}
