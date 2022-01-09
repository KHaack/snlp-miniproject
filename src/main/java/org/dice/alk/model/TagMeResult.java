package org.dice.alk.model;

import java.util.HashSet;
import java.util.Set;

public class TagMeResult {
    private Integer time;
    private String api;
    private String lang;
    private String timestamp;
    private String test;
    private Set<TagMeSpot> annotations = new HashSet<>();

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Set<TagMeSpot> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<TagMeSpot> annotations) {
        this.annotations = annotations;
    }
}
