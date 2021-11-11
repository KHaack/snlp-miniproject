package org.dice.alk.nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * This class is responsible for parsing and processing the input string.
 */
@Component
public class InputProcessor implements ITextProcessor {
	/**
	 * Tokenization	tokenize
	 * Sentence Splitting	ssplit
	 * Part of Speech Tagging	pos
	 * Dependency Parsing	depparse
	 * Lemmatization	lemma
	 * Named Entity Recognition	ner
	 */
	private static final String PIPELINE_PROPERTIES = "tokenize,ssplit,pos,depparse,lemma,ner";

	/**
	 *  Object that can be used to process a text.
	 */
	private final StanfordCoreNLP pipeline;


	/**
	 * Constructor.
	 */
	public InputProcessor() {
		Properties props = new Properties();
		props.setProperty("annotators", PIPELINE_PROPERTIES);
		pipeline = new StanfordCoreNLP(props);
	}
	
	@Override
	public List<String> processTextInput(String input) {

		// run annotators on text
		CoreDocument document = new CoreDocument(input);
		pipeline.annotate(document);


		List<String> tokens = new LinkedList<>();
		for (CoreEntityMention entity : document.entityMentions()) {
			tokens.add(entity.text());
		}
		// TODO extract relation from document

		return tokens;

	}

}
