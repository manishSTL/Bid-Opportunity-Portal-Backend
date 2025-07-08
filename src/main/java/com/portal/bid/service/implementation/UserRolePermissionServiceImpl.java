package com.portal.bid.service.implementation;

import com.portal.bid.entity.RolesPermission;
import com.portal.bid.repository.PermissionsRepository;
import com.portal.bid.repository.RolesPermissionRepo;
import com.portal.bid.repository.UserRoleRepo;
import com.portal.bid.service.UserRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserRolePermissionServiceImpl implements UserRolePermissionService {

    @Autowired
    private UserRoleRepo userRoleRepo;

    @Autowired
    private RolesPermissionRepo rolesPermissionRepo;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Override
    public Set<String> getPermissionsForUser(Integer userId) {
        // Get all role IDs for the user
        List<Integer> roleIds = userRoleRepo.findRoleIdsByUserId(userId);

        // Get all permission IDs for the roles
        Set<Integer> permissionIds = new HashSet<>();
        for (Integer roleId : roleIds) {
            List<RolesPermission> rolePermissions = rolesPermissionRepo.findByRoleId(Long.valueOf(roleId));
            for (RolesPermission rp : rolePermissions) {
                permissionIds.add(rp.getPermissionId());  // Collect permission IDs
            }
        }

        // Retrieve permission names for the collected permission IDs
        Set<String> permissions = new HashSet<>();
        for (Integer permissionId : permissionIds) {
            String permissionName = permissionsRepository.findPermissionNameById(permissionId);
            if (permissionName != null) {
                permissions.add(permissionName);
            }
        }

        return permissions;
    }
}
