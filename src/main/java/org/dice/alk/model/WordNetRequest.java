package org.dice.alk.model;

public enum WordNetRequest {

    substance_meronyms("/substance_meronyms/1/"),
    hyponyms("/hyponyms/1/"),
    antonyms("/antonyms/1/"),
    synonyms("/synonyms/1/"),
    substance_holonyms("/substance_holonyms/1/"),
    hypernyms("/hypernyms/1/"),
    causes("/causes/1/");

    private final String path;

    WordNetRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
