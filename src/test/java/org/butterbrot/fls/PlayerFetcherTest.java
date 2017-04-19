package org.butterbrot.fls;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Set;

import static org.butterbrot.fls.TestFileHelper.loadFile;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerFetcherTest {

    private static final String FILE_PLAYER = "/player.html";

    @InjectMocks
    private PlayerFetcher playerFetcher;

    @Mock
    private RestTemplate fumbblTemplate;

    @Test
    public void populate() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>(loadFile(FILE_PLAYER), HttpStatus.OK));
        PerformanceValue performance = new PerformanceValue(0, 0);
        playerFetcher.populate(performance);
        assertEquals("Player name not set correctly", "Kletran Minotaurson", performance.getPlayerName());
        assertEquals("Team name not set correctly", "[D-A-CH] Brüder des Donners", performance.getTeamName());
        assertEquals("Team url path not set correctly", "/p/team?op=view&team_id=874507", performance.getTeamUrlPath());
    }

    @Test(expected = RestClientException.class)
    public void httpError() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenThrow(new RestClientException("Expected exception"));
        playerFetcher.populate(new PerformanceValue(0, 0));
    }
}