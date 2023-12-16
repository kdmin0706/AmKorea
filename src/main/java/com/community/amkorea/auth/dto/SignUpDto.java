package com.community.amkorea.auth.dto;

import com.community.amkorea.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class SignUpDto {

  @Email
  private String email;

  @NotBlank
  private String password;

  @NotBlank
  @Size(max = 10)
  private String nickname;

  @NotBlank
  private String phoneNumber;

  public static SignUpDto fromEntity(Member member) {
    return SignUpDto.builder()
        .email(member.getEmail())
        .nickname(member.getNickname())
        .password(member.getPassword())
        .phoneNumber(member.getPhoneNumber())
        .build();
  }
}
