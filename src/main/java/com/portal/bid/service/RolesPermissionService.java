package com.portal.bid.service;


import com.portal.bid.entity.RolesPermission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface RolesPermissionService {
    RolesPermission save(RolesPermission rolesPermission);

    List<RolesPermission> findAll();

    Optional<RolesPermission> findByID(int id);

    Optional<RolesPermission> update(int id, RolesPermission rp);

    void deletebyID(int id);
    List<Integer> getPermissions(int id);
}
