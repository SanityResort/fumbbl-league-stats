package org.butterbrot.fls;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jayway.jsonpath.JsonPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TeamFetcher {

    private static final String TEAMS_URL = "https://fumbbl.com/xml:group?id={groupId}&op=members";
    private static final String TEAM_URL = "https://fumbbl.com/api/team/get/{teamId}";

    private final LoadingCache<Integer, Team> teamCache = CacheBuilder.newBuilder().expireAfterAccess(30,
            TimeUnit.MINUTES).build(new TeamLoader());

    @Resource
    private RestTemplate fumbblTemplate;

    @Resource
    private JAXBContext jaxbContext;

    public List<Team> getTeams(int groupId) throws JAXBException {
        List<Team> teams = new ArrayList<>();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Container container = loadPerformanceData(groupId);
        for (Element element : container.teams) {
            try (StringReader reader = new StringReader(element.outerHtml().replace("&nbsp;", " "))) {
                Team team = (Team) unmarshaller.unmarshal(reader);
                team.setGroupName(container.name);
                teams.add(team);
                teamCache.put(team.getId(), team);
            }
        }
        return teams;
    }

    public Team getTeam(int teamId) throws ExecutionException {
        return teamCache.get(teamId);
    }

    private Container loadPerformanceData(Integer groupId) {
        List<Element> teamElements = new ArrayList<>();
        ResponseEntity<String> responseEntity =
                fumbblTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl(TEAMS_URL).buildAndExpand(groupId).toUri(), String.class);

        String response = responseEntity.getBody();
        String name = null;
        if (StringUtils.hasText(response)) {

            Document doc = Jsoup.parse(response);
            teamElements.addAll(doc.getElementsByTag("team"));
            Elements nameElements = doc.select("group name");
            if (!nameElements.isEmpty()) {
                Element first = nameElements.first();
                if (first != null) {
                    name = first.text();
                }
            }
        }
        return new Container(teamElements, name);
    }

    private static class Container {
        private final List<Element> teams;
        private final String name;

        public Container(List<Element> teams, String name) {
            this.teams = teams;
            this.name = name;
        }
    }

    private class TeamLoader extends CacheLoader<Integer, Team> {
        @Override
        public Team load(@SuppressWarnings("NullableProblems") @ParametersAreNonnullByDefault Integer teamId) {
            ResponseEntity<String> responseEntity =
                    fumbblTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl(TEAM_URL).buildAndExpand(teamId).toUri(), String.class);
            String response = responseEntity.getBody();
            String coachName = JsonPath.read(response, "$.coach.name");
            String teamName = JsonPath.read(response, "$.name");

            return new Team(teamId, teamName, coachName, null);
        }
    }
}
