package org.butterbrot.fls;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Configuration
public class Config {

    @Bean
    public RestTemplate fumbblTemplate(){
        return new RestTemplate();
    }

    @Bean
    public JAXBContext jaxbContext() throws JAXBException {
        return JAXBContext.newInstance(Performance.class, Tournament.class);
    }

    @Bean
    public PerformanceMerger performanceMerger() {
        return new PerformanceMerger();
    }

    @Bean
    public PerformanceFetcher performanceFetcher() {
        return new PerformanceFetcher();
    }

    @Bean
    public TournamentFetcher tournamentFetcher() {
        return new TournamentFetcher();
    }

    @Bean
    public PerformanceEvaluator performanceEvaluator() {
        return new PerformanceEvaluator();
    }

    @Bean
    public PlayerFetcher playerFetcher() {
        return new PlayerFetcher();
    }
    @Bean
    public BBCodeBuilder bbCodeBuilder(){
        return new BBCodeBuilder();
    }
}
