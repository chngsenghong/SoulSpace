package com.soulspace.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AnalyticsDAOImpl implements AnalyticsDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, Object> getStudentAnalytics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // 1. Calculate Total Completed Courses (Progress = 100)
        String courseHql = "SELECT COUNT(e) FROM Enrollment e WHERE e.userId = :userId AND e.progressPercent = 100";
        Query courseQuery = entityManager.createQuery(courseHql);
        courseQuery.setParameter("userId", userId);
        Long coursesCompleted = (Long) courseQuery.getSingleResult();

        // 2. Calculate Total Lessons Completed (to estimate hours)
        String lessonHql = "SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.userId = :userId AND lp.isCompleted = true";
        Query lessonQuery = entityManager.createQuery(lessonHql);
        lessonQuery.setParameter("userId", userId);
        Long lessonsCompleted = (Long) lessonQuery.getSingleResult();

        // 3. Logic: Assume 30 mins (0.5 hours) per lesson
        double hoursSpent = lessonsCompleted * 0.5;

        // 4. Streak (Mock logic: 1 if active, 0 if not)
        int streak = (lessonsCompleted > 0) ? 1 : 0;

        // Pack into Map
        stats.put("hoursSpent", hoursSpent);
        stats.put("coursesCompleted", coursesCompleted.intValue());
        stats.put("dayStreak", streak);
        stats.put("certificatesEarned", coursesCompleted.intValue()); // Mocking certs = courses completed

        return stats;
    }
}