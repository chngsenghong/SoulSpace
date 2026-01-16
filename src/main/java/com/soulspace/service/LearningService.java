package com.soulspace.service;

import com.soulspace.dao.LearningDAO;
import com.soulspace.model.Enrollment;
import com.soulspace.model.Learning;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LearningService {

    @Autowired
    private LearningDAO learningDAO;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Learning> getAllModules() {
        return learningDAO.findAll();
    }

    public Learning getModuleById(Long id) {
        return learningDAO.findById(id);
    }
    
    // Updated to support filters from the UI
    public List<Learning> searchCourses(String keyword, String category, String level) {
        return learningDAO.searchCourses(keyword, category, level);
    }

    public void addCourse(Learning course) {
        if (course.getStudentCount() == 0) course.setStudentCount(0);
        learningDAO.save(course);
    }

    public boolean isUserEnrolled(Long userId, Long courseId) {
        String hql = "SELECT COUNT(e) FROM Enrollment e WHERE e.userId = :uid AND e.course.id = :cid";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("uid", userId)
                .setParameter("cid", courseId)
                .getSingleResult();
        return count > 0;
    }

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

    // --- NEW METHOD: Unenroll User ---
    public void unenrollUser(Long userId, Long courseId) {
        String hql = "SELECT e FROM Enrollment e WHERE e.userId = :uid AND e.course.id = :cid";
        List<Enrollment> enrollments = entityManager.createQuery(hql, Enrollment.class)
                .setParameter("uid", userId)
                .setParameter("cid", courseId)
                .getResultList();

        if (!enrollments.isEmpty()) {
            Enrollment enrollment = enrollments.get(0);
            Learning course = enrollment.getCourse();
            
            // Remove the enrollment record
            entityManager.remove(enrollment);
            
            // Decrement student count safely
            if (course.getStudentCount() > 0) {
                course.setStudentCount(course.getStudentCount() - 1);
                learningDAO.save(course);
            }
        }
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
}