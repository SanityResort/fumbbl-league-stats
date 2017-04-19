package org.butterbrot.fls;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PerformanceEvaluator {

    public List<PerformanceValue> evaluate(Set<Performance> performances, PerformanceAspect aspect, int limit) {
        List<PerformanceValue> sortedPerformances = performances.stream().map(new Function<Performance,
                PerformanceValue>() {
            @Override
            public PerformanceValue apply(Performance performance) {
                return new PerformanceValue(performance.getPlayerId(), aspect.getValue(performance), aspect.getTieBreakerValue(performance), aspect);
            }
        }).filter(new Predicate<PerformanceValue>() {
            @Override
            public boolean test(PerformanceValue performance) {
                return performance.getValue() > 0;
            }
        }).sorted().collect(Collectors.toList());

        PerformanceValue lastValue = null;
        int place = 0;
        for (PerformanceValue performanceValue: sortedPerformances) {
            if (lastValue == null || performanceValue.compareTo(lastValue) != 0 ) {
                place++;
                lastValue = performanceValue;
            }
            performanceValue.setPlace(place);

        }

        if (sortedPerformances.size() > limit) {
            PerformanceValue limitValue = sortedPerformances.get(limit - 1);
            int additionalElements = 0;
            for (int i = limit; i < sortedPerformances.size(); i++) {
                if (sortedPerformances.get(i).compareTo(limitValue) == 0) {
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
