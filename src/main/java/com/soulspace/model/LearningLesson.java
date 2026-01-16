package com.soulspace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "learning_lessons")
public class LearningLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Long courseId;

    private String title;

    @Column(columnDefinition = "TEXT") 
    private String content;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "is_free_preview")
    private Boolean isFreePreview;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Boolean getIsFreePreview() { return isFreePreview; }
    public void setIsFreePreview(Boolean freePreview) { isFreePreview = freePreview; }

    public String getType() {
        return (videoUrl != null && !videoUrl.isEmpty()) ? "Video" : "Reading";
    }

    public String getDuration() {
        if (durationMinutes == null) return "0 min";
        return durationMinutes + " min";
    }

    public boolean getIsFree() {
        return isFreePreview != null && isFreePreview;
    }
}