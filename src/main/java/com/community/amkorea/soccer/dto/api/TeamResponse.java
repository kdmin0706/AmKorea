package com.community.amkorea.soccer.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamResponse {
  /**
   * 팀 개별 ID
   */
  @JsonProperty("team_key")
  private String teamId;

  /**
   * 팀 이름
   */
  @JsonProperty("team_name")
  private String name;

  /**
   * 팀 소속 국가
   */
  @JsonProperty("team_country")
  private String country;

  /**
   * 경기장 정보
   */
  @JsonProperty("venue")
  private VenueInfo venueInfos;

  /**
   * 선수 정보
   */
  @JsonProperty("coaches")
  private List<CoachInfo> coachInfos;
}
