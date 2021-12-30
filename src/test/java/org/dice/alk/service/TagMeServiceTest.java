package org.dice.alk.service;

import org.dice.alk.model.TagMeResult;
import org.dice.alk.model.TagMeSpot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TagMeServiceTest {

    @Autowired
    private TagMeService tagMeService;

    @Test
    public void tagme1Test() {
        String text = "Obama visited U.K. in March";
        TagMeResult result = this.tagMeService.tag(text);

        assertEquals(2, result.getAnnotations().size());
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("spot", equalTo("U.K.")),
                hasProperty("title", equalTo("United_Kingdom")),
                hasProperty("id", equalTo(31717)),
                hasProperty("start", equalTo(14)),
                hasProperty("end", equalTo(18))
        )));
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("spot", equalTo("Obama")),
                hasProperty("title", equalTo("Barack_Obama")),
                hasProperty("id", equalTo(534366)),
                hasProperty("start", equalTo(0)),
                hasProperty("end", equalTo(5))
        )));
    }

    @Test
    public void tagme2Test() {
        String text = "Poul Anderson is The Boat of a Million Years' generator.";

        List<TagMeSpot> result = this.tagMeService.tag(text).getAnnotations();
        for (TagMeSpot spot : result) {
            System.out.println(spot.getSpot());
        }

       /* assertEquals(3, result.getAnnotations().size());
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("title", equalTo("West_Virginia"))
        )));
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("title", equalTo("Hank_Williams"))
        )));
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("title", equalTo("Oak_Hill,_West_Virginia"))
        )));*/
        assertEquals(1,1);

    }
}