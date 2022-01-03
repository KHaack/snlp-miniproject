package org.dice.alk.service;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Service
public class StanfordExtractorService {
    private static final String ANNOTATORS = "tokenize,ssplit,pos,lemma,depparse,natlog,openie";

    /**
     * Extract the relation triples from the passed text.
     *
     * @param text
     * @return
     */
    public List<RelationTriple> extract(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", ANNOTATORS);

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);

        /**
         * sentences
         */
        List<RelationTriple> triples = new LinkedList<>();
        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            triples.addAll(sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class));
        }

        return triples;
    }
}
