package org.dice.alk.model;

import java.util.LinkedList;
import java.util.List;

public class WikipediaDocument {
    private Entity entity;
    private List<WikipediaParagraph> paragraphs = new LinkedList<>();

    public WikipediaDocument(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<WikipediaParagraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<WikipediaParagraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
}
