package org.butterbrot.fls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Controller
@RequestMapping("/tournament")
@SpringBootApplication
public class Stats {

    @RequestMapping("{tournamentId}")
    @ResponseBody
    String tournament(@PathVariable String tournamentId) {
        return "Hello World! " + tournamentId;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Stats.class, args);
    }
}
