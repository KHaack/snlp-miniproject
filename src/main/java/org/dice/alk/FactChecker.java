package org.dice.alk;

import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.dice.alk.io.IOUtils;
import org.dice.alk.io.Sentence;
import org.dice.alk.nlp.ITextProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FactChecker {

	@Autowired
	private ITextProcessor inputProcessor;

	/**
	 * Empty constructor.
	 */
	public FactChecker() {
	}

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
			inputProcessor.processTextInput(curSent.getSentenceText());

			// TODO assign score

			model.add(curSent.getStatementFromSentence());
		}
		return model;
	}

}
