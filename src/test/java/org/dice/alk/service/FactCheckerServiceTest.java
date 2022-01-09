package org.dice.alk.service;

import org.dice.alk.model.Entity;
import org.dice.alk.model.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class FactCheckerServiceTest {

    @Autowired
    private NerService nerService;

    @Autowired
    private FactCheckerService factCheck;

    @Autowired
    private RelationService relationService;

    @Test
    public void factChecker1Test() {
        String text = "Joe Smith is from Norfolk, Virginia.";
        Set<Entity> entities = this.nerService.extractSentence(text);
        Sentence sentence = new Sentence(text);
        sentence.setEntities(entities);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(1.0, 0.5));
    }

    @Test
    public void factChecker2Test() {
        String text = "John Peel's birth place is Heswall.";
        Set<Entity> entities = this.nerService.extractSentence(text);
        Sentence sentence = new Sentence(text);
        sentence.setEntities(entities);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(1.0, 0.5));
    }

    @Test
    public void factChecker3Test() {
        String text = "Dr. Dre's birth place is The Hague.";
        Set<Entity> entities = this.nerService.extractSentence(text);
        Sentence sentence = new Sentence(text);
        sentence.setEntities(entities);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(0.0, 0.5));
    }

    @Test
    public void infoBoxOnlyTest() {
        String text = "Ai Sugiyama's birth place is Yokohama.";
        Sentence sentence = new Sentence(text);
        sentence.setEntities(this.nerService.extractSentence(sentence.getSentenceText()));
        this.relationService.extractRelation(sentence);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(1.0, 0.5));
    }

    @Test
    public void xxx() throws IOException {
        String inputFile = "./data/SNLP2020_training_test.tsv";
        String outputFile = "./data/SNLP2020_training_result.tsv";
        this.factCheck.factCheck(new FileReader(inputFile), new FileWriter(outputFile));
    }

}
