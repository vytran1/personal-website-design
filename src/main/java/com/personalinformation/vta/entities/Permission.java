package com.personalinformation.vta.entities;

import java.util.HashSet;
import java.util.Set;

public class Permission {

    private Integer id;
    private String name;
    private String description;

    private Set<RolePermission> rolePermissions = new HashSet<>();

    public Permission() {
    }

    public Permission(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(Set<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }
}
