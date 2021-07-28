package org.butterbrot.fls;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private TeamFetcher teamFetcher;

    @Resource
    private MatchPerformanceFetcher matchPerformanceFetcher;

    private final LoadingCache<Key,  List<PerformancesWrapper>> tournamentCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES).build(new TournamentLoader());

    private final LoadingCache<Key,  List<PerformancesWrapper>> teamCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES).build(new TeamsLoader());

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/selection")
    public String selection(@RequestParam String groupIds, @RequestParam String selection, Model model) throws
            JAXBException {
        if ("Load Tournaments".equals(selection)) {
            return tournaments(groupIds, model);
        } else if ("Load Teams".equals(selection)) {
            return teams(groupIds, model);
        }

        throw new IllegalArgumentException("Illegal selection " + selection);
    }

    private String tournaments(String groupIds, Model model) throws JAXBException {
        List<Tournament> tournaments = getTournaments(groupIds);
        Collections.sort(tournaments);
        List<List<Tournament>> tournamentsList = splitList(tournaments);

        model.addAttribute("tournamentsList", tournamentsList);
        return "tournaments";
    }

    private String teams(String groupIds, Model model) throws JAXBException {
        LinkedHashSet<Team> teams = getTeams(groupIds);
        List<List<Team>> teamsList = splitList(teams);

        model.addAttribute("teamsList", teamsList);
        return "teams";
    }

    private <T> List<List<T>> splitList(Collection<T> elements) {
        List<List<T>> tournamentsList = new ArrayList<>();

        List<T> first = new ArrayList<>();
        List<T> second = new ArrayList<>();
        List<T> third = new ArrayList<>();

        Iterator<T> iterator = elements.iterator();

        for (int i = 0; i < elements.size(); i++) {
            if (i % 3 == 0) {
                first.add(iterator.next());
            } else if (i % 3 == 1) {
                second.add(iterator.next());
            } else {
                third.add(iterator.next());
            }
        }

        tournamentsList.add(first);
        tournamentsList.add(second);
        tournamentsList.add(third);
        return tournamentsList;
    }

    @RequestMapping(value = "/tournamentPerformances")
    public String getTournamentPerformances(@RequestParam String groupIds, Model model) throws JAXBException,
            ExecutionException {
        List<Tournament> tournaments = getTournaments(groupIds);
        Set<String> combinedIds = tournaments.parallelStream().map(tournament -> tournament.getGroupId() + "_" + tournament.getId()).collect(Collectors.toSet());

        return postPerformances(combinedIds, model);
    }

    @RequestMapping(value = "/teamPerformances")
    public String getTeamPerformances(@RequestParam String groupIds, Model model) throws JAXBException,
            ExecutionException {
        LinkedHashSet<Team> teams = getTeams(groupIds);
        Set<String> teamIds = teams.parallelStream().map(team -> String.valueOf(team.getId())).collect(Collectors.toSet());

        return postPerformancesForTeams(teamIds, model);
    }


    private List<Tournament> getTournaments(@RequestParam String groupIds) throws JAXBException {
        List<Tournament> tournaments = new ArrayList<>();
        for (String groupId : groupIds.split(",")) {
            try {
                tournaments.addAll(tournamentFetcher.getTournaments(Integer.parseInt(StringUtils.trimWhitespace
                        (groupId))));
            } catch (NumberFormatException ex) {
                logger.error("Could not format groupId '{}'. Reason: {}", groupId, ex.getMessage());
            }
        }
        return tournaments;
    }

    private LinkedHashSet<Team> getTeams(@RequestParam String groupIds) throws JAXBException {
        LinkedHashSet<Team> teams = new LinkedHashSet<>();
        for (String groupId : groupIds.split(",")) {
            try {
                teams.addAll(teamFetcher.getTeams(Integer.parseInt(StringUtils.trimWhitespace(groupId))));
            } catch (NumberFormatException ex) {
                logger.error("Could not format groupId '{}'. Reason: {}", groupId, ex.getMessage());
            }
        }
        return teams;
    }

    @RequestMapping(value = "/performances")
    public String postPerformances(@RequestParam Set<String> tournamentIds, Model model) throws ExecutionException {
        List<PerformancesWrapper> wrappers = tournamentCache.get(new Key(tournamentIds));
        model.addAttribute("wrappers", wrappers);
        return "performances";
    }

    @RequestMapping(value = "/performancesForTeams")
    public String postPerformancesForTeams(@RequestParam Set<String> teamIds, Model model) throws ExecutionException {
        List<PerformancesWrapper> wrappers = teamCache.get(new Key(teamIds));
        model.addAttribute("wrappers", wrappers);
        return "performances";
    }

    private static class Key {
        private final String checksum;
        private final Set<String> ids;

        public Key(Set<String> ids) {
            this.ids = ids;
            List<String> idsList = new ArrayList<>(ids);
            Collections.sort(idsList);
            checksum = String.join(",", idsList);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            return checksum.equals(key.checksum);
        }

        @Override
        public int hashCode() {
            return checksum.hashCode();
        }
    }

    private class TournamentLoader extends CacheLoader<Key,  List<PerformancesWrapper>> {

        @Override
        public List<PerformancesWrapper> load(Key key) {
            return processPerformances(getPerformances(key.ids));
        }
    }


    private class TeamsLoader extends CacheLoader<Key,  List<PerformancesWrapper>> {

        @Override
        public List<PerformancesWrapper> load(Key key) {
            return processPerformances(getPerformancesForTeams(key.ids));
        }
    }


    private List<PerformancesWrapper> processPerformances(List<Performance> performanceList) {
        Set<Performance> performances = performanceMerger.merge(performanceList);

        List<PerformancesWrapper> wrappers = new ArrayList<>();
        Set<PerformanceValue> selectedPerformances = new HashSet<>();

        for (PerformanceAspect aspect : PerformanceAspect.orderedValues()) {
            List<PerformanceValue> sortedPerformances = performanceEvaluator.evaluate(performances, aspect, 10);

            selectedPerformances.addAll(sortedPerformances);

            wrappers.add(new PerformancesWrapper(sortedPerformances, aspect.getTitle(), aspect.getTieBreakerTitle()));
        }

        selectedPerformances.parallelStream().forEach(performance -> playerFetcher.populate(performance));

        wrappers.forEach(performancesWrapper -> bbCodeBuilder.populate(performancesWrapper));

        return wrappers;
    }

    private List<Performance> getPerformances(Set<String> combinedIds) {
        return combinedIds.parallelStream().flatMap((Function<String, Stream<Performance>>) combinedId -> {
            String[] splitIds = combinedId.split("_");
            try {
                return performanceFetcher.getPerformances(Integer.valueOf(splitIds[0]), Integer.valueOf
                        (splitIds[1])).parallelStream();
            } catch (JAXBException e) {
                throw new IllegalStateException("Could not load performance data for tournament: " + splitIds[1]);
            }
        }).collect(Collectors.toList());
    }

    private List<Performance> getPerformancesForTeams(Set<String> teamIds) {
        return teamIds.parallelStream().flatMap((Function<String, Stream<Performance>>) teamId -> {
            try {
                return matchPerformanceFetcher.getPerformances(Integer.parseInt(teamId)).stream();
            } catch (JAXBException e) {
                throw new IllegalStateException("Could not load performance data for team: " + teamId);
            }
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        SpringApplication.run(Stats.class, args);
    }
}
