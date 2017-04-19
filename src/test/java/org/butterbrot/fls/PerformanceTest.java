package org.butterbrot.fls;

import org.junit.Test;

import static org.junit.Assert.*;

public class PerformanceTest {

    @Test
    public void calculateMvp(){
        Performance performance1 = new Performance(0);
        Performance performance2 = new Performance(0);
        performance2.setMvps(2);
        performance1.add(performance2);
        assertEquals("Mvps not calculated correctly", 10, performance1.getSpp());
    }

    @Test
    public void calculateCompletions(){
        Performance performance1 = new Performance(0);
        Performance performance2 = new Performance(0);
        performance2.setCompletions(2);
        performance1.add(performance2);
        assertEquals("Completions not calculated correctly", 2, performance1.getSpp());
    }

    @Test
    public void calculateCas(){
        Performance performance1 = new Performance(0);
        Performance performance2 = new Performance(0);
        performance2.setCasualties(2);
        performance1.add(performance2);
        assertEquals("Cas not calculated correctly", 4, performance1.getSpp());
    }

    @Test
    public void calculateInterceptions(){
        Performance performance1 = new Performance(0);
        Performance performance2 = new Performance(0);
        performance2.setInterceptions(2);
        performance1.add(performance2);
        assertEquals("Interceptions not calculated correctly", 4, performance1.getSpp());
    }

    @Test
    public void calculateTouchdowns(){
        Performance performance1 = new Performance(0);
        Performance performance2 = new Performance(0);
        performance2.setTouchdowns(2);
        performance1.add(performance2);
        assertEquals("Touchdowns not calculated correctly", 6, performance1.getSpp());
    }

    @Test
    public void calculateAll(){
        Performance performance1 = new Performance(0);
        Performance performance2 = new Performance(0);
        performance2.setMvps(1);
        performance2.setCompletions(1);
        performance2.setCasualties(1);
        performance2.setInterceptions(1);
        performance2.setTouchdowns(1);
        performance1.add(performance2);
        assertEquals("Spps not calculated correctly", 13, performance1.getSpp());
    }

    @Test
    public void carryOver(){
        Performance performance1 = new Performance(0);
        Performance performance2 = new Performance(0);
        performance2.setTouchdowns(1);
        performance2.setMvps(1);
        performance1.add(performance2);
        Performance performance3 = new Performance(0);
        performance1.add(performance3);
        assertEquals("Sppss not carried over correctly", 8, performance1.getSpp());
    }
}