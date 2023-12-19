package com.community.amkorea.global.Util.Jwt;

import com.community.amkorea.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  /**
   * 토큰 인증 관련 exception 처리
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    Object exception = request.getAttribute("exception");

    if (exception instanceof ErrorCode errorCode) {
      setResponse(response, errorCode);
    }
  }

  /**
   * 한글 출력을 위해 getWriter() 사용
   */
  private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    Map<String, Object> map = new HashMap<>();

    map.put("errorCode", errorCode);
    map.put("errorMessage", errorCode.getDescription());

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(errorCode.getHttpStatus().value());

    response.getWriter().print(this.objectMapper.writeValueAsString(map));
  }
}
