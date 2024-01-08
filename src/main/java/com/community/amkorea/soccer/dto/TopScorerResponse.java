package com.community.amkorea.soccer.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TopScorerResponse {

  /**
   * 선수 이름
   */
  private String name;

  /**
   * 소속팀
   */
  private String teamName;

  /**
   * 등수
   */
  private String place;

  /**
   * 득점 수
   */
  private String goals;

  /**
   * 도움 수
   */
  private String assists;
}
