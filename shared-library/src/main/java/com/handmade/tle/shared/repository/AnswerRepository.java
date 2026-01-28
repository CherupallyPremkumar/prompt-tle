package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, String> {
    List<Answer> findByPromptId(String promptId);
}
