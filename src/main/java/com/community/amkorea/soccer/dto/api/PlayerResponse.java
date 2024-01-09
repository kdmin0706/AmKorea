package com.community.amkorea.soccer.dto.api;

import com.community.amkorea.soccer.dto.api.PlayerInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerResponse {

  /**
   * 팀 이름
   */
  @JsonProperty("team_name")
  private String name;

  /**
   * 선수 정보
   */
  @JsonProperty("players")
  private List<PlayerInfo> playerInfos;

}
