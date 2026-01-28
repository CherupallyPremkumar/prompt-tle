-- Drop tables if they exist
DROP TABLE IF EXISTS notification_data;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS flag;
DROP TABLE IF EXISTS bounty;
DROP TABLE IF EXISTS attachment;
DROP TABLE IF EXISTS prompt_activity;
DROP TABLE IF EXISTS prompt_revision;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS variable;
DROP TABLE IF EXISTS prompt_tags;
DROP TABLE IF EXISTS prompt;

-- Prompt Table (All Fields)
CREATE TABLE prompt (
    id VARCHAR(255) PRIMARY KEY,
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    tenant VARCHAR(255),
    created_by VARCHAR(255),
    version BIGINT,
    state_entry_time TIMESTAMP,
    sla_yellow_date TIMESTAMP,
    sla_red_date TIMESTAMP,
    sla_tending_late INT,
    sla_late INT,
    flow_id VARCHAR(255),
    state_id VARCHAR(255),
    slug VARCHAR(255) UNIQUE,
    title VARCHAR(255),
    description TEXT,
    template TEXT,
    body TEXT,
    system_prompt TEXT,
    task_type VARCHAR(50),
    validation_score INT,
    last_validated_at TIMESTAMP,
    semantic_version VARCHAR(50),
    parent_version_id VARCHAR(255),
    changelog TEXT,
    recommended_model VARCHAR(255),
    user_id VARCHAR(255),
    author_username VARCHAR(255),
    usage_count INT,
    view_count INT,
    favorite_count INT,
    score INT,
    accepted_answer_id VARCHAR(255),
    answer_count INT,
    comment_count INT,
    duplicate_of_prompt_id VARCHAR(255),
    close_reason VARCHAR(255),
    closed_at TIMESTAMP,
    closed_by_user_id VARCHAR(255),
    revision_number INT,
    is_featured BOOLEAN DEFAULT FALSE,
    forked_from_prompt_id VARCHAR(255),
    image_url VARCHAR(255),
    creator_role VARCHAR(255)
);

-- Category Table
CREATE TABLE category (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,
    icon VARCHAR(255),
    color VARCHAR(255),
    description TEXT,
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    tenant VARCHAR(255),
    created_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0
);

-- Prompt Vote Table
CREATE TABLE prompt_vote (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    prompt_id VARCHAR(255) NOT NULL,
    vote_type VARCHAR(50) NOT NULL,
    UNIQUE (user_id, prompt_id),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id),
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    tenant VARCHAR(255),
    created_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0
);

-- Prompt Tags
CREATE TABLE prompt_tags (
    prompt_id VARCHAR(255),
    tags VARCHAR(255),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Answer Table
CREATE TABLE answer (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    user_id VARCHAR(255),
    author_username VARCHAR(255),
    body TEXT,
    score INT,
    is_accepted BOOLEAN,
    created_at TIMESTAMP,
    revision_number INT,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Comment Table
CREATE TABLE comment (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    author VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Prompt Revision Table
CREATE TABLE prompt_revision (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    revision_number INT,
    user_id VARCHAR(255),
    title VARCHAR(255),
    body TEXT,
    change_comment VARCHAR(255),
    created_at TIMESTAMP,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Variable Table
CREATE TABLE variable (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    name VARCHAR(255),
    type VARCHAR(50),
    description VARCHAR(255),
    required BOOLEAN,
    default_value VARCHAR(255),
    example VARCHAR(255),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Prompt Activity Table
CREATE TABLE prompt_activity (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    activity_name VARCHAR(255),
    activity_success BOOLEAN,
    activity_comment VARCHAR(255),
    created_time TIMESTAMP
);

-- Favorite Table
CREATE TABLE favorite (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255),
    prompt_id VARCHAR(255),
    added_at TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Flag Table
CREATE TABLE flag (
    id VARCHAR(255) PRIMARY KEY,
    entity_type VARCHAR(50),
    entity_id VARCHAR(255),
    user_id VARCHAR(255),
    reason VARCHAR(50),
    details TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP
);

-- Bounty Table
CREATE TABLE bounty (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    sponsor_user_id VARCHAR(255),
    amount INT,
    description TEXT,
    started_at TIMESTAMP,
    expires_at TIMESTAMP,
    status VARCHAR(50),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Attachment Table
CREATE TABLE attachment (
    id VARCHAR(255) PRIMARY KEY,
    url VARCHAR(1000),
    caption VARCHAR(255),
    mime_type VARCHAR(255),
    entity_type VARCHAR(50),
    entity_id VARCHAR(255)
);

-- Notification Table
CREATE TABLE notification (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255),
    type VARCHAR(50),
    title VARCHAR(255),
    message TEXT,
    related_entity_type VARCHAR(50),
    related_entity_id VARCHAR(255),
    is_read BOOLEAN,
    created_at TIMESTAMP
);

-- Model Compatibility Table
CREATE TABLE model_compatibility (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    model_id VARCHAR(255),
    status VARCHAR(50),
    tested_at TIMESTAMP,
    test_pass_rate DOUBLE,
    notes TEXT,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Test Case Table
CREATE TABLE test_cases (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    input_values TEXT,
    expected_output TEXT,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Test Run Table
CREATE TABLE test_runs (
    id VARCHAR(255) PRIMARY KEY,
    test_case_id VARCHAR(255),
    model_id VARCHAR(255),
    timestamp TIMESTAMP,
    output TEXT,
    passed BOOLEAN,
    execution_time_ms BIGINT,
    tokens_used INT,
    FOREIGN KEY (test_case_id) REFERENCES test_cases(id)
);


-- Sample Data (Populating ALL Fields for p1)
INSERT INTO prompt (
    id, created_time, last_modified_time, last_modified_by, tenant, created_by, version,
    state_entry_time, sla_yellow_date, sla_red_date, sla_tending_late, sla_late,
    flow_id, state_id, slug, title, description, template, body, system_prompt,
    task_type, validation_score, last_validated_at, semantic_version,
    parent_version_id, changelog, recommended_model, user_id, author_username,
    usage_count, view_count, favorite_count, score, accepted_answer_id,
    answer_count, comment_count, duplicate_of_prompt_id, close_reason,
    closed_at, closed_by_user_id, revision_number, image_url, creator_role
) VALUES (
    'p1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'tle-tenant', 'user1', 1,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 0,
    'prompt-flow', 'VALIDATED', 'java-streams-guide', 'Java Streams Guide', 
    'A comprehensive guide to Java Streams API', 'Hello ${name}', 
    'Detailed content of the guide', 'Act as a Java expert',
    'TEXT_GENERATION', 85, CURRENT_TIMESTAMP, '1.0.0',
    NULL, 'Initial version created', 'gpt-4', 'user1', 'ExpertJavaCoder',
    100, 500, 50, 45, 'a1',
    1, 1, NULL, NULL,
    NULL, NULL, 1, 'http://example.com/java.png', 'SENIOR_DEVELOPER'
);

INSERT INTO prompt (id, slug, title, description, flow_id, state_id, task_type, author_username, validation_score)
VALUES ('p2', 'python-fastapi-tutorial', 'FastAPI Tutorial', 'Learn how to build APIs with Python and FastAPI', 'prompt-flow', 'DRAFT', 'CODE_GENERATION', 'user2', 0);

INSERT INTO prompt_tags (prompt_id, tags) VALUES ('p1', 'java'), ('p1', 'streams'), ('p2', 'python'), ('p2', 'fastapi');

INSERT INTO answer (id, prompt_id, author_username, body, score, is_accepted, created_at, revision_number, user_id)
VALUES ('a1', 'p1', 'expert_coder', 'You can use .map() to transform elements.', 10, TRUE, CURRENT_TIMESTAMP, 1, 'user3');

INSERT INTO comment (id, prompt_id, author, content, created_at)
VALUES ('c1', 'p1', 'user4', 'Very helpful guide!', CURRENT_TIMESTAMP);

INSERT INTO prompt_revision (id, prompt_id, title, change_comment, revision_number, user_id, body, created_at)
VALUES ('r1', 'p1', 'Java Streams Guide', 'Initial revision', 1, 'user1', 'Initial body text', CURRENT_TIMESTAMP);

INSERT INTO variable (id, prompt_id, name, type, description, required, default_value, example)
VALUES ('v1', 'p1', 'collection_name', 'STRING', 'Name of the collection to stream', TRUE, 'list', '["a", "b"]');

INSERT INTO prompt_activity (id, prompt_id, activity_name, activity_success, activity_comment, created_time)
VALUES ('act1', 'p1', 'PUBLISH', TRUE, 'Published by reviewer', CURRENT_TIMESTAMP);

INSERT INTO favorite (id, user_id, prompt_id, added_at, notes)
VALUES ('f1', 'user5', 'p1', CURRENT_TIMESTAMP, 'Useful for interview prep');

INSERT INTO flag (id, entity_type, entity_id, user_id, reason, details, status, created_at)
VALUES ('fl1', 'PROMPT', 'p2', 'user6', 'PLAGIARISM', 'Copied from blog', 'PENDING', CURRENT_TIMESTAMP);

INSERT INTO bounty (id, prompt_id, sponsor_user_id, amount, description, started_at, status)
VALUES ('b1', 'p1', 'user1', 500, 'Best explanation of parallel streams', CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO attachment (id, url, caption, mime_type, entity_type, entity_id)
VALUES ('att1', 'http://example.com/diagram.png', 'Architecture diagram', 'image/png', 'PROMPT', 'p1');

INSERT INTO notification (id, user_id, type, title, message, is_read, created_at, related_entity_type, related_entity_id)
VALUES ('n1', 'user1', 'SYSTEM', 'Welcome', 'Welcome to the platform!', FALSE, CURRENT_TIMESTAMP, 'SYSTEM', 'sys1');