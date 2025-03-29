package com.personalinformation.vta.features.role.deleteRole;


import com.personalinformation.vta.common.ICommandVoid;
import com.personalinformation.vta.common.exception.RoleNotFoundException;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.features.role.RoleRepository;
import io.membrane_api.jmediator.Handler;

import java.util.Optional;

record DeleteRoleCommand(Integer roleId) implements ICommandVoid{}

@Handler
public class DeleteRoleHandler {

    private final RoleRepository roleRepository;

    public DeleteRoleHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void deleteRole(DeleteRoleCommand command) throws RoleNotFoundException {
        Integer roleId = command.roleId();

        Optional<Role> roleOPT = roleRepository.findById(roleId);

        if(roleOPT.isEmpty()){
            throw new RoleNotFoundException("Not found role with the given id");
        }


        roleRepository.delete(roleOPT.get());
    }

}
