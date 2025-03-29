package com.personalinformation.vta.features.role.listAllRole;


import com.personalinformation.vta.features.role.RoleDTO;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@EnableMethodSecurity(jsr250Enabled = true)
public class ListAllRoleEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ListAllRoleEndpoint.class);
    private final JMediator jMediator;


    public ListAllRoleEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }


    @GetMapping("")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> listAllRole(){
        try {
            List<RoleDTO> roleDTOS = jMediator.send(new ListAllRoleCommand());
            return ResponseEntity.ok(roleDTOS);
        } catch (Throwable e) {
            log.info("Error From Call List All Role API {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
