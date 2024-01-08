package com.community.amkorea.soccer.config;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface SoccerApi {
  @GetExchange("/")
  List<Map<String, Object>> getApiInfo(@RequestParam("action") String action,
      @RequestParam("league_id") int No,
      @RequestParam("APIkey") String key);
}
