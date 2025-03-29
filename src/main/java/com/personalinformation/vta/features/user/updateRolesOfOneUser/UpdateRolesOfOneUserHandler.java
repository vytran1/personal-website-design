package com.personalinformation.vta.features.user.updateRolesOfOneUser;


import com.personalinformation.vta.common.ICommand;
import com.personalinformation.vta.common.ICommandVoid;
import com.personalinformation.vta.common.exception.UserNotFoundException;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.features.role.RoleDTO;
import com.personalinformation.vta.features.role.RoleMapper;
import com.personalinformation.vta.features.role.UserRoleRepository;
import com.personalinformation.vta.features.user.UserRepository;
import io.membrane_api.jmediator.Handler;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

record UpdateRolesOfOneUserCommand(Integer userId,List<RoleDTO> roleDTOS) implements ICommand<List<RoleDTO>>{}

@Handler
public class UpdateRolesOfOneUserHandler {


    private final UserRepository userRepository;
    private final RoleMapper roleMapper;
    private final UserRoleRepository userRoleRepository;

    public UpdateRolesOfOneUserHandler(UserRepository userRepository, RoleMapper roleMapper, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleMapper = roleMapper;
        this.userRoleRepository = userRoleRepository;
    }


    public List<RoleDTO> handler(UpdateRolesOfOneUserCommand command) throws UserNotFoundException {
        //Extract information from request

        Integer userId = command.userId();

        List<RoleDTO> roleDTOS = command.roleDTOS();

        System.out.println("New Role From Frontend");
        for(RoleDTO dto : roleDTOS){
            System.out.println(dto.toString());
        }

        //Query Database with the aim of getting user based on userId From Frontend

        User user = userRepository.findByUserId(userId);

        if(user == null){
            throw new UserNotFoundException("Not Exist User With The Given Id");
        }

        //Get List UserRoles From Database

        List<UserRole> userRolesFromDatabase = userRoleRepository.findByUserId(userId);

        //Convert List New Roles From Frontend To

        Set<UserRole> newUserRolesFromFrontend = roleDTOS.stream().map(roleDTO -> roleMapper.mapFromRoleDTOToUserRole(roleDTO,userId)).collect(Collectors.toSet());

        System.out.println("Set NewUserRolesFromFrontend");
        for(UserRole userRole : newUserRolesFromFrontend){
            System.out.println(userRole.toString());
        }

        //Collect User Role Need To Be Deleted

        List<UserRole>  userRolesToDelete = new ArrayList<>();

        for(UserRole userRoleFromDatabase : userRolesFromDatabase){
            if(!newUserRolesFromFrontend.contains(userRoleFromDatabase)){
                userRolesToDelete.add(userRoleFromDatabase.copy());
            }
        }

        System.out.println("Set UserRoleToDelete");
        for(UserRole userRoleDelete : userRolesToDelete){
            System.out.println(userRoleDelete.toString());
        }

        //Delete All Roles needing to delete

//        for(UserRole userRoleNeedToBeDeleted : userRolesToDelete){
//            System.out.println("User Id " + userRoleNeedToBeDeleted.getId().toString());
//            userRoleRepository.deleteAll(use);
//        }
        userRoleRepository.deleteAll(userRolesToDelete);

        //Save New Roles To Database

        userRoleRepository.saveAll(newUserRolesFromFrontend);



        return roleDTOS;

    }


}
