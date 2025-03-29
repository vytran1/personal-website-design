package com.personalinformation.vta.features.user.deleteUser;


import com.personalinformation.vta.common.exception.UserNotFoundException;
import io.membrane_api.jmediator.JMediator;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@EnableMethodSecurity(jsr250Enabled = true)
public class DeleteUserEndpoint {


    private static final Logger log = LoggerFactory.getLogger(DeleteUserEndpoint.class);
    private final JMediator jMediator;

    public DeleteUserEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @DeleteMapping("/{email}")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<?> deleteUser(@PathVariable String email){


        try {
            jMediator.send(new DeleteUserCommand(email));
            return ResponseEntity.ok().build();
        }
        catch (UserNotFoundException e){
            log.info("User not found {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (Throwable e) {
            log.info("Server Error {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }



}
