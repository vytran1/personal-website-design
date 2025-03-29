package com.personalinformation.vta.features.security.getToken;

import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.security.AuthResponse;
import com.personalinformation.vta.features.security.CustomUserDetails;
import io.membrane_api.jmediator.JMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class TokenEndpoint {


    private static final Logger log = LoggerFactory.getLogger(TokenEndpoint.class);

    private final AuthenticationManager authenticationManager;

    private final JMediator jMediator;

    public TokenEndpoint(AuthenticationManager authenticationManager, JMediator jMediator){
        this.authenticationManager = authenticationManager;
        this.jMediator = jMediator;
    }


    @PostMapping("/token")
    public ResponseEntity<? extends Object> getTokenAccess(@RequestBody User user){
         String username = user.getEmail();
         String password = user.getPassword();

         log.info("Username {}", username);
         log.info("Password {}", password);

         try{
             Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

             CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

             User user1 = customUserDetails.getUser();

             AuthResponse authResponse = jMediator.send(new GetTokenCommand(user1));

             return ResponseEntity.ok(authResponse);
         }catch (BadCredentialsException ex){
             return new ResponseEntity<>("Username or password is wrong", HttpStatus.UNAUTHORIZED);
         } catch (Throwable e) {
             log.error("Unexpected error occurred while generating token",e);
             return new ResponseEntity<>("Error Server",HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

}
