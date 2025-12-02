package com.soulspace.model;

public class Assessment {
    private String title;
    private String result; // e.g., "Moderate"
    private String colorClass; // e.g., "yellow", "green"
    private String date;

    public Assessment(String title, String result, String colorClass, String date) {
        this.title = title;
        this.result = result;
        this.colorClass = colorClass;
        this.date = date;
    }

    public String getTitle() { return title; }
    public String getResult() { return result; }
    public String getColorClass() { return colorClass; }
    public String getDate() { return date; }
}