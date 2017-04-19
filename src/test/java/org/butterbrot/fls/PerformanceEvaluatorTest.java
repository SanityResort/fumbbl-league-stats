package org.butterbrot.fls;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PerformanceEvaluatorTest {

    private static Set<Performance> unevaluatedPerformances = PerformanceTestHelper.unevaluatedPerformances();

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

    @Test
    public void tiebreakersSortAsc() {
        List<PerformanceValue> performances = evaluator.evaluate(PerformanceTestHelper.unevaluatedBlockPerformaces(), PerformanceAspect.CASUALTIES, 4);
        assertEquals("Highest cas must be first", 5, performances.get(0).getValue());
        assertEquals("Second highest cas must be second and third", 3, performances.get(1).getValue());
        assertEquals("Second highest cas must be second and third", 3, performances.get(2).getValue());
        assertEquals("Second place goes to lowest tiebreaker", 4, performances.get(1).getTiebreaker().intValue());
        assertEquals("Third place goes to highest tiebreaker", 7, performances.get(2).getTiebreaker().intValue());
    }

    @Test
    public void tiebreakersSortAscExtendsLimit() {
        List<PerformanceValue> performances = evaluator.evaluate(PerformanceTestHelper.unevaluatedBlockPerformaces(), PerformanceAspect.CASUALTIES, 4);
        assertEquals("Limit must be extended by one", 5, performances.size());
        assertEquals("Lasts elements cas count does not match", 2, performances.get(4).getValue());
        assertEquals("Second to lasts elements cas count does not match", 2, performances.get(3).getValue());
        assertEquals("Lasts elements block count does not match", 4, performances.get(4).getTiebreaker().intValue());
        assertEquals("Second to lasts elements block count does not match", 4, performances.get(3).getTiebreaker().intValue());
        assertFalse("Last two elements are identical", performances.get(4).getPlayerId() == performances.get(3).getPlayerId());
    }


    @Test
    public void tiebreakersSortDsc() {
        List<PerformanceValue> performances = evaluator.evaluate(PerformanceTestHelper.unevaluatedPassPerformaces(), PerformanceAspect.COMPLETIONS, 5);
        assertEquals("Highest completions must be first and second", 7, performances.get(0).getValue());
        assertEquals("Highest completions must be first and second", 7, performances.get(1).getValue());
        assertEquals("Second highest completions must be third and fourth", 4, performances.get(2).getValue());
        assertEquals("Second highest completions must be third and fourth", 4, performances.get(3).getValue());
        assertEquals("Third highest completions must be last", 2, performances.get(4).getValue());
        assertEquals("Second place goes to lowest tiebreaker", 10, performances.get(1).getTiebreaker().intValue());
        assertEquals("First place goes to highest tiebreaker", 12, performances.get(0).getTiebreaker().intValue());
    }

    @Test
    public void tiebreakersSortDscNumbersCorrectly() {
        List<PerformanceValue> performances = evaluator.evaluate(PerformanceTestHelper.unevaluatedPassPerformaces(), PerformanceAspect.COMPLETIONS, 6);
        assertEquals("First place is for first", 1, performances.get(0).getPlace());
        assertEquals("Second place is for second", 2, performances.get(1).getPlace());
        assertEquals("Third place is for third and fourth", 3, performances.get(2).getPlace());
        assertEquals("Third place is for third and fourth", 3, performances.get(3).getPlace());
        assertEquals("Fourth place is for fifth", 4, performances.get(4).getPlace());
        assertEquals("Fifth place is for sixth", 5, performances.get(5).getPlace());
    }

    @Test
    public void tiebreakersSortDscExtendsLimit() {
        List<PerformanceValue> performances = evaluator.evaluate(PerformanceTestHelper.unevaluatedPassPerformaces(), PerformanceAspect.COMPLETIONS, 3);
        assertEquals("Limit must be extended by one", 4, performances.size());
        assertEquals("Lasts elements completion count does not match", 4, performances.get(3).getValue());
        assertEquals("Second to lasts elements completion count does not match", 4, performances.get(2).getValue());
        assertEquals("Lasts elements pass count does not match", 6, performances.get(3).getTiebreaker().intValue());
        assertEquals("Second to lasts elements pass count does not match", 6, performances.get(2).getTiebreaker().intValue());
        assertFalse("Last two elements are identical", performances.get(2).getPlayerId() == performances.get(3).getPlayerId());
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