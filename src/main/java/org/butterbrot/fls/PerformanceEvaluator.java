package org.butterbrot.fls;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PerformanceEvaluator {

    public List<PerformanceValue> evaluate(Set<Performance> performances, PerformanceAspect aspect, int limit) {
        List<PerformanceValue> sortedPerformances = performances.stream().map(new Function<Performance,
                PerformanceValue>() {
            @Override
            public PerformanceValue apply(Performance performance) {
                return new PerformanceValue(performance.getPlayerId(), aspect.getValue(performance));
            }
        }).filter(new Predicate<PerformanceValue>() {
            @Override
            public boolean test(PerformanceValue performance) {
                return performance.getValue() > 0;
            }
        }).sorted(new Comparator<PerformanceValue>() {
            @Override
            public int compare(PerformanceValue performance1, PerformanceValue performance2) {
                return Integer.valueOf(performance2.getValue()).compareTo(performance1.getValue());
            }
        }).collect(Collectors.toList());

        if (sortedPerformances.size() > limit) {
            int limitValue = sortedPerformances.get(limit - 1).getValue();
            int additionalElements = 0;
            for (int i = limit; i < sortedPerformances.size(); i++) {
                if (sortedPerformances.get(i).getValue() == limitValue) {
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
