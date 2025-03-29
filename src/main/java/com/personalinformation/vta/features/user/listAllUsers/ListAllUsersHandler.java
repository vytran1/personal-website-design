package com.personalinformation.vta.features.user.listAllUsers;


import com.personalinformation.vta.common.IQuery;
import com.personalinformation.vta.common.exception.ForbiddenException;
import com.personalinformation.vta.common.exception.UserNotFoundException;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.features.permission.CheckPermissionService;
import com.personalinformation.vta.features.role.RoleMapper;
import com.personalinformation.vta.features.user.UserDTO;
import com.personalinformation.vta.features.user.UserMapper;
import com.personalinformation.vta.features.user.UserRepository;
import io.membrane_api.jmediator.Handler;

import java.util.List;
import java.util.Optional;
import java.util.Set;

record ListAllUsersCommand(User user) implements IQuery<List<UserDTO>>{};

@Handler
public class ListAllUsersHandler {


    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CheckPermissionService checkPermissionService;



    public ListAllUsersHandler(UserRepository userRepository,
                               UserMapper userMapper,
                               CheckPermissionService checkPermissionService
                               ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.checkPermissionService = checkPermissionService;

    }

    public List<UserDTO> listAllUsers(ListAllUsersCommand command) throws UserNotFoundException, ForbiddenException {

        User currentLogginUser = command.user();

        List<String> roleNames = currentLogginUser.getRoleNames();

        List<User> users = null;

        if(roleNames.contains("SUPER_ADMIN")){
            users = userRepository.findAll();
        }else if(roleNames.contains("ADMIN")){

            boolean hasManageUserPermission = checkPermissionService.checkPermission(currentLogginUser.getId(),"MANAGE_USER");

            if(!hasManageUserPermission){

                throw new ForbiddenException("You do not have permission for this function");

            }

            users = userRepository.listAllUserWhoHaveOnlyNormalRole();

        }


        List<UserDTO> userDTOS = (users != null ) ? users.stream().map(userMapper::mapFromEntityToDTO).toList() : List.of();


        return userDTOS;

    }
}
