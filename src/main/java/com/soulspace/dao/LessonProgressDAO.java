package com.soulspace.dao;

import com.soulspace.model.LessonProgress;
import java.util.List;

public interface LessonProgressDAO {
    void save(LessonProgress progress);
    LessonProgress findByUserAndLesson(Long userId, Long lessonId);
    boolean isLessonCompleted(Long userId, Long lessonId);
    int countCompletedLessonsInCourse(Long userId, List<Long> lessonIds);
    
    void deleteProgressByCourse(Long userId, Long courseId);
}