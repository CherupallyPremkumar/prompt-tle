package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Variable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariableRepository extends JpaRepository<Variable, String> {
    List<Variable> findByPromptId(String promptId);
}
