package com.portal.bid.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.portal.bid.entity.*;
import com.portal.bid.repository.RolesPermissionRepo;
import com.portal.bid.repository.UserRoleRepo;
import com.portal.bid.service.*;
import com.portal.bid.util.JWTUtil;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRolesService userRolesService;
    @Autowired
    private RolesPermissionService rolesPermissionService;
    @Autowired
    private PermissionsService permissionsService;

    @Autowired
    private JWTUtil util;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        if (user.getFirstName() == null || user.getFirstName().isEmpty() ||
                user.getLastName() == null || user.getLastName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPasswordHash() == null || user.getPasswordHash().isEmpty() ||
                user.getDepartmentId() == 0) { // Assuming 0 is not a valid departmentId

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: All required fields must be provided.");
        }
        System.out.print(user.getEmail()+" "+user.getPasswordHash());
        // Check if email already exists
        try {
            int id = userService.saveUser(user);
            System.out.print("pragya2222");

            String message = "User with id '" + id + "' saved successfully!";
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (DataIntegrityViolationException e) {
            // Print full stack trace for debugging
//            e.printStackTrace();

            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof SQLException) {
                SQLException sqlException = (SQLException) rootCause;
                if (sqlException.getSQLState().equals("23505")) { // SQLState for unique violation
                    String errorMessage = "Email already exists.";
//                    System.out.println(errorMessage + " - " + sqlException.getMessage());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
                }
            }
            // Handle other DataIntegrityViolationException cases
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during user creation.");
        }
    }

    @PostMapping("/loginUser")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
//        System.out.println("Received login request: " + request);

        // Extract username and password from the request
        String username = request.getUsername();
        String password = request.getPassword();
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        try {
            // Authenticate the user
            System.out.println("Attempting authentication...");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            System.out.println("Authentication successful for user: " + username);

            // Check if the user is inactive
            User storedUser = userService.findUserByEmail(username);
            if (storedUser.getStatus().name().equals("INACTIVE")) {
                System.out.println("User is inactive: " + username);
                throw new DisabledException("User is inactive and cannot log in.");
            }

        } catch (UsernameNotFoundException e) {
            System.out.println("User not found: " + username);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, "User not found"));
        } catch (BadCredentialsException e) {
            System.out.println("Invalid credentials for user: " + username);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, "Invalid credentials"));
        } catch (DisabledException e) {
            System.out.println("User is inactive: " + username);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UserResponse(null, "User is inactive and cannot log in"));
        } catch (Exception e) {
            System.out.println("Authentication failed for user: " + username);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponse(null, "Error during authentication"));
        }

        User storedUser = userService.findUserByEmail(username);
        // Generate JWT token;
        List<Integer> roles = userRolesService.getAllofid(storedUser.getId());
        Set<Integer> allPermissionIds = new HashSet<>();

        // Step 2: Loop through each role and get permission IDs
        for (Integer roleId : roles) {
            List<Integer> permissionIds = rolesPermissionService.getPermissions(roleId);
            allPermissionIds.addAll(permissionIds);  // Add all permission IDs for the role
        }
        List<String> permissionNames = new ArrayList<>();

        for (Integer permissionId : allPermissionIds) {
            String permissionName = permissionsService.allPermission(permissionId);
            permissionNames.add(permissionName);  // Add permission name to the list
        }

        String token = util.generateToken(username, permissionNames);
        System.out.println("Generated JWT token: " + token);

        // Return response with the token
        UserResponse response = new UserResponse(token, "Token generated successfully!");
        System.out.println("Response: " + response);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/getData")
    public ResponseEntity<String> testAfterLogin(Principal p){
        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" +p.getName());
    }


}