package org.dice.alk.model;

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
	 * The sentence's relation
	 */
	private String predicate;

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

    /**
     * Indicates whether the passed entity is inside the sentence or not.
     *
     * @param entity The entity to check.
     * @return
     */
    public boolean entityExists(Entity entity) {
        return this.entities.contains(entity);
    }

    public void setFactID(Integer factID) {
        this.factID = factID;
    }

	public Integer getFactID() {
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

	public Set<Entity> getEntities() {
		return entities;
	}

	public void setEntities(Set<Entity> entities) {
		this.entities = entities;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public Set<String> getRelations() {
		return relations;
	}

	public void setRelations(Set<String> relations) {
		this.relations = relations;
	}

	/**
	 * Prune the entities of this sentence.
	 */
	public void pruneEntities() {
		/*
		 * Removes entities contained in others.
		 * The sentence 'Foo's birth place is Denison, Texas'  should result in 'Denison, Texas'
		 * and should not contain 'Texas'.
		 */
		Set<Entity> pruneEntities = new HashSet<>();

		for (Entity e1 : this.getEntities()) {
			boolean contains = false;

			for (Entity e2 : this.getEntities()) {
				if (e1 != e2 && e2.getWikipediaTitle().contains(e1.getText())) {
					contains = true;
				}
			}

			if (!contains) {
				pruneEntities.add(e1);
			}
		}

		this.setEntities(pruneEntities);
	}
}
