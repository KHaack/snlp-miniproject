package org.dice.alk.service;

import org.dice.alk.model.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InputProcessorServiceTest {

    private final static String WIKIPEDIA_EP = "https://en.wikipedia.org/wiki/";
    @Autowired
    private InputProcessorService service;


    @Test
    public void testAnnotator() {
        // TODO just use parameterized runs
        String text = "Marvin Williams's team is Charlotte Hornets";
        Sentence sentence = new Sentence(0, text);

        this.service.processTextInput(sentence);
        String pred = sentence.getPredicate();

        assertThat(pred, is("team"));
    }

}
