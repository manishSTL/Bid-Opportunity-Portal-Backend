package com.portal.bid.entity;

public class UserResponse {

    private String token;
    private String message;
    private String refreshToken;
    // Constructor
    public UserResponse(String token,String refreshToken,String message) {
        this.token = token;
        this.message = message;
        this.refreshToken = refreshToken;
    }

    // Getters and Setters


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
