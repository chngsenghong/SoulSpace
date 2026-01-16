package com.soulspace.dao;

import com.soulspace.model.Enrollment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class EnrollmentDAOImpl implements EnrollmentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            entityManager.persist(enrollment);
        } else {
            entityManager.merge(enrollment);
        }
    }

    @Override
    public List<Enrollment> findByUserId(Long userId) {
        String hql = "FROM Enrollment e WHERE e.userId = :uid";
        TypedQuery<Enrollment> query = entityManager.createQuery(hql, Enrollment.class);
        query.setParameter("uid", userId);
        return query.getResultList();
    }

    @Override
    public Enrollment findByUserIdAndCourseId(Long userId, Long courseId) {
        try {
            String hql = "FROM Enrollment e WHERE e.userId = :uid AND e.course.id = :cid";
            TypedQuery<Enrollment> query = entityManager.createQuery(hql, Enrollment.class);
            query.setParameter("uid", userId);
            query.setParameter("cid", courseId);
            return query.getSingleResult();
        } catch (Exception e) {
            return null; // Return null if not enrolled
        }
    }
}