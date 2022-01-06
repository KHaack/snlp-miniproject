package org.dice.alk.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagMeResultTest {

    @Test
    public void getPruneAnnotationsTest() {
        TagMeResult result = new TagMeResult();

        TagMeSpot spot = new TagMeSpot();
        spot.setStart(0);
        spot.setEnd(1);
        spot.setTitle("xxx");
        result.getAnnotations().add(spot);

        spot = new TagMeSpot();
        spot.setStart(1);
        spot.setEnd(2);
        spot.setTitle("yyy");
        result.getAnnotations().add(spot);

        spot = new TagMeSpot();
        spot.setStart(3);
        spot.setEnd(5);
        spot.setTitle("xxx");
        result.getAnnotations().add(spot);

        assertEquals(3, result.getAnnotations().size());
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("title", is("xxx")),
                hasProperty("start", is(0)))));
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("title", is("yyy")),
                hasProperty("start", is(1)))));
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("title", is("xxx")),
                hasProperty("start", is(3)))));

        List<TagMeSpot> spots = result.getPruneAnnotations();
        assertEquals(2, spots.size());
        assertThat(spots, hasItem(allOf(
                hasProperty("title", is("xxx")),
                hasProperty("start", is(0)))));
        assertThat(spots, hasItem(allOf(
                hasProperty("title", is("yyy")),
                hasProperty("start", is(1)))));
    }
}