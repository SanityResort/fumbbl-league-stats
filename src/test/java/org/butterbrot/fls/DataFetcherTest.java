package org.butterbrot.fls;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DataFetcherTest {

    private static final String FILE_MATCHES_WITH_PAGING = "/matches_with_paging.xml";
    private static final String FILE_MATCHES_WITHOUT_PAGING = "/matches_without_paging.xml";
    private static Map<Integer, Performance> expectedPerformances = expectedPerformances();

    @InjectMocks
    private DataFetcher dataFetcher;

    @Mock
    private RestTemplate fumbblTemplate;

    @Test
    public void getPerformances() throws IOException {
        Set<Performance> actualPerformances = dataFetcher.getPerformances("", "");
        assertEquals("Sizes do not match", expectedPerformances.size(), actualPerformances.size());
    }

    @Test
    public void noPaging(){
        dataFetcher.getPerformances("", "");
        verify(fumbblTemplate).getForEntity(any(URI.class), eq(String.class));
        verifyNoMoreInteractions(fumbblTemplate);
    }

    @Test
    public void paging(){
        dataFetcher.getPerformances("", "");
        verify(fumbblTemplate, times(2)).getForEntity(any(URI.class), eq(String.class));
        verifyNoMoreInteractions(fumbblTemplate);
    }

    private String loadFile(String file) throws IOException {

        try (InputStream in = this.getClass().getResourceAsStream(file)) {
            return new String(IOUtils.toByteArray(in), "UTF-8");
        }
    }

    private void assertPerformances(Set<Performance> actualPerformances) {
        for (Performance performance: actualPerformances) {
            assertPerformance(expectedPerformances.get(performance.getPlayerId()), performance);
        }
    }

    private void assertPerformance(Performance expectedPerformance, Performance actualPerformance) {
        assertEquals("IDs do not match", expectedPerformance.getPlayerId(), actualPerformance.getPlayerId());
        assertEquals("Blocks do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getBlocks(), actualPerformance.getBlocks());
        assertEquals("Casualties do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getCasualties(), actualPerformance.getCasualties());
        assertEquals("Completions do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getCompletions(), actualPerformance.getCompletions());
        assertEquals("Fouls do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getFouls(), actualPerformance.getFouls());
        assertEquals("Interceptions do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getInterceptions(), actualPerformance.getInterceptions());
        assertEquals("Mvps do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getMvps(), actualPerformance.getMvps());
        assertEquals("Passing do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getPassing(), actualPerformance.getPassing());
        assertEquals("Rushing do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getRushing(), actualPerformance.getRushing());
        assertEquals("Touchdowns do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getTouchdowns(), actualPerformance.getTouchdowns());
        assertEquals("Turns do not match for PlayerId " + actualPerformance.getPlayerId(), expectedPerformance.getTurns(), actualPerformance.getTurns());
    }

    private static Performance createPerformance(int playerId, int blocks, int casualties, int completions, int fouls,
                                                 int interceptions, int mvps, int passing, int rushing, int touchdowns, int turns) {
        Performance performance = new Performance(playerId);
        performance.setBlocks(blocks);
        performance.setCasualties(casualties);
        performance.setCompletions(completions);
        performance.setFouls(fouls);
        performance.setInterceptions(interceptions);
        performance.setMvps(mvps);
        performance.setPassing(passing);
        performance.setRushing(rushing);
        performance.setTouchdowns(touchdowns);
        performance.setTurns(turns);

        return performance;
    }

    private static Map<Integer, Performance> expectedPerformances() {
        Map<Integer, Performance> expectedPerformances = new HashMap<>();
        expectedPerformances.put(10, createPerformance(10, 6 + 3, 1, 1, 0, 0 + 1,
                0, 5, 0 + 10, 1, 10 + 12));
        expectedPerformances.put(11, createPerformance(11, 3, 0, 0, 0, 1,
                0, 0, 10, 0, 12));
        expectedPerformances.put(12, createPerformance(12, 13 + 13 + 5, 1 + 1 + 1, 0, 0 + 1, 0,
                0, 0, 0, 0, 17 + 17 + 17));
        expectedPerformances.put(13, createPerformance(13, 4 + 4 + 13, 0 + 1, 0, 0, 0,
                1 + 1, 0, 0, 0, 15 + 15 + 17));
        expectedPerformances.put(14, createPerformance(14, 5 + 5, 1 + 1, 0, 1 + 1, 0,
                0, 0, 0, 0, 17 + 17));
        expectedPerformances.put(16, createPerformance(16, 6, 1, 1, 0, 0,
                0, 5, 0, 0, 10));
        expectedPerformances.put(17, createPerformance(17, 3, 0, 0, 0, 1,
                0, 0, 10, 0, 12));
        expectedPerformances.put(18, createPerformance(18, 6, 1, 1, 0, 0,
                0, 5, 0, 0, 10));
        expectedPerformances.put(19, createPerformance(19, 4, 0, 0, 0, 0,
                1, 0, 0, 0, 15));

        return expectedPerformances;
    }
}