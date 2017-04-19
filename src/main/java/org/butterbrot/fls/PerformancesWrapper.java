package org.butterbrot.fls;

import java.util.List;

public class PerformancesWrapper {
    private List<Performance> performances;
    private String title;

    public PerformancesWrapper(List<Performance> performances, String title) {
        this.performances = performances;
        this.title = title;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public String getTitle() {
        return title;
    }
}
