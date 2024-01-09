package com.community.amkorea.soccer.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StandingsResponse {

  /**
   * 리그 순위
   */
  @JsonProperty("overall_league_position")
  private String leaguePosition;

  /**
   * 리그 이름
   */
  @JsonProperty("league_name")
  private String leagueName;

  /**
   * 팀 이름
   */
  @JsonProperty("team_name")
  private String teamName;

  /**
   * 승리
   */
  @JsonProperty("overall_league_W")
  private String win;

  /**
   * 무승부
   */
  @JsonProperty("overall_league_D")
  private String draw;

  /**
   * 패배
   */
  @JsonProperty("overall_league_L")
  private String lose;
}
