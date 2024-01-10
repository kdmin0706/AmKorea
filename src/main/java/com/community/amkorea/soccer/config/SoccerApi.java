package com.community.amkorea.soccer.config;

import com.community.amkorea.soccer.dto.api.PlayerResponse;
import com.community.amkorea.soccer.dto.api.StandingsResponse;
import com.community.amkorea.soccer.dto.api.TeamResponse;
import com.community.amkorea.soccer.dto.api.TopScorerResponse;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface SoccerApi {
  @GetExchange("/")
  List<TeamResponse> getApiTeam(@RequestParam("action") String action,
      @RequestParam("league_id") int No,
      @RequestParam("APIkey") String key);

  @GetExchange("/")
  List<PlayerResponse> getApiInfoPlayer(@RequestParam("action") String action,
      @RequestParam("league_id") int No,
      @RequestParam("APIkey") String key);

  @GetExchange("/")
  List<TopScorerResponse> getApiScorer(@RequestParam("action") String action,
      @RequestParam("league_id") int No,
      @RequestParam("APIkey") String key);

  @GetExchange("/")
  List<StandingsResponse> getApiStandings(@RequestParam("action") String action,
      @RequestParam("league_id") int No,
      @RequestParam("APIkey") String key);
}
