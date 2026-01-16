package com.soulspace.dao;

import com.soulspace.model.Learning;
import java.util.List;

public interface LearningDAO {
    
    void save(Learning learning);
    
    Learning findById(Long id);
    
    List<Learning> findAll();
    
    void deleteById(Long id);

    // Specific search with filters for the Browse Page
    List<Learning> searchCourses(String keyword, String category, String level);
    
    List<Learning> findByTitleContainingIgnoreCase(String keyword);
}