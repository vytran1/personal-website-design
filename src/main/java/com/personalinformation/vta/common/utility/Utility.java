package com.personalinformation.vta.common.utility;

import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utility {


    public static Integer getIdOfCurrentLoginUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return user.getId();
    }


    public static String getEmailOfCurrentLoginUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return user.getEmail();
    }

    public static User getCurrentLoggedUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return user;
    }
}
