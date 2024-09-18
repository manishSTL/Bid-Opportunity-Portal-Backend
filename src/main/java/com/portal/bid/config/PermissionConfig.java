package com.portal.bid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class PermissionConfig {

    @Bean
    public Map<String, List<String>> permissionEndpointMap() {
        return Map.of(
                "View", List.of("GET"),
                "Edit", List.of("POST", "PUT"),
                "Admin", List.of("GET", "POST", "PUT", "DELETE")
        );
    }
}
