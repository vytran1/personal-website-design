package com.personalinformation.vta.features.security.getToken;


import com.personalinformation.vta.common.ICommand;
import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.candidate.CandidateRepository;
import com.personalinformation.vta.features.security.AuthResponse;
import com.personalinformation.vta.features.security.jwt.JwtUtility;
import com.personalinformation.vta.infrastructure.RefreshToken;
import com.personalinformation.vta.infrastructure.RefreshTokenRepository;
import io.membrane_api.jmediator.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.UUID;

record GetTokenCommand(User user) implements ICommand<AuthResponse>{};

@Handler
public class TokenHandler {


    private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);


    @Value("${app.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    private final CandidateRepository candidateRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtility jwtUtility;

    private final PasswordEncoder passwordEncoder;

    public TokenHandler(CandidateRepository candidateRepository,
                        RefreshTokenRepository refreshTokenRepository,
                        JwtUtility jwtUtility,
                        PasswordEncoder passwordEncoder){
        this.candidateRepository = candidateRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtility = jwtUtility;
        this.passwordEncoder = passwordEncoder;
    }




    public AuthResponse getAccessToken(GetTokenCommand command){

        User user = command.user();

        log.info("Step 1 Get User From Command " + user);


//        Candidate candidate = candidateRepository.findCandidateByEmail(user.getEmail()).get();

//        log.info("Step 2 Get Candidate based on email from client " + candidate);

        String accessToken = jwtUtility.generatingToken(user);

        log.info("Step 3 Generating JWT " + accessToken);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);

        String randomUUID = UUID.randomUUID().toString();

        log.info("Step 4 Generating Refresh Token " + randomUUID);

        response.setRefreshToken(randomUUID);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(passwordEncoder.encode(randomUUID));

        long refreshTokenExpirateMillis = System.currentTimeMillis() + refreshTokenExpiration * 60000;

        refreshToken.setExpiryTime(new Date(refreshTokenExpirateMillis));

        log.info("Step 5 Saving RefreshToken object " + refreshToken);

        refreshTokenRepository.save(refreshToken);

        return response;
    }


}
