package org.dice.alk.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WikipediaDocument {
    private Entity entity;
    private List<String> paragraphs = new LinkedList<>();
    private Map<String, String> infobox = new HashMap<>();

    public WikipediaDocument(Entity entity) {
        this.entity = entity;
    }

    public Map<String, String> getInfobox() {
        return infobox;
    }

    public void setInfobox(Map<String, String> infobox) {
        this.infobox = infobox;
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

    public String getInfoBoxAsString() {
        StringBuilder builder = new StringBuilder();

        for (String key : this.infobox.keySet()) {
            builder.append(key);
            builder.append(" ");
            builder.append(this.infobox.get(key));
            builder.append(". ");
        }

        return builder.toString();
    }
}
