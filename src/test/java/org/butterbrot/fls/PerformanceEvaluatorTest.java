package org.butterbrot.fls;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class PerformanceEvaluatorTest {

    private static Set<Performance> unevaluatedPerformances = PerformanceTestHelper.unevaluatesdPerformances();

    private PerformanceEvaluator evaluator = new PerformanceEvaluator();

    @Test
    public void evaluatesToLimitWithCutOff() {
        List<PerformanceValue> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 3);
        assertEquals("Size is incorrect", 3, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8),mapToAspect(performances));
    }

    @Test
    public void evaluatesToLimit() {
        List<PerformanceValue> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 8);
        assertEquals("Size is incorrect", 8, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8,7,3,3,3,1),mapToAspect(performances));
    }

    @Test
    public void evaluatesToLessThanLimit() {
        List<PerformanceValue> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 10);
        assertEquals("Size is incorrect", 8, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8,7,3,3,3,1),mapToAspect(performances));
    }

    @Test
    public void evaluatesToMoreThanLimit() {
        List<PerformanceValue> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 6);
        assertEquals("Size is incorrect", 7, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8,7,3,3,3),mapToAspect(performances));
    }


    private static List<Integer> mapToAspect(List<PerformanceValue> performances) {
        return performances.stream().map(new Function<PerformanceValue, Integer>() {
            @Override
            public Integer apply(PerformanceValue performance) {
                return performance.getValue();
            }
        }).collect(Collectors.toList());
    }

}