package com.community.amkorea.auth.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.community.amkorea.global.Util.Jwt.CustomAuthenticationEntryPoint;
import com.community.amkorea.global.Util.Jwt.CustomDeniedHandler;
import com.community.amkorea.global.Util.Jwt.JwtAuthenticationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomDeniedHandler customDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers(requestHasRoleUser()).hasRole("USER")
                .requestMatchers(requestHasRoleAdmin()).hasRole("ADMIN")
                .anyRequest().permitAll()
        )
        .exceptionHandling(configurer -> {
          configurer.authenticationEntryPoint(customAuthenticationEntryPoint);
          configurer.accessDeniedHandler(customDeniedHandler);
        })
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  private RequestMatcher[] requestHasRoleUser() {
    List<RequestMatcher> requestMatchers = List.of(
        antMatcher("/api/member"),
        antMatcher(POST, "/api/post"),
        antMatcher(PUT, "/api/post"),
        antMatcher(DELETE, "/api/post"),
        antMatcher(POST, "/api/post/like"),
        antMatcher(POST, "/api/post/unlike")
    );
    return requestMatchers.toArray(RequestMatcher[]::new);
  }

  private RequestMatcher[] requestHasRoleAdmin() {
    List<RequestMatcher> requestMatchers = List.of(
        antMatcher("/api/admin/**")
    );

    return requestMatchers.toArray(RequestMatcher[]::new);
  }

}
