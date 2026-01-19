package com.soulspace.dao;

import java.util.List;

import com.soulspace.model.LearningLesson;

public interface LearningLessonDAO {
    List<LearningLesson> findByCourseId(Long courseId);
    void save(LearningLesson lesson);
    LearningLesson findById(Long id);
}