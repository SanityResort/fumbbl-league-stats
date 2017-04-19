package org.butterbrot.fls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/tournaments")
    public String getTournaments(@RequestParam int groupId, Model model) throws JAXBException {
        List<Tournament> tournaments = tournamentFetcher.getTournaments(groupId);
        Collections.sort(tournaments);
        model.addAttribute("tournaments", tournaments);
        model.addAttribute("groupId", groupId);
        return "tournaments";
    }

    @RequestMapping("/performances")
    public String getPerformances(@RequestParam Set<Integer> tournamentIds, @RequestParam Integer groupId, Model
            model) {

        Set<Performance> performances = performanceMerger.merge(getPerformances(tournamentIds, groupId));

        List<PerformancesWrapper> wrappers = new ArrayList<>();
        Set<PerformanceValue> selectedPerformances = new HashSet<>();

        for (PerformanceAspect aspect : PerformanceAspect.values()) {
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

        model.addAttribute("wrappers", wrappers);

        return "performances";
    }

    private List<Performance> getPerformances(Set<Integer> tournamentIds, Integer groupId) {
        return tournamentIds.parallelStream().flatMap(new Function<Integer, Stream<Performance>>() {
            @Override
            public Stream<Performance> apply(Integer tournamentId) {
                try {
                    return performanceFetcher.getPerformances(groupId, tournamentId).stream();
                } catch (JAXBException e) {
                    throw new IllegalStateException("Could not load performance data for tournament: " + tournamentId);
                }
            }
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Stats.class, args);
    }
}
