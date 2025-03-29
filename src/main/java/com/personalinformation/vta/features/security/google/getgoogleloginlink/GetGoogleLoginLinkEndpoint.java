package com.personalinformation.vta.features.security.google.getgoogleloginlink;

import io.membrane_api.jmediator.JMediator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class GetGoogleLoginLinkEndpoint {


    private final JMediator jMediator;

    public GetGoogleLoginLinkEndpoint(JMediator jMediator) {
        this.jMediator = jMediator;
    }

    @GetMapping("/google/link")
    public ResponseEntity<?> getGoogleLoginLink() throws Throwable {
        String googleLoginLink = jMediator.send(new GetGoogleLoginLinkCommand());
        Map<String, String> response = new HashMap<>();
        response.put("url", googleLoginLink);
        return ResponseEntity.ok(response);
    }
}
