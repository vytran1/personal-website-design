package com.personalinformation.vta.features.security;

import com.personalinformation.vta.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user){
        this.user = user;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {



        List<SimpleGrantedAuthority> authorities = this.user.getRoleNames().stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .toList();

        System.out.println("User Role " + this.user.getRoleNames());
        System.out.println("User Authorities: " + authorities);
        return authorities;


    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    public User getUser(){
        return this.user;
    }
}
