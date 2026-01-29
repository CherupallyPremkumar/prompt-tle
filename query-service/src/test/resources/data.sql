-- Defaults
-- Tenant: 'Handmade'

-- Roles
INSERT INTO roles (id, name, description, created_time, version, tenant) VALUES 
('role-user', 'ROLE_USER', 'Standard user with access to basic features', CURRENT_TIMESTAMP, 1, 'Handmade'),
('role-moderator', 'ROLE_MODERATOR', 'Moderator with powers to flag and review content', CURRENT_TIMESTAMP, 1, 'Handmade'),
('role-admin', 'ROLE_ADMIN', 'Administrator with full system access', CURRENT_TIMESTAMP, 1, 'Handmade'),
('role-google', 'ROLE_GOOGLE', 'User authenticated via Google', CURRENT_TIMESTAMP, 1, 'Handmade')
;

-- ACLs
INSERT INTO acls (id, name, description, created_time, version, tenant) VALUES
('acl-read-prompt', 'READ_PROMPT', 'Can view prompts', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-write-prompt', 'WRITE_PROMPT', 'Can create and edit prompts', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-delete-prompt', 'DELETE_PROMPT', 'Can delete prompts', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-write-comment', 'WRITE_COMMENT', 'Can post comments', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-moderate-content', 'MODERATE_CONTENT', 'Can moderate user content', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-admin-access', 'ADMIN_ACCESS', 'Full administrative access', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-view-analytics', 'VIEW_ANALYTICS', 'Can view system analytics', CURRENT_TIMESTAMP, 1, 'Handmade')
;

-- Users (Bootstrap & Sample)
INSERT INTO users (id, email, google_sub, provider, provider_id, password_hash, status, full_name, username, bio, is_active, email_verified, created_at, created_time, version, tenant) VALUES
('user-system', 'system@handmade.com', NULL, 'LOCAL', 'system', NULL, 'ACTIVE', 'System Administrator', 'system', 'Maintains the platform.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-admin-default', 'admin@handmade.com', NULL, 'LOCAL', 'admin', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Default Admin', 'admin', 'Default Administrator Account.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-alice', 'alice@example.com', NULL, 'LOCAL', 'alice', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Alice Developer', 'alice_dev', 'Java & Spring enthusiast.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-bob', 'bob@example.com', NULL, 'LOCAL', 'bob', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Bob Builder', 'bob_builds', 'Frontend react wizard.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-charlie', 'charlie@example.com', NULL, 'LOCAL', 'charlie', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Charlie Data', 'charlie_data', 'Python and AI researcher.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade')
;

-- Role ACL Mappings
-- Admin gets everything
INSERT INTO role_acls (role_id, acl_id) VALUES 
('role-admin', 'acl-read-prompt'),
('role-admin', 'acl-write-prompt'),
('role-admin', 'acl-delete-prompt'),
('role-admin', 'acl-write-comment'),
('role-admin', 'acl-moderate-content'),
('role-admin', 'acl-admin-access'),
('role-admin', 'acl-view-analytics')
;

-- Moderator
INSERT INTO role_acls (role_id, acl_id) VALUES 
('role-moderator', 'acl-read-prompt'),
('role-moderator', 'acl-write-prompt'),
('role-moderator', 'acl-write-comment'),
('role-moderator', 'acl-moderate-content')
;

-- User
INSERT INTO role_acls (role_id, acl_id) VALUES 
('role-user', 'acl-read-prompt'),
('role-user', 'acl-write-prompt'),
('role-user', 'acl-write-comment')
;

-- User Role Assignments
INSERT INTO user_roles (user_id, role_id) VALUES
('user-admin-default', 'role-admin'),
('user-alice', 'role-user'),
('user-bob', 'role-user'),
('user-charlie', 'role-user')
;

-- Categories
INSERT INTO category (id, name, slug, icon, color, description, created_time, version, tenant) VALUES
('cat-code-gen', 'Code Generation', 'code-generation', '💻', '#3B82F6', 'Prompts for generating code snippets and functions', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-debugging', 'Debugging', 'debugging', '🐛', '#EF4444', 'Prompts for finding and fixing bugs', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-refactoring', 'Refactoring', 'refactoring', '♻️', '#10B981', 'Prompts for cleaning up and optimizing code', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-analysis', 'Data Analysis', 'analysis', '📊', '#8B5CF6', 'Prompts for analyzing data and statistics', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-docs', 'Documentation', 'documentation', '📝', '#F59E0B', 'Prompts for writing documentation and comments', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-testing', 'Testing', 'testing', '🧪', '#EC4899', 'Prompts for generating unit and integration tests', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-learning', 'Learning & Education', 'learning', '�', '#14B8A6', 'Prompts for learning new concepts', CURRENT_TIMESTAMP, 1, 'Handmade')
;

-- Tags
INSERT INTO tag (id, name, description, usage_count, category, is_required, created_time, version, tenant) VALUES
('tag-python', 'python', 'Python programming language', 5, 'code-generation', TRUE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-java', 'java', 'Java programming language', 3, 'code-generation', TRUE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-javascript', 'javascript', 'JavaScript programming language', 2, 'code-generation', TRUE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-react', 'react', 'React frontend library', 1, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-spring-boot', 'spring-boot', 'Spring Boot framework', 1, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-sql', 'sql', 'Structured Query Language', 0, 'analysis', TRUE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-regex', 'regex', 'Regular Expressions', 0, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-beginners', 'beginners', 'Good for beginners', 0, 'learning', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade')
;

-- Badges
INSERT INTO badge (id, name, description, type, icon_url, criteria, created_time, version, tenant) VALUES
('badge-first-prompt', 'Prompt Pioneer', 'Created your first prompt', 'BRONZE', '/icons/badges/pioneer.png', 'create_prompt_count >= 1', CURRENT_TIMESTAMP, 1, 'Handmade'),
('badge-prolific-prompter', 'Prolific Prompter', 'Created 10 or more prompts', 'SILVER', '/icons/badges/prolific.png', 'create_prompt_count >= 10', CURRENT_TIMESTAMP, 1, 'Handmade'),
('badge-guru', 'Guru', 'Answered 100 prompts', 'GOLD', '/icons/badges/guru.png', 'answer_count >= 100', CURRENT_TIMESTAMP, 1, 'Handmade')
;

-- Prompts
-- Prompt 1: Java Stream Filter (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number,
                    user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count,
                    flow_id, state_id, state_entry_time, created_by, created_time, version, tenant) 
VALUES
('prompt-1', 'java-filter-list', TRUE, 'How to filter a list in Java streams?', 'Best practices for filtering lists.', 
 'I need a prompt that generates a Java method to filter a List<String> based on a predicate.', 
 'code_generation', 85, '1.0.0', 1,
 'user-alice', 'alice_dev', 120, 500, 15, 2, 1,
 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP, 'user-alice', CURRENT_TIMESTAMP, 1, 'Handmade');

-- Prompt 2: React Component (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number,
                    user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id,
                    flow_id, state_id, state_entry_time, created_by, created_time, version, tenant) 
VALUES
('prompt-2', 'react-functional-component', FALSE, 'Generate React Functional Component', 'Create a standard functional component.',
 'Write a prompt to generate a React functional component with TypeScript props and useEffect.',
 'code_generation', 92, '1.0.0', 1,
 'user-bob', 'bob_builds', 45, 120, 5, 1, 1, 'answer-1',
 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP, 'user-bob', CURRENT_TIMESTAMP, 1, 'Handmade');

-- Prompt 3: Python Data Analysis (VALIDATED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number,
                    user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count,
                    flow_id, state_id, state_entry_time, created_by, created_time, version, tenant) 
VALUES
('prompt-3', 'python-pandas-agg', FALSE, 'Pandas Aggregation Prompt', 'Aggregating data frames.',
 'Create a prompt that explains how to use groupby and agg in Pandas.',
 'analysis', 98, '1.0.0', 1,
 'user-charlie', 'charlie_data', 0, 10, 0, 0, 0,
 'prompt-flow', 'VALIDATED', CURRENT_TIMESTAMP, 'user-charlie', CURRENT_TIMESTAMP, 1, 'Handmade');

-- Prompt Tags Mappings
INSERT INTO prompt_tags (prompt_id, tags) VALUES
('prompt-1', 'java'),
('prompt-1', 'beginners'),
('prompt-2', 'react'),
('prompt-2', 'javascript'),
('prompt-3', 'python');

-- Comments
INSERT INTO comment (id, prompt_id, author, content, created_at, created_time, version, tenant) VALUES
('comment-1', 'prompt-1', 'bob_builds', 'Make sure to handle nulls!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('comment-2', 'prompt-1', 'alice_dev', 'Good point, thanks Bob.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('comment-3', 'prompt-2', 'charlie_data', 'Do we need React.memo?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade');

-- Answers
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES
('answer-1', 'prompt-2', 'user-alice', 'alice_dev', 'Here is a prompt: "Create a React functional component named UserProfile that takes name and age as props..."', 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade');

-- Votes (Likes)
INSERT INTO prompt_vote (id, user_id, prompt_id, vote_type, created_time, version, tenant) VALUES
('vote-1', 'user-bob', 'prompt-1', 'UPVOTE', CURRENT_TIMESTAMP, 1, 'Handmade'),
('vote-2', 'user-charlie', 'prompt-1', 'UPVOTE', CURRENT_TIMESTAMP, 1, 'Handmade'),
('vote-3', 'user-alice', 'prompt-2', 'UPVOTE', CURRENT_TIMESTAMP, 1, 'Handmade');

-- User Reputation
INSERT INTO user_reputation (user_id, total_reputation, created_time, version, tenant) VALUES
('user-alice', 150, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-bob', 120, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-charlie', 50, CURRENT_TIMESTAMP, 1, 'Handmade');

-- Flag (Content Moderation Sample)
INSERT INTO flag (id, entity_type, entity_id, user_id, reason, details, status, created_at, created_time, version, tenant) VALUES
('flag-1', 'PROMPT', 'prompt-3', 'user-bob', 'NEEDS_IMPROVEMENT', 'Description is too vague.', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade');
