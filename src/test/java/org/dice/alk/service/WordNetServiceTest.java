package org.dice.alk.service;

import org.dice.alk.model.WordNetRequest;
import org.dice.alk.model.WordNetResult;
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
public class WordNetServiceTest {

    @Autowired
    private WordNetService wordNetService;

    @Test
    public void synonymTest() {
        String word = "race";
        WordNetResult[] result = this.wordNetService.get(WordNetRequest.synonyms, word);

        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("noun")),
                hasProperty("word", equalTo("subspecies"))
        )));
        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("verb")),
                hasProperty("word", equalTo("rush"))
        )));
    }

    @Test
    public void antonymsTest() {
        String word = "married";
        WordNetResult[] result = this.wordNetService.get(WordNetRequest.antonyms, word);

        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("adjective")),
                hasProperty("word", equalTo("unmarried"))
        )));
    }

    @Test
    public void causesTest() {
        String word = "kill";
        WordNetResult[] result = this.wordNetService.get(WordNetRequest.causes, word);

        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("verb")),
                hasProperty("word", equalTo("die"))
        )));
    }

    @Test
    public void hypernymsTest() {
        String word = "oak";
        WordNetResult[] result = this.wordNetService.get(WordNetRequest.hypernyms, word);

        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("noun")),
                hasProperty("word", equalTo("tree"))
        )));
    }

    @Test
    public void hyponymsTest() {
        String word = "tree";
        WordNetResult[] result = this.wordNetService.get(WordNetRequest.hyponyms, word);

        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("noun")),
                hasProperty("word", equalTo("oak"))
        )));
    }

    @Test
    public void substanceHolonymsTest() {
        String word = "cellulose";
        WordNetResult[] result = this.wordNetService.get(WordNetRequest.substance_holonyms, word);

        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("noun")),
                hasProperty("word", equalTo("paper"))
        )));
    }

    @Test
    public void substanceMeronymsTest() {
        String word = "paper";
        WordNetResult[] result = this.wordNetService.get(WordNetRequest.substance_meronyms, word);

        assertThat(List.of(result), hasItem(allOf(
                hasProperty("partOfSpeech", equalTo("noun")),
                hasProperty("word", equalTo("cellulose"))
        )));
    }
}