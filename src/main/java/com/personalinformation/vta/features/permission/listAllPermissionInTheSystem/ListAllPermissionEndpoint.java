package com.personalinformation.vta.features.permission.listAllPermissionInTheSystem;

import com.personalinformation.vta.features.permission.PermissionDTO;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@EnableMethodSecurity(jsr250Enabled = true)
public class ListAllPermissionEndpoint {

    private final JMediator jMediator;

    public ListAllPermissionEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }


    @GetMapping("")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> listAllPermissionInTheSystem(){

        try {
            List<PermissionDTO> permissionDTOS = jMediator.send(new ListAllPermissionQuery());
            return ResponseEntity.ok(permissionDTOS);
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }


    }
}
