package org.dice.alk.model;

import java.util.List;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;

/**
 * Holds the sentence identification elements
 */
public class Sentence {

	private final String FACT_URI = "http://swc2017.aksw.org/task2/dataset/";
	private final Property TRUTH_VALUE = ResourceFactory.createProperty("http://swc2017.aksw.org/hasTruthValue");
	/**
	 * Fact ID
	 */
	private int factID;
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
	private List<TagMeSpot> entities;

	/**
	 * The sentence's relation
	 */
	private String predicate;

	/**
	 * Constructor
	 *
	 * @param factID       The fact ID.
	 * @param sentenceText The sentence text.
	 */
	public Sentence(int factID, String sentenceText) {
		this.factID = factID;
		this.sentenceText = sentenceText;
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
		return new Sentence(id, text);
	}

	/**
	 * Creates the corresponding {@link Statement} object.
	 *
	 * @return
	 */
	public Statement getStatementFromSentence() {
		String factURI = FACT_URI + factID;
		Resource subject = ResourceFactory.createResource(factURI);
		RDFNode object = ResourceFactory.createTypedLiteral(score);
		return ResourceFactory.createStatement(subject, TRUTH_VALUE, object);
	}

	// getters and setters

	public int getFactID() {
		return factID;
	}

	public String getSentenceText() {
		return sentenceText;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<TagMeSpot> getEntities() {
		return entities;
	}

	public void setEntities(List<TagMeSpot> entities) {
		this.entities = entities;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

}
