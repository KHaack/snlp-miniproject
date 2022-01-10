package org.dice.alk.service;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.dice.alk.model.WikipediaDocument;
import org.dice.alk.model.WikipediaParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Set;

@Service
public class FactCheckerService {

    private final static int MAX_PARAGRAPH_DEPTH = 6;

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FactCheckerService.class);

    @Autowired
    private NerService nerService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private WikipediaService wikipedia;

    /**
     * Reads from file, fact checks and writes results to output file
     *
     * @param input  input.
     * @param output Output filepath.
     */
    public void factCheck(InputStreamReader input, Writer output) {
        // read sentences from given file
        Set<Sentence> sentences = IOUtils.readFromFile(input);

        // fact check all sentences and get result as model
        Model model = this.factCheck(sentences);

        // write results to file
        IOUtils.writeResultsToFile(model, output);
    }

    /**
     * Fact checks sentences
     *
     * @param sentences
     * @return
     */
    public Model factCheck(Set<Sentence> sentences) {
        Model model = ModelFactory.createDefaultModel();

        int i = 0;
        for (Sentence sentence : sentences) {
            try {
                sentence.setScore(this.factCheck(sentence));
            } catch (Exception ex) {
                LOGGER.warn("error  {} in {}", ex.getMessage(), sentence.getSentenceText());
                sentence.setScore(0.5);
            }
            LOGGER.info("{} => {}", sentence.getScore(), sentence.getSentenceText());

            Statement statement = sentence.getStatementFromSentence();
            model.add(statement);


            i++;

            if (i % 10 == 0) {
                LOGGER.info("{}/{} ", i, sentences.size());
            }
        }

        return model;
    }

    /**
     * FactCheck for the passed paragraph.
     *
     * @param toCheck
     * @param document
     * @param paragraph
     * @return
     */
    private Double factCheckParagraph(Sentence toCheck, WikipediaDocument document, WikipediaParagraph paragraph) {
        int foundEntities = 0;
        for (Entity entity : toCheck.getEntities()) {
            // 4. check entity exists
            // skip entity from wikipedia article
            if (!entity.equals(document.getEntity())) {
                if (paragraph.getUrls().contains(entity.getWikipediaTitle())) {
                    foundEntities++;
                }
            }
        }

        if (foundEntities >= 1) {
            LOGGER.info("possible: " + paragraph.getText());
            return 1.0;
        }

        return null;
    }

    /**
     * Search for facts.
     *
     * @param toCheck The sentence to check.
     * @return
     */
    public double factCheck(Sentence toCheck) {
        LOGGER.info("fact check for: " + toCheck.getSentenceText());
        Set<Entity> entities = this.nerService.extractSentence(toCheck.getSentenceText());
        toCheck.setEntities(entities);
        this.relationService.extractRelation(toCheck);

        // to checkable with only one entity
        if (toCheck.getEntities().size() < 2) {
            return 0.5;
        }

        // check without relation
        for (Entity entity : toCheck.getEntities()) {
            LOGGER.info("check wikipedia for: " + entity.getWikipediaTitle());
            WikipediaDocument document = this.wikipedia.fetch(entity);

            // check paragraphs
            for (int i = 0; i < Math.min(document.getParagraphs().size(), MAX_PARAGRAPH_DEPTH); i++) {
                Double result = this.factCheckParagraph(toCheck, document, document.getParagraphs().get(i));

                if (null != result) {
                    return result;
                }
            }
        }

        return 0.0;
    }

    /**
     * Check the relations.
     *
     * @param toCheck
     * @param possibleSentence
     * @return
     */
    private double checkRelation(Sentence toCheck, Sentence possibleSentence) {
        LOGGER.info("check relation");
        if (toCheck.getRelations().size() == 0) {
            LOGGER.info("\tno relation to check => true");
            return 1.0;
        }

        for (String relation : toCheck.getRelations()) {
            if (possibleSentence.getRelations().contains(relation)) {
                return 1.0;
            }
        }

        return 0.0;
    }

}
