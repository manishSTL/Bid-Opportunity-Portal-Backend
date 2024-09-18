package com.portal.bid.repository;

import com.portal.bid.entity.RolesPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface RolesPermissionRepo extends JpaRepository<RolesPermission,Integer> {

    @Query("SELECT rp.permissionId FROM RolesPermission rp WHERE rp.roleId = :roleId")
    List<Integer> findPermissionIdsByRoleId(@Param("roleId") int roleId);

    List<RolesPermission> findByRoleId(Long aLong);
}
