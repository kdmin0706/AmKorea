package com.community.amkorea.global.exception;

import static com.community.amkorea.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.community.amkorea.global.exception.ErrorCode.INVALID_REQUEST;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ErrorResponse handleCustomException(CustomException e) {
    log.error("CustomException is occurred." + e);
    return new ErrorResponse(e.getErrorCode(),
        e.getErrorCode().getHttpStatus(), e.getErrorCode().getDescription());
  }

  /**
   * VALIDATION ERROR
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("CustomException is occurred." + e);
    return new ErrorResponse(INVALID_REQUEST,
        INVALID_REQUEST.getHttpStatus(), INVALID_REQUEST.getDescription());
  }


  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception e) {
    log.error("Exception is occurred." + e);
    return new ErrorResponse(INTERNAL_SERVER_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getDescription());
  }
}
