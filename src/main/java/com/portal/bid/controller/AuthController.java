package com.portal.bid.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

import com.portal.bid.entity.*;
import com.portal.bid.repository.RolesPermissionRepo;
import com.portal.bid.repository.UserRoleRepo;
import com.portal.bid.service.*;
import com.portal.bid.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
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
    @Autowired
    private TokenService tokenService;


    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        if (isInvalidUserInput(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: All required fields must be provided.");
        }

        try {
            User savedUser = userService.saveUser(user);
            String message = "User with id '" + savedUser.getId() + "' saved successfully!";
            if (savedUser.getParent() != null) {
                message += " Parent assigned with id '" + savedUser.getParent().getId() + "'.";
            } else {
                message += " No parent assigned.";
            }
            return new ResponseEntity<>(message, HttpStatus.CREATED);       } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof SQLException) {
                SQLException sqlException = (SQLException) rootCause;
                if ("23505".equals(sqlException.getSQLState())) {
                    String errorMessage = "Email already exists.";
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data integrity violation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during user creation: " + e.getMessage());
        }
    }

    // Helper method to validate user input
    private boolean isInvalidUserInput(User user) {
        return user.getFirstName() == null || user.getFirstName().isEmpty() ||
                user.getLastName() == null || user.getLastName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPasswordHash() == null || user.getPasswordHash().isEmpty() ||
                user.getDepartmentId() == 0; // Assuming 0 is not a valid departmentId
    }


//    @PostMapping("/loginUser")
//    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
////        System.out.println("Received login request: " + request);
//
//        // Extract username and password from the request
//        String username = request.getUsername();
//        String password = request.getPassword();
//        System.out.println("Username: " + username);
//        System.out.println("Password: " + password);
//
//        try {
//            // Authenticate the user
//            System.out.println("Attempting authentication...");
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//            System.out.println("Authentication successful for user: " + username);
//
//            // Check if the user is inactive
//            User storedUser = userService.findUserByEmail(username);
//            if (storedUser.getStatus().name().equals("INACTIVE")) {
//                System.out.println("User is inactive: " + username);
//                throw new DisabledException("User is inactive and cannot log in.");
//            }
//
//        } catch (UsernameNotFoundException e) {
//            System.out.println("User not found: " + username);
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, "User not found"));
//        } catch (BadCredentialsException e) {
//            System.out.println("Invalid credentials for user: " + username);
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, "Invalid credentials"));
//        } catch (DisabledException e) {
//            System.out.println("User is inactive: " + username);
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UserResponse(null, "User is inactive and cannot log in"));
//        } catch (Exception e) {
//            System.out.println("Authentication failed for user: " + username);
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponse(null, "Error during authentication"));
//        }
//
//        User storedUser = userService.findUserByEmail(username);
//        // Generate JWT token;
//        List<Integer> roles = userRolesService.getAllofid(storedUser.getId());
//        Set<Integer> allPermissionIds = new HashSet<>();
//
//        // Step 2: Loop through each role and get permission IDs
//        for (Integer roleId : roles) {
//            List<Integer> permissionIds = rolesPermissionService.getPermissions(roleId);
//            allPermissionIds.addAll(permissionIds);  // Add all permission IDs for the role
//        }
//        List<String> permissionNames = new ArrayList<>();
//
//        for (Integer permissionId : allPermissionIds) {
//            String permissionName = permissionsService.allPermission(permissionId);
//            permissionNames.add(permissionName);  // Add permission name to the list
//        }
//
//        String token = util.generateToken(username, permissionNames, storedUser.getId());
//        System.out.println("Generated JWT token: " + token);
//
//        // Return response with the token
//        UserResponse response = new UserResponse(token, "Token generated successfully!");
//        System.out.println("Response: " + response);
//
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/loginUser")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User storedUser = userService.findUserByEmail(username);
            if (storedUser.getStatus().name().equals("INACTIVE")) {
                throw new DisabledException("User is inactive and cannot log in.");
            }
            List<Integer> roles = userRolesService.getAllofid(storedUser.getId());
            Set<Integer> allPermissionIds = new HashSet<>();
            for (Integer roleId : roles) {
                allPermissionIds.addAll(rolesPermissionService.getPermissions(roleId));
            }
            List<String> permissionNames = new ArrayList<>();
            for (Integer permissionId : allPermissionIds) {
                permissionNames.add(permissionsService.allPermission(permissionId));
            }
            String accessToken = util.generateToken(username, permissionNames, storedUser.getId());
            String refreshToken = util.generateRefreshToken(username, storedUser.getId());
            UserResponse response = new UserResponse(accessToken, refreshToken, "Tokens generated successfully!");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, null, "User not found"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, null, "Invalid credentials"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UserResponse(null, null, "User is inactive and cannot log in"));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserResponse(null, null, "Token has expired"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserResponse(null, null, "Error during authentication"));
        }
    }


    @PostMapping("/getData")
    public ResponseEntity<String> testAfterLogin(Principal p){
        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" +p.getName());
    }


    @PostMapping("/renewToken")
    public ResponseEntity<UserResponse> renewToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        System.out.println("Received refresh token: " + refreshToken);

        if (refreshToken == null || refreshToken.isEmpty()) {
            System.out.println("Refresh token is missing in the request body.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserResponse(null, null, "Refresh token is required"));
        }

        try {
            // Check if the refresh token is blacklisted
            if (tokenService.isTokenBlacklisted(refreshToken)) {
                System.out.println("Refresh token is blacklisted.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new UserResponse(null, null, "Refresh token is blacklisted"));
            }

            // Extract claims from the refresh token
            Claims claims = util.getClaims(refreshToken);
            System.out.println("Extracted Claims: " + claims);

            // Check if the refresh token is expired
            if (util.isTokenExpired(refreshToken)) {
                System.out.println("Refresh token is expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new UserResponse(null, null, "Refresh token expired"));
            }

            // Extract user information from the claims
            String username = claims.getSubject();
            Integer userId = (Integer) claims.get("user_id");
            System.out.println("Extracted Username: " + username);
            System.out.println("Extracted User ID: " + userId);

            // Check if the user exists and is active
            User storedUser = userService.findUserByEmail(username);
            if (storedUser == null) {
                System.out.println("User not found: " + username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new UserResponse(null, null, "User not found"));
            }
            if ("INACTIVE".equals(storedUser.getStatus().name())) {
                System.out.println("User is inactive: " + username);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new UserResponse(null, null, "User is inactive and cannot log in"));
            }

            // Generate permissions and new access token
            List<Integer> roles = userRolesService.getAllofid(storedUser.getId());
            Set<Integer> allPermissionIds = new HashSet<>();
            for (Integer roleId : roles) {
                allPermissionIds.addAll(rolesPermissionService.getPermissions(roleId));
            }

            List<String> permissionNames = new ArrayList<>();
            for (Integer permissionId : allPermissionIds) {
                permissionNames.add(permissionsService.allPermission(permissionId));
            }

            String newAccessToken = util.generateToken(username, permissionNames, userId);
            System.out.println("New access token generated successfully.");

            return ResponseEntity.ok(new UserResponse(newAccessToken, refreshToken, "Access token renewed successfully"));

        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponse(null, null, "Refresh token expired"));
        } catch (Exception e) {
            System.out.println("Error in token renewal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponse(null, null, "Invalid refresh token"));
        }
    }



    // In package `com.portal.bid.controller`
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken,
                                         @RequestHeader("RefreshToken") String refreshToken) {
        String cleanedAccessToken = accessToken.replace("Bearer ", "");
        Date accessTokenExpiryDate = util.getExpirationDate(cleanedAccessToken);

        String cleanedRefreshToken = refreshToken.replace("Bearer ", "");
        Date refreshTokenExpiryDate = util.getExpirationDate(cleanedRefreshToken);

        // Blacklist both tokens
        tokenService.blacklistToken(cleanedAccessToken, accessTokenExpiryDate);
        tokenService.blacklistToken(cleanedRefreshToken, refreshTokenExpiryDate);

        return ResponseEntity.ok("User logged out successfully. Tokens invalidated.");
    }








}