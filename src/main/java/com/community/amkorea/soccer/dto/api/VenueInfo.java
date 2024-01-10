package com.community.amkorea.soccer.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VenueInfo {

  /**
   * 구장 이름
   */
  @JsonProperty("venue_name")
  private String name;

  /**
   * 구장 주소
   */
  @JsonProperty("venue_address")
  private String address;

  /**
   * 구장 연고지
   */
  @JsonProperty("venue_city")
  private String city;

}
