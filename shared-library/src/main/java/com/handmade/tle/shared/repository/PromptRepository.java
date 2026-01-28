package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  public interface PromptRepository extends JpaRepository<Prompt,String> {}
