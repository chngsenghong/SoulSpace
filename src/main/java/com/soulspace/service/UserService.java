package com.soulspace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soulspace.dao.UserDAO;
import com.soulspace.model.User;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Transactional
    public void registerUser(User user) {
        userDAO.saveUser(user);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        // Ensure your UserDAO has findById (we added this in previous steps)
        return userDAO.findById(id);
    }
    
    @Transactional(readOnly = true)
    public void updateUserProfile(User user) {
        userDAO.saveUser(user);
    }

    @Transactional(readOnly = true)
    public List<User> getProfessionals() {
        return userDAO.findByRole("PROFESSIONAL");
    }
}