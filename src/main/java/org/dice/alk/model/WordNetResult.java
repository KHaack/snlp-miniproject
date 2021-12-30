package org.dice.alk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordNetResult {
    @JsonProperty("POS")
    private String partOfSpeech;
    @JsonProperty("word")
    private String word;

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
