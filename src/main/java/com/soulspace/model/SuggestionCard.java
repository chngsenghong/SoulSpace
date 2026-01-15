package com.soulspace.model;

public class SuggestionCard {
    private String title;
    private String description;
    private String message;
    private String iconPath;

    // 1. No-Argument Constructor (Good practice)
    public SuggestionCard() {}

    // 2. All-Arguments Constructor (Used in your Controller)
    public SuggestionCard(String title, String description, String message, String iconPath) {
        this.title = title;
        this.description = description;
        this.message = message;
        this.iconPath = iconPath;
    }

    // 3. GETTERS (Crucial! Thymeleaf needs these to read ${card.title})
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