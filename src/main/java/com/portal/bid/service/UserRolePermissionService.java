package com.portal.bid.service;

import java.util.Set;

public interface UserRolePermissionService {
    Set<String> getPermissionsForUser(Integer userId);
}
