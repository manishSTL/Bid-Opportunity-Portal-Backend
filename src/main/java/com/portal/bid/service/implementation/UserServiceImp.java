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
    public User saveUser(User user) throws IllegalArgumentException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // Handle parent assignment
        if (user.getParent() != null && user.getParent().getId() != 0) {
            Optional<User> parentUserOpt = userRepository.findById((long) user.getParent().getId());
            if (parentUserOpt.isPresent()) {
                user.setParent(parentUserOpt.get());
            } else {
                throw new IllegalArgumentException("Parent user not found.");
            }
        } else {
            user.setParent(null);  // Explicitly set parent to null if not specified or invalid
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
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
            System.out.println("-----------------");
            System.out.println(userDetails.getParent());
            // Update the parent if provided
            if (userDetails.getParent() != null) {
                user.setParent(userDetails.getParent());
            } else {
                user.setParent(null);  // Handle cases where parent is null
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

    @Override
    public List<User> getChildUsers(Long parentId) {
        Optional<User> parentUser = userRepository.findById(parentId);
        return parentUser.map(userRepository::findByParent).orElse(new ArrayList<>());
    }

    @Override
    public List<User> getAllUsersInHierarchy(Long rootUserId) {
        List<User> allUsers = new ArrayList<>();
        Optional<User> rootUser = userRepository.findById(rootUserId);

        if (rootUser.isPresent()) {
            allUsers.add(rootUser.get());
            addChildrenRecursively(rootUser.get(), allUsers);
        }

        return allUsers;
    }

    private void addChildrenRecursively(User parent, List<User> allUsers) {
        List<User> children = userRepository.findByParent(parent);
        for (User child : children) {
            allUsers.add(child);
            addChildrenRecursively(child, allUsers);
        }
    }
    @Override
    public Optional<User> assignParent(int childUserId, int parentUserId) {
        Optional<User> childUserOpt = userRepository.findById((long) childUserId);
        Optional<User> parentUserOpt = userRepository.findById((long) parentUserId);

        if (childUserOpt.isPresent() && parentUserOpt.isPresent()) {
            User childUser = childUserOpt.get();
            User parentUser = parentUserOpt.get();

            // Check for circular reference
            if (isCircularReference(childUser, parentUser)) {
                throw new IllegalArgumentException("Circular reference detected. Cannot assign parent.");
            }

            childUser.setParent(parentUser);
            userRepository.save(childUser);
            return Optional.of(childUser);
        }

        return Optional.empty();
    }

    private boolean isCircularReference(User child, User potentialParent) {
        User current = potentialParent;
        while (current != null) {
            if (current.getId() == child.getId()) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
