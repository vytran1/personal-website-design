package com.personalinformation.vta.features.user.listAllRoleOfOneUser;


import com.personalinformation.vta.common.exception.UserHasExistException;
import com.personalinformation.vta.common.exception.UserNotFoundException;
import com.personalinformation.vta.features.role.RoleDTO;
import io.membrane_api.jmediator.JMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ListAllRoleOfOneUserEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ListAllRoleOfOneUserEndpoint.class);
    private final JMediator jMediator;

    public ListAllRoleOfOneUserEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @GetMapping("/roles/{userId}")
    public ResponseEntity<?> listAllRoleOfOneUser(@PathVariable("userId") Integer userId){

        try {
            List<RoleDTO> roleDTOS = jMediator.send(new ListAllRoleOfOneUserCommand(userId));
            return ResponseEntity.ok(roleDTOS);
        } catch (UserNotFoundException e) {
            log.error("Error from List All Role Of One User {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
