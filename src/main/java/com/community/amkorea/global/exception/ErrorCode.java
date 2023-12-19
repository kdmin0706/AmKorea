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
  INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다."),
  EMAIL_NOT_VERITY(HttpStatus.BAD_REQUEST, "이메일 인증이 되지 않았습니다."),

  /**
   * 401 Unauthorized
   */
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 접근 권한이 없습니다."),
  UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다."),
  WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다."),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
  UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),

  /**
   * 404 Not Found
   */
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
  EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다"),

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
