package com.community.amkorea.soccer.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum League {
  PREMIER_LEAGUE("잉글랜드", 152),
  LA_LIGA("스페인", 302),
  BUNDESLIGA("독일", 168),
  SERIE_A("이탈리아", 207),
  LIGUE_1("프랑스", 168);

  private final String country;
  private final int code;

  public static League create(String requestLeague) {
    return Arrays.stream(League.values())
        .filter(v -> v.toString().equals(requestLeague))
        .findAny()
        .orElse(null);
  }
}
