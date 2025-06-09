package com.portal.bid.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

import com.portal.bid.config.EndpointPermission;
import com.portal.bid.config.PermissionConfig;
import com.portal.bid.security.CustomUserDetails;
import com.portal.bid.service.TokenService;
import com.portal.bid.service.UserRolePermissionService;
import com.portal.bid.util.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;

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

    @Autowired
    private Map<String, List<EndpointPermission>> permissionEndpointMap;

//    @Autowired
//    private Map<String, ModulePermissions> modulePermissionsMap;

    @Autowired
    TokenService tokenService;

//    @Autowired
//    public SecurityFilter(PermissionConfig permissionConfig) {
//        this.permissionEndpointMap = permissionConfig.permissionEndpointMap();
//    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//    }
private boolean hasPermissionForEndpoint(List<String> userPermissions, String requestPath, String httpMethod) {
    for (String permission : userPermissions) {
        List<EndpointPermission> endpointPermissions = permissionEndpointMap.get(permission);
        if (endpointPermissions != null) {
            for (EndpointPermission endpoint : endpointPermissions) {
                if (endpoint.matches(requestPath, httpMethod)) {
                    return true;
                }
            }
        }
    }
    return false;
}

    // Check if the user has permission for the HTTP method
//    private boolean hasPermissionForMethod(Set<String> permissions, String httpMethod) {
//        for (String permission : permissions) {
//            List<String> allowedMethods = permissionEndpointMap.get(permission);
//            if (allowedMethods != null && allowedMethods.contains(httpMethod)) {
//                return true;
//            }
//        }
//        return false;
//    }
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        System.out.println("Entering Security Filter");

        // Extract the Authorization header
        String token = request.getHeader("Authorization");
        System.out.println("Authorization header: " + token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            System.out.println("Extracted token: " + token);

            try {
                // Check if the token is blacklisted
                if (tokenService.isTokenBlacklisted(token)) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Token is blacklisted");
                    return; // Stop further processing as the token is blacklisted
                }

                // Extract username from the token
                String username = util.getSubject(token);
                System.out.println("Extracted username: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Validate the token, and check if it's expired
                    if (util.isTokenExpired(token)) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write("Token has expired");
                        return; // Stop processing further, as the token is expired
                    }

                    // If token is valid, load user details
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
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token has expired");
                return; // Do not continue processing, as the token is expired
            } catch (Exception e) {
                // Handle other exceptions, such as invalid tokens
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid token");
                return; // Stop further processing on invalid token
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
            String token1 = request.getHeader("Authorization").substring(7);
            List<String> userPermissions = util.getPermissionsFromToken(token1);

            String requestPath = request.getRequestURI();
            String httpMethod = request.getMethod();

            if (!hasPermissionForEndpoint(userPermissions, requestPath, httpMethod)) {
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
