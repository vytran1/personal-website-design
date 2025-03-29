package com.personalinformation.vta.entities;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleId implements Serializable {

    private Integer userId;

    private Integer roleId;

    public UserRoleId() {
    }

    public UserRoleId(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getRoleId(), that.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRoleId());
    }

    @Override
    public String toString() {
        return "UserRoleId{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
