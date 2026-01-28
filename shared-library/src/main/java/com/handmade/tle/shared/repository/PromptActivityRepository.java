package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.PromptActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromptActivityRepository extends JpaRepository<PromptActivityLog, String> {
}
