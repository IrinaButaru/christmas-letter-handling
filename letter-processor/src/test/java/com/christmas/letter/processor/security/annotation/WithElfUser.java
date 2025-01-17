package com.christmas.letter.processor.security.annotation;

import com.christmas.letter.processor.helper.UserTestHelper;
import com.christmas.letter.processor.security.WithUserDetailsSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserDetailsSecurityContextFactory.class)
@WithUserDetails(value = UserTestHelper.ELF_EMAIL)
public @interface WithElfUser {
}
