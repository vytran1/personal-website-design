package com.personalinformation.vta.features.role.checkRoleNameIsExistInSystem;

import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.features.role.RoleRepository;
import io.membrane_api.jmediator.Handler;


record CheckUniqueOfRoleNameQuery(String roleName) implements IQuery<Boolean>{}

@Handler
public class CheckUniqueOfRoleNameHandler {


    private final RoleRepository roleRepository;

    public CheckUniqueOfRoleNameHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public boolean handler(CheckUniqueOfRoleNameQuery query){

        String roleName = query.roleName();

        return roleRepository.existsByName(roleName);
    }
}
