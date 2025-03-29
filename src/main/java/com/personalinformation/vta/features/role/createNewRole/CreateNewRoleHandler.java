package com.personalinformation.vta.features.role.createNewRole;


import com.personalinformation.vta.common.ICommandVoid;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.features.role.RoleDTO;
import com.personalinformation.vta.features.role.RoleMapper;
import com.personalinformation.vta.features.role.RoleRepository;
import io.membrane_api.jmediator.Handler;

record CreateNewRoleCommand(RoleDTO roleDTO) implements ICommandVoid{}

@Handler
public class CreateNewRoleHandler {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public CreateNewRoleHandler(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }


    public void handler(CreateNewRoleCommand command){
        RoleDTO roleDTO = command.roleDTO();

        Role role = roleMapper.mapFromRoleDTOToRole(roleDTO);

        roleRepository.save(role);
    }
}
