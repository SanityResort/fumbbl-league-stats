package org.butterbrot.fls;

import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

public class PlayerFetcher {

    private static final Logger logger = LoggerFactory.getLogger(PlayerFetcher.class);

    private static final String PLAYER_URL = "https://fumbbl.com/api/player/get/{playerId}";
    private static final String TEAM_URL_PATH = "/p/team?op=view&team_id=";

    @Resource
    private RestTemplate fumbblTemplate;

    @Resource
    private TeamFetcher teamFetcher;

    public void populate(PerformanceValue performance) {
        ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(
                UriComponentsBuilder.fromHttpUrl(PLAYER_URL).buildAndExpand(performance.getPlayerId()).toUri(), String.class);

        String response = responseEntity.getBody();

        String name = JsonPath.read(response, "$.name");
        int teamId = JsonPath.read(response, "$.teamId");

        String teamName = null;

        try {
            Team team = teamFetcher.getTeam(teamId);
            teamName = team.getName();
        } catch (ExecutionException e) {
            logger.error("Could not load team for id " + teamId + " while loading data for player with id " + performance.getPlayerId());
        }

        performance.setPlayerName(name);
        performance.setTeamName(teamName);
        performance.setTeamUrlPath(TEAM_URL_PATH + teamId);

    }
}
