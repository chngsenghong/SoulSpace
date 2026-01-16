package com.soulspace.dao;

import com.soulspace.model.LearningLesson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class LearningLessonDAOImpl implements LearningLessonDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LearningLesson> findByCourseId(Long courseId) {
        String hql = "FROM LearningLesson l WHERE l.courseId = :cid ORDER BY l.id ASC";
        TypedQuery<LearningLesson> query = entityManager.createQuery(hql, LearningLesson.class);
        query.setParameter("cid", courseId);
        return query.getResultList();
    }

    @Override
    public void save(LearningLesson lesson) {
        if(lesson.getId() == null) {
            entityManager.persist(lesson);
        } else {
            entityManager.merge(lesson);
        }
    }

    // --- ADD THIS IMPLEMENTATION ---
    @Override
    public LearningLesson findById(Long id) {
        // entityManager.find returns the object directly, or null if not found
        return entityManager.find(LearningLesson.class, id);
    }
}