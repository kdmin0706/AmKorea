package com.community.amkorea.bookmark.dto;

import com.community.amkorea.bookmark.entity.Bookmark;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkResponse {

  private Long memberId;

  private String teamId;

  private String teamName;

  private String country;

  private String venue;

  private String coach;

  public static BookmarkResponse fromEntity(Bookmark bookmark) {
    return BookmarkResponse.builder()
        .memberId(bookmark.getMember().getId())
        .teamId(bookmark.getTeam().getId())
        .teamName(bookmark.getTeam().getName())
        .country(bookmark.getTeam().getCountry())
        .venue(bookmark.getTeam().getVenue())
        .coach(bookmark.getTeam().getCoach())
        .build();
  }
}
