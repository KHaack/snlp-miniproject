package org.dice.alk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.jena.rdf.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds the sentence identification elements
 */
public class Sentence {

	private final String FACT_URI = "http://swc2017.aksw.org/task2/dataset/";

	private final Property TRUTH_VALUE = ResourceFactory.createProperty("http://swc2017.aksw.org/hasTruthValue");

	/**
	 * Fact ID
	 */
	private Integer factID;
	/**
	 * The sentence text
	 */
	private String sentenceText;
	/**
	 * The calculated fact-checking score from the designed system.
	 */
	private double score;

	/**
	 * The sentence's entities
	 */
	private Set<Entity> entities = new HashSet<>();

	/**
	 * The relations in this sentence.
	 */
	private Set<String> relations = new HashSet<>();

	/**
	 * Constructor.
	 *
	 * @param factID       The fact ID.
	 * @param sentenceText The sentence text.
	 */
	public Sentence(Integer factID, String sentenceText) {
		this.factID = factID;
		this.sentenceText = sentenceText;
	}

	/**
	 * Constructor.
	 *
	 * @param sentenceText The sentence text.
	 */
	public Sentence(String sentenceText) {
		this(null, sentenceText);
	}

	/**
	 * Sentence factory method from a {@link String[]}. It is assumed 0 to be the
	 * factID and 1 to be the sentence text.
	 *
	 * @param input
	 * @return
	 */
	public static Sentence createSentence(String[] input) {
		if (input.length < 2) {
			throw new IllegalArgumentException("Cannot create sentence from input.");
		}

		int id = Integer.parseInt(input[0].trim());
		String text = input[1].trim();

		Sentence sentence = new Sentence(id, text);
		if (input.length >= 3) {
			if (!input[2].isEmpty()) {
				sentence.setScore(Double.parseDouble(input[2]));
			}
		}

		return sentence;
	}

	/**
	 * Creates the corresponding {@link Statement} object.
	 *
	 * @return
	 */
	@JsonIgnore
	public Statement getStatementFromSentence() {
		String factURI = FACT_URI + factID;
		Resource subject = ResourceFactory.createResource(factURI);
		RDFNode object = ResourceFactory.createTypedLiteral(score);
		return ResourceFactory.createStatement(subject, TRUTH_VALUE, object);
	}

    public void setFactID(Integer factID) {
        this.factID = factID;
    }

	@JsonProperty("factId")
	public Integer getFactID() {
		return factID;
	}

	@JsonProperty("sentenceText")
	public String getSentenceText() {
		return sentenceText;
	}

	@JsonProperty("score")
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@JsonProperty("entities")
	public Set<Entity> getEntities() {
		return entities;
	}

	public void setEntities(Set<Entity> entities) {
		this.entities = entities;
	}

	@JsonProperty("relations")
	public Set<String> getRelations() {
		return relations;
	}

	public void setRelations(Set<String> relations) {
		this.relations = relations;
	}
}
