package com.personalinformation.vta.features.role.updatePermissionOfOneRole;

import com.personalinformation.vta.common.exception.RoleNotFoundException;
import com.personalinformation.vta.features.permission.PermissionDTO;
import io.membrane_api.jmediator.JMediator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@EnableMethodSecurity(jsr250Enabled = true)
public class UpdatePermissionOfOneRoleEndpoint {


    private final JMediator jMediator;

    public UpdatePermissionOfOneRoleEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @PutMapping("/permissions/{roleId}")
    public ResponseEntity<?> updatePermissionOfOneRole(@PathVariable("roleId") Integer roleId, @RequestBody List<PermissionDTO> permissionDTOS){

        try {
            List<PermissionDTO> result = jMediator.send(new UpdatePermissionOfOneRoleCommand(roleId,permissionDTOS));
            return ResponseEntity.ok(result);
        }
        catch (RoleNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        catch (Throwable e) {
            return ResponseEntity.internalServerError().build();
        }


    }
}
