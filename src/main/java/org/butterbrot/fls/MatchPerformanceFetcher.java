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
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchPerformanceFetcher {

    private static final String MATCHES_URL = "https://fumbbl.com/xml:matches?id={teamId}&p={pagingId}";

    @Resource
    private RestTemplate fumbblTemplate;

    @Resource
    private JAXBContext jaxbContext;


    public List<Performance> getPerformances(int teamId) throws JAXBException {
        List<Performance> performances = new ArrayList<>();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        for (Element element : loadPerformanceData(teamId)) {
            try (StringReader reader = new StringReader(element.outerHtml().replace("&nbsp;", " "))){
                performances.add((Performance) unmarshaller.unmarshal(reader));
            }
        }
        return performances;
    }


    private List<Element> loadPerformanceData(Integer teamId) {
        int pagingId = 1;
        List<Element> performanceElements = new ArrayList<>();
        Set<String> matchIds = new HashSet<>();
        int lastMatchCount = 0;
        do {
            URI uri = UriComponentsBuilder.fromHttpUrl(MATCHES_URL).buildAndExpand(teamId, pagingId++).toUri();
            ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(uri, String.class);

            String response = responseEntity.getBody();
            if (StringUtils.hasText(response)) {

                Document doc = Jsoup.parse(response);
                Elements matches = doc.select("match");
                for (Element match : matches) {
                    if (!matchIds.contains(match.id())) {
                        matchIds.add(match.id());
                        Elements perfElements = match.select("home[id=" + teamId + "] performance, away[id=" + teamId
                                + "] performance");
                        performanceElements.addAll(perfElements);
                    }
                }
                lastMatchCount = matches.size();
            }
        } while (lastMatchCount > 0);
        return performanceElements;
    }
}
