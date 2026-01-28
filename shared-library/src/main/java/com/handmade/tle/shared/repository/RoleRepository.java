package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Role;
import com.handmade.tle.shared.model.RoleName;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(RoleName name);
}
