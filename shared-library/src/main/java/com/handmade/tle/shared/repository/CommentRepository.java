package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByPromptId(String promptId);
}
