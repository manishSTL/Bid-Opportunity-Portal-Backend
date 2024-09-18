package com.portal.bid.service.implementation;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.portal.bid.entity.User;
import com.portal.bid.repository.UserRepository;
import com.portal.bid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                new ArrayList<>()
        );
    }

    @Override
    public Integer saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user).getId();
    }

    @Override
    public User createUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setEmployeeId(userDetails.getEmployeeId());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setMobile(userDetails.getMobile());
            user.setDepartmentId(userDetails.getDepartmentId());
            user.setStatus(userDetails.getStatus());
            if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
            }
            return userRepository.save(user);
        });
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void loginUser(String email, String plainPassword, User u) {
        User storedUser = userRepository.findByEmail(email);
        if (storedUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        if ("INACTIVE".equals(storedUser.getStatus().name())) {
            throw new DisabledException("User is inactive and cannot log in.");
        }

        if (!passwordEncoder.matches(plainPassword, storedUser.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }
        // Proceed with additional login steps if necessary
    }


    @Override
    public User getUserByFirstAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndOptionalLastName(firstName, lastName);
    }
}
