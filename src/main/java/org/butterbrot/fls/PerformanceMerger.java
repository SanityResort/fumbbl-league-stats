package org.butterbrot.fls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerformanceMerger {


    public List<Performance> merge(List<Performance> performances) {
        Map<Integer, Performance> mergedPerformances = new HashMap<>();
        for (Performance performance: performances) {
            int id = performance.getPlayerId();
            Performance mergedPerformance = mergedPerformances.getOrDefault(id, new Performance(id));
            mergedPerformance.add(performance);
            mergedPerformances.put(id, mergedPerformance);
        }

        return new ArrayList<>(mergedPerformances.values());
    }

}
