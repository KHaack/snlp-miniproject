package org.dice.alk.service;

import org.dice.alk.model.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class FactCheckerServiceTest {

    @Autowired
    private StanfordExtractorService stanfordExtractorService;

    @Autowired
    private FactCheckerService factCheck;

    @Test
    public void testFactChecker() {
        String text = "Joe Smith is from Norfolk, Virginia.";
        List<Sentence> sentences = this.stanfordExtractorService.extract(text);

        assertEquals(1, sentences.size());
        double score = this.factCheck.factCheck(sentences.get(0));

        assertThat(score, closeTo(1.0, 0.5));
    }

}
