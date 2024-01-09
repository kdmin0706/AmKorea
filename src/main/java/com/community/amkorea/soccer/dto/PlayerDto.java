package com.community.amkorea.soccer.dto;

import com.community.amkorea.soccer.entity.Player;
import com.community.amkorea.soccer.entity.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerDto {

  /**
   * 선수 개인 ID
   */
  private String playerId;

  /**
   * 선수 이름
   */
  private String name;

  /**
   * 포지션
   */
  private String position;

  /**
   * 선수 나이
   */
  private String age;

  /**
   * 등번호
   */
  private String number;

  /**
   * 소속팀
   */
  private String teamName;

  public Player toEntity(Team team, String playerId) {
    return Player.builder()
        .id(playerId)
        .name(name)
        .position(position)
        .age(age)
        .number(number)
        .team(team)
        .build();
  }

  public static PlayerDto fromEntity(Player player) {
    return PlayerDto.builder()
        .playerId(player.getId())
        .name(player.getName())
        .position(player.getPosition())
        .age(player.getAge())
        .number(player.getNumber())
        .teamName(player.getTeam().getName())
        .build();
  }
}
