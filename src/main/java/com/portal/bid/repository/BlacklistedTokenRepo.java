package com.portal.bid.repository;

import com.portal.bid.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// In package `com.portal.bid.repository`
public interface BlacklistedTokenRepo extends JpaRepository<BlacklistedToken, Long> {
    Optional<BlacklistedToken> findByToken(String token);
}
