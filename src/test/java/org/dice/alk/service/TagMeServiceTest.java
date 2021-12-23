package org.dice.alk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
    public void tagmeTest() throws Exception {
        String text = "obama visited uk";
        TagMeResult result = this.tagMeService.tag(text);

        assertEquals(2, result.getAnnotations().size());
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("spot", equalTo("uk")),
                hasProperty("title", equalTo("United Kingdom")),
                hasProperty("id", equalTo(31717)),
                hasProperty("start", equalTo(14)),
                hasProperty("end", equalTo(16))
        )));
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("spot", equalTo("obama")),
                hasProperty("title", equalTo("Barack Obama")),
                hasProperty("id", equalTo(534366)),
                hasProperty("start", equalTo(0)),
                hasProperty("end", equalTo(5))
        )));
    }
}