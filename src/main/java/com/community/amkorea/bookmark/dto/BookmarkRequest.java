package com.community.amkorea.bookmark.dto;

import com.community.amkorea.bookmark.entity.Bookmark;
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

  public Bookmark toEntity() {
    return Bookmark.builder().build();
  }

  @Builder
  public BookmarkRequest(String teamId) {
    this.teamId = teamId;
  }

}
