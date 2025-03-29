package com.personalinformation.vta.features.security.google.getgoogleloginlink;

import com.personalinformation.vta.common.ICommand;
import io.membrane_api.jmediator.Handler;
import org.springframework.beans.factory.annotation.Value;

record GetGoogleLoginLinkCommand() implements ICommand<String>{}

@Handler
public class GetGoogleLoginLinkHandler {


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;


    public String handler(GetGoogleLoginLinkCommand command){
        String googleLoginUri = "https://accounts.google.com/o/oauth2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=email%20profile";

        return googleLoginUri;
    }

}
