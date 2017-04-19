package org.butterbrot.fls;

import java.util.List;

public class PerformancesWrapper {
    private List<PerformanceValue> performances;
    private String title;

    public PerformancesWrapper(List<PerformanceValue> performances, String title) {
        this.performances = performances;
        this.title = title;
    }

    public List<PerformanceValue> getPerformances() {
        return performances;
    }

    public String getTitle() {
        return title;
    }
}
