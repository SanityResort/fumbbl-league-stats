package org.butterbrot.fls;

public class PerformanceValue {

    private int playerId;

    private String playerName;
    private String teamName;
    private String teamUrlPath;

    private int value;

    public PerformanceValue(int playerId, int value) {
        this.playerId = playerId;
        this.value = value;
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
}
