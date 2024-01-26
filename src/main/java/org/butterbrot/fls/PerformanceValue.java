package org.butterbrot.fls;

public class PerformanceValue implements Comparable<PerformanceValue> {

    private final int playerId;
    private int place;
    private String playerName;
    private String teamName;
    private String teamUrlPath;

    private final int value;
    private final Integer tiebreaker;

    private final PerformanceAspect aspect;

    public PerformanceValue(int playerId, int value, Integer tiebreaker, PerformanceAspect aspect) {
        this.playerId = playerId;
        this.value = value;
        this.tiebreaker = tiebreaker;
        this.aspect = aspect;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
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
    public int compareTo(@SuppressWarnings("NullableProblems") PerformanceValue that) {
        if (that == null) {
            throw new IllegalArgumentException("Comparing with null value");
        }

        if (this.aspect != that.aspect) {
            throw new IllegalArgumentException("Comparing different aspects: " + this.aspect.name() + " and " + that.aspect.name());
        }

        int result = Integer.compare(that.getValue(), this.getValue());

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
