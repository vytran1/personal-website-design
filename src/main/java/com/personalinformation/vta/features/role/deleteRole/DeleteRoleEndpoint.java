package com.personalinformation.vta.features.role.deleteRole;

import com.personalinformation.vta.common.exception.RoleNotFoundException;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@EnableMethodSecurity(jsr250Enabled = true)
public class DeleteRoleEndpoint {


    private static final Logger log = LoggerFactory.getLogger(DeleteRoleEndpoint.class);
    private final JMediator jMediator;

    public DeleteRoleEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @DeleteMapping("/{roleId}")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") Integer roleId){

        try {

            jMediator.send(new DeleteRoleCommand(roleId));
            return ResponseEntity.ok().build();

        }
        catch (RoleNotFoundException e){

            log.error("Not found role with given role id {}",roleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }


    }
}
