package com.soulspace.model;

public class Comment {
    private String authorName;
    private String authorInitials;
    private String authorColor;
    private String content;
    private String timeAgo;

    public Comment(String authorName, String authorInitials, String authorColor, String content, String timeAgo) {
        this.authorName = authorName;
        this.authorInitials = authorInitials;
        this.authorColor = authorColor;
        this.content = content;
        this.timeAgo = timeAgo;
    }

    public String getAuthorName() { return authorName; }
    public String getAuthorInitials() { return authorInitials; }
    public String getAuthorColor() { return authorColor; }
    public String getContent() { return content; }
    public String getTimeAgo() { return timeAgo; }
}