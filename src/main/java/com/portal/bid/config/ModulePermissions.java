package com.portal.bid.config;

import java.util.*;

public class ModulePermissions {
    private final Map<String, Set<EndpointPermission>> permissionMappings;

    private ModulePermissions(Map<String, Set<EndpointPermission>> permissionMappings) {
        this.permissionMappings = permissionMappings;
    }

    public Set<EndpointPermission> getPermissionsForPermissionType(String permission) {
        return permissionMappings.getOrDefault(permission, Collections.emptySet());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, Set<EndpointPermission>> permissionMappings = new HashMap<>();

        public Builder addPermission(String permission, Set<EndpointPermission> endpoints) {
            permissionMappings.put(permission, endpoints);
            return this;
        }

        public ModulePermissions build() {
            return new ModulePermissions(permissionMappings);
        }
    }
}