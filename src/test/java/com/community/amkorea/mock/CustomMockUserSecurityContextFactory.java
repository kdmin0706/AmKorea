package com.community.amkorea.mock;

import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class CustomMockUserSecurityContextFactory implements
    WithSecurityContextFactory<CustomMockUser> {

  @Override
  public SecurityContext createSecurityContext(CustomMockUser annotation) {
    String email = annotation.email();

    Authentication auth = new UsernamePasswordAuthenticationToken(email, "",
        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(auth);

    return context;
  }
}
