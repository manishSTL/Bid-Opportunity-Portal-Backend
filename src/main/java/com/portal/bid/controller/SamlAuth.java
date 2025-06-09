package com.portal.bid.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bid.entity.User;
import com.portal.bid.service.PermissionsService;
import com.portal.bid.service.RolesPermissionService;
import com.portal.bid.service.SAMLResponseParser;
import com.portal.bid.service.TokenService;
import com.portal.bid.service.UserRolesService;
import com.portal.bid.service.UserService;
import com.portal.bid.util.JWTUtil;

@RestController
@RequestMapping("/saml")
public class SamlAuth {

    private static final Logger logger = LoggerFactory.getLogger(SamlAuth.class);
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

    /**
     * Custom ACS Endpoint to handle SAML Responses
     * URL: /saml2/acs
     */
    @PostMapping("/acs")
    public ResponseEntity<Void> handleSAMLResponse(@RequestParam("SAMLResponse") String samlResponse) {
        logger.info("Received SAML Response at ACS endpoint");
        String FRONTEND_URL = "https://stlsalesnxt.sterliteapps.com:61445";

        try {
            // Step 1: Decode the Base64 encoded SAML response
            String decodedResponse = new String(Base64.getDecoder().decode(samlResponse), StandardCharsets.UTF_8);
            logger.debug("Decoded SAML Response: {}", decodedResponse);

            // Step 2: Validate the SAML Response
            if (!validateSAMLResponse(decodedResponse)) {
                logger.error("SAML Response validation failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .location(URI.create(FRONTEND_URL + "/?error=invalid_saml_response"))
                        .build();
            }

            // Step 3: Extract email from the SAML assertion
            String email = extractEmailFromAssertion(decodedResponse);
            if (email == null || email.isEmpty()) {
                logger.error("Failed to extract email from SAML assertion");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .location(URI.create(FRONTEND_URL + "/?error=email_not_found"))
                        .build();
            }
            logger.info("User authenticated successfully by IDP: {}", email);

            // Step 4: Fetch user from the database and validate
            User storedUser = userService.findUserByEmail(email);
            if (storedUser == null) {
                logger.warn("User does not exist in the application database: {}", email);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .location(URI.create(FRONTEND_URL + "/?error=user_not_found"))
                        .build();
            }

            if ("INACTIVE".equalsIgnoreCase(storedUser.getStatus().name())) {
                logger.warn("User account is inactive: {}", email);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .location(URI.create(FRONTEND_URL + "/?error=account_inactive"))
                        .build();
            }

            // Step 5: Fetch user's permissions
            List<Integer> roles = userRolesService.getAllofid(storedUser.getId());
            if (roles.isEmpty()) {
                logger.warn("User has no assigned roles: {}", email);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .location(URI.create(FRONTEND_URL + "/?error=no_roles_assigned"))
                        .build();
            }

            Set<Integer> allPermissionIds = new HashSet<>();
            for (Integer roleId : roles) {
                allPermissionIds.addAll(rolesPermissionService.getPermissions(roleId));
            }

            if (allPermissionIds.isEmpty()) {
                logger.warn("User has no permissions: {}", email);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .location(URI.create(FRONTEND_URL + "/?error=no_permissions"))
                        .build();
            }

            List<String> permissionNames = new ArrayList<>();
            for (Integer permissionId : allPermissionIds) {
                permissionNames.add(permissionsService.allPermission(permissionId));
            }

            // Step 6: Generate tokens
            String accessToken = util.generateToken(email, permissionNames, storedUser.getId());
            String refreshToken = util.generateRefreshToken(email, storedUser.getId());

            // Step 7: Redirect with tokens
            URI redirectUri = URI.create(FRONTEND_URL + "/?token=" + accessToken + "&refreshToken=" + refreshToken);
            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();

        } catch (IllegalArgumentException e) {
            logger.error("Invalid Base64 encoding of SAML Response", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .location(URI.create(FRONTEND_URL + "/?error=invalid_encoding"))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error while processing SAML Response", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .location(URI.create(FRONTEND_URL + "/?error=internal_server_error"))
                    .build();
        }
    }

    /**
     * Validates the SAML Response for signature, audience, and expiry
     */
    private boolean validateSAMLResponse(String samlResponse) {
        // Add signature validation, audience matching, and expiry checks here
        // logger.debug("Validating SAML Response...");
        return true; // Placeholder: Replace with actual validation logic
    }

    /**
     * Extracts the email from the SAML assertion
     */
    private String extractEmailFromAssertion(String samlResponse) {
        // Parse the SAML Response XML to extract user information (e.g., email)
        // logger.debug("Extracting email from SAML assertion...");
        // Example logic to extract email from <saml2:NameID>
        // This is a placeholder; you will need to use XML parsing or a library like DOM
        // or JAXB.
        String email = SAMLResponseParser.extractEmailFromAssertion(samlResponse);
        return email;
    }
}
