package com.portal.bid.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.portal.bid.config.PermissionConfig;
import com.portal.bid.security.CustomUserDetails;
import com.portal.bid.service.UserRolePermissionService;
import com.portal.bid.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil util;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRolePermissionService userRolePermissionService;

    @Autowired
    private PermissionConfig permissionConfig;

    private Map<String, List<String>> permissionEndpointMap;

    @Autowired
    public SecurityFilter(PermissionConfig permissionConfig) {
        this.permissionEndpointMap = permissionConfig.permissionEndpointMap();
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//    }

    // Check if the user has permission for the HTTP method
    private boolean hasPermissionForMethod(Set<String> permissions, String httpMethod) {
        for (String permission : permissions) {
            List<String> allowedMethods = permissionEndpointMap.get(permission);
            if (allowedMethods != null && allowedMethods.contains(httpMethod)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        System.out.println("Entering Security Filter");

        // Extract the Authorization header
        String token = request.getHeader("Authorization");
        System.out.println("Authorization header: " + token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            System.out.println("Extracted token: " + token);

            String username = util.getSubject(token);
            System.out.println("Extracted username: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                System.out.println("Loaded user details: " + user);

                boolean isValid = util.isValidToken(token, user.getUsername());
                System.out.println("Is token valid: " + isValid);

                if (isValid) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set in SecurityContext");
                }
            }
        } else {
            System.out.println("Token is null or does not start with 'Bearer '");
        }

        // Check if the user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            System.out.println("User is authenticated: " + user);

            // Bypass permission check for a specific user
            if ("admin123@gmail.com".equals(user.getUsername())) {
                System.out.println("Bypassing permission check for user: " + user.getUsername());
                filterChain.doFilter(request, response);  // Proceed with the filter chain
                return;
            }

            // Fetch user permissions
            Set<String> userPermissions = userRolePermissionService.getPermissionsForUser(user.getId());
            System.out.println("User permissions: " + userPermissions);

            // Get the HTTP method of the request
            String httpMethod = request.getMethod();
            System.out.println("Request method: " + httpMethod);

            // Check permissions against the HTTP method
            if (!hasPermissionForMethod(userPermissions, httpMethod)) {
                System.out.println("User does not have permission for method: " + httpMethod);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("You don't have permission to access this resource.");
                return;
            }
        } else {
            System.out.println("User is not authenticated or principal is not of type UserDetails");
        }

        filterChain.doFilter(request, response);
        System.out.println("Filter chain processed");
    }
}

