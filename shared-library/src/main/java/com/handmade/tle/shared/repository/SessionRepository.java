package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Session;
import com.handmade.tle.shared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, String> {
    @Query("SELECT s FROM Session s WHERE s.user = :user AND s.isActive = true AND (s.expiresAt IS NULL OR s.expiresAt > :now)")
    List<Session> findActiveSessionsByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Session s SET s.isActive = false WHERE s.expiresAt < :now")
    void deactivateExpiredSessions(@Param("now") LocalDateTime now);
}
