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
    private StandfortExtractorService stanfordExtractorService;

    @Autowired
    private FactCheckerService factCheck;

    @Test
    public void factChecker1() {
        String text = "Joe Smith is from Norfolk, Virginia.";
        List<Sentence> sentences = this.stanfordExtractorService.extract(text);

        assertEquals(1, sentences.size());
        double score = this.factCheck.factCheck(sentences.get(0), 10, 1500, sentences.get(0).getEntities().size() - 1);

        assertThat(score, closeTo(1.0, 0.5));
    }

    @Test
    public void factChecker2() {
        String text = "John Peel's birth place is Heswall.";
        List<Sentence> sentences = this.stanfordExtractorService.extract(text);

        assertEquals(1, sentences.size());
        double score = this.factCheck.factCheck(sentences.get(0), 10, 1500, sentences.get(0).getEntities().size() - 1);

        assertThat(score, closeTo(1.0, 0.5));
    }

    @Test
    public void factChecker3() {
        String text = "Dr. Dre's birth place is The Hague.";
        List<Sentence> sentences = this.stanfordExtractorService.extract(text);

        assertEquals(1, sentences.size());
        double score = this.factCheck.factCheck(sentences.get(0), 10, 1500, sentences.get(0).getEntities().size() - 1);

        assertThat(score, closeTo(0.0, 0.5));
    }

    @Test
    public void xxx() {
        String inputFile = "./data/SNLP2020_training_test.tsv";
        String outputFile = "./data/SNLP2020_training_result.tsv";
        this.factCheck.factCheck(inputFile, outputFile);
    }

}
