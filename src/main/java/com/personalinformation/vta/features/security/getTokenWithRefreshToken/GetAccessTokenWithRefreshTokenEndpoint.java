package com.personalinformation.vta.features.security.getTokenWithRefreshToken;

import com.personalinformation.vta.common.exception.RefreshTokenExpiredException;
import com.personalinformation.vta.common.exception.RefreshTokenNotFoundException;
import com.personalinformation.vta.features.security.AuthResponse;
import com.personalinformation.vta.infrastructure.RefreshTokenRequest;
import io.membrane_api.jmediator.JMediator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class GetAccessTokenWithRefreshTokenEndpoint {

    private final JMediator jMediator;

    public GetAccessTokenWithRefreshTokenEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<?> getAccessTokenWithRefreshToken(@RequestBody RefreshTokenRequest request){

        try {
            AuthResponse authResponse =jMediator.send(new GetAccessTokenWithRefreshTokenCommand(request));
            return ResponseEntity.ok(authResponse);
        } catch (RefreshTokenNotFoundException | RefreshTokenExpiredException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Throwable e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
