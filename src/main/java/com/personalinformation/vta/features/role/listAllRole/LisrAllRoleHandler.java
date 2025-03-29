package com.personalinformation.vta.features.role.listAllRole;


import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.features.role.RoleDTO;
import com.personalinformation.vta.features.role.RoleMapper;
import com.personalinformation.vta.features.role.RoleRepository;
import io.membrane_api.jmediator.Handler;

import java.util.List;

record ListAllRoleCommand() implements IQuery<List<RoleDTO>>{}

@Handler
public class LisrAllRoleHandler {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;


    public LisrAllRoleHandler(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }


    public List<RoleDTO> listAllRole(ListAllRoleCommand command){

        List<Role> roles = roleRepository.findAll();


        List<RoleDTO> roleDTOS = roles.stream().map(roleMapper::mapFromRoleEntityToDTO).toList();


        return roleDTOS;

    }
}
