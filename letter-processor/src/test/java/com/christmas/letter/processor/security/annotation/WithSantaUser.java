package com.christmas.letter.processor.helper;

import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserDetailsSecurityContextFactory.class)
@WithUserDetails(value = "santa@email.com")
public @interface WithSantaUser { }
