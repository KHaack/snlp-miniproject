package org.dice.alk.model;

import java.util.ArrayList;
import java.util.List;

public class TagMeResult {
    private Integer time;
    private String api;
    private String lang;
    private String timestamp;
    private String test;
    private List<TagMeSpot> annotations = new ArrayList<>();

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

    public List<TagMeSpot> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<TagMeSpot> annotations) {
        this.annotations = annotations;
    }
}
