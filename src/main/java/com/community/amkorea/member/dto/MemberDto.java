package com.community.amkorea.member.dto;

import com.community.amkorea.member.entity.Member;
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
public class MemberDto {

  private Long id;
  private String email;
  private String nickname;
  private String password;
  private String phoneNumber;
  private String roleType;
  private boolean emailAuth;

  public static MemberDto fromEntity(Member member) {
    return MemberDto.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .password(member.getPassword())
        .roleType(member.getRoleType().getCode())
        .phoneNumber(member.getPhoneNumber())
        .emailAuth(member.isEmailAuth())
        .build();
  }
}
