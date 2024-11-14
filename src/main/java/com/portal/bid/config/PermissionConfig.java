package com.portal.bid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.*;

@Configuration
public class PermissionConfig {

    @Bean
    public Map<String, List<EndpointPermission>> permissionEndpointMap() {
        Map<String, List<EndpointPermission>> permissionsMap = new HashMap<>();


        permissionsMap.put("EDIT", List.of(
                new EndpointPermission("/api/approvals/**", Set.of("POST", "PUT")),
                new EndpointPermission("/api/approval-request/**", Set.of("POST", "PUT")),
                new EndpointPermission("/api/go-no-go/**", Set.of("POST", "PUT")),
                new EndpointPermission("/api/gonogostatus/**", Set.of("POST", "PUT")),
                new EndpointPermission("/api/plans/**", Set.of("POST", "PUT")),
                new EndpointPermission("/api/user/logout", Set.of("POST"))
                ));

        // Finance permissions
        permissionsMap.put("VIEW", List.of(
                new EndpointPermission("/api/agp/**", Set.of("GET")),
                new EndpointPermission("/api/approvals/**", Set.of("GET")),
                new EndpointPermission("/api/approval-requests/**", Set.of("GET")),
                new EndpointPermission("/api/user/**", Set.of("GET")),
                new EndpointPermission("/api/business-units/**", Set.of("GET")),
                new EndpointPermission("/api/deals/**", Set.of("GET")),
                new EndpointPermission("/api/deal-status/**", Set.of("GET")),
                new EndpointPermission("/api/department/**", Set.of("GET")),
                new EndpointPermission("/api/opportunities/**", Set.of("GET")),
                new EndpointPermission("/api/fy/**", Set.of("GET")),
                new EndpointPermission("/api/go-no-go/**", Set.of("GET")),
                new EndpointPermission("/api/gonogostatus/**", Set.of("GET")),
                new EndpointPermission("/api/plans/**", Set.of("GET")),
                new EndpointPermission("/api/priority/**", Set.of("GET")),
                new EndpointPermission("/api/scrape/**", Set.of("GET")),
                new EndpointPermission("/api/business-segments/**", Set.of("GET")),
                new EndpointPermission("/api/agp1", Set.of("GET")),
                new EndpointPermission("/api/allusers", Set.of("GET")),
                new EndpointPermission("/api/user/logout", Set.of("POST"))

                //new EndpointPermission("/api/reports/finance/**", Set.of("GET"))
        ));

        permissionsMap.put("ADMIN", List.of(
                new EndpointPermission("/api/**", Set.of("GET", "POST", "PUT", "DELETE"))
        ));

        return permissionsMap;
    }
}