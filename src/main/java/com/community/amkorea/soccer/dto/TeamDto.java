package com.community.amkorea.soccer.dto;

import com.community.amkorea.soccer.entity.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamDto {

  /**
   * 팀 개별 ID
   */
  private String teamId;

  /**
   * 팀 이름
   */
  private String name;

  /**
   * 팀 소속 국가
   */
  private String country;

  /**
   * 팀 경기장
   */
  private String venue;

  /**
   * 감독
   */
  private String coach;

  public Team toEntity() {
    return Team.builder()
        .id(teamId)
        .name(name)
        .country(country)
        .venue(venue)
        .coach(coach)
        .build();
  }

  public static TeamDto fromEntity(Team team) {
    return TeamDto.builder()
        .teamId(team.getId())
        .name(team.getName())
        .country(team.getCountry())
        .venue(team.getVenue())
        .coach(team.getCoach())
        .build();
  }

}
