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
public class StandingsResponse {

  /**
   * 리그 순위
   */
  private String leaguePosition;

  /**
   * 리그 이름
   */
  private String leagueName;

  /**
   * 팀 이름
   */
  private String teamName;

  /**
   * 승리
   */
  private String win;

  /**
   * 무승부
   */
  private String draw;

  /**
   * 패배
   */
  private String lose;
}
