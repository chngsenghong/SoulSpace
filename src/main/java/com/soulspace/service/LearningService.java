package com.soulspace.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soulspace.dao.LearningDAO;
import com.soulspace.model.Enrollment;
import com.soulspace.model.Learning;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LearningService {

    @Autowired
    private LearningDAO learningDAO;

    @PersistenceContext
    private EntityManager entityManager;

    // --- READ OPERATIONS ---

    public Learning getModuleById(Long id) {
        return learningDAO.findById(id);
    }
    
    public List<Learning> searchCourses(String keyword, String category, String level) {
        return learningDAO.searchCourses(keyword, category, level);
    }

    // FIX: Only ONE getAllModules method (Sorted by Date)
    public List<Learning> getAllModules() {
        return entityManager.createQuery("FROM Learning l ORDER BY l.dateCreated DESC", Learning.class).getResultList();
    }

    public List<Learning> getEnrolledCourses(Long userId) {
        List<Enrollment> enrollments = entityManager.createQuery("FROM Enrollment e WHERE e.userId = :uid", Enrollment.class)
                .setParameter("uid", userId)
                .getResultList();
        
        List<Learning> courses = new ArrayList<>();
        for (Enrollment e : enrollments) {
            courses.add(e.getCourse());
        }
        return courses;
    }

    public boolean isUserEnrolled(Long userId, Long courseId) {
        String hql = "SELECT COUNT(e) FROM Enrollment e WHERE e.userId = :uid AND e.course.id = :cid";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("uid", userId)
                .setParameter("cid", courseId)
                .getSingleResult();
        return count > 0;
    }

    // --- WRITE OPERATIONS (CREATE / UPDATE / DELETE) ---

    public void saveModule(Learning learning) {
        if (learning.getId() == null) {
            if (learning.getDateCreated() == null) {
                learning.setDateCreated(LocalDate.now());
            }
            // Ensure non-null defaults
            if (learning.getStudentCount() == 0) learning.setStudentCount(0);
            
            entityManager.persist(learning);
        } else {
            entityManager.merge(learning);
        }
    }

    public void deleteModule(Long id) {
        Learning learning = entityManager.find(Learning.class, id);
        if (learning != null) {
            // First remove enrollments to avoid foreign key issues
            entityManager.createQuery("DELETE FROM Enrollment e WHERE e.course.id = :cid")
                         .setParameter("cid", id)
                         .executeUpdate();
            
            entityManager.remove(learning);
        }
    }

    // Helper for backward compatibility
    public void addCourse(Learning course) {
        saveModule(course);
    }

    // --- ENROLLMENT ACTIONS ---

    public void enrollUser(Long userId, Long courseId) {
        if (!isUserEnrolled(userId, courseId)) {
            Learning course = learningDAO.findById(courseId);
            if (course != null) {
                Enrollment enrollment = new Enrollment();
                enrollment.setUserId(userId);
                enrollment.setCourse(course);
                entityManager.persist(enrollment);
                
                course.setStudentCount(course.getStudentCount() + 1);
                learningDAO.save(course);
            }
        }
    }

    public void unenrollUser(Long userId, Long courseId) {
        String hql = "SELECT e FROM Enrollment e WHERE e.userId = :uid AND e.course.id = :cid";
        List<Enrollment> enrollments = entityManager.createQuery(hql, Enrollment.class)
                .setParameter("uid", userId)
                .setParameter("cid", courseId)
                .getResultList();

        if (!enrollments.isEmpty()) {
            Enrollment enrollment = enrollments.get(0);
            Learning course = enrollment.getCourse();
            
            entityManager.remove(enrollment);
        
            if (course.getStudentCount() > 0) {
                course.setStudentCount(course.getStudentCount() - 1);
                learningDAO.save(course);
            }
        }
    }
}