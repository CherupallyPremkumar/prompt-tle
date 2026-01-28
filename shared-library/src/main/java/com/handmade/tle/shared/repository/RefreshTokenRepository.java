package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.RefreshToken;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);

    java.util.List<RefreshToken> findByUser(com.handmade.tle.shared.model.User user);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM RefreshToken r WHERE r.expiresAt < :now OR r.revoked = true")
    void deleteInvalidTokens(@org.springframework.data.repository.query.Param("now") java.time.LocalDateTime now);
}
