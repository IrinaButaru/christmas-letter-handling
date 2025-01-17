package com.christmas.letter.processor.security;

import com.christmas.letter.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithUserDetails;

@AllArgsConstructor
public final class WithUserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithUserDetails> {

    private final UserInfoService userInfoService;

    @Override
    public SecurityContext createSecurityContext(WithUserDetails annotation) {
        String username = annotation.value();
//        assert
        UserDetails principal = userInfoService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal,
                principal.getPassword(),
                principal.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
