package com.personalinformation.vta.infrastructure;

public class RefreshTokenRequest {

    private String username;

    private String refreshToken;


    public RefreshTokenRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
