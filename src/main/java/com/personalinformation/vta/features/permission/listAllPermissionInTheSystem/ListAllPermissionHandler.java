package com.personalinformation.vta.features.permission.listAllPermissionInTheSystem;


import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.entities.Permission;
import com.personalinformation.vta.features.permission.PermissionDTO;
import com.personalinformation.vta.features.permission.PermissionMapper;
import com.personalinformation.vta.features.permission.PermissionRepository;
import io.membrane_api.jmediator.Handler;

import java.util.List;

record ListAllPermissionQuery() implements IQuery<List<PermissionDTO>>{}

@Handler
public class ListAllPermissionHandler {


    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;


    public ListAllPermissionHandler(PermissionRepository permissionRepository,
                                    PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }


    public List<PermissionDTO> listAllPermissionInTheSystem(ListAllPermissionQuery query){
        List<Permission> permissions = permissionRepository.findAll();

        List<PermissionDTO> permissionDTOS = permissions.stream().map(permissionMapper::mapFromPermissionToPermissionDTO).toList();

        return permissionDTOS;
    }
}
