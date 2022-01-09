package org.dice.alk.service;

import org.dice.alk.model.TagMeResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TagMeExtractorServiceTest {

    @Autowired
    private TagMeExtractorService tagMeExtractorService;

    @Test
    public void tagme1Test() {
        String text = "Obama visited U.K. in March.";
        TagMeResult result = this.tagMeExtractorService.extract(text);

        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("spot", equalTo("U.K.")),
                hasProperty("title", equalTo("United_Kingdom"))
        )));
        assertThat(result.getAnnotations(), hasItem(allOf(
                hasProperty("spot", equalTo("Obama")),
                hasProperty("title", equalTo("Barack_Obama"))
        )));
    }

    @Test
    public void tagme2Test() {
        String text = "IBM's subordinate is Rational Software.";
        TagMeResult result = this.tagMeExtractorService.extract(text);

        assertThat(result.getAnnotations(), hasItem(hasProperty("spot", containsString("IBM"))));
        assertThat(result.getAnnotations(), hasItem(hasProperty("title", equalTo("IBM"))));

        assertThat(result.getAnnotations(), hasItem(hasProperty("spot", containsString("Rational Software"))));
        assertThat(result.getAnnotations(), hasItem(hasProperty("title", equalTo("Rational_Software"))));
    }

    @Test
    public void tagme3Test() {
        String text = "Frank Herbert is An Unusual Angle's author.";
        TagMeResult result = this.tagMeExtractorService.extract(text);

        assertThat(result.getAnnotations(), hasItem(hasProperty("spot", containsString("Frank Herbert"))));
        assertThat(result.getAnnotations(), hasItem(hasProperty("title", equalTo("Frank_Herbert"))));
    }
}