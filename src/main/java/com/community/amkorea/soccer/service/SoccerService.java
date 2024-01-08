package com.community.amkorea.soccer.service;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.soccer.config.SoccerApi;
import com.community.amkorea.soccer.dto.PlayerDto;
import com.community.amkorea.soccer.dto.StandingsResponse;
import com.community.amkorea.soccer.dto.TeamDto;
import com.community.amkorea.soccer.dto.TopScorerResponse;
import com.community.amkorea.soccer.entity.Player;
import com.community.amkorea.soccer.entity.Team;
import com.community.amkorea.soccer.enums.League;
import com.community.amkorea.soccer.repository.PlayerRepository;
import com.community.amkorea.soccer.repository.TeamRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@Slf4j
@Service
@RequiredArgsConstructor
public class SoccerService {

  private final SoccerApi soccerApi;
  private final TeamRepository teamRepository;
  private final PlayerRepository playerRepository;

  @Value("${spring.data.soccer.apiKey}")
  private String key;

  @Scheduled(cron = "0 0 0 * * *")
  public void getSoccerApiData() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    Arrays.stream(League.values()).forEach(w -> {
      getTeamData(w.getCode());
      getPlayerData(w.getCode());
    });

    stopWatch.stop();
    log.info(stopWatch.prettyPrint());
    log.info("API 실행 시간(s): " + stopWatch.getTotalTimeSeconds());
  }

  public void getTeamData(int code) {
    List<Team> teams = new ArrayList<>();

    List<String> allTeam = teamRepository.findByAllId();

    List<Map<String, Object>> teamInfo = soccerApi.getApiInfo("get_teams", code, key);

    teamInfo.forEach(t -> {
      Map<String, Object> venueMap = (Map<String, Object>) t.get("venue");
      List<Map<String, Object>> coachesList = (List<Map<String, Object>>) t.get("coaches");

      TeamDto teamDto = TeamDto.builder()
          .teamId(t.get("team_key").toString())
          .name(t.get("team_name").toString())
          .country(t.get("team_country").toString())
          .venue(venueMap.get("venue_name").toString())
          .coach(coachesList.get(0).get("coach_name").toString())
          .build();

      Team team = teamDto.toEntity();
      boolean isNew = !allTeam.contains(team.getId());
      team.setIsNewStatus(isNew);

      teams.add(team);
    });

    teamRepository.saveAll(teams);
  }

  public void getPlayerData(int code) {
    List<String> allPlayer = playerRepository.findByAllId();

    List<Map<String, Object>> teamInfo = soccerApi.getApiInfo("get_teams", code, key);

    List<Player> players = new ArrayList<>();

    teamInfo.forEach(t -> {
      List<Map<String, Object>> playerList = (List<Map<String, Object>>) t.get("players");

      String teamName = t.get("team_name").toString();
      Team team = getTeamName(teamName);

      playerList.forEach(p -> {
        PlayerDto playerDto = PlayerDto.builder()
            .playerId(p.get("player_id").toString())
            .name(p.get("player_name").toString())
            .age(p.get("player_age").toString())
            .position(p.get("player_type").toString())
            .number(p.get("player_number").toString())
            .teamName(team.getName())
            .build();

        Player player = playerDto.toEntity(team, playerDto.getPlayerId());
        boolean isNew = !allPlayer.contains(player.getId());
        player.setIsNewStatus(isNew);

        players.add(player);

      });
    });

    playerRepository.saveAll(players);
  }

  private Team getTeamName(String name) {
    return teamRepository.findByName(name)
        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
  }

  public List<TopScorerResponse> getTop10Scorer(League league) {
    List<TopScorerResponse> scorerList = new ArrayList<>();

    List<Map<String, Object>> top10Scorer
        = soccerApi.getApiInfo("get_topscorers", league.getCode(), key);

    top10Scorer.stream()
        .limit(10)
        .forEach(t -> {
          TopScorerResponse response = TopScorerResponse.builder()
              .place(t.get("player_place").toString())
              .name(t.get("player_name").toString())
              .teamName(t.get("team_name").toString())
              .goals(t.get("goals").toString())
              .assists(t.get("assists").toString())
              .build();

      scorerList.add(response);
    });

    return scorerList;
  }

  @Transactional(readOnly = true)
  public TeamDto findTeam(String teamId) {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
    return TeamDto.fromEntity(team);
  }

  @Transactional(readOnly = true)
  public PlayerDto findPlayer(String playerId, String teamId) {
    Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PLAYER_NOT_FOUND));

    if (!Objects.equals(teamId, player.getTeam().getId())) {
      throw new CustomException(ErrorCode.PLAYER_NOT_TEAM);
    }

    return PlayerDto.fromEntity(player);
  }


  public List<StandingsResponse> getStandings(League league) {
    List<StandingsResponse> standingList = new ArrayList<>();

    List<Map<String, Object>> standings
        = soccerApi.getApiInfo("get_standings", league.getCode(), key);

    standings.forEach(t -> {
          StandingsResponse response = StandingsResponse.builder()
              .leaguePosition(t.get("overall_league_position").toString())
              .leagueName(t.get("league_name").toString())
              .teamName(t.get("team_name").toString())
              .win(t.get("overall_league_W").toString())
              .draw(t.get("overall_league_D").toString())
              .lose(t.get("overall_league_L").toString())
              .build();

          standingList.add(response);
        });

    return standingList;
  }

}
