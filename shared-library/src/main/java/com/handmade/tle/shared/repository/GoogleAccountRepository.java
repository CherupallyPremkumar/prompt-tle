package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.GoogleAccount;
import com.handmade.tle.shared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GoogleAccountRepository extends JpaRepository<GoogleAccount, String> {
    Optional<GoogleAccount> findByGoogleUserId(String googleUserId);

    List<GoogleAccount> findByUser(User user);

    Optional<GoogleAccount> findByAccessToken(String accessToken);
}
