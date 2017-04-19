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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
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


    @RequestMapping("/group/{groupId}/tournaments")
    public String getTournaments(@PathVariable int groupId, Model model) throws JAXBException {
        List<Tournament> tournaments = tournamentFetcher.getTournaments(groupId);
        Collections.sort(tournaments);
        model.addAttribute("tournaments", tournaments);
        model.addAttribute("groupId", groupId);
        return "tournaments";
    }

    @RequestMapping("/performances")
    public String getPerformances(@RequestParam Set<Integer> tournamentIds, @RequestParam Integer groupId, Model model){

        List<Performance> performances = tournamentIds.parallelStream().flatMap(new Function<Integer, Stream<Performance>>() {
            @Override
            public Stream<Performance> apply(Integer tournamentId) {
                try {
                    return performanceFetcher.getPerformances(groupId, tournamentId).stream();
                } catch (JAXBException e) {
                    throw new IllegalStateException("Could not load performance data for tournament: " + tournamentId);
                }
            }
        }).collect(Collectors.toList());

        model.addAttribute("performances", performanceMerger.merge(performances));

        return "performances";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Stats.class, args);
    }
}
