package org.dice.alk.service;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FactCheckerService {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FactCheckerService.class);

    @Autowired
    private InputProcessorService inputProcessor;

    @Autowired
    private StanfordExtractorService stanfordExtractorService;

    @Autowired
    private WikipediaService wikipedia;

    /**
     * Reads from file, fact checks and writes results to output file
     *
     * @param inputFile  Input filepath.
     * @param outputFile Output filepath.
     */
    public void factCheck(String inputFile, String outputFile) {
        // read sentences from given file
        Set<Sentence> sentences = IOUtils.readFromFile(inputFile);

        // fact check all sentences and get result as model
        Model model = this.factCheck(sentences);

        // write results to file
        IOUtils.writeResultsToFile(model, outputFile);
    }

    /**
     * Fact checks sentences
     *
     * @param sentences
     * @return
     */
    public Model factCheck(Set<Sentence> sentences) {
        Model model = ModelFactory.createDefaultModel();

        for (Sentence sentence : sentences) {
            this.inputProcessor.getPredicate(sentence);
            sentence.setScore(this.factCheck(sentence));
            Statement statement = sentence.getStatementFromSentence();
            model.add(statement);
        }

        return model;
    }

    /**
     * Search for facts.
     *
     * @param toCheck The sentence to check.
     * @return
     */
    public double factCheck(Sentence toCheck) {
        return factCheck(toCheck, null, null, null);
    }

    /**
     * Search for facts.
     *
     * @param toCheck           The sentence to check.
     * @param maxSentences      Number of maximum wikipedia sentences to check
     * @param maxTextLength     Maximum wikipadia text length to check.
     * @param minimumEntityHits Minimum entities that should be in a possible sentence
     * @return
     */
    public double factCheck(Sentence toCheck, Integer maxSentences, Integer maxTextLength, Integer minimumEntityHits) {
        Map<Entity, String> urlSet = this.inputProcessor.getWikipediaURLSAsSet(toCheck);
        List<Sentence> foundSentences = new LinkedList<>();

        for (Entity entity : toCheck.getEntities()) {
            LOGGER.info("search for entity: " + entity.getWikipediaTitle());
        }

        for (Map.Entry<Entity, String> entry : urlSet.entrySet()) {
            LOGGER.info("check wikipedia for: " + entry.getKey().getWikipediaTitle());

            // 1. split into sentences
            // 2. entityLinking
            // 3. ner
            String wikipediaContent = this.wikipedia.fetch(entry.getValue());
            if (null != maxTextLength) {
                wikipediaContent = wikipediaContent.substring(0, Math.min(maxTextLength, wikipediaContent.length()));
            }

            List<Sentence> sentences = this.stanfordExtractorService.extract(wikipediaContent);

            // 4. check entity exists
            int end = sentences.size();

            if (null != maxSentences) {
                end = Math.min(maxSentences, sentences.size());
            }

            for (int i = 0; i < end; i++) {
                int foundEntities = 0;
                Sentence sentence = sentences.get(i);
                LOGGER.info("sentence: " + sentence.getSentenceText());

                for (Entity entity : toCheck.getEntities()) {
                    // skip entity from wikipedia article
                    if (!entity.equals(entry.getKey())) {
                        if (sentence.entityExists(entity)) {
                            foundEntities++;
                        }
                    }
                }

                if (foundEntities >= Math.max(minimumEntityHits, toCheck.getEntities().size() - 1)) {
                    LOGGER.info("\tpossible!");
                    foundSentences.add(sentence);
                }
                LOGGER.info("----------------------------");
            }

            // 5. check possible sentences relation
            // 5.1 get synonyms
        }

        return foundSentences.size() > 0 ? 1.0 : 0.0;
    }

}
