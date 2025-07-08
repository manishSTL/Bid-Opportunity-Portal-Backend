package com.portal.bid.service;

import com.portal.bid.entity.BlacklistedToken;
import com.portal.bid.repository.BlacklistedTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Autowired
    private BlacklistedTokenRepo blacklistedTokenRepo;

    // Method to blacklist a token with its expiration date
    public void blacklistToken(String token, Date expiryDate) {
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expiryDate);
        blacklistedTokenRepo.save(blacklistedToken);
    }

    // Check if a token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepo.findByToken(token).isPresent();
    }
}
