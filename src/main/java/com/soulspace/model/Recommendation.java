package com.soulspace.model;

public class Recommendation {
    private String title;
    private String type;
    private String duration;
    private int progress; 
    private String iconType; 

    public Recommendation(String title, String type, String duration, int progress, String iconType) {
        this.title = title;
        this.type = type;
        this.duration = duration;
        this.progress = progress;
        this.iconType = iconType;
    }

    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getDuration() { return duration; }
    public int getProgress() { return progress; }
    public String getIconType() { return iconType; }
}