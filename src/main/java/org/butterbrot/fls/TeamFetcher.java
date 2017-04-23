package org.butterbrot.fls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TeamFetcher {

    private static final String TEAMS_URL = "https://fumbbl.com/xml:group?id={groupId}&op=members";

    @Resource
    private RestTemplate fumbblTemplate;

    @Resource
    private JAXBContext jaxbContext;

    public List<Team> getTeams(int groupId) throws JAXBException {
        List<Team> teams = new ArrayList<>();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Container container = loadPerformanceData(groupId);
        for (Element element : container.teams) {
            try (StringReader reader = new StringReader(element.outerHtml().replace("&nbsp;", " "))){
                Team team = (Team) unmarshaller.unmarshal(reader);
                team.setGroupName(container.name);
                teams.add(team);
            }
        }
        return teams;
    }


    private Container loadPerformanceData(Integer groupId) {
        List<Element> teamElements = new ArrayList<>();
        ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl
                (TEAMS_URL).buildAndExpand(groupId).toUri(), String.class);

        String response = responseEntity.getBody();
        String name = null;
        if (StringUtils.hasText(response)) {

            Document doc = Jsoup.parse(response);
            teamElements.addAll(doc.getElementsByTag("team"));
            Elements nameElements = doc.select("group name");
            if (!nameElements.isEmpty()) {
                name = nameElements.first().text();
            }
        }
        return new Container(teamElements, name);
    }

    private static class Container {
        private List<Element> teams;
        private String name;

        public Container(List<Element> teams, String name) {
            this.teams = teams;
            this.name = name;
        }
    }
}
