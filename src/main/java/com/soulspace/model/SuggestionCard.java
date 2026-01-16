package com.soulspace.model;

public class SuggestionCard {
    private String title;
    private String description;
    private String message;
    private String iconPath;

    public SuggestionCard() {}

    public SuggestionCard(String title, String description, String message, String iconPath) {
        this.title = title;
        this.description = description;
        this.message = message;
        this.iconPath = iconPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}