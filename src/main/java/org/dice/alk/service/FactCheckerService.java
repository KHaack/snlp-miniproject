package org.dice.alk.service;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.dice.alk.io.IOUtils;
import org.dice.alk.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FactCheckerService {

    @Autowired
    private InputProcessorService inputProcessor;

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

            // TODO assign score
            // TODO if only one wikipedia page was found, ie the api wasn't able
            // to identify all of the present entities, maybe search for the 
            // unidentified text

            model.add(curSent.getStatementFromSentence());
        }

        return model;
    }

}
