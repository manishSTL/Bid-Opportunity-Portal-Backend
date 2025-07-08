package com.portal.bid.config;

import org.springframework.util.AntPathMatcher;
import java.util.Set;

public class EndpointPermission {
    private final String pattern;
    private final Set<String> allowedMethods;
    private final AntPathMatcher pathMatcher;

    public EndpointPermission(String pattern, Set<String> allowedMethods) {
        this.pattern = pattern;
        this.allowedMethods = allowedMethods;
        this.pathMatcher = new AntPathMatcher();
    }

    public boolean matches(String requestPath, String method) {
        return pathMatcher.match(pattern, requestPath) && allowedMethods.contains(method);
    }
}