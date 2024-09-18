package com.portal.bid.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles_permissions")
public class RolesPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_id",nullable = false)
    private int roleId;

    @Column(name = "permission_id",nullable = false)
    private  int permissionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
}
