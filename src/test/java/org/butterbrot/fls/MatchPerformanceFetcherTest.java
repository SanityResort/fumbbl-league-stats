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
import java.util.List;

import static org.butterbrot.fls.TestFileHelper.loadFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MatchPerformanceFetcherTest {

    private static final String FILE_MATCHES = "/matches.xml";
    private static final String FILE_NO_MATCHES = "/nomatches.xml";


    @InjectMocks
    private MatchPerformanceFetcher matchPerformanceFetcher;

    @Mock
    private RestTemplate fumbblTemplate;

    @Before
    public void setUp() throws JAXBException {
        ReflectionTestUtils.setField(matchPerformanceFetcher, "jaxbContext", JAXBContext.newInstance(Performance
                .class));
    }

    @Test
    public void getPerformances() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>
                (loadFile(FILE_MATCHES), HttpStatus.OK)).thenReturn(new ResponseEntity<String>(loadFile
                (FILE_NO_MATCHES), HttpStatus.OK));
        List<Performance> performances = matchPerformanceFetcher.getPerformances(1);
        assertEquals("Performance list size does not match", 4, performances.size());
        assertTrue("Did not contain performance for player " + 10, performances.contains(PerformanceTestHelper
                .createPerformance(10, 8, 0, 1, 0, 0, 0, 0, 0, 1, 16)));
        assertTrue("Did not contain performance for player " + 11, performances.contains(PerformanceTestHelper
                .createPerformance(11, 6, 1, 0, 0, 0, 1, 0, 0, 0, 16)));
        assertTrue("Did not contain performance for player " + 11, performances.contains(PerformanceTestHelper
                .createPerformance(11, 9, 2, 0, 0, 0, 0, 0, 0, 0, 0)));
        assertTrue("Did not contain performance for player " + 12, performances.contains(PerformanceTestHelper
                .createPerformance(12, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
    }

    @Test(expected = RestClientException.class)
    public void httpError() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenThrow(new RestClientException
                ("Expected exception"));
        matchPerformanceFetcher.getPerformances(1);
    }

    @Test
    public void noPaging() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>
                (loadFile(FILE_NO_MATCHES), HttpStatus.OK));
        matchPerformanceFetcher.getPerformances(1);
        verify(fumbblTemplate).getForEntity(any(URI.class), eq(String.class));
        verifyNoMoreInteractions(fumbblTemplate);
    }

    @Test
    public void paging() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>
                (loadFile(FILE_MATCHES), HttpStatus.OK)).thenReturn(new ResponseEntity<String>(loadFile
                (FILE_NO_MATCHES), HttpStatus.OK));
        matchPerformanceFetcher.getPerformances(1);
        verify(fumbblTemplate, times(2)).getForEntity(any(URI.class), eq(String.class));
        verifyNoMoreInteractions(fumbblTemplate);
    }

}