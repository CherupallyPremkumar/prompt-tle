package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.AuthProvider;
import com.handmade.tle.shared.model.User;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    Optional<User> findByGoogleSub(String googleSub);

    @org.springframework.data.jpa.repository.Query("select u from User u left join fetch u.roles r left join fetch r.acls where u.email = :email")
    Optional<User> findByEmailWithRoles(@org.springframework.data.repository.query.Param("email") String email);

    @org.springframework.data.jpa.repository.Query("select u from User u left join fetch u.roles r left join fetch r.acls where u.id = :id")
    Optional<User> findByIdWithRoles(@org.springframework.data.repository.query.Param("id") String id);
}