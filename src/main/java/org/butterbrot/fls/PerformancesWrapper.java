package org.butterbrot.fls;

import java.util.List;

public class PerformancesWrapper {
    private List<PerformanceValue> performances;
    private String title;
    private String tiebreakerTitle;

    public PerformancesWrapper(List<PerformanceValue> performances, String title, String tiebreakerTitle) {
        this.performances = performances;
        this.title = title;
        this.tiebreakerTitle = tiebreakerTitle;
    }

    public List<PerformanceValue> getPerformances() {
        return performances;
    }

    public String getTiebreakerTitle() {
        return tiebreakerTitle;
    }

    public String getTitle() {
        return title;
    }
}
