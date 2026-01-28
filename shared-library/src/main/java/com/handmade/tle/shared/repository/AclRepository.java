package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Acl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AclRepository extends JpaRepository<Acl, String> {
    Optional<Acl> findByName(String name);
}
