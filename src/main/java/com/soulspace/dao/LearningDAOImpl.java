package com.soulspace.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.soulspace.model.Learning;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class LearningDAOImpl implements LearningDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Learning learning) {
        if (learning.getId() == null) {
            entityManager.persist(learning);
        } else {
            entityManager.merge(learning);
        }
    }

    @Override
    public Learning findById(Long id) {
        return entityManager.find(Learning.class, id);
    }

    @Override
    public List<Learning> findAll() {
        return entityManager.createQuery("FROM Learning", Learning.class).getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Learning learning = findById(id);
        if (learning != null) {
            entityManager.remove(learning);
        }
    }

    @Override
    public List<Learning> findByTitleContainingIgnoreCase(String keyword) {
        String hql = "FROM Learning l WHERE LOWER(l.title) LIKE LOWER(:keyword)";
        return entityManager.createQuery(hql, Learning.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    // --- NEW: Dynamic Filter Query ---
    @Override
    public List<Learning> searchCourses(String keyword, String category, String level) {
        StringBuilder hql = new StringBuilder("FROM Learning l WHERE 1=1");
        
        if (keyword != null && !keyword.isEmpty()) {
            hql.append(" AND (LOWER(l.title) LIKE LOWER(:keyword) OR LOWER(l.description) LIKE LOWER(:keyword))");
        }
        if (category != null && !category.equals("All Categories")) {
            hql.append(" AND l.category = :category");
        }
        if (level != null && !level.equals("All Levels")) {
            hql.append(" AND l.level = :level");
        }

        TypedQuery<Learning> query = entityManager.createQuery(hql.toString(), Learning.class);

        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if (category != null && !category.equals("All Categories")) {
            query.setParameter("category", category);
        }
        if (level != null && !level.equals("All Levels")) {
            query.setParameter("level", level);
        }

        return query.getResultList();
    }
}