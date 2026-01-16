package com.soulspace.dao;

import com.soulspace.model.Enrollment;
import java.util.List;

public interface EnrollmentDAO {
    void save(Enrollment enrollment);
    List<Enrollment> findByUserId(Long userId);
    Enrollment findByUserIdAndCourseId(Long userId, Long courseId);
}