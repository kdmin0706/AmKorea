package com.community.amkorea.soccer.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerInfo {

  /**
   * 선수 개인 ID
   */
  @JsonProperty("player_key")
  private String playerId;

  /**
   * 선수 이름
   */
  @JsonProperty("player_name")
  private String name;

  /**
   * 포지션
   */
  @JsonProperty("player_type")
  private String position;

  /**
   * 선수 나이
   */
  @JsonProperty("player_age")
  private String age;

  /**
   * 등번호
   */
  @JsonProperty("player_number")
  private String number;

  /**
   * 소속팀
   */
  @JsonIgnore
  private String teamName;
}
