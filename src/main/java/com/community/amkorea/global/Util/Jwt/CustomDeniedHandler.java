package com.community.amkorea.global.Util.Jwt;

import com.community.amkorea.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomDeniedHandler implements AccessDeniedHandler {

  /**
   * 권한에 맞지 않은 요청에 대한 exception
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    setResponse(response);
  }

  /**
   * 한글 출력을 위해 getWriter() 사용
   */
  private void setResponse(HttpServletResponse response) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<>();

    map.put("errorCode", ErrorCode.UNAUTHORIZED);
    map.put("errorMessage", ErrorCode.UNAUTHORIZED.getDescription());

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(ErrorCode.UNAUTHORIZED.getHttpStatus().value());

    response.getWriter().print(objectMapper.writeValueAsString(map));
  }
}