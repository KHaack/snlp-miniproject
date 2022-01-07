package org.dice.alk.service;

import org.dice.alk.model.Sentence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class StanfordExtractorServiceTest {

    @Autowired
    private StanfordExtractorService service;

    @Test
    public void ner1Test() {
        String text = "Joe Smith is from Seattle.";
        List<Sentence> result = this.service.extract(text);

        assertThat(result.get(0).getEntities(), hasItem(hasProperty("text", equalTo("Joe Smith"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("Joe_Smith_(basketball)"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("text", equalTo("Seattle"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("Seattle"))));
    }

    @Test
    public void corefTest() {
        String text = "Joe Smith is from Seattle. He was named to the All-Rookie Team.";
        List<Sentence> result = this.service.extract(text);

        assertThat(result.get(0).getEntities(), hasItem(hasProperty("text", equalTo("Joe Smith"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("Joe_Smith_(basketball)"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("text", equalTo("Seattle"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("Seattle"))));

        assertThat(result.get(1).getEntities(), hasItem(hasProperty("text", equalTo("He"))));
        assertThat(result.get(1).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("Joe_Smith_(basketball)"))));
        assertThat(result.get(1).getEntities(), hasItem(hasProperty("text", equalTo("All-Rookie Team"))));
        assertThat(result.get(1).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("NBA_All-Rookie_Team"))));
    }

    @Test
    public void extractSentence1Test() {
        String text = "Jamie Yeo was born in 1990. His death place is Princeton, New Jersey.";
        List<Sentence> result = this.service.extract(text);

        assertThat(result, hasItem(hasProperty("sentenceText", equalTo("Jamie Yeo was born in 1990."))));
        assertThat(result, hasItem(hasProperty("sentenceText", equalTo("His death place is Princeton, New Jersey."))));
    }

    @Test
    public void extractSentence2Test() {
        String text = "The film stars John Cusack, Amanda Peet, Chiwetel Ejiofor, Oliver Platt, Thandiwe Newton, Danny Glover, and Woody Harrelson.";
        List<Sentence> result = this.service.extract(text);

        assertThat(result, hasItem(hasProperty("sentenceText", equalTo("The film stars John Cusack, Amanda Peet, Chiwetel Ejiofor, Oliver Platt, Thandiwe Newton, Danny Glover, and Woody Harrelson."))));
    }

    @Test
    public void extractSentence3Test() {
        String text = "Born and raised in Norfolk, Joe Smith was the College Player of the Year at Maryland";
        List<Sentence> result = this.service.extract(text);

        assertThat(result.get(0).getEntities(), hasItem(hasProperty("text", equalTo("Joe Smith"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("Joe_Smith_(basketball)"))));

        assertThat(result.get(0).getEntities(), hasItem(hasProperty("text", equalTo("Norfolk"))));
        assertThat(result.get(0).getEntities(), hasItem(hasProperty("wikipediaTitle", equalTo("Norfolk"))));
    }

}