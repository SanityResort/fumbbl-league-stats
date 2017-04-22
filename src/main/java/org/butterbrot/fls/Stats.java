package org.butterbrot.fls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@SpringBootApplication
public class Stats {

    private static final Logger logger = LoggerFactory.getLogger(Stats.class);

    @Resource
    private TournamentFetcher tournamentFetcher;

    @Resource
    private PerformanceFetcher performanceFetcher;

    @Resource
    private PerformanceMerger performanceMerger;

    @Resource
    private PerformanceEvaluator performanceEvaluator;

    @Resource
    private PlayerFetcher playerFetcher;

    @Resource
    private BBCodeBuilder bbCodeBuilder;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/tournaments")
    public String postTournaments(@RequestParam String groupIds, Model model) throws JAXBException {
        List<Tournament> tournaments = getTournaments(groupIds);
        Collections.sort(tournaments);
        List<List<Tournament>> tournamentsList = new ArrayList<>();

        List<Tournament> first = new ArrayList<>();
        List<Tournament> second = new ArrayList<>();
        List<Tournament> third = new ArrayList<>();

        for (int i = 0; i<tournaments.size(); i++) {
            if (i%3 == 0) {
                first.add(tournaments.get(i));
            } else if (i%3 == 1){
                second.add(tournaments.get(i));
            }else {
                third.add(tournaments.get(i));
            }
        }

        tournamentsList.add(first);
        tournamentsList.add(second);
        tournamentsList.add(third);

        model.addAttribute("tournamentsList", tournamentsList);
        return "tournaments";
    }

    @RequestMapping(value = "/tournamentPerformances")
    public String getPerformances(@RequestParam String groupIds, Model model) throws JAXBException {
        List<Tournament> tournaments = getTournaments(groupIds);
        Set<String> combinedIds = tournaments.stream().map(new Function<Tournament, String>() {
            @Override
            public String apply(Tournament tournament) {
                return tournament.getGroupId()+"_"+tournament.getId();
            }
        }).collect(Collectors.toSet());

        return postPerformances(combinedIds, model);
    }

        private List<Tournament> getTournaments(@RequestParam String groupIds) throws JAXBException {
        List<Tournament> tournaments = new ArrayList<>();
        for (String groupId: groupIds.split(",")) {
            try {
                tournaments.addAll(tournamentFetcher.getTournaments(Integer.valueOf(StringUtils.trimWhitespace(groupId))));
            } catch (NumberFormatException ex) {
                logger.error("Could not format groupId '{}'. Reason: {}", groupId, ex.getMessage());
            }
        }
        return tournaments;
    }

    @RequestMapping(value = "/performances")
    public String postPerformances(@RequestParam Set<String> tournamentIds, Model
            model) {

        Set<Performance> performances = performanceMerger.merge(getPerformances(tournamentIds));

        List<PerformancesWrapper> wrappers = new ArrayList<>();
        Set<PerformanceValue> selectedPerformances = new HashSet<>();

        for (PerformanceAspect aspect : PerformanceAspect.orderedValues()) {
            List<PerformanceValue> sortedPerformances = performanceEvaluator.evaluate(performances, aspect, 10);

            selectedPerformances.addAll(sortedPerformances);

            wrappers.add(new PerformancesWrapper(sortedPerformances, aspect
                    .getTitle(), aspect.getTieBreakerTitle()));
        }

        selectedPerformances.parallelStream().forEach(new Consumer<PerformanceValue>() {
            @Override
            public void accept(PerformanceValue performance) {
                playerFetcher.populate(performance);
            }
        });

        wrappers.forEach(new Consumer<PerformancesWrapper>() {
            @Override
            public void accept(PerformancesWrapper performancesWrapper) {
                bbCodeBuilder.populate(performancesWrapper);
            }
        });

        model.addAttribute("wrappers", wrappers);

        return "performances";
    }

    private List<Performance> getPerformances(Set<String> combinedIds) {
        return combinedIds.parallelStream().flatMap(new Function<String, Stream<Performance>>() {
            @Override
            public Stream<Performance> apply(String combinedId) {
                String[] splitIds = combinedId.split("_");
                try {
                    return performanceFetcher.getPerformances(Integer.valueOf(splitIds[0]), Integer.valueOf(splitIds[1])).stream();
                } catch (JAXBException e) {
                    throw new IllegalStateException("Could not load performance data for tournament: " + splitIds[1]);
                }
            }
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Stats.class, args);
    }
}
