package org.dice.alk.service;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharSequenceNodeFactory;
import com.googlecode.concurrenttrees.solver.LCSubstringSolver;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for parsing and processing the input string.
 */
@Service
public class InputProcessorService {

	@Value("${wikipedia.endpoint}")
	private String wikipediaEndpoint;

	private final String[] UNWANTED = { " is ", " the ", " a ", " an ", "'s ", "'", "\\s+" };
	private final String[] UNWANTED_PRED = { "of ", "\\p{Punct}" };

	/**
	 * Existing relations in the dataset
	 */
	private final String[] PREDICATES = { 
			"birth place", 
			"death place", 
			"award", 
			"author", 
			"spouse", 
			"stars", 
			"team",
			"subsidiary",
			"foundation place"};

	/**
	 * Retrieves the size of the longest common substring between 2 strings.
	 * 
	 * @param string1
	 * @param string2
	 * @return
	 */
	public int getLongestCommonSubstringSize(String string1, String string2) {
		LCSubstringSolver solver = new LCSubstringSolver(new DefaultCharSequenceNodeFactory());
		solver.add(string1);
		solver.add(string2);
		return solver.getLongestCommonSubstring().length();
	}

	/**
	 * FIXME better ways of doing this
	 *
	 * @param input
	 * @return
	 */
	public String cleanSentence(String input) {
		for (String bl : UNWANTED) {
			input = input.replaceAll(bl, " ");
		}
		return input;
	}

	/**
	 * Returns predicate from input sentence by removing the identified entities
	 * mentioned in the phrase and by removing punctuation. FIXME this fails if we
	 * can't identify all entities, this dataset has only 10 relations or so, maybe
	 * we can just search for them directly
	 *
	 * @param sentence
	 * @return
	 */
	public void getPredicate(Sentence sentence) {
		// preprocess input, remove all unnecessary stuff
		String input = this.cleanSentence(sentence.getSentenceText());

		// remove mentions of entities
		for (Entity spot : sentence.getEntities()) {
			input = input.replace(spot.getText(), "");
		}

		// remove extra useless stuff
		for (String bl : UNWANTED_PRED) {
			input = input.replaceAll(bl, "");
		}

		// FIXME this fails if we can't identify all entities, this dataset has only
		// get predicate by subtracting the identified entities
		// 10 relations or so, maybe we can just search for them directly
		sentence.setPredicate(input.trim());
	}

	/**
	 * Returns the corresponding Wikipedia articles
	 *
	 * @param relevantItems
	 * @return
	 */
	public List<String> getWikipediaURLS(List<Entity> relevantItems) {
		List<String> wikipediaPaths = new ArrayList<>();
		for (Entity spot : relevantItems) {
			wikipediaPaths.add(wikipediaEndpoint + spot.getWikipediaTitle());
		}
		return wikipediaPaths;
	}

	/**
	 * Returns for a given text it return a map containing the spot and the wikipedia of the spot
	 *
	 * @param sentence
	 * @return
	 */
	public Map<Entity, String> getWikipediaURLSAsSet(Sentence sentence) {
		List<Entity> relevantItems = sentence.getEntities();
		Map<Entity, String> wikipediaPaths = new HashMap<>();

		for (Entity spot : relevantItems) {
			wikipediaPaths.put(spot, wikipediaEndpoint + spot.getWikipediaTitle());
		}

		return wikipediaPaths;
	}
}
