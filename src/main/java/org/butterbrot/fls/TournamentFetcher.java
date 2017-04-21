package org.butterbrot.fls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        for (Element element : loadTournamentData(groupId)) {
            try (StringReader reader = new StringReader(element.outerHtml().replace("&nbsp;", " "))) {
                tournaments.add((Tournament) unmarshaller.unmarshal(reader));
            }
        }
        return tournaments;
    }


    private List<Element> loadTournamentData(int groupId) {
        List<Element> tournamentElements = new ArrayList<>();
        ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl
                (TOURNAMENTS_URL).buildAndExpand(groupId).toUri(), String.class);

        String response = responseEntity.getBody();
        if (StringUtils.hasText(response)) {
            Document doc = Jsoup.parse(response);
            tournamentElements.addAll(doc.getElementsByTag("tournament"));
        }
        return tournamentElements;
    }
}
