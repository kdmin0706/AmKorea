package com.community.amkorea.soccer.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopScorerResponse {

  /**
   * 선수 이름
   */
  @JsonProperty("player_name")
  private String name;

  /**
   * 소속팀
   */
  @JsonProperty("team_name")
  private String teamName;

  /**
   * 등수
   */
  @JsonProperty("player_place")
  private String place;

  /**
   * 득점 수
   */
  @JsonProperty("goals")
  private String goals;

  /**
   * 도움 수
   */
  @JsonProperty("assists")
  private String assists;
}
