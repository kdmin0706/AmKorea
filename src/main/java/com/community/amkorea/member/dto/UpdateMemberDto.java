package com.community.amkorea.member.dto;

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
public class UpdateMemberDto {

  @NotBlank(message = "닉네임 입력은 필수입니다.")
  private String nickname;

  @NotBlank(message = "비밀번호 입력은 필수입니다.")
  private String password;

  @NotBlank(message = "휴대전화번호 입력은 필수입니다.")
  private String phoneNumber;

  public static UpdateMemberDto from(MemberDto memberDto) {
    return UpdateMemberDto.builder()
        .nickname(memberDto.getNickname())
        .password(memberDto.getPassword())
        .phoneNumber(memberDto.getPhoneNumber())
        .build();
  }
}
