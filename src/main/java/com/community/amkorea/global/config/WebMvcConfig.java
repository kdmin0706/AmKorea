package com.community.amkorea.global.config;

import com.community.amkorea.auth.config.LoginUserArgumentResolver;
import com.community.amkorea.soccer.config.LeagueEnumConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final LoginUserArgumentResolver loginUserArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginUserArgumentResolver);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new LeagueEnumConverter());
  }
}
