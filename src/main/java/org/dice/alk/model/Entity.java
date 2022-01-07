package org.dice.alk.model;

import java.util.Objects;

public class Entity {
    private String text;
    private String wikipediaTitle;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWikipediaTitle() {
        return wikipediaTitle;
    }

    public void setWikipediaTitle(String wikipediaTitle) {
        this.wikipediaTitle = wikipediaTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        if (null == wikipediaTitle) return false;

        Entity entity = (Entity) o;
        if (null == entity.wikipediaTitle) return false;
        return wikipediaTitle.equals(entity.wikipediaTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wikipediaTitle);
    }
}
