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

public class PlayerFetcher {

    private static final String PLAYER_URL = "https://fumbbl.com/p/player?player_id={playerId}";

    @Resource
    private RestTemplate fumbblTemplate;

    public void populate(PerformanceValue performance) {
        ResponseEntity<String> responseEntity = fumbblTemplate.getForEntity(
                UriComponentsBuilder.fromHttpUrl(PLAYER_URL).buildAndExpand(performance.getPlayerId()).toUri(), String.class);

        String response = responseEntity.getBody();
        if (StringUtils.hasText(response)){
            Document doc = Jsoup.parse(response);
            Elements nammElements = doc.select("td[class=name]");
            if (!nammElements.isEmpty()) {
                performance.setPlayerName(nammElements.first().text());
            }
            Elements linkElements = doc.select("div[class=pagecontent] > div > div > a");
            if (!linkElements.isEmpty()) {
                Element linkElement = linkElements.first();
                performance.setTeamUrlPath(linkElement.attr("href"));
                performance.setTeamName(linkElement.text());
            }
        }
    }
}
