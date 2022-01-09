package org.dice.alk.service;

import org.dice.alk.exception.NerException;
import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.dice.alk.model.TagMeResult;
import org.dice.alk.model.TagMeSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

@Service
public class NerService {
    @Autowired
    private TagMeExtractorService tagMe;

    @Autowired
    private StandfortExtractorService standfort;

    /**
     * Mix the two sets together.
     */
    protected static Set<Entity> mixEntities(Set<Entity>... sets) {
        /*
         * Removes entities contained in others.
         * The sentence 'Foo's birth place is Denison, Texas'  should result in 'Denison, Texas'
         * and should not contain 'Texas'.
         */
        Set<Entity> alreadyAdded = new HashSet<>();

        for (Set<Entity> set : sets) {
            for (Entity toAdd : set) {
                boolean contains = false;

                // add only new
                for (Entity added : alreadyAdded) {
                    if (toAdd != added && added.getWikipediaTitle().contains(toAdd.getText())) {
                        contains = true;
                    }
                }

                if (!contains) {
                    alreadyAdded.add(toAdd);
                }
            }
        }

        return alreadyAdded;
    }

    /**
     * Extract the entities from the passed sentence.
     *
     * @param sentence
     * @return
     */
    public Set<Entity> extractSentence(String sentence) {
        try {
            Future<Set<Entity>> resultTagMe = this.tagMe.extractAsync(sentence);
            Future<Set<Entity>> resultStandfort = this.standfort.extractAsync(sentence);

            while (true) {
                if (resultTagMe.isDone() && resultStandfort.isDone()) {
                    break;
                }

                Thread.sleep(50);
            }

            return mixEntities(resultTagMe.get(), resultStandfort.get());
        } catch (Exception ex) {
            throw new NerException("Unable to extract entities.", ex);
        }
    }

    public List<Sentence> extractParagraph(String paragraph) {
        try {
            List<Sentence> senteces = this.standfort.extract(paragraph);

            for (Sentence sentence : senteces) {
                TagMeResult tagMeResult = this.tagMe.extract(sentence.getSentenceText());

                Set<Entity> entities = new HashSet<>();
                for (TagMeSpot spot : tagMeResult.getAnnotations()) {
                    Entity entity = new Entity();
                    entity.setWikipediaTitle(spot.getTitle());
                    entity.setText(spot.getSpot());
                    entities.add(entity);
                }

                sentence.setEntities(mixEntities(sentence.getEntities(), entities));

            }

            return senteces;
        } catch (Exception ex) {
            throw new NerException("Unable to extract entities.", ex);
        }
    }
}
