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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TournamentFetcher {

    private static final String TOURNAMENTS_URL = "http://fumbbl.com/xml:group?id={groupId}&op=tourneys";

    @Resource
    private RestTemplate fumbblTemplate;

    @Resource
    private JAXBContext jaxbContext;

    Set<Tournament> getTournaments(int groupId) throws JAXBException {
        Set<Tournament> tournaments = new HashSet<>();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        for (Element element : loadTournamentData(groupId)) {
            tournaments.add((Tournament) unmarshaller.unmarshal(new StringReader(element.outerHtml())));
        }
        return tournaments;
    }


    private List<Element> loadTournamentData(int groupId) {
        List<Element> tournamentElements = new ArrayList<>();
        ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(
                UriComponentsBuilder.fromHttpUrl(TOURNAMENTS_URL).buildAndExpand(groupId).toUri(), String.class);

        String response = responseEntity.getBody();
        if (StringUtils.hasText(response)){
        Document doc = Jsoup.parse(response);

        tournamentElements.addAll(doc.getElementsByTag("tournament"));
        }
        return tournamentElements;
    }
}