package org.butterbrot.fls;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.butterbrot.fls.PerformanceTestHelper.assertPerformances;
import static org.butterbrot.fls.PerformanceTestHelper.createPerformance;
import static org.butterbrot.fls.PerformanceTestHelper.unmergedPerformances;


public class PerformanceMergerTest {

    private final PerformanceMerger performanceMerger = new PerformanceMerger();

    @Test
    public void merge() {
        Set<Performance> mergedPerformances = performanceMerger.merge(unmergedPerformances());
        assertPerformances(expectedPerformances(), mergedPerformances);
    }


    private static Set<Performance> expectedPerformances() {
        Set<Performance> expectedPerformances = new HashSet<>();
        expectedPerformances.add(createPerformance(10, 6 + 3, 1, 1, 0, 1,
                0, 5, 10, 1, 10 + 12));
        expectedPerformances.add(createPerformance(11, 3, 0, 0, 0, 1,
                0, 0, 10, 0, 12));
        expectedPerformances.add(createPerformance(12, 13 + 13 + 5, 1 + 1 + 1, 0, 1, 0,
                1, 0, 0, 0, 17 + 17 + 17));
        expectedPerformances.add(createPerformance(13, 4 + 4 + 13, 1, 0, 0, 0,
                1, 0, 0, 0, 15 + 15 + 17));
        expectedPerformances.add(createPerformance(14, 5 + 5, 1 + 1, 0, 1 + 1, 0,
                0, 0, 0, 0, 17 + 17));
        expectedPerformances.add(createPerformance(16, 6, 1, 1, 0, 0,
                0, 5, 0, 0, 10));
        expectedPerformances.add(createPerformance(17, 3, 0, 0, 0, 1,
                0, 0, 10, 0, 12));
        expectedPerformances.add(createPerformance(18, 6, 1, 1, 0, 0,
                0, 5, 0, 0, 10));
        expectedPerformances.add(createPerformance(19, 4, 0, 0, 0, 0,
                1, 0, 0, 0, 15));

        return expectedPerformances;
    }

}