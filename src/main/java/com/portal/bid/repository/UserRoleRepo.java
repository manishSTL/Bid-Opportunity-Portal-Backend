package com.portal.bid.repository;

import com.portal.bid.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepo extends JpaRepository<UserRoles,Integer> {
    @Query("SELECT ur.roleID FROM UserRoles ur WHERE ur.userID = :userID")
    List<Integer> findRoleIdsByUserId(@Param("userID") long userID);
}