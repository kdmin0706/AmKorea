package com.community.amkorea.soccer.config;

import com.community.amkorea.soccer.enums.League;
import org.springframework.core.convert.converter.Converter;

public class LeagueEnumConverter implements Converter<String, League> {
  @Override
  public League convert(String requestCategory) {
    return League.create(requestCategory.toUpperCase());
  }
}