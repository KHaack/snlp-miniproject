package org.dice.alk.service;

import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.dice.alk.model.WordNetRequest;
import org.dice.alk.model.WordNetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RelationService {

    private static final Map<String, String> FILTER_ENTITIES = new HashMap();
    private static final Set<String> RELATIONS = new HashSet<>();

    static {
        FILTER_ENTITIES.put("Movie_star", "movie star");
        FILTER_ENTITIES.put("Squad", "squad");
        FILTER_ENTITIES.put("Team", "team");
        FILTER_ENTITIES.put("Subsidiary", "Subsidiary");
        FILTER_ENTITIES.put("Author", "author");
        FILTER_ENTITIES.put("Lanterne_rouge", "last place");
        FILTER_ENTITIES.put("Award", "award");
        FILTER_ENTITIES.put("Death_anniversary", "death place");
        FILTER_ENTITIES.put("Spouse", "spouse");
        FILTER_ENTITIES.put("Place_of_birth", "birth place");
        FILTER_ENTITIES.put("The_Better_Half", "better half");
        FILTER_ENTITIES.put("Honour", "honour");
        FILTER_ENTITIES.put("Celebrity", "star");
        FILTER_ENTITIES.put("Team_sport", "team");
        FILTER_ENTITIES.put("Innovation", "innovation");
    }

    static {
        RELATIONS.add("star");
        RELATIONS.add("squad");
        RELATIONS.add("team");
        RELATIONS.add("subsidiary");
        RELATIONS.add("author");
        RELATIONS.add("last place");
        RELATIONS.add("award");
        RELATIONS.add("death place");
        RELATIONS.add("spouse");
        RELATIONS.add("better half");
        RELATIONS.add("honour");
        RELATIONS.add("innovation");
        RELATIONS.add("foundation place");
        RELATIONS.add("generator");
        RELATIONS.add("birth place");
        RELATIONS.add("nascence place");
        RELATIONS.add("subordinate");
    }

    private Map<String, Set<String>> cache = new HashMap<>();

    @Autowired
    private WordNetService wordNetService;

    private String getRelation(Sentence sentence) {
        for (String relation : RELATIONS) {
            if (sentence.getSentenceText().contains(relation)) {
                return relation;
            }
        }
        return null;
    }

    public void extractRelation(Sentence sentence) {
        Set<Entity> filtered = new HashSet<>();
        for (Entity e : sentence.getEntities()) {
            if (null != e.getWikipediaTitle() && !FILTER_ENTITIES.containsKey(e.getWikipediaTitle())) {
                filtered.add(e);
            }
        }

        String relation = this.getRelation(sentence);
        if (null != relation) {
            sentence.getRelations().add(relation);

            if (!this.cache.containsKey(relation)) {
                // fill cache
                this.cache.put(relation, new HashSet<>());

                for (WordNetResult r : this.wordNetService.get(WordNetRequest.synonyms, relation)) {
                    this.cache.get(relation).add(r.getWord());
                }
                for (WordNetResult r : this.wordNetService.get(WordNetRequest.hypernyms, relation)) {
                    this.cache.get(relation).add(r.getWord());
                }
            }

            // use cache
            sentence.getRelations().addAll(this.cache.get(relation));
        }

        sentence.setEntities(filtered);
    }
}
