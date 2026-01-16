package com.soulspace.dao;

import com.soulspace.model.LessonProgress;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query; // Import added for Query
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class LessonProgressDAOImpl implements LessonProgressDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(LessonProgress progress) {
        if (progress.getId() == null) {
            entityManager.persist(progress);
        } else {
            entityManager.merge(progress);
        }
    }

    @Override
    public LessonProgress findByUserAndLesson(Long userId, Long lessonId) {
        try {
            String hql = "FROM LessonProgress lp WHERE lp.userId = :userId AND lp.lesson.id = :lessonId";
            TypedQuery<LessonProgress> query = entityManager.createQuery(hql, LessonProgress.class);
            query.setParameter("userId", userId);
            query.setParameter("lessonId", lessonId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean isLessonCompleted(Long userId, Long lessonId) {
        LessonProgress lp = findByUserAndLesson(userId, lessonId);
        return lp != null && lp.isCompleted();
    }

    @Override
    public int countCompletedLessonsInCourse(Long userId, List<Long> lessonIds) {
        if (lessonIds == null || lessonIds.isEmpty()) return 0;
        
        String hql = "SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.userId = :userId " +
                     "AND lp.lesson.id IN :lessonIds AND lp.isCompleted = true";
        TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
        query.setParameter("userId", userId);
        query.setParameter("lessonIds", lessonIds);
        return query.getSingleResult().intValue();
    }

    @Override
    @Transactional
    public void deleteProgressByCourse(Long userId, Long courseId) {
        // Delete all progress records for this user where the lesson belongs to the specific course
        String hql = "DELETE FROM LessonProgress lp WHERE lp.userId = :userId " +
                     "AND lp.lesson.id IN (SELECT l.id FROM LearningLesson l WHERE l.courseId = :courseId)";
        
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("courseId", courseId);
        query.executeUpdate();
    }
}