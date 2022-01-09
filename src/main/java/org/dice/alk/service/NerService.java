package org.dice.alk.service;

import org.dice.alk.exception.NerException;
import org.dice.alk.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    public Set<Entity> extract(String sentence) {
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
}
