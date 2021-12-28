package org.dice.alk.model;

public class TagMeMetrics {
    private long time_tokenize;
    private long time_spot;
    private long time_disambiguation;
    private long time_evaluation;

    public long getTime_tokenize() {
        return time_tokenize;
    }

    public void setTime_tokenize(long time_tokenize) {
        this.time_tokenize = time_tokenize;
    }

    public long getTime_spot() {
        return time_spot;
    }

    public void setTime_spot(long time_spot) {
        this.time_spot = time_spot;
    }

    public long getTime_disambiguation() {
        return time_disambiguation;
    }

    public void setTime_disambiguation(long time_disambiguation) {
        this.time_disambiguation = time_disambiguation;
    }

    public long getTime_evaluation() {
        return time_evaluation;
    }

    public void setTime_evaluation(long time_evaluation) {
        this.time_evaluation = time_evaluation;
    }
}
