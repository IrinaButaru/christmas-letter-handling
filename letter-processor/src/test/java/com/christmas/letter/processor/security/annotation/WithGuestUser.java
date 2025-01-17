package com.christmas.letter.processor.security.annotation;

import com.christmas.letter.processor.security.WithUserDetailsSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.christmas.letter.processor.helper.UserTestHelper.GUEST_EMAIL;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserDetailsSecurityContextFactory.class)
@WithUserDetails(value = GUEST_EMAIL)
public @interface WithGuestUser {
}
