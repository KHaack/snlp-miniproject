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
    private RequestService request;

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

        for (Sentence curSent : sentences) {

            // process input text
            inputProcessor.processTextInput(curSent);
            Map<String, String> res = inputProcessor.getWikipediaURLSAsSet(curSent.getSentenceText());
            // classic way, loop a Map
            boolean found = false;
            for (Map.Entry<String, String> entry : res.entrySet()) {
                for (Map.Entry<String, String> secondLoopEntry : res.entrySet()) {
                    String wikipediaPageContent = request.fetch(entry.getValue());
                    if(!Objects.equals(entry.getKey(), secondLoopEntry.getKey())){
                        if(wikipediaPageContent.contains(secondLoopEntry.getKey())){
                            found = true;
                            break;
                        }
                    }

                }
            }
            if(found)
                curSent.setScore(1.0);
            else
                curSent.setScore(0.0);
            model.add(curSent.getStatementFromSentence());
        }

        return model;
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

        for (Sentence curSent : sentences) {

            // process input text
            inputProcessor.processTextInput(curSent);
            Map<String, String> res = inputProcessor.getWikipediaURLSAsSet(curSent.getSentenceText());
            // classic way, loop a Map
            boolean found = false;
            for (Map.Entry<String, String> entry : res.entrySet()) {
                for (Map.Entry<String, String> secondLoopEntry : res.entrySet()) {
                    String wikipediaPageContent = request.fetch(entry.getValue());
                    if(!Objects.equals(entry.getKey(), secondLoopEntry.getKey())){
                        if(wikipediaPageContent.contains(secondLoopEntry.getKey()))
                            found = true;
                    }

                }
            }
            if(found)
                curSent.setScore(1.0);
            else
                curSent.setScore(0.0);
            // TODO assign score
            // TODO if only one wikipedia page was found, ie the api wasn't able
            // to identify all of the present entities, maybe search for the
            // unidentified text

            model.add(curSent.getFactID()+" "+curSent.getSentenceText()+ " "+ curSent.getScore());
        }

        return model;
    }

}
