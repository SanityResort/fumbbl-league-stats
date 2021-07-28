package org.butterbrot.fls;

import java.util.List;

public class PerformancesWrapper {
    private final List<PerformanceValue> performances;
    private final String title;
    private final String tiebreakerTitle;
    private String bbCode;

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

    public String getBbCode() {
        return bbCode;
    }

    public void setBbCode(String bbCode) {
        this.bbCode = bbCode;
    }
}
