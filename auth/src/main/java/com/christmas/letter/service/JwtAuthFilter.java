package com.christmas.letter.service;

import com.christmas.letter.exception.GlobalExceptionHandler;
import com.christmas.letter.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserInfoService userInfoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTH_HEADER_NAME);
        if(authHeader == null || !authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX))
            throw new UnauthorizedException(GlobalExceptionHandler.UNAUTHORIZED_MESSAGE);

        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(username != null || authentication == null) {
                authenticate(username, token, request);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            throw new UnauthorizedException(GlobalExceptionHandler.UNAUTHORIZED_MESSAGE);
        }
    }

    private void authenticate(String username, String token, HttpServletRequest request) {
        UserDetails userDetails = userInfoService.loadUserByUsername(username);

        if(jwtService.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null,
                    userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}
