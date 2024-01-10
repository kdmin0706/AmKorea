package com.community.amkorea.soccer.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoachInfo {

  @JsonProperty("coach_name")
  private String name;

  @JsonProperty("coach_country")
  private String country;

  @JsonProperty("coach_age")
  private String age;
}
