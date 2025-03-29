package com.personalinformation.vta.features.user;


import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.features.role.RoleDTO;
import com.personalinformation.vta.features.role.RoleMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {


    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserDTO mapFromEntityToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setProvider(user.getProvider().toString());

        Set<UserRole> userRoles = user.getUserRoles();

        Set<RoleDTO> roleDTOS = userRoles.stream().map(roleMapper::mapFromUserRoleToRole).collect(Collectors.toSet());

        userDTO.setRoles(roleDTOS);

        return userDTO;
    }
}
