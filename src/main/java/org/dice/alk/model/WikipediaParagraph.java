package org.dice.alk.model;

import java.util.HashSet;
import java.util.Set;

public class WikipediaParagraph {
    private String text;
    private Set<String> urls = new HashSet<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
}
