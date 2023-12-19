package com.community.amkorea.member.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String code;
}
