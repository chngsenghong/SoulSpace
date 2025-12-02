package com.soulspace.model;

public class SuggestionCard {
    private String title;
    private String description;
    private String actionText; // The text sent to the chat when clicked
    private String iconPath;   // The SVG path data (d attribute)

    public SuggestionCard(String title, String description, String actionText, String iconPath) {
        this.title = title;
        this.description = description;
        this.actionText = actionText;
        this.iconPath = iconPath;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getActionText() { return actionText; }
    public void setActionText(String actionText) { this.actionText = actionText; }

    public String getIconPath() { return iconPath; }
    public void setIconPath(String iconPath) { this.iconPath = iconPath; }
}