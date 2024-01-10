package com.community.amkorea.soccer.service;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.soccer.config.SoccerApi;
import com.community.amkorea.soccer.dto.PlayerDto;
import com.community.amkorea.soccer.dto.api.PlayerResponse;
import com.community.amkorea.soccer.dto.api.StandingsResponse;
import com.community.amkorea.soccer.dto.TeamDto;
import com.community.amkorea.soccer.dto.api.TeamResponse;
import com.community.amkorea.soccer.dto.api.TopScorerResponse;
import com.community.amkorea.soccer.entity.Player;
import com.community.amkorea.soccer.entity.Team;
import com.community.amkorea.soccer.enums.League;
import com.community.amkorea.soccer.repository.PlayerRepository;
import com.community.amkorea.soccer.repository.TeamRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    List<TeamResponse> responses = soccerApi.getApiTeam("get_teams", code, key);
    responses.forEach(e -> {
      TeamDto teamDto = TeamDto.builder()
          .teamId(e.getTeamId())
          .name(e.getName())
          .country(e.getCountry())
          .venue(e.getVenueInfos().getName())
          .coach(e.getCoachInfos().get(0).getName())
          .build();

      Team team = teamDto.toEntity();
      boolean isNew = !allTeam.contains(team.getId());
      team.setIsNewStatus(isNew);

      teams.add(team);
    });

    teamRepository.saveAll(teams);
  }

  public void getPlayerData(int code) {
    List<Player> players = new ArrayList<>();
    List<String> allPlayer = playerRepository.findByAllId();

    List<PlayerResponse> responses = soccerApi.getApiInfoPlayer("get_teams", code, key);
    responses.forEach(p -> {
      Team team = getTeamName(p.getName());

      p.getPlayerInfos().forEach(info -> {
        PlayerDto playerDto = PlayerDto.builder()
            .playerId(info.getPlayerId())
            .name(info.getName())
            .age(info.getAge())
            .position(info.getPosition())
            .number(info.getNumber())
            .teamName(p.getName())
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
    List<TopScorerResponse> top10Scorer
        = soccerApi.getApiScorer("get_topscorers", league.getCode(), key);

    return top10Scorer.stream().limit(10).toList();
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
    return soccerApi.getApiStandings("get_standings", league.getCode(), key);
  }

}
