package com.portal.bid.service;

import com.portal.bid.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(int id);
    Role save(Role role);
    void deleteById(int id);
}
