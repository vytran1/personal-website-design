package com.personalinformation.vta.features.user.updateRolesOfOneUser;

import com.personalinformation.vta.common.exception.UserNotFoundException;
import com.personalinformation.vta.features.role.RoleDTO;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@EnableMethodSecurity(jsr250Enabled = true)
public class UpdateRolesOfOneUserEndpoint {

    private static final Logger log = LoggerFactory.getLogger(UpdateRolesOfOneUserEndpoint.class);
    private final JMediator jMediator;



    public UpdateRolesOfOneUserEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }


    @PutMapping("/roles/{userId}")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> updateRolesOfOneUser(@PathVariable("userId") Integer userId, @RequestBody List<RoleDTO> roleDTOS){

        try {
            List<RoleDTO> newRolesList = jMediator.send(new UpdateRolesOfOneUserCommand(userId,roleDTOS));
            return ResponseEntity.ok(newRolesList);
        }
        catch (UserNotFoundException e){
            log.error("Not Exist {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (Throwable e) {
            e.printStackTrace();
            log.error("Interval Error {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }


    }
}
