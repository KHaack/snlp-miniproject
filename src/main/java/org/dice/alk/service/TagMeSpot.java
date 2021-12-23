package org.dice.alk.service;

public class TagMeSpot {
    private String spot;
    private int start;
    private double link_probability;
    private double rho;
    private int end;
    private int id;
    private String title;

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public double getLink_probability() {
        return link_probability;
    }

    public void setLink_probability(double link_probability) {
        this.link_probability = link_probability;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
