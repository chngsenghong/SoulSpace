package com.soulspace.dao;

import com.soulspace.model.LearningLesson;
import java.util.List;

public interface LearningLessonDAO {
    List<LearningLesson> findByCourseId(Long courseId);
    void save(LearningLesson lesson);
    
    // --- ADD THIS METHOD ---
    LearningLesson findById(Long id);
}