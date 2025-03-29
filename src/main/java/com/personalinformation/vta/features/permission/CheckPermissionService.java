package com.personalinformation.vta.features.permission;

import com.personalinformation.vta.common.exception.UserNotFoundException;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckPermissionService {



    private final UserRepository userRepository;

    public CheckPermissionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkPermission(Integer userId, String permission) throws UserNotFoundException {
        Optional<User> userOPT = userRepository.findById(userId);

        if(!userOPT.isPresent()){
            throw new UserNotFoundException("Not Exist User With The Given Id");
        }

        User user = userOPT.get();

        return user.getUserRoles().stream()
                .map(userRole -> userRole.getRole())
                .flatMap(role -> role.getRolePermissions().stream())
                .map(rolePermission -> rolePermission.getPermission())
                .anyMatch(p -> p.getName().equals(permission));

    }
}
