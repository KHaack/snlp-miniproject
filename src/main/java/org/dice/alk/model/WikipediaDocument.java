package org.dice.alk.model;

import java.util.LinkedList;
import java.util.List;

public class WikipediaDocument {
    private Entity entity;
    private List<String> paragraphs = new LinkedList<>();

    public WikipediaDocument(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }
}
