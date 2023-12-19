package com.community.amkorea.global.Util.Jwt.dto;

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
public class CustomUserDto {

  private String email;

  private String password;

  private String roleType;

  public static CustomUserDto fromEntity(Member member) {
    return CustomUserDto.builder()
        .email(member.getEmail())
        .password(member.getPassword())
        .roleType(member.getRoleType().getCode())
        .build();
  }
}
