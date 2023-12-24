package com.community.amkorea.member.dto;

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

  private String nickname;
  private String password;
  private String phoneNumber;

  public static UpdateMemberDto from(MemberDto memberDto) {
    return UpdateMemberDto.builder()
        .nickname(memberDto.getNickname())
        .password(memberDto.getPassword())
        .phoneNumber(memberDto.getPhoneNumber())
        .build();
  }
}
