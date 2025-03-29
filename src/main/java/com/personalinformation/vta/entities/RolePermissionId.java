package com.personalinformation.vta.entities;

import java.io.Serializable;
import java.util.Objects;

public class RolePermissionId implements Serializable {

    private Integer roleId;

    private Integer permissionId;

    public RolePermissionId() {
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionId that = (RolePermissionId) o;
        return Objects.equals(getRoleId(), that.getRoleId()) && Objects.equals(getPermissionId(), that.getPermissionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getPermissionId());
    }
}
