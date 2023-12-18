package com.community.amkorea.global.Util.Mail.dto;

import jakarta.validation.constraints.Email;
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
public class VerifyMailRequest {

  @NotBlank(message = "이메일 입력은 필수입니다.")
  @Email(message = "이메일 형식에 맞게 입력해주세요")
  private String email;

  @NotBlank(message = "인증번호를 맞게 입력해주세요")
  private String code;

}
