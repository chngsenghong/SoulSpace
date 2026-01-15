package com.soulspace.dao;
import java.util.List;

import com.soulspace.model.User;

public interface UserDAO {
    void saveUser(User user);
    User getUserByEmail(String email);
    User loginUser(String email, String password);
    User findById(Long id);
    List<User> findByRole(String role);
}