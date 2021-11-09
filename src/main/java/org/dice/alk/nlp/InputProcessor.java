package org.dice.alk.nlp;

import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * This class is responsible for parsing and processing the input string.
 *
 */
@Component
public class InputProcessor implements ITextProcessor {
	
	/**
	 *  Object that can be used to process a text.
	 */
	private final StanfordCoreNLP pipeline;
	
	
	/**
	 * Constructor.
	 */
	public InputProcessor() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, entitylink, kbp"); 
		pipeline = new StanfordCoreNLP(props);
	}
	
	@Override
	public List<String> processTextInput(String input) {
		
		// run annotators on text
        Annotation document = new Annotation(input);
        pipeline.annotate(document);
		
        
        // TODO extract the entities from the document
        
        // TODO extract relation from document
        
        return null;
		
	}

}
