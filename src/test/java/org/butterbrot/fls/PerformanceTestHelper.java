package org.butterbrot.fls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class PerformanceTestHelper {

    static void assertPerformances(Collection<Performance> expectedPerformances, Collection<Performance> actualPerformances) {
        assertEquals("Sizes do not match", expectedPerformances.size(), actualPerformances.size());
        for (Performance expectedPerformance : expectedPerformances) {
            assertTrue("Did not find performance for id: " + expectedPerformance.getPlayerId(), actualPerformances.contains(expectedPerformance));
        }
    }

    static Performance createPerformance(int playerId, int blocks, int casualties, int completions, int fouls,
                                                   int interceptions, int mvps, int passing, int rushing, int touchdowns, int turns) {
        Performance performance = new Performance(playerId);
        performance.setBlocks(blocks);
        performance.setCasualties(casualties);
        performance.setCompletions(completions);
        performance.setFouls(fouls);
        performance.setInterceptions(interceptions);
        performance.setMvps(mvps);
        performance.setPassing(passing);
        performance.setRushing(rushing);
        performance.setTouchdowns(touchdowns);
        performance.setTurns(turns);

        return performance;
    }


    static List<Performance> unmergedPerformances() {
        List<Performance> expectedPerformances = new ArrayList<>();
        expectedPerformances.add(createPerformance(10, 6, 1, 1, 0, 0,
                0, 5, 0, 1, 10));
        expectedPerformances.add(createPerformance(10, 3, 0, 0, 0, 1,
                0, 0, 10, 0, 12));
        expectedPerformances.add(createPerformance(11, 3, 0, 0, 0, 1,
                0, 0, 10, 0, 12));
        expectedPerformances.add(createPerformance(12, 13, 1, 0, 0, 0,
                0, 0, 0, 0, 17));
        expectedPerformances.add(createPerformance(12, 5, 1, 0, 1, 0,
                0, 0, 0, 0, 17));
        expectedPerformances.add(createPerformance(12, 13, 1, 0, 0, 0,
                1, 0, 0, 0, 17));
        expectedPerformances.add(createPerformance(13, 4, 0, 0, 0, 0,
                1, 0, 0, 0, 15));
        expectedPerformances.add(createPerformance(13, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 15));
        expectedPerformances.add(createPerformance(13, 13, 1, 0, 0, 0,
                0, 0, 0, 0, 17));
        expectedPerformances.add(createPerformance(14, 5, 1, 0, 1, 0,
                0, 0, 0, 0, 17));
        expectedPerformances.add(createPerformance(14, 5, 1, 0, 1, 0,
                0, 0, 0, 0, 17));
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


    static Set<Performance> unevaluatesdPerformances () {
        Set<Performance> unevaluatedPerformances = new HashSet<>();
        for (int id = 10; id>0; id-- ) {
            int value = id > 6 ? id : (id>3?3:(id > 2 ? 1: 0));
            unevaluatedPerformances.add(createPerformance(id, value, value, value, value, value, value, value, value, value, value));
        }
        return unevaluatedPerformances;
    }
}
