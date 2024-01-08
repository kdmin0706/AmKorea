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
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
  INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "인증번호가 올바르지 않습니다."),
  EMAIL_NOT_VERITY(HttpStatus.BAD_REQUEST, "이메일 인증이 되지 않았습니다."),
  WRITE_NOT_YOURSELF(HttpStatus.BAD_REQUEST, "본인이 작성하지 않았습니다."),
  POST_ALREADY_LIKE(HttpStatus.BAD_REQUEST, "현재 게시물은 좋아요가 눌려있습니다."),
  POST_ALREADY_UNLIKE(HttpStatus.BAD_REQUEST, "현재 게시물은 좋아요 상태가 아닙니다."),
  COMMENT_ALREADY_LIKE(HttpStatus.BAD_REQUEST, "현재 댓글은 좋아요가 눌려있습니다."),
  COMMENT_ALREADY_UNLIKE(HttpStatus.BAD_REQUEST, "현재 댓글은 좋아요 상태가 아닙니다."),
  PLAYER_NOT_TEAM(HttpStatus.BAD_REQUEST, "해당 팀에 소속되어 있지 않은 선수입니다."),
  REQUEST_PARAM_NOT_MATCH(HttpStatus.BAD_REQUEST, "요청 파라미터가 알맞지 않습니다"),
  ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 등록되어있습니다."),

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
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다"),
  POST_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글 카테고리입니다"),
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다"),
  TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다"),
  PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 선수입니다"),
  BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 북마크입니다."),


  /**
   * 409 conflict
   */
  DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),


  /**
   * 500 Internal Server Error
   */
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
  API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "API 로드시 오류가 발생했습니다.");


  private final HttpStatus httpStatus;
  private final String description;
}
