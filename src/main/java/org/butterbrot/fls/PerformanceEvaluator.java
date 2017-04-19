package org.butterbrot.fls;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PerformanceEvaluator {

    public List<Performance> evaluate(Set<Performance> performances, PerformanceAspect aspect, int limit) {
        List<Performance> sortedPerformances = performances.stream().filter(new Predicate<Performance>() {
            @Override
            public boolean test(Performance performance) {
                return aspect.getValue(performance) > 0;
            }
        }).sorted(new Comparator<Performance>() {
            @Override
            public int compare(Performance performance1, Performance performance2) {
                return aspect.getValue(performance2).compareTo(aspect.getValue(performance1));
            }
        }).collect(Collectors.toList());

        if (sortedPerformances.size() > limit) {
            int limitValue = aspect.getValue(sortedPerformances.get(limit - 1));
            int additionalElements = 0;
            for (int i = limit; i < sortedPerformances.size(); i++) {
                if (aspect.getValue(sortedPerformances.get(i)) == limitValue) {
                    additionalElements++;
                } else {
                    break;
                }
            }
            return sortedPerformances.subList(0, limit + additionalElements);
        }

        return sortedPerformances;
    }
}
