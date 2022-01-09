package org.dice.alk.service;

import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RelationService {

    private static final Set<String> RELATION_ENTITIES = new HashSet<>();

    static {
        RELATION_ENTITIES.add("Movie_star");
        RELATION_ENTITIES.add("Squad");
        RELATION_ENTITIES.add("Team");
        RELATION_ENTITIES.add("Subsidiary");
        RELATION_ENTITIES.add("Author");
        RELATION_ENTITIES.add("Lanterne_rouge");
        RELATION_ENTITIES.add("Award");
        RELATION_ENTITIES.add("Death_anniversary");
        RELATION_ENTITIES.add("Spouse");
        RELATION_ENTITIES.add("Place_of_birth");
        RELATION_ENTITIES.add("The_Better_Half");
        RELATION_ENTITIES.add("Honour");
        RELATION_ENTITIES.add("Celebrity");
        RELATION_ENTITIES.add("Team_sport");
        RELATION_ENTITIES.add("Innovation");
    }

    public void extractRelation(Sentence sentence) {
        Set<Entity> filtered = new HashSet<>();
        for (Entity e : sentence.getEntities()) {
            if (null != e.getWikipediaTitle() && !RELATION_ENTITIES.contains(e.getWikipediaTitle())) {
                filtered.add(e);
            }
        }
        sentence.setEntities(filtered);
    }
}
