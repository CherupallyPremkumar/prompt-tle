-- Users
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- User fields
    email VARCHAR(255) NOT NULL UNIQUE,
    google_sub VARCHAR(255) UNIQUE,
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255),
    password_hash VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    full_name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    bio TEXT,
    picture_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    last_login TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_google_sub ON users(google_sub);
CREATE INDEX IF NOT EXISTS idx_provider_id ON users(provider_id);

-- Roles
CREATE TABLE IF NOT EXISTS roles (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Role fields
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- ACLs
CREATE TABLE IF NOT EXISTS acls (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Acl fields
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_acl_name ON acls(name);

-- User Roles Join Table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(255) NOT NULL,
    role_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Role ACLs Join Table
CREATE TABLE IF NOT EXISTS role_acls (
    role_id VARCHAR(255) NOT NULL,
    acl_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_id, acl_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (acl_id) REFERENCES acls(id)
);

-- Uploads (Does NOT extend BaseJpaEntity)
CREATE TABLE IF NOT EXISTS uploads (
    upload_id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    file_key VARCHAR(255) NOT NULL,
    content_type VARCHAR(255),
    file_size BIGINT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    expires_at TIMESTAMP,
    download_url VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Prompt (Extends AbstractJpaStateEntity -> BaseJpaEntity)
CREATE TABLE IF NOT EXISTS prompt (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- AbstractJpaStateEntity fields
    state_entry_time TIMESTAMP,
    sla_yellow_date TIMESTAMP,
    sla_red_date TIMESTAMP,
    sla_tending_late INT,
    sla_late INT,
    flowId VARCHAR(255), 
    stateId VARCHAR(255),
    -- Prompt fields
    slug VARCHAR(255) UNIQUE,
    is_featured BOOLEAN DEFAULT FALSE,
    forked_from_prompt_id VARCHAR(255),
    title VARCHAR(255),
    description TEXT,
    template TEXT,
    body TEXT,
    system_prompt TEXT,
    task_type VARCHAR(50),
    validation_score INT DEFAULT 0,
    last_validated_at TIMESTAMP,
    semantic_version VARCHAR(255),
    parent_version_id VARCHAR(255),
    changelog VARCHAR(255),
    recommended_model VARCHAR(255),
    user_id VARCHAR(255),
    author_username VARCHAR(255),
    usage_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    favorite_count INT DEFAULT 0,
    score INT DEFAULT 0,
    accepted_answer_id VARCHAR(255),
    duplicate_of_prompt_id VARCHAR(255),
    close_reason VARCHAR(255),
    closed_at TIMESTAMP,
    closed_by_user_id VARCHAR(255),
    revision_number INT DEFAULT 1,
    image_url VARCHAR(255),
    creator_role VARCHAR(255),
    answer_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Prompt Tags
CREATE TABLE IF NOT EXISTS prompt_tags (
    prompt_id VARCHAR(255) NOT NULL,
    tags VARCHAR(255),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Answer
CREATE TABLE IF NOT EXISTS answer (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Answer fields
    prompt_id VARCHAR(255),
    user_id VARCHAR(255),
    author_username VARCHAR(255),
    body TEXT,
    score INT DEFAULT 0,
    is_accepted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    comment_count INT DEFAULT 0,
    revision_number INT DEFAULT 1,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Attachment
CREATE TABLE IF NOT EXISTS attachment (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Attachment fields
    url VARCHAR(1000),
    caption VARCHAR(255),
    mime_type VARCHAR(255),
    entity_type VARCHAR(50),
    entity_id VARCHAR(255),
    prompt_id VARCHAR(255),
    answer_id VARCHAR(255),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id),
    FOREIGN KEY (answer_id) REFERENCES answer(id)
);

-- Badge
CREATE TABLE IF NOT EXISTS badge (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Badge fields
    name VARCHAR(255),
    description VARCHAR(255),
    type VARCHAR(50),
    icon_url VARCHAR(255),
    criteria VARCHAR(255)
);

-- Bounty
CREATE TABLE IF NOT EXISTS bounty (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Bounty fields
    prompt_id VARCHAR(255),
    sponsor_user_id VARCHAR(255),
    amount INT,
    description TEXT,
    started_at TIMESTAMP,
    expires_at TIMESTAMP,
    awarded_to_answer_id VARCHAR(255),
    awarded_to_user_id VARCHAR(255),
    awarded_at TIMESTAMP,
    status VARCHAR(50),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id),
    FOREIGN KEY (sponsor_user_id) REFERENCES users(id),
    FOREIGN KEY (awarded_to_answer_id) REFERENCES answer(id),
    FOREIGN KEY (awarded_to_user_id) REFERENCES users(id)
);

-- Category
CREATE TABLE IF NOT EXISTS category (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Category fields
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,
    icon VARCHAR(255),
    color VARCHAR(255),
    description TEXT
);

-- Comment
CREATE TABLE IF NOT EXISTS comment (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Comment fields
    prompt_id VARCHAR(255),
    author VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Favorite
CREATE TABLE IF NOT EXISTS favorite (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Favorite fields
    user_id VARCHAR(255),
    prompt_id VARCHAR(255),
    added_at TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Favorite Custom Tags
CREATE TABLE IF NOT EXISTS favorite_custom_tags (
    favorite_id VARCHAR(255) NOT NULL,
    custom_tag VARCHAR(255),
    FOREIGN KEY (favorite_id) REFERENCES favorite(id)
);

-- Flag
CREATE TABLE IF NOT EXISTS flag (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Flag fields
    entity_type VARCHAR(50),
    entity_id VARCHAR(255),
    user_id VARCHAR(255),
    reason VARCHAR(50),
    details TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP,
    resolved_at TIMESTAMP,
    resolved_by_user_id VARCHAR(255),
    resolution VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (resolved_by_user_id) REFERENCES users(id)
);

-- Google Accounts
CREATE TABLE IF NOT EXISTS google_accounts (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- GoogleAccount fields
    google_user_id VARCHAR(255) NOT NULL UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    picture VARCHAR(255),
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    access_token_expires_at TIMESTAMP,
    connected_at TIMESTAMP NOT NULL,
    disconnected_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Notification
CREATE TABLE IF NOT EXISTS notification (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Notification fields
    user_id VARCHAR(255),
    type VARCHAR(50),
    title VARCHAR(255),
    message TEXT,
    related_entity_type VARCHAR(50),
    related_entity_id VARCHAR(255),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Notification Data
CREATE TABLE IF NOT EXISTS notification_data (
    notification_id VARCHAR(255) NOT NULL,
    data_key VARCHAR(255),
    data_value VARCHAR(255),
    FOREIGN KEY (notification_id) REFERENCES notification(id)
);

-- Prompt Activity Log
CREATE TABLE IF NOT EXISTS prompt_activity (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- PromptActivityLog fields
    activity_name VARCHAR(255),
    activity_success BOOLEAN,
    activity_comment VARCHAR(255),
    prompt_id VARCHAR(255),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Prompt Revision
CREATE TABLE IF NOT EXISTS prompt_revision (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- PromptRevision fields
    prompt_id VARCHAR(255),
    revision_number INT,
    user_id VARCHAR(255),
    title VARCHAR(255),
    body TEXT,
    change_comment VARCHAR(255),
    created_at TIMESTAMP,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Prompt Revision Tags
CREATE TABLE IF NOT EXISTS prompt_revision_tags (
    prompt_revision_id VARCHAR(255) NOT NULL,
    tags VARCHAR(255),
    FOREIGN KEY (prompt_revision_id) REFERENCES prompt_revision(id)
);

-- Prompt Vote
CREATE TABLE IF NOT EXISTS prompt_vote (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- PromptVote fields
    user_id VARCHAR(255) NOT NULL,
    prompt_id VARCHAR(255) NOT NULL,
    vote_type VARCHAR(50) NOT NULL,
    UNIQUE(user_id, prompt_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Refresh Tokens
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- RefreshToken fields
    user_id VARCHAR(255) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Sessions
CREATE TABLE IF NOT EXISTS sessions (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Session fields
    user_id VARCHAR(255) NOT NULL,
    ip_address VARCHAR(255) NOT NULL,
    user_agent VARCHAR(255),
    device_type VARCHAR(255),
    location VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    last_activity_at TIMESTAMP,
    expires_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tag
CREATE TABLE IF NOT EXISTS tag (
    id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- Tag fields
    name VARCHAR(255) UNIQUE,
    description VARCHAR(255),
    usage_count INT DEFAULT 0,
    category VARCHAR(255),
    is_required BOOLEAN DEFAULT FALSE
);

-- Test Cases (No inheritance)
CREATE TABLE IF NOT EXISTS test_cases (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    input_values TEXT,
    expected_output TEXT,
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);

-- Test Runs (No inheritance)
CREATE TABLE IF NOT EXISTS test_runs (
    id VARCHAR(255) PRIMARY KEY,
    test_case_id VARCHAR(255),
    model_id VARCHAR(255),
    timestamp TIMESTAMP,
    output TEXT,
    passed BOOLEAN,
    execution_time_ms INT,
    tokens_used INT,
    FOREIGN KEY (test_case_id) REFERENCES test_cases(id)
);

-- User Reputation
CREATE TABLE IF NOT EXISTS user_reputation (
    user_id VARCHAR(255) PRIMARY KEY,
    -- BaseJpaEntity fields
    created_time TIMESTAMP,
    last_modified_time TIMESTAMP,
    last_modified_by VARCHAR(255),
    created_by VARCHAR(255),
    tenant VARCHAR(255),
    version BIGINT,
    -- UserReputation fields
    total_reputation INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- User Tag Reputation
CREATE TABLE IF NOT EXISTS user_tag_reputation (
    user_id VARCHAR(255) NOT NULL,
    tag_name VARCHAR(255),
    points INT,
    FOREIGN KEY (user_id) REFERENCES user_reputation(user_id)
);

-- User Badge Join
CREATE TABLE IF NOT EXISTS user_badge (
    user_id VARCHAR(255) NOT NULL,
    badge_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, badge_id),
    FOREIGN KEY (user_id) REFERENCES user_reputation(user_id),
    FOREIGN KEY (badge_id) REFERENCES badge(id)
);

-- Variable (No inheritance)
CREATE TABLE IF NOT EXISTS variable (
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

-- Model Compatibility (No inheritance)
CREATE TABLE IF NOT EXISTS model_compatibility (
    id VARCHAR(255) PRIMARY KEY,
    prompt_id VARCHAR(255),
    model_id VARCHAR(255),
    status VARCHAR(50),
    tested_at TIMESTAMP,
    test_pass_rate DOUBLE,
    notes VARCHAR(255),
    FOREIGN KEY (prompt_id) REFERENCES prompt(id)
);
