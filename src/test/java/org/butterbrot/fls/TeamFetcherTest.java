package org.butterbrot.fls;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.butterbrot.fls.TestFileHelper.loadFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeamFetcherTest {

    private static final String FILE_TEAMS = "/teams.xml";

    @InjectMocks
    private TeamFetcher teamFetcher;

    @Mock
    private RestTemplate fumbblTemplate;

    @Before
    public void setUp() throws JAXBException {
        ReflectionTestUtils.setField(teamFetcher, "jaxbContext", JAXBContext.newInstance(Team.class));
    }

    @Test
    public void getTeams() throws IOException, JAXBException {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>(loadFile(FILE_TEAMS), HttpStatus.OK));
        List<Team> teams = teamFetcher.getTeams(0);
        assertEquals("Team list size does not match", 3, teams.size());
        assertTrue("Did not contain team "+ 874289, teams.contains(new Team(874289,"[D-A-CH] Pest Beuler", "Eisenherz","D-A-CH")));
        assertTrue("Did not contain team "+874359, teams.contains(new Team(874359,"[D-A-CH]Black Forest Crushers", "fuzzel1985","D-A-CH")));
        assertTrue("Did not contain team "+874376, teams.contains(new Team(874376,"[D-A-CH]s-Unternehmen", "Hollaender","D-A-CH")));
    }

    @Test(expected = RestClientException.class)
    public void httpError() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenThrow(new RestClientException("Expected exception"));
        teamFetcher.getTeams(0);
    }

}