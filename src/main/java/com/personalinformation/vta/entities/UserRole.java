package com.personalinformation.vta.entities;

import jakarta.persistence.EmbeddedId;

import java.util.Objects;

public class UserRole  {
    private UserRoleId id = new UserRoleId();
    private User user;
    private Role role;


    public UserRole() {
    }

    public UserRole(Integer userId, Integer roleId){
        this.id = new UserRoleId(userId,roleId);
    }

    public UserRole(Role role){
        this.role = role;
    }

    public UserRole(Integer userId,Integer roleId,String roleName){
        this.id = new UserRoleId(userId,roleId);
        this.role = new Role(roleId,roleName);
    }

    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        UserRole userRole = (UserRole) o;
        return Objects.equals(getId(), userRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id.toString() +
                '}';
    }

    public UserRole copy(){
        UserRole shallowCopy = new UserRole();
        shallowCopy.getId().setUserId(this.id.getUserId());
        shallowCopy.getId().setRoleId(this.id.getRoleId());
        return shallowCopy;
    }
}
