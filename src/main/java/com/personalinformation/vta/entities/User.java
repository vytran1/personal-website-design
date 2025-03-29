package com.personalinformation.vta.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class User {

    private Integer id;

    private String email;

    private String password;

    private Provider provider;

    private Set<UserRole> userRoles = new HashSet<>();

    public User(Integer id) {
        this.id = id;
    }

    public User() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Provider getProvider() {
        return provider;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<String> getRoleNames(){
        return this.userRoles.stream().map(userRole -> {
            return userRole.getRole().getName();
        }).collect(Collectors.toList());
    }

    public void addRole(UserRole role){
        this.userRoles.add(role);
    }
}
