package org.butterbrot.fls;

public class Performance {

    private int playerId;

    private int blocks;
    private int casualties;
    private int completions;
    private int fouls;
    private int interceptions;
    private int mvps;
    private int passing;
    private int rushing;
    private int touchdowns;
    private int turns;

    public void add(Performance other) {
        if (this.getPlayerId() != other.getPlayerId()) {
            throw new IllegalArgumentException("PlayerIds did not match. This had "+this.getPlayerId()+" - Other had "+ other.getPlayerId());
        }

        this.setBlocks(this.getBlocks()+other.getBlocks());
        this.setCasualties(this.getCasualties()+other.getCasualties());
        this.setCompletions(this.getCompletions()+other.getCompletions());
        this.setFouls(this.getFouls()+other.getFouls());
        this.setInterceptions(this.getInterceptions()+other.getInterceptions());
        this.setMvps(this.getMvps()+other.getMvps());
        this.setPassing(this.getPassing()+other.getPassing());
        this.setRushing(this.getRushing()+other.getRushing());
        this.setTouchdowns(this.getTouchdowns()+other.getTouchdowns());
        this.setTurns(this.getTurns()+other.getTurns());
    }

    public Performance(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getCompletions() {
        return completions;
    }

    public void setCompletions(int completions) {
        this.completions = completions;
    }

    public int getTouchdowns() {
        return touchdowns;
    }

    public void setTouchdowns(int touchdowns) {
        this.touchdowns = touchdowns;
    }

    public int getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(int interceptions) {
        this.interceptions = interceptions;
    }

    public int getCasualties() {
        return casualties;
    }

    public void setCasualties(int casualties) {
        this.casualties = casualties;
    }

    public int getMvps() {
        return mvps;
    }

    public void setMvps(int mvps) {
        this.mvps = mvps;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getRushing() {
        return rushing;
    }

    public void setRushing(int rushing) {
        this.rushing = rushing;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getFouls() {
        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }
}
