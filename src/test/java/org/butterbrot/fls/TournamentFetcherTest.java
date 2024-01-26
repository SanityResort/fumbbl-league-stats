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
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.butterbrot.fls.TestFileHelper.loadFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TournamentFetcherTest {

    private static final String FILE_TOURNAMENTS = "/tournaments.xml";
    private static final Set<Tournament> expectedTournaments = expectedTournaments();

    @InjectMocks
    private TournamentFetcher tournamentFetcher;

    @Mock
    private RestTemplate fumbblTemplate;

    @Before
    public void setUp() throws JAXBException {
        ReflectionTestUtils.setField(tournamentFetcher, "jaxbContext", JAXBContext.newInstance(Tournament.class));
    }

    @Test
    public void getTournaments() throws Exception{
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<>(loadFile(FILE_TOURNAMENTS), HttpStatus.OK));
        List<Tournament> actualTournaments = tournamentFetcher.getTournaments(1234);
        assertEquals("Size of tournaments did not match", expectedTournaments.size(), actualTournaments.size());
        for (Tournament tournament: expectedTournaments) {
            assertTrue("Tournament not found for id: " + tournament.getId(), actualTournaments.contains(tournament));
        }
    }

    @Test(expected = RestClientException.class)
    public void httpError() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenThrow(new RestClientException("Expected exception"));
        tournamentFetcher.getTournaments(0);
    }

    private static Set<Tournament> expectedTournaments() {
        Set<Tournament> tournaments = new HashSet<>();
        tournaments.add(new Tournament(1, "Grotty Little Tournament I", 1, "Grotty Little Tournament", 1234));
        tournaments.add(new Tournament(2, "Grotty Little Tournament II", 2, "Grotty Little Tournament", 1234));
        tournaments.add(new Tournament(3, "Grotty Little Tournament III", 3, "Grotty Little Tournament", 1234));
        tournaments.add(new Tournament(4, "Grotty Little Tournament IV", 4, "Grotty Little Tournament", 1234));
        tournaments.add(new Tournament(5, "Grotty Little Tournament V", 5, "Grotty Little Tournament", 1234));
        return tournaments;
    }
}