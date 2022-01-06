package org.dice.alk.service;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FactCheckerService {

    @Autowired
    private InputProcessorService inputProcessor;
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
        Model model = factCheck(sentences);

        // write results to file
        IOUtils.writeResultsToFile(model, outputFile);
    }

    /**
     * Fact checks sentences
     *
     * @param sentences
     * @return
     */
    private Model factCheck(Set<Sentence> sentences) {
        Model model = ModelFactory.createDefaultModel();

        for (Sentence sentence : sentences) {
            // process input text
            this.inputProcessor.processTextInput(sentence);
            Map<String, String> urlSet = this.inputProcessor.getWikipediaURLSAsSet(sentence.getSentenceText());

            double score = this.searchForFact(urlSet);
            sentence.setScore(score);

            model.add(sentence.getStatementFromSentence());
        }

        return model;
    }

    /**
     * Search for facts.
     *
     * @param urlSet
     * @return
     */
    private double searchForFact(Map<String, String> urlSet) {
        boolean found = false;

        for (Map.Entry<String, String> entry : urlSet.entrySet()) {
            for (Map.Entry<String, String> secondLoopEntry : urlSet.entrySet()) {
                String wikipediaPageContent = this.wikipedia.fetch(entry.getValue());

                if (!Objects.equals(entry.getKey(), secondLoopEntry.getKey())) {
                    if (wikipediaPageContent.contains(secondLoopEntry.getKey())) {
                        found = true;
                        break;
                    }
                }

            }
        }

        return found ? 1.0 : 0.0;
    }

    /**
     * Fact checks sentences to return them as list for the browser
     *
     * @param inputFile
     * @return
     */
    public List<String> factCheckAndReturnAList(String inputFile) {
        // read sentences from given file
        Set<Sentence> sentences = IOUtils.readFromFile(inputFile);

        // fact check all sentences and get result as model
        List<String> model = new ArrayList<>();

        for (Sentence sentence : sentences) {
            // process input text
            this.inputProcessor.processTextInput(sentence);
            Map<String, String> urlSet = this.inputProcessor.getWikipediaURLSAsSet(sentence.getSentenceText());

            // classic way, loop a Map
            double score = this.searchForFact(urlSet);
            sentence.setScore(score);

            model.add(sentence.getFactID() + " " + sentence.getSentenceText() + " " + sentence.getScore());
        }

        return model;
    }

}
