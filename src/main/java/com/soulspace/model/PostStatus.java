package com.soulspace.model;

public enum PostStatus {
    PUBLISHED,       // Visible to everyone
    PENDING_REVIEW,  // Hidden, waiting for faculty check
    FLAGGED_RESOLVED // Reviewed and handled (either approved or kept hidden)
}