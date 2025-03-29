package com.personalinformation.vta.features.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalinformation.vta.common.exception.JwtValidationException;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.features.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);


    private final JwtUtility jwtUtility;


    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    public JwtTokenFilter(JwtUtility jwtUtility){
        this.jwtUtility = jwtUtility;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(!hasAuthorizationBearer(request)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = getBearerToken(request);
        LOGGER.info("Token " + token);

        try{
            Claims claims = jwtUtility.validateToken(token);
            UserDetails userDetails = getUserDetails(claims);
            setAuthenticationContext(userDetails,request);
            filterChain.doFilter(request,response);
            clearAuthenticationContext();
        }catch (JwtValidationException e){
            LOGGER.error(e.getMessage(),e);
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        }
    }


    private boolean hasAuthorizationBearer(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        LOGGER.info("Authorization Header: " + header);

        if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")){
            return false;
        }else{
            return true;
        }
    }

    private String getBearerToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String[] array = header.split(" ");
        if(array.length == 2){
            return array[1];
        }
        return null;
    }

    private UserDetails getUserDetails(Claims claims){
        String subject = (String) claims.get(Claims.SUBJECT);
        String[] array = subject.split(",");

        Integer id = Integer.valueOf(array[0]);
        String email = array[1];

        User user = new User();
        user.setId(id);
        user.setEmail(email);

//        ObjectMapper objectMapper = new ObjectMapper();
        //List<String> roleNames = objectMapper.convertValue(claims.get("top"),List.class);

//        List<String> roleNames = (List<String>) claims.get("role");
//        System.out.println("List Role " + roleNames);

        // Lấy danh sách roles từ claims
        List<Map<String, Object>> roles = (List<Map<String, Object>>) claims.get("role");
        System.out.println("List Role " + roles);

        Set<UserRole> userRoles = new HashSet<>();

        for (Map<String, Object> roleData : roles) {
            Integer roleId = (Integer) roleData.get("id");
            String roleName = (String) roleData.get("name");
            userRoles.add(new UserRole(id,roleId,roleName));
        }

        System.out.println("Set Role " + userRoles);
        for(UserRole userRole : userRoles){
            System.out.println(userRole.getRole().getName());
        }

        user.setUserRoles(userRoles);

        LOGGER.info("Information parsed from JWT: " + id + " " + email);

        return new CustomUserDetails(user);
    }

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request){
        var authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void clearAuthenticationContext(){
        SecurityContextHolder.clearContext();
    }
}
