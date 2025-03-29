package com.personalinformation.vta.features.role.updatePermissionOfOneRole;


import com.personalinformation.vta.common.ICommand;
import com.personalinformation.vta.common.exception.RoleNotFoundException;
import com.personalinformation.vta.entities.Permission;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.entities.RolePermission;
import com.personalinformation.vta.features.permission.PermissionDTO;
import com.personalinformation.vta.features.permission.PermissionMapper;
import com.personalinformation.vta.features.permission.RolePermissionRepository;
import com.personalinformation.vta.features.role.RoleRepository;
import io.membrane_api.jmediator.Handler;

import java.util.*;
import java.util.stream.Collectors;

record UpdatePermissionOfOneRoleCommand(Integer roleId, List<PermissionDTO> permissionDTO) implements ICommand<List<PermissionDTO>>{}

@Handler
public class UpdatePermissionOfOneRoleHandler {

    private final RoleRepository roleRepository;
    private final PermissionMapper permissionMapper;
    private final RolePermissionRepository rolePermissionRepository;

    public UpdatePermissionOfOneRoleHandler(RoleRepository roleRepository,
                                            PermissionMapper permissionMapper,
                                            RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionMapper = permissionMapper;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public List<PermissionDTO> handler(UpdatePermissionOfOneRoleCommand command) throws RoleNotFoundException {
        Optional<Role> roleOPT = roleRepository.findById(command.roleId());

        if(roleOPT.isEmpty()){
            throw new RoleNotFoundException("Not Found Role With The Given Id");
        }

        Role role = roleOPT.get();

        List<PermissionDTO> permissionDTOS = command.permissionDTO();


        List<RolePermission> permissionFromDatabase = rolePermissionRepository.findByRoleId(role.getId());

        Set<RolePermission> newRolePermissionFromFrontend = permissionDTOS.stream().map(p -> permissionMapper.mapFromPermissionDTOToRolePermission(role,p)).collect(Collectors.toSet());

        List<RolePermission> permissionToDeleted = new ArrayList<>();

        //Filter role permission need to be deleted
        for(RolePermission currentPermission : permissionFromDatabase){
            if(!newRolePermissionFromFrontend.contains(currentPermission)){
                permissionToDeleted.add(currentPermission.copy());
            }
        }


        //Delete Old role permission
        rolePermissionRepository.deleteAll(permissionToDeleted);

        //Save New Permission
        rolePermissionRepository.saveAll(newRolePermissionFromFrontend);

        return permissionDTOS;
    }
}
