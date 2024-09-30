package com.portal.bid.service;

import com.portal.bid.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    User saveUser(User user) throws IllegalArgumentException;
    User createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    Optional<User> updateUser(Long id, User userDetails);
    User findUserByEmail(String email); // Ensure this method is declared
    User getUserByFirstAndLastName(String firstName, String lastName);
    Optional<User> assignParent(int childUserId, int parentUserId); // New method to assign parent
    List<User> getChildUsers(Long parentId);
    List<User> getAllUsersInHierarchy(Long rootUserId);
}
