package com.personalinformation.vta.features.permission;

import com.personalinformation.vta.entities.Permission;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.entities.RolePermission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    //Use For List All Permission In The System
    public PermissionDTO mapFromPermissionToPermissionDTO(Permission permission){
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(permission.getId());
        permissionDTO.setName(permission.getName());
        return permissionDTO;
    }

    //Use for List All Permission Of One Role
    public PermissionDTO mapFromRolePermissionToPermissionDTO(RolePermission rolePermission){
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(rolePermission.getPermission().getId());
        permissionDTO.setName(rolePermission.getPermission().getName());
        return permissionDTO;
    }

    public RolePermission mapFromPermissionDTOToRolePermission(Role role, PermissionDTO permissionDTO){
        RolePermission rolePermission = new RolePermission();
        rolePermission.getId().setRoleId(role.getId());
        rolePermission.getId().setPermissionId(permissionDTO.getId());
        rolePermission.setRole(role);
        rolePermission.setPermission(new Permission(permissionDTO.getId()));
        return rolePermission;
    }
}
