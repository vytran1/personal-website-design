package com.personalinformation.vta.features.user.listAllRoleOfOneUser;


import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.common.exception.UserNotFoundException;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.features.role.RoleDTO;
import com.personalinformation.vta.features.role.RoleMapper;
import com.personalinformation.vta.features.user.UserRepository;
import io.membrane_api.jmediator.Handler;

import java.util.List;
import java.util.Optional;
import java.util.Set;

record ListAllRoleOfOneUserCommand(Integer userId) implements IQuery<List<RoleDTO>>{}

@Handler
public class ListAllRoleOfOneUserHandler {


    private final UserRepository userRepository;


    private final RoleMapper roleMapper;

    public ListAllRoleOfOneUserHandler(UserRepository userRepository,RoleMapper roleMapper) {
        this.userRepository = userRepository;
        this.roleMapper = roleMapper;
    }


    public List<RoleDTO> handler(ListAllRoleOfOneUserCommand command) throws UserNotFoundException {
        Integer userId = command.userId();

        Optional<User> userOPT = userRepository.findById(userId);

        if(!userOPT.isPresent()){
            throw new UserNotFoundException("Not exist user with the given id");
        }

        User user = userOPT.get();

        Set<UserRole> userRoles = user.getUserRoles();


        List<RoleDTO> ownedRoleDTO = userRoles.stream().map(roleMapper::mapFromUserRoleToRoleWithId).toList();


        return ownedRoleDTO;

    }
}
