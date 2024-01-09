package com.community.amkorea.soccer.enums;

import static com.community.amkorea.global.exception.ErrorCode.LEAGUE_PARAM_NOT_MATCH;

import com.community.amkorea.global.exception.CustomException;
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
    if (requestLeague == null) {
      throw new CustomException(LEAGUE_PARAM_NOT_MATCH);
    }

    for (League value : League.values()) {
      if (value.toString().equals(requestLeague)) {
        return value;
      }
    }

    throw new CustomException(LEAGUE_PARAM_NOT_MATCH);
  }
}
