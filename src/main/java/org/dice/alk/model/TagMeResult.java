package org.dice.alk.model;

import java.util.ArrayList;
import java.util.List;

public class TagMeResult {
    private TagMeMetrics metrics;
    private List<TagMeSpot> annotations = new ArrayList<>();

    public TagMeMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(TagMeMetrics metrics) {
        this.metrics = metrics;
    }

    public List<TagMeSpot> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<TagMeSpot> annotations) {
        this.annotations = annotations;
    }
}
