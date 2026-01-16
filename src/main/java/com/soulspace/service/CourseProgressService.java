package com.soulspace.service;

import com.soulspace.dao.AnalyticsDAO;
import com.soulspace.dao.EnrollmentDAO;
import com.soulspace.dao.LearningDAO; // ADD THIS IMPORT
import com.soulspace.dao.LearningLessonDAO;
import com.soulspace.dao.LessonProgressDAO;
import com.soulspace.model.Enrollment;
import com.soulspace.model.Learning;
import com.soulspace.model.LearningLesson;
import com.soulspace.model.LessonProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseProgressService {

    @Autowired private LessonProgressDAO lessonProgressDAO;
    @Autowired private AnalyticsDAO analyticsDAO;
    @Autowired private EnrollmentDAO enrollmentDAO;
    @Autowired private LearningLessonDAO learningLessonDAO;
    @Autowired private LearningDAO learningDAO; // INJECT LEARNING DAO

    @Transactional
    public void markLessonComplete(Long userId, Long lessonId) {
        // FIX 1: Removed .orElse(null) because DAO returns object directly
        LearningLesson lesson = learningLessonDAO.findById(lessonId);
        
        if (lesson == null) return;

        // 1. Check if already exists using DAO
        LessonProgress existing = lessonProgressDAO.findByUserAndLesson(userId, lessonId);
        
        if (existing == null) {
            // 2. Save new progress using DAO
            LessonProgress newProgress = new LessonProgress(userId, lesson);
            lessonProgressDAO.save(newProgress);
            
            // FIX 2: Fetch Course manually because 'lesson' only has 'courseId'
            Learning course = learningDAO.findById(lesson.getCourseId());
            
            // 3. Update Course Percentage
            if (course != null) {
                updateCoursePercentage(userId, course);
            }
        }
    }

    public boolean isLessonCompleted(Long userId, Long lessonId) {
        return lessonProgressDAO.isLessonCompleted(userId, lessonId);
    }

    public Map<String, Object> getUserAnalytics(Long userId) {
        return analyticsDAO.getStudentAnalytics(userId);
    }

    private void updateCoursePercentage(Long userId, Learning course) {
        List<LearningLesson> allLessons = learningLessonDAO.findByCourseId(course.getId());
        if (allLessons.isEmpty()) return;

        // Extract IDs for DAO query
        List<Long> lessonIds = allLessons.stream().map(LearningLesson::getId).collect(Collectors.toList());

        // Get count from DAO
        int completedCount = lessonProgressDAO.countCompletedLessonsInCourse(userId, lessonIds);
        
        // Calculate Percentage
        int percent = (int) (((double) completedCount / allLessons.size()) * 100);

        // Update Enrollment using DAO
        Enrollment enrollment = enrollmentDAO.findByUserIdAndCourseId(userId, course.getId());
        
        if (enrollment != null) {
            enrollment.setProgressPercent(percent);
            enrollmentDAO.save(enrollment);
        }
    }
}