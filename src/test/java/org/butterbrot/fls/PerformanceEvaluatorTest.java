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
        List<Performance> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 3);
        assertEquals("Size is incorrect", 3, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8),mapToAspect(performances, PerformanceAspect.BLOCKS));
    }

    @Test
    public void evaluatesToLimit() {
        List<Performance> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 8);
        assertEquals("Size is incorrect", 8, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8,7,3,3,3,1),mapToAspect(performances, PerformanceAspect.BLOCKS));
    }

    @Test
    public void evaluatesToLessThanLimit() {
        List<Performance> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 10);
        assertEquals("Size is incorrect", 8, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8,7,3,3,3,1),mapToAspect(performances, PerformanceAspect.BLOCKS));
    }

    @Test
    public void evaluatesToMoreThanLimit() {
        List<Performance> performances = evaluator.evaluate(unevaluatedPerformances, PerformanceAspect.BLOCKS, 6);
        assertEquals("Size is incorrect", 7, performances.size());
        assertEquals("List is not propertly sorted", Arrays.asList(10,9,8,7,3,3,3),mapToAspect(performances, PerformanceAspect.BLOCKS));
    }


    private static List<Integer> mapToAspect(List<Performance> performances, PerformanceAspect aspect) {
        return performances.stream().map(new Function<Performance, Integer>() {
            @Override
            public Integer apply(Performance performance) {
                return aspect.getValue(performance);
            }
        }).collect(Collectors.toList());
    }

}