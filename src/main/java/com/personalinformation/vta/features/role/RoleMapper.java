package com.personalinformation.vta.features.role;

import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {



    public RoleDTO mapFromUserRoleToRole(UserRole userRole){
        RoleDTO roleDTO = new RoleDTO();
        Role role = userRole.getRole();

        roleDTO.setName(role.getName());
        return roleDTO;
    }


    public RoleDTO mapFromRoleEntityToDTO(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }


    public RoleDTO mapFromUserRoleToRoleWithId(UserRole userRole){
        RoleDTO roleDTO = new RoleDTO();

        Role role = userRole.getRole();

        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());

        return roleDTO;
    }

    public UserRole mapFromRoleDTOToUserRole(RoleDTO dto, Integer userId){

        UserRole userRole = new UserRole();
        userRole.getId().setUserId(userId);
        userRole.getId().setRoleId(dto.getId());
        userRole.setRole(new Role(dto.getId()));
        userRole.setUser(new User(userId));

        return userRole;
    }

//    public RoleDTO mapFromUserRoleToRoleWithId2(UserRole userRole){
//        RoleDTO roleDTO = new RoleDTO();
//
//        roleDTO.setName(userRole);
//
//    }

    public Role mapFromRoleDTOToRole(RoleDTO roleDTO){
        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        return role;
    }
}
