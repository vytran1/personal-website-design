package com.personalinformation.vta.features.security;

import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.features.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOPT = userRepository.findByEmail(username);

        if(!userOPT.isPresent()){
            throw new UsernameNotFoundException("No user found with the given username");
        }

        return new CustomUserDetails(userOPT.get());
    }
}
