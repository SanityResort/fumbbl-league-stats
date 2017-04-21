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

public class PerformanceFetcher {

    private static final String MATCHES_URL = "https://fumbbl.com/xml:group?id={groupId}&op=matches&t={tournamentid}&paging={pagingId}";

    @Resource
    private RestTemplate fumbblTemplate;

    @Resource
    private JAXBContext jaxbContext;

    public List<Performance> getPerformances(Integer groupId, Integer tournamentId) throws JAXBException {
        List<Performance> performances = new ArrayList<>();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        for (Element element : loadPerformanceData(groupId, tournamentId)) {
            performances.add((Performance) unmarshaller.unmarshal(new StringReader(element.outerHtml().replace("&nbsp;", " "))));
        }
        return performances;
    }

    private List<Element> loadPerformanceData(Integer groupId, Integer tournamentId) {
        String pagingId = "0";
        List<Element> performanceElements = new ArrayList<>();
        while (pagingId != null) {
            ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(MATCHES_URL).buildAndExpand(groupId, tournamentId, pagingId).toUri(), String.class);

            String response = responseEntity.getBody();
            if (StringUtils.hasText(response)) {

                Document doc = Jsoup.parse(response);
                Elements nextPage = doc.getElementsByTag("nextPage");
                if (nextPage.isEmpty()){
                    pagingId = null;
                } else {
                    pagingId = nextPage.first().text();
                }
                performanceElements.addAll(doc.getElementsByTag("performance"));
            } else {
                break;
            }
        }
        return performanceElements;
    }
}
