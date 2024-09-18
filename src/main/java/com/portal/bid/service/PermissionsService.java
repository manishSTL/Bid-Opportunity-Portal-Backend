package com.portal.bid.service;

import com.portal.bid.entity.Permissions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PermissionsService {
    List<Permissions> getAllPermissions();
    Optional<Permissions> getPermissionById(int id);
    Permissions createPermission(Permissions permission);
    Permissions updatePermission(int id, Permissions permission);
    boolean deletePermission(int id);
    String allPermission(int id);
//    Map<String, String> loadPermissionEndpointMap();
}
