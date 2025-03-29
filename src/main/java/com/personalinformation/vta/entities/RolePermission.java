package com.personalinformation.vta.entities;

import java.util.Objects;

public class RolePermission {

    private RolePermissionId id = new RolePermissionId();

    private Role role;

    private Permission permission;

    public RolePermission() {
    }

    public RolePermissionId getId() {
        return id;
    }

    public void setId(RolePermissionId id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public RolePermission copy(){
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(this.id);
        return rolePermission;
    }
}
