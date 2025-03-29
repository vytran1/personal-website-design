package com.personalinformation.vta.features.role.createNewRole;

import com.personalinformation.vta.features.role.RoleDTO;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@EnableMethodSecurity(jsr250Enabled = true)
public class CreateNewRoleEndpoint {


    private final JMediator jMediator;

    public CreateNewRoleEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @PostMapping("/create")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> createNewRole(@RequestBody RoleDTO roleDTO){

        try {
            jMediator.send(new CreateNewRoleCommand(roleDTO));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
