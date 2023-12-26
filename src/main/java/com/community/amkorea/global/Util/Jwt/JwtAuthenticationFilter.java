package com.community.amkorea.global.Util.Jwt;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.global.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;
  private final RedisService redisService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = tokenProvider.resolveTokenFromRequest(request);

    try {
      if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
        //로그아웃 조건
        if (Objects.isNull(redisService.getData(token))) {
          Authentication authentication = tokenProvider.getAuthentication(token);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR);
        }
      }
    } catch (CustomException e) {
      request.setAttribute("exception", e.getErrorCode());
    }

    filterChain.doFilter(request, response);
  }
}
