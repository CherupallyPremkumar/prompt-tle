package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.UserReputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReputationRepository extends JpaRepository<UserReputation, String> {
}
