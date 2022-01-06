package org.dice.alk.model;

import java.util.*;

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

    /**
     * Prunes duplicate pages from the API result.
     */
    public void pruneAnnotations() {
        List<TagMeSpot> annotations = getAnnotations();

        // no point in fixing if it's just 2
        if (annotations.size() == 2)
            return;

        // check if they have multiple mentions of the exact same page
        Map<String, TagMeSpot> map = new HashMap<>();
        annotations.forEach(spot -> map.putIfAbsent(spot.getTitle(), spot));
        annotations = new ArrayList<>(map.values());

        // decide on direction, consider the one with smallest start
        Collections.sort(annotations, Comparator.comparingInt(TagMeSpot::getStart));

        this.annotations = annotations;
    }
}
