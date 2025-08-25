package com.portal.bid.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.portal.bid.entity.User;
import com.portal.bid.entity.UserRequest;
import com.portal.bid.entity.UserResponse;
import com.portal.bid.service.PermissionsService;
import com.portal.bid.service.RolesPermissionService;
import com.portal.bid.service.TokenService;
import com.portal.bid.service.UserRolesService;
import com.portal.bid.service.UserService;
import com.portal.bid.util.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;

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
    public ResponseEntity<String> saveUser(@Valid @RequestBody User user) {
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

    @PostMapping("/sso/login")
    public ResponseEntity<UserResponse> ssoLogin(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal) {
        try {
            // Extract email (or unique identifier) from SSO principal
            String email = null;
            if (principal.getAttributes().get("email") != null) {
                // Attempt to extract email with type checking and casting
                Object emailAttribute = principal.getAttributes().get("email");
                
                if (emailAttribute instanceof List) {
                    // If it's a list, take the first element
                    List<Object> emailList = (List<Object>) emailAttribute;
                    if (!emailList.isEmpty()) {
                        email = String.valueOf(emailList.get(0));
                    }
                } else if (emailAttribute instanceof String) {
                    // If it's already a string
                    email = (String) emailAttribute;
                } else if (emailAttribute != null) {
                    // Convert to string for other possible types
                    email = emailAttribute.toString();
                }
            }else if(principal.getAttributes().get("emailAddress") != null) {
                // Attempt to extract email with type checking and casting
                Object emailAttribute = principal.getAttributes().get("emailAddress");
                
                if (emailAttribute instanceof List) {
                    // If it's a list, take the first element
                    List<Object> emailList = (List<Object>) emailAttribute;
                    if (!emailList.isEmpty()) {
                        email = String.valueOf(emailList.get(0));
                    }
                } else if (emailAttribute instanceof String) {
                    // If it's already a string
                    email = (String) emailAttribute;
                } else if (emailAttribute != null) {
                    // Convert to string for other possible types
                    email = emailAttribute.toString();
                }
            }
    
            // Option D: Fallback to getName() if no attribute found
            if (email == null) {
                email = principal.getName();
            }

            // Verify user exists in the database
            User storedUser = userService.findUserByEmail(email);
            if (storedUser == null || "INACTIVE".equals(storedUser.getStatus().name())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new UserResponse(null, null, "User not authorized to log in"));
            }

            // Fetch user's permissions
            List<Integer> roles = userRolesService.getAllofid(storedUser.getId());
            Set<Integer> allPermissionIds = new HashSet<>();
            for (Integer roleId : roles) {
                allPermissionIds.addAll(rolesPermissionService.getPermissions(roleId));
            }

            List<String> permissionNames = new ArrayList<>();
            for (Integer permissionId : allPermissionIds) {
                permissionNames.add(permissionsService.allPermission(permissionId));
            }

            // Generate tokens
            String accessToken = util.generateToken(email, permissionNames, storedUser.getId());
            String refreshToken = util.generateRefreshToken(email, storedUser.getId());

            // Create response
            UserResponse response = new UserResponse(accessToken, refreshToken, "SSO login successful");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle errors and log for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserResponse(null, null, "Error during SSO login: " + e.getMessage()));
        }
    }

     @GetMapping("/sso-error")
    public ResponseEntity<?> handleLoginError(@RequestParam(value = "error", required = false) boolean error) {
        if (error) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "SAML Authentication Failed"));
        }
        return ResponseEntity.badRequest().build();
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