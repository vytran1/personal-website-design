package com.personalinformation.vta.features.security.google.googleloginprocess;


import com.personalinformation.vta.common.exception.UserHasExistException;
import com.personalinformation.vta.features.security.AuthResponse;
import io.membrane_api.jmediator.JMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class GoogleLoginProcessEndpoint {

    private static final Logger log = LoggerFactory.getLogger(GoogleLoginProcessEndpoint.class);
    private final JMediator jMediator;

    public GoogleLoginProcessEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> process(@RequestParam("token") String token){

        try {
            AuthResponse authResponse = jMediator.send(new GoogleLoginProcessCommand(token));
            return ResponseEntity.ok(authResponse);
        }
        catch (UserHasExistException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (RuntimeException e){
            log.error("Error {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }
}
