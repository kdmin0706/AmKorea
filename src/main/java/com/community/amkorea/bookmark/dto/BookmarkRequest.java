package com.community.amkorea.bookmark.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkRequest {

  @NotBlank(message = "팀 아이디를 입력해주세요")
  private String teamId;

  @Builder
  public BookmarkRequest(String teamId) {
    this.teamId = teamId;
  }

}
