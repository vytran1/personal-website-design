package com.personalinformation.vta.features.security.google.googleloginprocess;


import com.personalinformation.vta.common.ICommand;
import com.personalinformation.vta.common.exception.UserHasExistException;
import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.Provider;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.features.candidate.CandidateRepository;
import com.personalinformation.vta.features.role.UserRoleRepository;
import com.personalinformation.vta.features.security.AuthResponse;
import com.personalinformation.vta.features.security.jwt.JwtUtility;
import com.personalinformation.vta.features.user.UserRepository;
import com.personalinformation.vta.infrastructure.RefreshToken;
import com.personalinformation.vta.infrastructure.RefreshTokenRepository;
import io.membrane_api.jmediator.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

record GoogleLoginProcessCommand(String code) implements ICommand<AuthResponse>{};

@Handler
public class GoogleLoginProcessHandler {

    private static final Logger log = LoggerFactory.getLogger(GoogleLoginProcessHandler.class);
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.registration.google.user-info-uri}")
    private String userUri;

    @Value("${app.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    private final UserRepository userRepository;

    private final CandidateRepository candidateRepository;

    private final JwtUtility jwtUtility;

    private final RestTemplate restTemplate = new RestTemplate();

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRoleRepository userRoleRepository;

    public GoogleLoginProcessHandler(CandidateRepository candidateRepository,
                                     JwtUtility jwtUtility,
                                     UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     RefreshTokenRepository refreshTokenRepository,
                                     UserRoleRepository userRoleRepository) {
        this.candidateRepository = candidateRepository;
        this.jwtUtility = jwtUtility;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository =refreshTokenRepository;
        this.userRoleRepository = userRoleRepository;
    }



    public AuthResponse handler(GoogleLoginProcessCommand command) throws UserHasExistException {
        String code = command.code();

        // 1️⃣ Đổi code lấy access token từ Google
        Map<String, String> tokenRequest = Map.of(
                "code", code,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", "http://localhost:4200/login/oauth2/callback",  // ⚠️ Phải trùng với redirect_uri đã cấu hình ở Google Console
                "grant_type", "authorization_code"
        );

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUri, tokenRequest, Map.class);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Lỗi khi lấy access token từ Google");
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2️⃣ Dùng access token để lấy thông tin user từ Google
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userResponse = restTemplate.exchange(userUri, HttpMethod.GET, entity, Map.class);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Lỗi khi lấy thông tin user từ Google");
        }

        Map<String, Object> userData = userResponse.getBody();
        String email = (String) userData.get("email");
        String name = (String) userData.get("name");

        log.info("Email from Google {}",email);
        log.info("Name from Google {}", name);

        Optional<User> userOPT = userRepository.findByEmail(email);

        if(userOPT.isPresent()){

            User user = userOPT.get();
            if(!user.getProvider().equals(Provider.GOOGLE)){
                throw new UserHasExistException("Your email has exist in our system with other authentication type");
            }

            AuthResponse authResponse = getAccessToken(user);
            return authResponse;
        }else{
            User user = new User();
            user.setEmail(email);
            user.setProvider(Provider.GOOGLE);
            user.setPassword("");
            User savedUser = userRepository.save(user);

            Candidate candidate = new Candidate();
            candidate.setFirstName(name);
            candidate.setLastName(name);
            candidate.setEmail(email);
            candidate.setAddress("You should set up address by yourself in dashboard");
            candidate.setLongDescription("You should set up long description by yourself in dashboard");
            candidate.setShortDescription("You should set up short description by yourself in dashboard");
            candidate.setPhoneNumber("01234567891");
            candidate.setImage("default.png");
            candidate.setDob(LocalDate.now());
            candidateRepository.save(candidate);

            UserRole userRole = new UserRole(savedUser.getId(),1);
            userRoleRepository.save(userRole);

            savedUser.addRole(userRole);

            return getAccessToken(savedUser);
        }
    }




    public AuthResponse getAccessToken(User user){



//        log.info("Step 1 Get User From Command " + user);
//
//
//        Candidate candidate = candidateRepository.findCandidateByEmail(user.getEmail()).get();
//
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
