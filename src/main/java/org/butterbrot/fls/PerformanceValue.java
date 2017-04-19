package org.butterbrot.fls;

public class PerformanceValue implements Comparable {

    private int playerId;

    private String playerName;
    private String teamName;
    private String teamUrlPath;

    private int value;
    private Integer tiebreaker;

    private PerformanceAspect aspect;

    public PerformanceValue(int playerId, int value, Integer tiebreaker, PerformanceAspect aspect) {
        this.playerId = playerId;
        this.value = value;
        this.tiebreaker = tiebreaker;
        this.aspect = aspect;
    }

    public Integer getTiebreaker() {
        return tiebreaker;
    }

    public int getValue() {
        return value;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamUrlPath() {
        return teamUrlPath;
    }

    public void setTeamUrlPath(String teamUrlPath) {
        this.teamUrlPath = teamUrlPath;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Comparing with null value");
        }
        if (! (o instanceof PerformanceValue)) {
            throw new IllegalArgumentException("Comparing invalid class: " + o.getClass().getSimpleName());
        }

        PerformanceValue that = (PerformanceValue) o;

        if (this.aspect != that.aspect) {
            throw new IllegalArgumentException("Comparing different aspects: " + this.aspect.name() + " and " + that.aspect.name());
        }

        int result = Integer.valueOf(that.getValue()).compareTo(this.getValue());

        if (result == 0 && aspect.hasTiebreaker()) {
            if (aspect.isSortTiebreakAsc()) {
                result = this.getTiebreaker().compareTo(that.getTiebreaker());
            } else {
                result = that.getTiebreaker().compareTo(this.getTiebreaker());
            }
        }

        return result;
    }
}
