package com.personalinformation.vta.features.role.listAllPermissionOfOneRole;

import com.personalinformation.vta.common.exception.RoleNotFoundException;
import com.personalinformation.vta.features.permission.PermissionDTO;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@EnableMethodSecurity(jsr250Enabled = true)
public class ListAllPermissionOfOneRoleEndpoint {

    private final JMediator jMediator;

    public ListAllPermissionOfOneRoleEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @GetMapping("/permissions/{roleId}")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> listAllPermissionOfOneRoleEndpoint(@PathVariable("roleId") Integer roleId){

        try {
            List<PermissionDTO> permissionDTOS = jMediator.send(new ListAllPermissionOfOneRoleQuery(roleId));
            return ResponseEntity.ok(permissionDTOS);
        }
        catch (RoleNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
