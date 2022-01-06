package org.dice.alk.service;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharSequenceNodeFactory;
import com.googlecode.concurrenttrees.solver.LCSubstringSolver;
import org.dice.alk.exception.InputProcessorException;
import org.dice.alk.model.Sentence;
import org.dice.alk.model.TagMeResult;
import org.dice.alk.model.TagMeSpot;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private TagMeService tagMeService;

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
	 * Returns the present entities ordered by appearance position
	 * 
	 * @param sentence
	 * @return
	 */
	public void processTextInput(Sentence sentence) {
		// preprocess input, remove all unnecessary stuff
		String input = this.cleanSentence(sentence.getSentenceText());

		// get result from API and parse it
		TagMeResult result = this.tagMeService.tag(input);
		sentence.setEntities(result.getAnnotations());

		// signal this sentence as false
		if (result.getAnnotations().size() < 2) {
			throw new InputProcessorException("not 2 entities found.");
		}

		// get predicate by subtracting the identified entities
		String predicate = this.getPredicate(input, result.getAnnotations());
		sentence.setPredicate(predicate);
	}

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
	 * @param input
	 * @param relevantItems
	 * @return
	 */
	public String getPredicate(String input, List<TagMeSpot> relevantItems) {
		String predicate = new String(input);

		// remove mentions of entities
		for (TagMeSpot spot : relevantItems) {
			predicate = predicate.replace(spot.getSpot(), "");
		}

		// remove extra useless stuff
		for (String bl : UNWANTED_PRED) {
			predicate = predicate.replaceAll(bl, "");
		}

		// FIXME this fails if we can't identify all entities, this dataset has only
		// 10 relations or so, maybe we can just search for them directly

		return predicate.trim();
	}
	
	/**
	 * Retrieves predicate from list if existing, otherwise fallback to other
	 * method.
	 * 
	 * @param input
	 * @param relevantItems
	 * @return
	 */
	public String getPredicateFromExisting(String input, List<TagMeSpot> relevantItems) {
		for (String rel : PREDICATES) {
			if(input.contains(rel)) {
				return rel;
			}
		}
		
		//TODO synonyms
		
		return getPredicate(input, relevantItems);
	}

	/**
	 * Returns the corresponding Wikipedia articles
	 * 
	 * @param relevantItems
	 * @return
	 */
	public List<String> getWikipediaURLS(List<TagMeSpot> relevantItems) {
		List<String> wikipediaPaths = new ArrayList<>();
		for (TagMeSpot spot : relevantItems) {
			wikipediaPaths.add(wikipediaEndpoint + spot.getTitle());
		}
		return wikipediaPaths;
	}

	/**
	 * Returns for a given text it return a map containing the spot and the wikipedia of the spot
	 *
	 * @param input
	 * @return
	 */
	public Map<String, String> getWikipediaURLSAsSet(String input) {
		List<TagMeSpot> relevantItems = this.tagMeService.tag(input).getAnnotations();
		Map<String, String> wikipediaPaths = new HashMap<>();

		for (TagMeSpot spot : relevantItems) {
			wikipediaPaths.put(spot.getSpot(), wikipediaEndpoint + spot.getTitle());
		}
		return wikipediaPaths;
	}
}
