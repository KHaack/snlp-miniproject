package org.dice.alk.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TagMeResult {
    private String test;
    private List<TagMeSpot> annotations = new ArrayList<>();
    private int time;
    private String api;
    private String lang;
    private Date timestamp;

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
