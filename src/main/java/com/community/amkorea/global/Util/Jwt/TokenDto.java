package com.community.amkorea.global.Util.Jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
  private String accessToken;
  private String refreshToken;

  public static TokenDto of(String accessToken, String refreshToken) {
    return TokenDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
