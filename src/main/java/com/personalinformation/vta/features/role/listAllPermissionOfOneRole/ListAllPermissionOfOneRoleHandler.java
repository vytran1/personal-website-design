package com.personalinformation.vta.features.role.listAllPermissionOfOneRole;


import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.common.exception.RoleNotFoundException;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.entities.RolePermission;
import com.personalinformation.vta.features.permission.PermissionDTO;
import com.personalinformation.vta.features.permission.PermissionMapper;
import com.personalinformation.vta.features.role.RoleRepository;
import io.membrane_api.jmediator.Handler;

import java.util.List;
import java.util.Optional;
import java.util.Set;

record ListAllPermissionOfOneRoleQuery(Integer roleId) implements IQuery<List<PermissionDTO>>{}

@Handler
public class ListAllPermissionOfOneRoleHandler {

    private final RoleRepository roleRepository;

    private final PermissionMapper permissionMapper;

    public ListAllPermissionOfOneRoleHandler(RoleRepository roleRepository, PermissionMapper permissionMapper) {
        this.roleRepository = roleRepository;
        this.permissionMapper = permissionMapper;
    }

    public List<PermissionDTO> listAllPermissionOfOneRole(ListAllPermissionOfOneRoleQuery query) throws RoleNotFoundException {

        Optional<Role> roleOPT = roleRepository.findById(query.roleId());

        if(roleOPT.isEmpty()){
            throw new RoleNotFoundException("Not found role with the given id");
        }

        Set<RolePermission> rolePermissions = roleOPT.get().getRolePermissions();

        List<PermissionDTO> permissionDTOS = rolePermissions.stream().map(permissionMapper::mapFromRolePermissionToPermissionDTO).toList();

        return permissionDTOS;
    }
}
