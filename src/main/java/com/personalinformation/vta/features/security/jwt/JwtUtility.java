package com.personalinformation.vta.features.security.jwt;

import com.personalinformation.vta.common.exception.JwtValidationException;
import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtility {


    private static final String SECRET_KEY_ALGORITHM = "HmacSHA512";

    @Value("${app.security.jwt.issuer}")
    private String issuerName;

    @Value("${app.security.jwt.secret}")
    private String secretKey;

    @Value("${app.security.jwt.access-token.expiration}")
    private int accessTokenExpiration;


    public String generatingToken(User user){
        if(user == null || user.getEmail() == null || user.getId() == null){
            throw new IllegalArgumentException("User Object is null or its fields have null values");
        }

        long expirationTimeMilis = System.currentTimeMillis() + accessTokenExpiration * 60000;

        String subject = String.format("%s,%s",user.getId(),user.getEmail());


//        List<String> roleNames = user.getRoleNames();

        // Chuyển đổi danh sách roles sang List<Map<String, Object>>
        List<Map<String, Object>> roles = user.getUserRoles().stream()
                .map(userRole -> {
                    Map<String, Object> roleData = new HashMap<>();
                    roleData.put("id", userRole.getRole().getId());
                    roleData.put("name", userRole.getRole().getName());
                    return roleData;
                })
                .collect(Collectors.toList());

//        System.out.println("List Role Name " + roleNames);

        System.out.println("List Role " + roles.toString());

        return Jwts.builder()
                .subject(subject)
                .issuer(issuerName)
                .issuedAt(new Date())
                .expiration(new Date(expirationTimeMilis))
                .claim("role",roles)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),Jwts.SIG.HS512)
                .compact();
    }


    public Claims validateToken(String token) throws JwtValidationException {
        try{
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(),SECRET_KEY_ALGORITHM);

            return Jwts.parser().verifyWith(keySpec).build().parseSignedClaims(token).getPayload();
        }catch (ExpiredJwtException ex){
            throw new JwtValidationException("Access Token expired",ex);
        }catch (IllegalArgumentException ex){
            throw new JwtValidationException("Access Token is illegal",ex);
        }catch (MalformedJwtException ex){
            throw new JwtValidationException("Access Token is not well formed",ex);
        }catch (UnsupportedJwtException ex){
            throw new JwtValidationException("Access Token is not supported",ex);
        }
    }


    public int getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(int accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }
}
