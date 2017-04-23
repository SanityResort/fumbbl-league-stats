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
        int lastMatchCount = 0;
        do {
            ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(MATCHES_URL).buildAndExpand(teamId, pagingId++).toUri(), String.class);

            String response = responseEntity.getBody();
            if (StringUtils.hasText(response)) {

                Document doc = Jsoup.parse(response);
                Elements perfElements = doc.select("home[id="+teamId+"] performance, away[id="+teamId+"] performance");
                lastMatchCount = perfElements.size();
                performanceElements.addAll(perfElements);
            }
        } while(lastMatchCount > 0);
        return performanceElements;
    }
}
