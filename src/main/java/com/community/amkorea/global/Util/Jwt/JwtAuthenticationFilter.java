package com.community.amkorea.global.Util.Jwt;

import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.global.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        //access-token 로그아웃 여부 확인
        if (redisService.getData(token) != null) {
          Authentication authentication = tokenProvider.getAuthentication(token);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (SecurityException | MalformedJwtException e) {
      request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
    } catch (ExpiredJwtException e) {
      request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN);
    } catch (UnsupportedJwtException e) {
      request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN);
    } catch (IllegalArgumentException e) {
      request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN);
    } catch (Exception e) {
      log.error("doFilterInternal() Exception Message : {}", e.getMessage());
      request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR);
    }

    filterChain.doFilter(request, response);
  }
}
