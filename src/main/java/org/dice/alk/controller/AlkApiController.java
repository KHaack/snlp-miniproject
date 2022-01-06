package org.dice.alk.controller;

import org.dice.alk.service.FactCheckerService;
import org.dice.alk.service.WikipediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@Validated
public class AlkApiController {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AlkApiController.class);
    @Autowired
    private WikipediaService service;
    @Autowired
    private FactCheckerService factCheckerService;



    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public boolean ping() {
        LOGGER.info("ping called");
        return true;
    }

    @RequestMapping(value = "/fetchText", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String fetch() {
        return service.fetch("https://en.wikipedia.org/wiki/Elon_Musk");
    }

    @RequestMapping(value = "/fetchTable", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, String>  fetchTable() {
        return service.fetchTable("https://en.wikipedia.org/wiki/Elon_Musk");
    }

    @RequestMapping(value = "/testFactCheck", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<String> testFactCheck() {
        String inputFile = "./data/SNLP2020_training_test.tsv";
        String outputFile = "./data/SNLP2020_training_result.tsv";
        return this.factCheckerService.factCheckAndReturnAList(inputFile);
    }

}
