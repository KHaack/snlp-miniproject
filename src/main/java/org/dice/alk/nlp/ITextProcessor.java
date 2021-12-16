package org.dice.alk.nlp;

import java.util.Set;

/**
 * The classes implementing this interface should reflect different ways of
 * processing a string input and retrieve the objects of interest to further
 * process the sentence.
 *
 */
public interface ITextProcessor {

	/**
	 * Processes an input sentence and returns a parsed result.
	 * 
	 * @param input Input sentence.
	 * @return 		The parsed result.
	 */
	Set<String> processTextInput(String input);

}