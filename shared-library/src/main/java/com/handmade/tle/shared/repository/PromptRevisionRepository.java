package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.PromptRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptRevisionRepository extends JpaRepository<PromptRevision, String> {
    List<PromptRevision> findByPromptId(String promptId);
}
