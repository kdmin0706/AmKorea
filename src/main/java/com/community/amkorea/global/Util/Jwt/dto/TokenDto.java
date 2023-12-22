package com.community.amkorea.global.Util.Jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
  private String accessToken;
  private String refreshToken;

  private Long accessTokenExpireTime;
  private Long refreshTokenExpireTime;
}
