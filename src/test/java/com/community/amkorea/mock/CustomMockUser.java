package com.community.amkorea.mock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomMockUserSecurityContextFactory.class)
public @interface CustomMockUser {

  String email() default "test@test.com";

  String role() default "ROLE_USER";
}
