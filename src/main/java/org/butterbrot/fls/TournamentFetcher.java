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

public class TournamentFetcher {

    private static final String TOURNAMENTS_URL = "https://fumbbl.com/xml:group?id={groupId}&op=tourneys";

    @Resource
    private RestTemplate fumbblTemplate;

    @Resource
    private JAXBContext jaxbContext;

    List<Tournament> getTournaments(int groupId) throws JAXBException {
        List<Tournament> tournaments = new ArrayList<>();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Container container = loadTournamentData(groupId);
        for (Element element : container.tournaments) {
            try (StringReader reader = new StringReader(element.outerHtml().replace("&nbsp;", " "))) {
                Tournament tournament = (Tournament) unmarshaller.unmarshal(reader);
                tournament.setGroupId(groupId);
                tournament.setGroupName(container.name);
                tournaments.add(tournament);
            }
        }
        return tournaments;
    }


    private Container loadTournamentData(int groupId) {
        List<Element> tournamentElements = new ArrayList<>();
        ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl
                (TOURNAMENTS_URL).buildAndExpand(groupId).toUri(), String.class);

        String response = responseEntity.getBody();
        String name = "";
        if (StringUtils.hasText(response)) {
            Document doc = Jsoup.parse(response);
            tournamentElements.addAll(doc.getElementsByTag("tournament"));
             Elements nameElements = doc.select("group name");
             if (!nameElements.isEmpty()) {
                 name = nameElements.first().text();
             }
        }
        return new Container(tournamentElements, name);
    }

    private static class Container {
        private List<Element> tournaments;
        private String name;

        public Container(List<Element> tournaments, String name) {
            this.tournaments = tournaments;
            this.name = name;
        }
    }
}
