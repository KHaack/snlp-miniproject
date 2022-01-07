package org.dice.alk.service;

import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Service
public class StanfordExtractorService {
    //
    // tipple: tokenize,ssplit,pos,lemma,depparse,natlog,openie
    // coreference: tokenize,ssplit,pos,lemma,ner,parse,dcoref,entitylink
    // relation: relation
    private static final String ANNOTATORS = "tokenize,ssplit,pos,lemma,ner,parse,dcoref,entitylink,relation";

    /**
     * Extract the sentences from the passed text.
     *
     * @param text The text to parse.
     * @return
     */
    public List<Sentence> extract(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", ANNOTATORS);

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        /*
         * sentences
         */
        List<Sentence> sentences = new LinkedList<>();
        for (CoreSentence s : document.sentences()) {
            Sentence sentence = new Sentence(s.text());
            sentences.add(sentence);

            // relation
            if (s.coreMap().containsKey(MachineReadingAnnotations.RelationMentionsAnnotation.class)) {
                for (RelationMention relation : s.coreMap().get(MachineReadingAnnotations.RelationMentionsAnnotation.class)) {
                    if (!relation.isNegativeRelation()) {
                        sentence.getRelations().add(relation.getType());
                    }
                }
            }

            // mentions
            for (CoreEntityMention mention : s.entityMentions()) {
                Entity entity = new Entity();
                entity.setText(mention.text());
                entity.setWikipediaTitle(mention.entity());
                sentence.getEntities().add(entity);
            }
        }

        return sentences;
    }
}
