package com.community.amkorea.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  /**
   * 400 Bad Request
   */
  PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),


  /**
   * 401 Unauthorized
   */
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 접근 권한이 없습니다."),

  /**
   * 404 Not Found
   */
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

  /**
   * 409 conflict
   */
  DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),

  /**
   * 500 Internal Server Error
   */
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.");


  private final HttpStatus httpStatus;
  private final String description;
}
