package com.portal.bid.repository;

import com.portal.bid.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
    // Custom queries can be added here if needed
    // Method to find the permission name by id
    @Query("SELECT p.permissionName FROM Permissions p WHERE p.id = :id")
    String findPermissionNameById(@Param("id") Integer id);

//    String findPermissionStringById(Integer permissionId);
}
