package org.butterbrot.fls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "performance")
@XmlAccessorType(XmlAccessType.FIELD)
public class Performance {

    @XmlAttribute(name = "player")
    private int playerId;

    @XmlAttribute(name = "blocks")
    private int blocks;
    @XmlAttribute(name = "casualties")
    private int casualties;
    @XmlAttribute(name = "completions")
    private int completions;
    @XmlAttribute(name = "fouls")
    private int fouls;
    @XmlAttribute(name = "interceptions")
    private int interceptions;
    @XmlAttribute(name = "mvps")
    private int mvps;
    @XmlAttribute(name = "passing")
    private int passing;
    @XmlAttribute(name = "rushing")
    private int rushing;
    @XmlAttribute(name = "touchdowns")
    private int touchdowns;
    @XmlAttribute(name = "turns")
    private int turns;

    private int spp;

    public void add(Performance other) {
        if (this.getPlayerId() != other.getPlayerId()) {
            throw new IllegalArgumentException("PlayerIds did not match. This had " + this.getPlayerId() + " - Other " +
                    "had " + other.getPlayerId());
        }

        this.setBlocks(this.getBlocks() + other.getBlocks());
        this.setCasualties(this.getCasualties() + other.getCasualties());
        this.setCompletions(this.getCompletions() + other.getCompletions());
        this.setFouls(this.getFouls() + other.getFouls());
        this.setInterceptions(this.getInterceptions() + other.getInterceptions());
        this.setMvps(this.getMvps() + other.getMvps());
        this.setPassing(this.getPassing() + other.getPassing());
        this.setRushing(this.getRushing() + other.getRushing());
        this.setTouchdowns(this.getTouchdowns() + other.getTouchdowns());
        this.setTurns(this.getTurns() + other.getTurns());
        calculateSpp();
    }

    private Performance() {
        //jaxb
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

    public int getSpp() {
        return spp;
    }

    public void calculateSpp() {
        this.spp = this.mvps * 5 + this.completions + this.touchdowns * 3 + this.casualties * 2 + this.interceptions *2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Performance that = (Performance) o;

        if (playerId != that.playerId) return false;
        if (blocks != that.blocks) return false;
        if (casualties != that.casualties) return false;
        if (completions != that.completions) return false;
        if (fouls != that.fouls) return false;
        if (interceptions != that.interceptions) return false;
        if (mvps != that.mvps) return false;
        if (passing != that.passing) return false;
        if (rushing != that.rushing) return false;
        if (touchdowns != that.touchdowns) return false;
        return turns == that.turns;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + blocks;
        result = 31 * result + casualties;
        result = 31 * result + completions;
        result = 31 * result + fouls;
        result = 31 * result + interceptions;
        result = 31 * result + mvps;
        result = 31 * result + passing;
        result = 31 * result + rushing;
        result = 31 * result + touchdowns;
        result = 31 * result + turns;
        return result;
    }
}
