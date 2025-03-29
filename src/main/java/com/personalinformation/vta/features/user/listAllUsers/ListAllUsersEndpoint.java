package com.personalinformation.vta.features.user.listAllUsers;

import com.personalinformation.vta.common.exception.ForbiddenException;
import com.personalinformation.vta.common.utility.Utility;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.user.UserDTO;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@EnableMethodSecurity(jsr250Enabled = true)
public class ListAllUsersEndpoint {

    private final JMediator jMediator;


    public ListAllUsersEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @GetMapping("")
    @RolesAllowed({"ADMIN","SUPER_ADMIN"})
    public ResponseEntity<?> listAllUsers(){
        User currentLoggedUser = Utility.getCurrentLoggedUser();

        try {
            List<UserDTO> userDTOS = jMediator.send(new ListAllUsersCommand(currentLoggedUser));
            return userDTOS.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(userDTOS);
        }
        catch (ForbiddenException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
