package org.dice.alk.api;

import org.apache.jena.rdf.model.StmtIterator;
import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Sentence;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

    @RequestMapping(value = "/testFactCheck", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<String> testFactCheck() throws FileNotFoundException {
        String inputFile = "./data/SNLP2020_training_test.tsv";
        FileReader inputStreamReader = new FileReader(inputFile);
        Set<Sentence> sentences = IOUtils.readFromFile(inputStreamReader);

        List<String> strings = new LinkedList<>();
        StmtIterator it = this.factCheckerService.factCheck(sentences).listStatements();
        while (it.hasNext()) {
            strings.add(it.nextStatement().getString());
        }
        return strings;
    }
}
