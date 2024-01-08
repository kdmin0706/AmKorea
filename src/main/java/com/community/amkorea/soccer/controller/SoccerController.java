package com.community.amkorea.soccer.controller;

import com.community.amkorea.soccer.enums.League;
import com.community.amkorea.soccer.service.SoccerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/soccer")
@RequiredArgsConstructor
public class SoccerController {

  private final SoccerService soccerService;

  @GetMapping("/team")
  public ResponseEntity<?> getTeam(@RequestParam("id") String teamId) {
    return ResponseEntity.ok(soccerService.findTeam(teamId));
  }

  @GetMapping("/player")
  public ResponseEntity<?> getPlayer(@RequestParam("id") String playerId,
      @RequestParam("team") String teamId) {
    return ResponseEntity.ok(soccerService.findPlayer(playerId, teamId));
  }

  @GetMapping("/standing")
  public ResponseEntity<?> getLeagueStanding(@RequestParam League league) {
    return ResponseEntity.ok(soccerService.getStandings(league));
  }

  @GetMapping("/scorer")
  public ResponseEntity<?> getTop10Scorer(@RequestParam League league) {
    return ResponseEntity.ok(soccerService.getTop10Scorer(league));
  }

}
