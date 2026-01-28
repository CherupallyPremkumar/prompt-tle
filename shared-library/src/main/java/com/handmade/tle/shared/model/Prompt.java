package com.handmade.tle.shared.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

@Entity
@Table(name = "prompt")
public class Prompt extends AbstractJpaStateEntity implements ActivityEnabledStateEntity {

    @Column(unique = true)
    public String slug;

    @Column(name = "is_featured")
    public boolean isFeatured = false;

    @Column(name = "forked_from_prompt_id")
    public String forkedFromPromptId;

    public String title;

    @Column(columnDefinition = "TEXT")
    public String description;

    @Column(columnDefinition = "TEXT")
    public String template;

    @Column(columnDefinition = "TEXT")
    public String body;

    @Column(name = "system_prompt", columnDefinition = "TEXT")
    public String systemPrompt;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    public TaskType taskType;

    @ElementCollection
    public List<String> tags = new ArrayList<>();

    @Column(name = "validation_score")
    public int validationScore = 0;

    @Column(name = "last_validated_at")
    public Date lastValidatedAt;

    @Column(name = "semantic_version")
    public String semanticVersion;

    @Column(name = "parent_version_id")
    public String parentVersionId;

    public String changelog;

    @Column(name = "recommended_model")
    public String recommendedModel;

    @Column(name = "user_id")
    public String userId;

    @Column(name = "author_username")
    public String authorUsername;

    @Column(name = "usage_count")
    public int usageCount = 0;

    @Column(name = "view_count")
    public int viewCount = 0;

    @Column(name = "favorite_count")
    public int favoriteCount = 0;

    public int score = 0;

    @Column(name = "accepted_answer_id")
    public String acceptedAnswerId;

    @Column(name = "answer_count")
    public int answerCount = 0;

    @Column(name = "comment_count")
    public int commentCount = 0;

    @Column(name = "duplicate_of_prompt_id")
    public String duplicateOfPromptId;

    @Column(name = "close_reason")
    public String closeReason;

    @Column(name = "closed_at")
    public Date closedAt;

    @Column(name = "closed_by_user_id")
    public String closedByUserId;

    @Column(name = "revision_number")
    public int revisionNumber = 1;

    @Column(name = "image_url")
    public String imageUrl;

    @Column(name = "creator_role")
    public String creatorRole;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    public List<Variable> variables = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    public List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    public List<Answer> answers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    public List<Attachment> attachments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    public List<PromptRevision> revisions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    public List<ModelCompatibility> modelCompatibility = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    public List<TestCase> testCases = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    public List<PromptActivityLog> activities = new ArrayList<>();

    @Override
    public Collection<ActivityLog> obtainActivities() {
        Collection<ActivityLog> acts = new ArrayList<>();
        if (activities != null) {
            acts.addAll(activities);
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        PromptActivityLog activityLog = new PromptActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
