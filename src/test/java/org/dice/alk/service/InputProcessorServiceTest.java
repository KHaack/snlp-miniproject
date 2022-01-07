package org.dice.alk.service;

import org.dice.alk.model.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InputProcessorServiceTest {

    private final static String WIKIPEDIA_EP = "https://en.wikipedia.org/wiki/";
    @Autowired
    private InputProcessorService inputProcessorService;

    @Test
    public void cleanSentenceTest() {
        String text = "Marvin Williams's team is Charlotte Hornets";
        String result = this.inputProcessorService.cleanSentence(text);
        String expected = "Marvin Williams team Charlotte Hornets";

        assertEquals(expected, result);
    }


    @Test
    public void annotatorTest() {
        // TODO just use parameterized runs
        String text = "Marvin Williams's team is Charlotte Hornets";
        Sentence sentence = new Sentence(0, text);

        this.inputProcessorService.getPredicate(sentence);
        String pred = sentence.getPredicate();

        assertThat(pred, is("team"));
    }

}
