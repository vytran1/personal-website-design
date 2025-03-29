package com.personalinformation.vta.features.security.getTokenWithRefreshToken;


import com.personalinformation.vta.common.ICommand;
import com.personalinformation.vta.common.exception.RefreshTokenExpiredException;
import com.personalinformation.vta.common.exception.RefreshTokenNotFoundException;
import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.candidate.CandidateRepository;
import com.personalinformation.vta.features.security.AuthResponse;
import com.personalinformation.vta.features.security.getToken.TokenHandler;
import com.personalinformation.vta.features.security.jwt.JwtUtility;
import com.personalinformation.vta.features.user.UserRepository;
import com.personalinformation.vta.infrastructure.RefreshToken;
import com.personalinformation.vta.infrastructure.RefreshTokenRepository;
import com.personalinformation.vta.infrastructure.RefreshTokenRequest;
import io.membrane_api.jmediator.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

record GetAccessTokenWithRefreshTokenCommand(RefreshTokenRequest request) implements ICommand<AuthResponse>{};


@Handler
public class GetAccessTokenWithRefreshTokenHandler {

    private static final Logger log = LoggerFactory.getLogger(GetAccessTokenWithRefreshTokenHandler.class);

    @Value("${app.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

//    private final CandidateRepository candidateRepository;

    private final UserRepository userRepository;


    private final JwtUtility jwtUtility;



    public GetAccessTokenWithRefreshTokenHandler(RefreshTokenRepository refreshTokenRepository,
                                                 PasswordEncoder passwordEncoder,
                                                 UserRepository userRepository,
                                                 JwtUtility jwtUtility){
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
//        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
        this.jwtUtility = jwtUtility;
    }


    public AuthResponse handler(GetAccessTokenWithRefreshTokenCommand command){



        RefreshTokenRequest request = command.request();

        log.info("Extrac information from request");

        String username = request.getUsername();
        String rawRefreshToken = request.getRefreshToken();

        log.info("Username from Refresh API call " + username);
        log.info("Raw Refresh Token from Refresh API call " + rawRefreshToken);

        log.info("Get All RefreshToken belong to the request user");
        List<RefreshToken> listTokenByUsername = refreshTokenRepository.findRefreshTokenByUsername(username);

        RefreshToken foundRefreshToken = null;

        log.info("Find the refresh token which is match with the token in the request");
        for(RefreshToken refreshToken : listTokenByUsername){
            if(passwordEncoder.matches(rawRefreshToken,refreshToken.getToken())){
                foundRefreshToken = refreshToken;
            }
        }

        log.info(" the token is expired or deleted from database so throw exception");

        if(foundRefreshToken == null){
            throw new RefreshTokenNotFoundException("");
        }

        Date currentTime = new Date();

        if(foundRefreshToken.getExpiryTime().before(currentTime)){
            throw new RefreshTokenExpiredException("");
        }

        log.info("the token is exist and is not expired, we generate new token and new refresh token then saving them to database and also delete the old refresh token");
        AuthResponse authResponse = getAccessToken(foundRefreshToken.getUser());

        refreshTokenRepository.delete(foundRefreshToken);

        return authResponse;
    }



    public AuthResponse getAccessToken(User user){



//        log.info("Step 1 Get User From Command " + user);


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
