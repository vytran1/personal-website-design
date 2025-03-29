package com.personalinformation.vta.features.role.checkRoleNameIsExistInSystem;

import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@EnableMethodSecurity(jsr250Enabled = true)
public class CheckUniqueOfRoleNameEndpoint {


    private static final Logger log = LoggerFactory.getLogger(CheckUniqueOfRoleNameEndpoint.class);
    private final JMediator jMediator;

    public CheckUniqueOfRoleNameEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @GetMapping("/unique/name/{roleName}")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> checkUniqueOfRoleName(@PathVariable("roleName") String roleName){
        try {
            boolean isUnique = jMediator.send(new CheckUniqueOfRoleNameQuery(roleName));
            return ResponseEntity.ok(isUnique);
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
