package org.dice.alk.service;

import java.util.*;
import java.util.stream.Collectors;

import org.dice.alk.model.Sentence;
import org.dice.alk.model.TagMeResult;
import org.dice.alk.model.TagMeSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharSequenceNodeFactory;
import com.googlecode.concurrenttrees.solver.LCSubstringSolver;

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
		String input = preprocessInput(sentence.getSentenceText());
		System.out.println(input);

		// get result from API and parse it
		TagMeResult result = this.tagMeService.tag(input);

		// signal this sentence as false
		if (result.getAnnotations().size() < 2) {
			return;
		}

		// sort multiple entities problem
		List<TagMeSpot> entities = pruneDuplicates(result);
		sentence.setEntities(entities);

		// get predicate by subtracting the identified entities
		String predicate = getPredicate(input, result.getAnnotations());
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
	public String preprocessInput(String input) {
		String text = new String(input);
		for (String bl : UNWANTED) {
			text = text.replaceAll(bl, " ");
		}
		return text;
	}

	/**
	 * Prunes duplicate pages from the API result.
	 * 
	 * @param result
	 * @return
	 */
	public List<TagMeSpot> pruneDuplicates(TagMeResult result) {
		List<TagMeSpot> annotations = result.getAnnotations();

		// no point in fixing if it's just 2
		if (annotations.size() == 2)
			return annotations;

		// check if they have multiple mentions of the exact same page
		Map<String, TagMeSpot> map = new HashMap<>();
		annotations.forEach(spot -> map.putIfAbsent(spot.getTitle(), spot));
		annotations = map.values().stream().collect(Collectors.toList());

		// decide on direction, consider the one with smallest start
		Collections.sort(annotations, (o1, o2) -> o1.getStart() - o2.getStart());

		return annotations;
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
	 * Retrieves all found wikipedia paths.
	 * 
	 * @deprecated Might not be useful in our scenario anymore, but keeping it for
	 *             testing purposes.
	 * @param input
	 * @return
	 */
	public List<String> getAllWikipediaPaths(String input) {
		TagMeResult result = this.tagMeService.tag(input);
		return getWikipediaURLS(result.getAnnotations());
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

	public Map<String, String> getWikipediaURLSAsSet(String input) {
		List<TagMeSpot> relevantItems = this.tagMeService.tag(input).getAnnotations();
		Map<String, String> wikipediaPaths = new HashMap<>();
		for (TagMeSpot spot : relevantItems) {
			wikipediaPaths.put(spot.getSpot(), wikipediaEndpoint + spot.getTitle());
		}
		return wikipediaPaths;
	}
}
