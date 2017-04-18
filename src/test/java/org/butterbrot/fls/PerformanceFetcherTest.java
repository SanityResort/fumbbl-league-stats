package org.butterbrot.fls;

import org.apache.commons.io.IOUtils;
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
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceFetcherTest extends PerformanceTest {

    private static final String FILE_MATCHES_WITH_PAGING = "/matches_with_paging.xml";
    private static final String FILE_MATCHES_WITHOUT_PAGING = "/matches_without_paging.xml";
    private static List<Performance> expectedPerformances = unmergedPerformances();

    @InjectMocks
    private PerformanceFetcher performanceFetcher;

    @Mock
    private RestTemplate fumbblTemplate;

    @Before
    public void setUp() throws JAXBException {
        ReflectionTestUtils.setField(performanceFetcher, "jaxbContext", JAXBContext.newInstance(Performance.class));
    }

    @Test
    public void getPerformances() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>(loadFile(FILE_MATCHES_WITHOUT_PAGING), HttpStatus.OK));
        List<Performance> actualPerformances = performanceFetcher.getPerformances("", "");
        assertPerformances(expectedPerformances, actualPerformances);
    }

    @Test(expected = RestClientException.class)
    public void httpError() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenThrow(new RestClientException("Expected exception"));
        performanceFetcher.getPerformances("", "");
    }

    @Test
    public void noPaging() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>(loadFile(FILE_MATCHES_WITHOUT_PAGING), HttpStatus.OK));
        performanceFetcher.getPerformances("", "");
        verify(fumbblTemplate).getForEntity(any(URI.class), eq(String.class));
        verifyNoMoreInteractions(fumbblTemplate);
    }

    @Test
    public void paging() throws Exception {
        when(fumbblTemplate.getForEntity(any(URI.class), eq(String.class))).thenReturn(new ResponseEntity<String>(loadFile(FILE_MATCHES_WITH_PAGING), HttpStatus.OK))
                .thenReturn(new ResponseEntity<String>(loadFile(FILE_MATCHES_WITHOUT_PAGING), HttpStatus.OK));
        performanceFetcher.getPerformances("", "");
        verify(fumbblTemplate, times(2)).getForEntity(any(URI.class), eq(String.class));
        verifyNoMoreInteractions(fumbblTemplate);
    }

    private String loadFile(String file) throws IOException {

        try (InputStream in = this.getClass().getResourceAsStream(file)) {
            return new String(IOUtils.toByteArray(in), "UTF-8");
        }
    }
}