package com.soulspace.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soulspace.dao.AnalyticsDAO;
import com.soulspace.dao.EnrollmentDAO;
import com.soulspace.dao.LearningDAO; 
import com.soulspace.dao.LearningLessonDAO;
import com.soulspace.dao.LessonProgressDAO;
import com.soulspace.model.Enrollment;
import com.soulspace.model.Learning;
import com.soulspace.model.LearningLesson;
import com.soulspace.model.LessonProgress;

@Service
public class CourseProgressService {

    @Autowired private LessonProgressDAO lessonProgressDAO;
    @Autowired private AnalyticsDAO analyticsDAO;
    @Autowired private EnrollmentDAO enrollmentDAO;
    @Autowired private LearningLessonDAO learningLessonDAO;
    @Autowired private LearningDAO learningDAO; 

    @Transactional
    public void markLessonComplete(Long userId, Long lessonId) {
        LearningLesson lesson = learningLessonDAO.findById(lessonId);
        
        if (lesson == null) return;

        LessonProgress existing = lessonProgressDAO.findByUserAndLesson(userId, lessonId);
        
        if (existing == null) {
            LessonProgress newProgress = new LessonProgress(userId, lesson);
            lessonProgressDAO.save(newProgress);
            
            Learning course = learningDAO.findById(lesson.getCourseId());
            
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

        List<Long> lessonIds = allLessons.stream().map(LearningLesson::getId).collect(Collectors.toList());

        int completedCount = lessonProgressDAO.countCompletedLessonsInCourse(userId, lessonIds);
        
        int percent = (int) (((double) completedCount / allLessons.size()) * 100);

        Enrollment enrollment = enrollmentDAO.findByUserIdAndCourseId(userId, course.getId());
        
        if (enrollment != null) {
            enrollment.setProgressPercent(percent);
            enrollmentDAO.save(enrollment);
        }
    }
}