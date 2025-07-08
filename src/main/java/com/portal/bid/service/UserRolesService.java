package com.portal.bid.service;

import com.portal.bid.entity.UserRoles;

import java.util.List;
import java.util.Optional;

public interface UserRolesService {

    UserRoles save(UserRoles userRoles);

    Optional<UserRoles> updateUser(int id, UserRoles userRoles);

    Optional<UserRoles> get(int id);

    void delete(int id);

    List<UserRoles> getAll();
    List<Integer> getAllofid(int id);

}