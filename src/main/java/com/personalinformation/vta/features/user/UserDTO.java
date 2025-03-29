package com.personalinformation.vta.features.user;

import com.personalinformation.vta.features.role.RoleDTO;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Integer id;
    private String email;
    private String provider;
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
