package org.dice.alk.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SentenceTest {

    @Test
    public void pruneEntities1Test() {
        Sentence sentence = new Sentence("");

        Entity entity = new Entity();
        entity.setText("Sevier County, Tennessee");
        entity.setWikipediaTitle("Sevier_County,_Tennessee");
        sentence.getEntities().add(entity);

        entity = new Entity();
        entity.setText("Tennessee");
        entity.setWikipediaTitle("Tennessee");
        sentence.getEntities().add(entity);

        assertEquals(2, sentence.getEntities().size());

        sentence.pruneEntities();
        assertEquals(1, sentence.getEntities().size());

        assertThat(sentence.getEntities(), hasItem(hasProperty("text", equalTo("Sevier County, Tennessee"))));
    }

    @Test
    public void pruneEntities2Test() {
        Sentence sentence = new Sentence("");

        Entity entity = new Entity();
        entity.setText("Coldwater, Michigan");
        entity.setWikipediaTitle("Coldwater,_Michigan");
        sentence.getEntities().add(entity);

        entity = new Entity();
        entity.setText("Michigan");
        entity.setWikipediaTitle("Michigan");
        sentence.getEntities().add(entity);

        assertEquals(2, sentence.getEntities().size());

        sentence.pruneEntities();
        assertEquals(1, sentence.getEntities().size());

        assertThat(sentence.getEntities(), hasItem(hasProperty("text", equalTo("Coldwater, Michigan"))));
    }
}