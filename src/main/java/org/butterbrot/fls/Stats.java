package org.butterbrot.fls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.util.Set;

@Controller
@SpringBootApplication
public class Stats {

    @Resource
    private TournamentFetcher tournamentFetcher;

    @RequestMapping("/group/{groupId}/tournaments")
    @ResponseBody
    String tournament(@PathVariable int groupId) throws JAXBException {
        Set<Tournament> tournaments = tournamentFetcher.getTournaments(groupId);
        return String.valueOf(tournaments.size());
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Stats.class, args);
    }
}
