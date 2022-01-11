package org.dice.alk.service;

import org.dice.alk.model.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class FactCheckerServiceTest {

    @Autowired
    private FactCheckerService factCheck;

    @Test
    public void factChecker1Test() {
        String text = "Prince (musician)'s birth place is Minneapolis.";
        Sentence sentence = new Sentence(text);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(1.0, 0.5));
    }

    @Test
    public void factChecker2Test() {
        String text = "John Peel's birth place is Heswall.";
        Sentence sentence = new Sentence(text);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(1.0, 0.5));
    }

    @Test
    public void factChecker3Test() {
        String text = "Dr. Dre's birth place is The Hague.";
        Sentence sentence = new Sentence(text);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(0.0, 0.5));
    }

    @Test
    public void infoBoxOnlyTest() {
        String text = "Ai Sugiyama's birth place is Yokohama.";
        Sentence sentence = new Sentence(text);

        double score = this.factCheck.factCheck(sentence);

        assertThat(score, closeTo(1.0, 0.5));
    }
}
