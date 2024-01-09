package com.community.amkorea.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReissueDto {

  @NotBlank(message = "엑세스 토큰은 필수입니다.")
  private String accessToken;

  @NotBlank(message = "리프레시 토큰은 필수입니다.")
  private String refreshToken;
}
