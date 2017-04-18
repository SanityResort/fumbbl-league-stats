package org.butterbrot.fls;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.butterbrot.fls.PerformanceTestHelper.*;


public class PerformanceMergerTest {

    private PerformanceMerger performanceMerger = new PerformanceMerger();

    @Test
    public void merge() {
        List<Performance> mergedPerformances = performanceMerger.merge(unmergedPerformances());
        assertPerformances(expectedPerformances(), mergedPerformances);
    }


    private static List<Performance> expectedPerformances() {
        List<Performance> expectedPerformances = new ArrayList<>();
        expectedPerformances.add(createPerformance(10, 6 + 3, 1, 1, 0, 0 + 1,
                0, 5, 0 + 10, 1, 10 + 12));
        expectedPerformances.add(createPerformance(11, 3, 0, 0, 0, 1,
                0, 0, 10, 0, 12));
        expectedPerformances.add(createPerformance(12, 13 + 13 + 5, 1 + 1 + 1, 0, 0 + 1, 0,
                1, 0, 0, 0, 17 + 17 + 17));
        expectedPerformances.add(createPerformance(13, 4 + 4 + 13, 0 + 1, 0, 0, 0,
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