-- Defaults
-- Tenant: 'Handmade'

-- Roles
INSERT INTO roles (id, name, description, created_time, version, tenant) VALUES 
('role-user', 'ROLE_USER', 'Standard user with access to basic features', CURRENT_TIMESTAMP, 1, 'Handmade'),
('role-moderator', 'ROLE_MODERATOR', 'Moderator with powers to flag and review content', CURRENT_TIMESTAMP, 1, 'Handmade'),
('role-admin', 'ROLE_ADMIN', 'Administrator with full system access', CURRENT_TIMESTAMP, 1, 'Handmade'),
('role-google', 'ROLE_GOOGLE', 'User authenticated via Google', CURRENT_TIMESTAMP, 1, 'Handmade');


-- ACLs
INSERT INTO acls (id, name, description, created_time, version, tenant) VALUES
('acl-read-prompt', 'READ_PROMPT', 'Can view prompts', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-write-prompt', 'WRITE_PROMPT', 'Can create and edit prompts', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-delete-prompt', 'DELETE_PROMPT', 'Can delete prompts', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-write-comment', 'WRITE_COMMENT', 'Can post comments', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-moderate-content', 'MODERATE_CONTENT', 'Can moderate user content', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-admin-access', 'ADMIN_ACCESS', 'Full administrative access', CURRENT_TIMESTAMP, 1, 'Handmade'),
('acl-view-analytics', 'VIEW_ANALYTICS', 'Can view system analytics', CURRENT_TIMESTAMP, 1, 'Handmade')

-- Users (Bootstrap & Sample)
INSERT INTO users (id, email, google_sub, provider, provider_id, password_hash, status, full_name, username, bio, is_active, email_verified, created_at, created_time, version, tenant) VALUES
('user-system', 'system@handmade.com', NULL, 'LOCAL', 'system', NULL, 'ACTIVE', 'System Administrator', 'system', 'Maintains the platform.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-admin-default', 'admin@handmade.com', NULL, 'LOCAL', 'admin', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Default Admin', 'admin', 'Default Administrator Account.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-alice', 'alice@example.com', NULL, 'LOCAL', 'alice', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Alice Developer', 'alice_dev', 'Java & Spring enthusiast.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-bob', 'bob@example.com', NULL, 'LOCAL', 'bob', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Bob Builder', 'bob_builds', 'Frontend react wizard.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-charlie', 'charlie@example.com', NULL, 'LOCAL', 'charlie', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Charlie Data', 'charlie_data', 'Python and AI researcher.', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Handmade')

-- Role ACL Mappings
-- Admin gets everything
INSERT INTO role_acls (role_id, acl_id) VALUES 
('role-admin', 'acl-read-prompt'),
('role-admin', 'acl-write-prompt'),
('role-admin', 'acl-delete-prompt'),
('role-admin', 'acl-write-comment'),
('role-admin', 'acl-moderate-content'),
('role-admin', 'acl-admin-access'),
('role-admin', 'acl-view-analytics');


-- Moderator
INSERT INTO role_acls (role_id, acl_id) VALUES 
('role-moderator', 'acl-read-prompt'),
('role-moderator', 'acl-write-prompt'),
('role-moderator', 'acl-write-comment'),
('role-moderator', 'acl-moderate-content');


-- User
INSERT INTO role_acls (role_id, acl_id) VALUES 
('role-user', 'acl-read-prompt'),
('role-user', 'acl-write-prompt'),
('role-user', 'acl-write-comment');

-- Google User
INSERT INTO role_acls (role_id, acl_id) VALUES 
('role-google', 'acl-read-prompt'),
('role-google', 'acl-write-prompt'),
('role-google', 'acl-write-comment');

-- User Role Assignments
INSERT INTO user_roles (user_id, role_id) VALUES
('user-admin-default', 'role-admin'),
('user-alice', 'role-user'),
('user-bob', 'role-user'),
('user-charlie', 'role-user');

-- Categories
INSERT INTO category (id, name, slug, icon, color, description, created_time, version, tenant) VALUES
('cat-code-gen', 'Code Generation', 'code-generation', '💻', '#3B82F6', 'Prompts for generating code snippets and functions', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-debugging', 'Debugging', 'debugging', '🐛', '#EF4444', 'Prompts for finding and fixing bugs', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-refactoring', 'Refactoring', 'refactoring', '♻️', '#10B981', 'Prompts for cleaning up and optimizing code', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-analysis', 'Data Analysis', 'analysis', '📊', '#8B5CF6', 'Prompts for analyzing data and statistics', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-docs', 'Documentation', 'documentation', '📝', '#F59E0B', 'Prompts for writing documentation and comments', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-testing', 'Testing', 'testing', '🧪', '#EC4899', 'Prompts for generating unit and integration tests', CURRENT_TIMESTAMP, 1, 'Handmade'),
('cat-learning', 'Learning & Education', 'learning', '�', '#14B8A6', 'Prompts for learning new concepts', CURRENT_TIMESTAMP, 1, 'Handmade')

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

-- Badges
INSERT INTO badge (id, name, description, type, icon_url, criteria, created_time, version, tenant) VALUES
('badge-first-prompt', 'Prompt Pioneer', 'Created your first prompt', 'BRONZE', '/icons/badges/pioneer.png', 'create_prompt_count >= 1', CURRENT_TIMESTAMP, 1, 'Handmade'),
('badge-prolific-prompter', 'Prolific Prompter', 'Created 10 or more prompts', 'SILVER', '/icons/badges/prolific.png', 'create_prompt_count >= 10', CURRENT_TIMESTAMP, 1, 'Handmade'),
('badge-guru', 'Guru', 'Answered 100 prompts', 'GOLD', '/icons/badges/guru.png', 'answer_count >= 100', CURRENT_TIMESTAMP, 1, 'Handmade')


-- Prompts
-- Prompt 1: Java Stream Filter (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number,
                    user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count,
                    flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) 
VALUES
('prompt-1', 'java-filter-list', TRUE, 'How to filter a list in Java streams?', 'Best practices for filtering lists.', 
 'I need a prompt that generates a Java method to filter a List<String> based on a predicate.', 
 'code_generation', 85, '1.0.0', 1,
 'user-alice', 'alice_dev', 120, 500, 15, 2, 1,
 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP, 'user-alice', CURRENT_TIMESTAMP, 1, 'Handmade', 0, 0, 0);

-- Prompt 2: React Component (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number,
                    user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id,
                    flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) 
VALUES
('prompt-2', 'react-functional-component', FALSE, 'Generate React Functional Component', 'Create a standard functional component.',
 'Write a prompt to generate a React functional component with TypeScript props and useEffect.',
 'code_generation', 92, '1.0.0', 1,
 'user-bob', 'bob_builds', 45, 120, 5, 1, 1, NULL,
 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP, 'user-bob', CURRENT_TIMESTAMP, 1, 'Handmade', 0, 0, 0);

-- Prompt 3: Python Data Analysis (VALIDATED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number,
                    user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count,
                    flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) 
VALUES
('prompt-3', 'python-pandas-agg', FALSE, 'Pandas Aggregation Prompt', 'Aggregating data frames.',
 'Create a prompt that explains how to use groupby and agg in Pandas.',
 'analysis', 98, '1.0.0', 1,
 'user-charlie', 'charlie_data', 0, 10, 0, 0, 0,
 'prompt-flow', 'VALIDATED', CURRENT_TIMESTAMP, 'user-charlie', CURRENT_TIMESTAMP, 1, 'Handmade', 0, 0, 0);

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

UPDATE prompt SET accepted_answer_id = 'answer-1' WHERE id = 'prompt-2';

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


-- =====================================================
-- PRODUCTION DATA FOR HANDMADE PLATFORM
-- 20+ Realistic Prompts with Full Ecosystem
-- =====================================================

-- Additional Users (Diverse Community)
INSERT INTO users (id, email, google_sub, provider, provider_id, password_hash, status, full_name, username, bio, is_active, email_verified, created_at, created_time, version, tenant) VALUES 
('user-diana', 'diana@example.com', NULL, 'LOCAL', 'diana', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Diana Chen', 'diana_ml', 'ML engineer specializing in NLP and transformers.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '90 days', CURRENT_TIMESTAMP - INTERVAL '90 days', 1, 'Handmade'),
('user-ethan', 'ethan@example.com', NULL, 'LOCAL', 'ethan', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Ethan Rodriguez', 'ethan_fullstack', 'Full-stack developer, Node.js & PostgreSQL expert.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '120 days', CURRENT_TIMESTAMP - INTERVAL '120 days', 1, 'Handmade'),
('user-fiona', 'fiona@example.com', NULL, 'LOCAL', 'fiona', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Fiona Kumar', 'fiona_devops', 'DevOps engineer, Kubernetes and CI/CD enthusiast.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP - INTERVAL '60 days', 1, 'Handmade'),
('user-george', 'george@example.com', NULL, 'LOCAL', 'george', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'George Thompson', 'george_data', 'Data scientist with focus on time series analysis.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP - INTERVAL '45 days', 1, 'Handmade'),
('user-hannah', 'hannah@example.com', NULL, 'LOCAL', 'hannah', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Hannah Lee', 'hannah_mobile', 'Mobile developer, React Native and Flutter.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '30 days', 1, 'Handmade'),
('user-ivan', 'ivan@example.com', NULL, 'LOCAL', 'ivan', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Ivan Petrov', 'ivan_backend', 'Backend architect, microservices expert.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '75 days', CURRENT_TIMESTAMP - INTERVAL '75 days', 1, 'Handmade'),
('user-julia', 'julia@example.com', NULL, 'LOCAL', 'julia', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Julia Martinez', 'julia_qa', 'QA engineer, test automation specialist.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP - INTERVAL '50 days', 1, 'Handmade'),
('user-kevin', 'kevin@example.com', NULL, 'LOCAL', 'kevin', '$2a$10$NotARealHashJustPlaceholder', 'ACTIVE', 'Kevin O''Brien', 'kevin_security', 'Security researcher and ethical hacker.', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days', CURRENT_TIMESTAMP - INTERVAL '100 days', 1, 'Handmade')
ON CONFLICT (id) DO NOTHING;

-- Assign roles to new users
INSERT INTO user_roles (user_id, role_id) VALUES 
('user-diana', 'role-user'),
('user-ethan', 'role-user'),
('user-fiona', 'role-moderator'),
('user-george', 'role-user'),
('user-hannah', 'role-user'),
('user-ivan', 'role-user'),
('user-julia', 'role-moderator'),
('user-kevin', 'role-user')
ON CONFLICT (user_id, role_id) DO NOTHING;
-- Additional Tags
INSERT INTO tag (id, name, description, usage_count, category, is_required, created_time, version, tenant) VALUES 
('tag-typescript', 'typescript', 'TypeScript programming language', 8, 'code-generation', TRUE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-nodejs', 'nodejs', 'Node.js runtime environment', 6, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-docker', 'docker', 'Docker containerization', 4, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-kubernetes', 'kubernetes', 'Kubernetes orchestration', 2, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-mongodb', 'mongodb', 'MongoDB database', 3, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-postgresql', 'postgresql', 'PostgreSQL database', 5, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-graphql', 'graphql', 'GraphQL API', 2, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-rest-api', 'rest-api', 'RESTful API design', 7, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-aws', 'aws', 'Amazon Web Services', 3, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-machine-learning', 'machine-learning', 'Machine Learning', 4, 'analysis', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-data-viz', 'data-viz', 'Data Visualization', 2, 'analysis', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-async', 'async', 'Asynchronous programming', 5, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-testing', 'testing', 'Unit and Integration Testing', 6, 'testing', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-security', 'security', 'Security best practices', 3, 'code-generation', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade'),
('tag-performance', 'performance', 'Performance optimization', 4, 'refactoring', FALSE, CURRENT_TIMESTAMP, 1, 'Handmade')
ON CONFLICT (id) DO NOTHING;


-- =====================================================
-- 20+ PRODUCTION-READY PROMPTS
-- =====================================================

-- Prompt 4: JWT Authentication (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-4', 'jwt-auth-nodejs', TRUE, 'Implement JWT Authentication in Node.js', 'Secure REST API with JWT tokens', 'I need a comprehensive prompt that generates a Node.js Express middleware for JWT authentication including token generation, validation, and refresh token logic. Should include error handling and security best practices.', 'code_generation', 88, '1.0.0', 1, 'user-ethan', 'ethan_fullstack', 245, 1203, 42, 8, 3, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '5 days', 'user-ethan', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 5: SQL Query Optimization (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-5', 'optimize-postgres-query', FALSE, 'Optimize Slow PostgreSQL Query', 'Performance tuning for complex joins', 'Create a prompt that analyzes a slow PostgreSQL query with multiple joins and subqueries, and suggests optimizations including index strategies, query rewriting, and EXPLAIN ANALYZE interpretation.', 'refactoring', 94, '1.0.0', 1, 'user-ethan', 'ethan_fullstack', 189, 876, 31, 5, 2, 'answer-5', 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP - INTERVAL '12 days', 'user-ethan', CURRENT_TIMESTAMP - INTERVAL '12 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 6: React Custom Hooks (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-6', 'react-custom-hooks-api', TRUE, 'Build Custom React Hook for API Calls', 'Reusable data fetching hook with TypeScript', 'Need a prompt to generate a custom React hook (useApi) that handles loading states, error handling, caching, and automatic retries. Should be TypeScript-first with proper generic types.', 'code_generation', 91, '1.0.0', 1, 'user-bob', 'bob_builds', 312, 1544, 56, 12, 4, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '3 days', 'user-bob', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 7: Docker Multi-Stage Build (VALIDATED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-7', 'docker-multistage-nodejs', FALSE, 'Docker Multi-Stage Build for Node.js', 'Optimized production Docker image', 'Generate a Dockerfile using multi-stage builds for a Node.js application. Should minimize image size, separate dev dependencies, and include security scanning. Target final image under 150MB.', 'code_generation', 96, '1.0.0', 1, 'user-fiona', 'fiona_devops', 98, 512, 18, 2, 1, 'prompt-flow', 'VALIDATED', CURRENT_TIMESTAMP - INTERVAL '8 days', 'user-fiona', CURRENT_TIMESTAMP - INTERVAL '8 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 8: Python Async Web Scraper (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-8', 'python-async-scraper', FALSE, 'Asynchronous Web Scraper with aiohttp', 'Fast concurrent web scraping', 'Create a prompt for building an async web scraper using aiohttp and BeautifulSoup. Should handle rate limiting, retries, concurrent requests (10+), and proxy rotation. Include progress tracking.', 'code_generation', 87, '1.0.0', 1, 'user-charlie', 'charlie_data', 156, 723, 28, 6, 2, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '7 days', 'user-charlie', CURRENT_TIMESTAMP - INTERVAL '7 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 9: Machine Learning Model Deployment (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-9', 'deploy-ml-model-fastapi', TRUE, 'Deploy ML Model with FastAPI', 'Production-ready model serving', 'Generate code to deploy a scikit-learn or PyTorch model using FastAPI. Include input validation with Pydantic, batch prediction endpoint, health checks, and Docker containerization.', 'code_generation', 93, '1.0.0', 1, 'user-diana', 'diana_ml', 278, 1432, 67, 15, 5, 'answer-9', 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP - INTERVAL '10 days', 'user-diana', CURRENT_TIMESTAMP - INTERVAL '10 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 10: GraphQL Schema Design (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-10', 'graphql-schema-ecommerce', FALSE, 'Design GraphQL Schema for E-commerce', 'Complete schema with relationships', 'Need a prompt to generate a comprehensive GraphQL schema for an e-commerce platform including Users, Products, Orders, Reviews. Should include proper relationships, pagination, filtering, and mutations.', 'code_generation', 89, '1.0.0', 1, 'user-ivan', 'ivan_backend', 134, 689, 22, 4, 1, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '4 days', 'user-ivan', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 11: Jest Unit Tests (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-11', 'jest-unit-tests-react', FALSE, 'Comprehensive Jest Tests for React Components', 'Testing with React Testing Library', 'Generate a prompt that creates thorough unit tests for React components using Jest and React Testing Library. Include testing hooks, async operations, user interactions, and accessibility checks.', 'testing', 92, '1.0.0', 1, 'user-julia', 'julia_qa', 201, 945, 34, 7, 3, 'answer-11', 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP - INTERVAL '15 days', 'user-julia', CURRENT_TIMESTAMP - INTERVAL '15 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 12: Kubernetes Deployment (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-12', 'k8s-deployment-yaml', TRUE, 'Kubernetes Deployment YAML with Best Practices', 'Production-grade K8s manifests', 'Create a prompt for generating Kubernetes deployment manifests including Deployment, Service, ConfigMap, Secret, HPA, and resource limits. Should follow security best practices and include health probes.', 'code_generation', 95, '1.0.0', 1, 'user-fiona', 'fiona_devops', 167, 834, 39, 9, 2, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '6 days', 'user-fiona', CURRENT_TIMESTAMP - INTERVAL '6 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 13: MongoDB Aggregation Pipeline (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-13', 'mongodb-aggregation-analytics', FALSE, 'Complex MongoDB Aggregation for Analytics', 'Multi-stage pipeline with lookups', 'Need a prompt that generates MongoDB aggregation pipelines for complex analytics queries including $lookup for joins, $group for aggregations, $project for shaping, and $facet for multiple aggregations.', 'analysis', 90, '1.0.0', 1, 'user-george', 'george_data', 143, 678, 25, 5, 2, 'answer-13', 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP - INTERVAL '9 days', 'user-george', CURRENT_TIMESTAMP - INTERVAL '9 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 14: React Native Navigation (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-14', 'react-native-navigation-setup', FALSE, 'React Native Navigation Setup with TypeScript', 'Tab and stack navigation', 'Generate code for setting up React Navigation v6 in a React Native app with TypeScript. Include bottom tabs, stack navigation, deep linking, and authentication flow.', 'code_generation', 86, '1.0.0', 1, 'user-hannah', 'hannah_mobile', 198, 1023, 41, 11, 3, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '2 days', 'user-hannah', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 15: AWS Lambda Function (VALIDATED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-15', 'aws-lambda-s3-trigger', FALSE, 'AWS Lambda with S3 Trigger and SNS', 'Serverless file processing', 'Create a prompt for an AWS Lambda function (Python) triggered by S3 uploads that processes images (resize/compress), stores metadata in DynamoDB, and sends SNS notifications. Include error handling.', 'code_generation', 97, '1.0.0', 1, 'user-ethan', 'ethan_fullstack', 87, 421, 16, 3, 1, 'prompt-flow', 'VALIDATED', CURRENT_TIMESTAMP - INTERVAL '14 days', 'user-ethan', CURRENT_TIMESTAMP - INTERVAL '14 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 16: Time Series Forecasting (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-16', 'time-series-prophet-forecast', TRUE, 'Time Series Forecasting with Prophet', 'Sales prediction model', 'Generate Python code for time series forecasting using Facebook Prophet. Include data preprocessing, trend/seasonality detection, holiday effects, hyperparameter tuning, and visualization of predictions with confidence intervals.', 'analysis', 88, '1.0.0', 1, 'user-george', 'george_data', 223, 1156, 48, 10, 4, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '1 day', 'user-george', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade', 0, 0, 0);

-- Prompt 17: OAuth2 Implementation (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-17', 'oauth2-google-spring-boot', FALSE, 'OAuth2 Login with Google in Spring Boot', 'Social authentication integration', 'Need a prompt that generates Spring Boot OAuth2 configuration for Google login including security config, user details service, success/failure handlers, and session management.', 'code_generation', 91, '1.0.0', 1, 'user-alice', 'alice_dev', 176, 892, 36, 8, 2, 'answer-17', 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP - INTERVAL '11 days', 'user-alice', CURRENT_TIMESTAMP - INTERVAL '11 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 18: API Rate Limiting (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-18', 'api-rate-limiting-redis', TRUE, 'Implement API Rate Limiting with Redis', 'Token bucket algorithm', 'Create a prompt for implementing sliding window rate limiting using Redis. Should support per-user and per-endpoint limits, return proper HTTP 429 responses with Retry-After headers, and include monitoring metrics.', 'code_generation', 94, '1.0.0', 1, 'user-ivan', 'ivan_backend', 289, 1487, 61, 14, 5, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '4 days', 'user-ivan', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 19: SQL Injection Prevention (VALIDATED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-19', 'prevent-sql-injection-java', FALSE, 'Prevent SQL Injection in Java/JDBC', 'Secure database queries', 'Generate a prompt showing how to refactor vulnerable SQL queries to use PreparedStatement, explain parameterized queries, input validation, and ORM best practices (JPA/Hibernate). Include before/after examples.', 'code_generation', 99, '1.0.0', 1, 'user-kevin', 'kevin_security', 112, 634, 29, 6, 1, 'prompt-flow', 'VALIDATED', CURRENT_TIMESTAMP - INTERVAL '13 days', 'user-kevin', CURRENT_TIMESTAMP - INTERVAL '13 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 20: CI/CD Pipeline (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-20', 'github-actions-cicd', FALSE, 'GitHub Actions CI/CD Pipeline', 'Automated testing and deployment', 'Create a complete GitHub Actions workflow for a Node.js app including: lint, test, build, Docker image creation, push to registry, and deploy to Kubernetes. Include conditional deployment based on branch.', 'code_generation', 92, '1.0.0', 1, 'user-fiona', 'fiona_devops', 234, 1267, 53, 12, 4, 'answer-20', 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP - INTERVAL '8 days', 'user-fiona', CURRENT_TIMESTAMP - INTERVAL '8 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 21: Data Visualization (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-21', 'plotly-interactive-dashboard', TRUE, 'Interactive Dashboard with Plotly Dash', 'Real-time data visualization', 'Generate code for an interactive dashboard using Plotly Dash with multiple chart types (line, bar, scatter), dropdown filters, date range selector, and real-time data updates via callbacks.', 'code_generation', 87, '1.0.0', 1, 'user-diana', 'diana_ml', 167, 823, 32, 7, 2, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '5 days', 'user-diana', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 22: WebSocket Server (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-22', 'websocket-chat-nodejs', FALSE, 'WebSocket Chat Server with Socket.io', 'Real-time messaging application', 'Create a prompt for building a WebSocket chat server using Socket.io with features: rooms, private messages, typing indicators, user presence, message history (Redis), and reconnection handling.', 'code_generation', 89, '1.0.0', 1, 'user-ethan', 'ethan_fullstack', 193, 967, 38, 9, 3, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '3 days', 'user-ethan', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 23: Regex Pattern (ANSWERED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, accepted_answer_id, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-23', 'regex-email-validation', FALSE, 'Advanced Email Validation Regex', 'RFC-compliant email pattern', 'Generate a comprehensive regex pattern for email validation that handles edge cases: plus addressing, international domains, quoted strings, IP addresses. Include explanation of each part and test cases.', 'code_generation', 85, '1.0.0', 1, 'user-alice', 'alice_dev', 298, 1534, 44, 6, 2, 'answer-23', 'prompt-flow', 'ANSWERED', CURRENT_TIMESTAMP - INTERVAL '16 days', 'user-alice', CURRENT_TIMESTAMP - INTERVAL '16 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 24: Error Handling Middleware (VALIDATED)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-24', 'express-error-handling', FALSE, 'Centralized Error Handling in Express', 'Production error middleware', 'Create an Express.js error handling middleware that categorizes errors (validation, auth, database), logs appropriately, returns consistent JSON responses, and handles async errors with proper stack traces in dev.', 'code_generation', 96, '1.0.0', 1, 'user-ivan', 'ivan_backend', 145, 712, 27, 4, 1, 'prompt-flow', 'VALIDATED', CURRENT_TIMESTAMP - INTERVAL '10 days', 'user-ivan', CURRENT_TIMESTAMP - INTERVAL '10 days', 1, 'Handmade', 0, 0, 0);

-- Prompt 25: Pandas Data Cleaning (OPEN)
INSERT INTO prompt (id, slug, is_featured, title, description, body, task_type, validation_score, semantic_version, revision_number, user_id, author_username, usage_count, view_count, favorite_count, comment_count, answer_count, flow_id, state_id, state_entry_time, created_by, created_time, version, tenant, score, sla_late, sla_tending_late) VALUES 
('prompt-25', 'pandas-data-cleaning-pipeline', TRUE, 'Comprehensive Pandas Data Cleaning Pipeline', 'Handle messy datasets', 'Generate a Pandas pipeline for cleaning messy CSV data: handle missing values (multiple strategies), remove duplicates, fix data types, normalize text, detect outliers, and create data quality report.', 'analysis', 90, '1.0.0', 1, 'user-charlie', 'charlie_data', 267, 1389, 58, 13, 4, 'prompt-flow', 'OPEN', CURRENT_TIMESTAMP - INTERVAL '2 days', 'user-charlie', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade', 0, 0, 0);

-- =====================================================
-- PROMPT TAGS MAPPINGS
-- =====================================================

INSERT INTO prompt_tags (prompt_id, tags) VALUES 
-- Prompt 4: JWT Auth
('prompt-4', 'nodejs'),
('prompt-4', 'javascript'),
('prompt-4', 'security'),
('prompt-4', 'rest-api'),

-- Prompt 5: SQL Optimization
('prompt-5', 'postgresql'),
('prompt-5', 'sql'),
('prompt-5', 'performance'),

-- Prompt 6: React Hooks
('prompt-6', 'react'),
('prompt-6', 'typescript'),
('prompt-6', 'javascript'),
('prompt-6', 'async'),

-- Prompt 7: Docker
('prompt-7', 'docker'),
('prompt-7', 'nodejs'),

-- Prompt 8: Python Scraper
('prompt-8', 'python'),
('prompt-8', 'async'),

-- Prompt 9: ML Deployment
('prompt-9', 'python'),
('prompt-9', 'machine-learning'),
('prompt-9', 'docker'),
('prompt-9', 'rest-api'),

-- Prompt 10: GraphQL
('prompt-10', 'graphql'),
('prompt-10', 'nodejs'),
('prompt-10', 'typescript'),

-- Prompt 11: Jest Tests
('prompt-11', 'react'),
('prompt-11', 'testing'),
('prompt-11', 'javascript'),

-- Prompt 12: Kubernetes
('prompt-12', 'kubernetes'),
('prompt-12', 'docker'),

-- Prompt 13: MongoDB
('prompt-13', 'mongodb'),
('prompt-13', 'nodejs'),

-- Prompt 14: React Native
('prompt-14', 'react'),
('prompt-14', 'typescript'),
('prompt-14', 'javascript'),

-- Prompt 15: AWS Lambda
('prompt-15', 'aws'),
('prompt-15', 'python'),

-- Prompt 16: Time Series
('prompt-16', 'python'),
('prompt-16', 'machine-learning'),
('prompt-16', 'data-viz'),

-- Prompt 17: OAuth2
('prompt-17', 'java'),
('prompt-17', 'spring-boot'),
('prompt-17', 'security'),

-- Prompt 18: Rate Limiting
('prompt-18', 'nodejs'),
('prompt-18', 'rest-api'),
('prompt-18', 'performance'),

-- Prompt 19: SQL Injection
('prompt-19', 'java'),
('prompt-19', 'sql'),
('prompt-19', 'security'),

-- Prompt 20: CI/CD
('prompt-20', 'docker'),
('prompt-20', 'kubernetes'),
('prompt-20', 'nodejs'),

-- Prompt 21: Data Viz
('prompt-21', 'python'),
('prompt-21', 'data-viz'),

-- Prompt 22: WebSocket
('prompt-22', 'nodejs'),
('prompt-22', 'javascript'),
('prompt-22', 'async'),

-- Prompt 23: Regex
('prompt-23', 'regex'),
('prompt-23', 'java'),

-- Prompt 24: Error Handling
('prompt-24', 'nodejs'),
('prompt-24', 'javascript'),
('prompt-24', 'rest-api'),

-- Prompt 25: Pandas
('prompt-25', 'python')
ON CONFLICT (prompt_id, tags) DO NOTHING;

-- =====================================================
-- ANSWERS
-- =====================================================

-- Answer for Prompt 5 (SQL Optimization)
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES 
('answer-5', 'prompt-5', 'user-ethan', 'ethan_fullstack', 
'Here''s a comprehensive prompt for SQL query optimization:

"Analyze the following PostgreSQL query and suggest optimizations:

1. Run EXPLAIN ANALYZE and identify bottlenecks
2. Check for missing indexes on JOIN and WHERE columns
3. Consider using CTEs for complex subqueries
4. Look for N+1 query patterns
5. Suggest materialized views for expensive aggregations
6. Recommend partitioning strategies for large tables

Include the optimized query and expected performance improvement."', 
24, TRUE, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '10 days', 1, 'Handmade');

-- Answer for Prompt 9 (ML Model)
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES 
('answer-9', 'prompt-9', 'user-diana', 'diana_ml', 
'Perfect prompt for deploying ML models:

"Create a FastAPI application to serve a machine learning model with:

1. Pydantic models for request/response validation
2. /predict endpoint with input preprocessing
3. /batch-predict for processing multiple samples
4. Model versioning and A/B testing support
5. Health check endpoint
6. Prometheus metrics integration
7. Docker multi-stage build
8. Error handling for invalid inputs

Include model loading strategy and caching for performance."', 
42, TRUE, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '8 days', 1, 'Handmade');

-- Answer for Prompt 11 (Jest Tests)
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES 
('answer-11', 'prompt-11', 'user-julia', 'julia_qa', 
'Comprehensive testing prompt:

"Generate Jest tests for this React component covering:

1. Component rendering with different props
2. User interactions (clicks, inputs, form submissions)
3. Async operations (API calls) with MSW mocking
4. Hook behavior (useState, useEffect, custom hooks)
5. Conditional rendering scenarios
6. Error states and edge cases
7. Accessibility checks with jest-axe
8. Snapshot testing for visual regression

Use React Testing Library best practices - test user behavior, not implementation."', 
31, TRUE, CURRENT_TIMESTAMP - INTERVAL '13 days', CURRENT_TIMESTAMP - INTERVAL '13 days', 1, 'Handmade');

-- Answer for Prompt 13 (MongoDB)
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES 
('answer-13', 'prompt-13', 'user-ivan', 'ivan_backend', 
'Here''s how to structure MongoDB aggregation prompts:

"Build a MongoDB aggregation pipeline that:

1. Uses $match early to filter documents
2. $lookup to join collections (optimize with indexing)
3. $unwind arrays when needed
4. $group for aggregations with accumulators
5. $project to reshape output
6. $sort and $limit for pagination
7. $facet for multiple aggregations in parallel

Include index recommendations and explain execution with .explain()."', 
19, TRUE, CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days', 1, 'Handmade');

-- Answer for Prompt 17 (OAuth2)
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES 
('answer-17', 'prompt-17', 'user-alice', 'alice_dev', 
'OAuth2 Spring Boot prompt:

"Implement Google OAuth2 login in Spring Boot:

1. Add spring-boot-starter-oauth2-client dependency
2. Configure application.yml with Google client credentials
3. Create SecurityConfig extending WebSecurityConfigurerAdapter
4. Implement OAuth2UserService to customize user details
5. Add success/failure handlers
6. Store user info in database on first login
7. Configure CORS for frontend integration
8. Add logout endpoint to clear session

Include error handling and redirect URIs configuration."', 
28, TRUE, CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP - INTERVAL '9 days', 1, 'Handmade');

-- Answer for Prompt 20 (CI/CD)
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES 
('answer-20', 'prompt-20', 'user-fiona', 'fiona_devops', 
'GitHub Actions CI/CD workflow prompt:

"Create a .github/workflows/deploy.yml that:

1. Triggers on push to main/develop branches
2. Runs linter (ESLint) and tests (Jest) in parallel
3. Builds Docker image with proper tagging (commit SHA + branch)
4. Pushes to Docker Hub/ECR with authentication
5. Deploys to Kubernetes using kubectl apply
6. Uses GitHub secrets for credentials
7. Sends Slack notification on failure
8. Implements deployment approval for production

Include workflow status badges and artifact caching for faster builds."', 
37, TRUE, CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 1, 'Handmade');

-- Answer for Prompt 23 (Regex)
INSERT INTO answer (id, prompt_id, user_id, author_username, body, score, is_accepted, created_at, created_time, version, tenant) VALUES 
('answer-23', 'prompt-23', 'user-kevin', 'kevin_security', 
'Email validation regex prompt:

"Create a robust email validation regex:

Pattern: ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$

Breakdown:
- Local part: alphanumeric + special chars (._%+-)
- @ symbol required
- Domain: alphanumeric + dots/hyphens
- TLD: 2+ letters

Edge cases to handle:
✓ user+tag@example.com
✓ user.name@example.co.uk
✗ @example.com (no local part)
✗ user@.com (invalid domain)

For production, use validator libraries (validator.js, Apache Commons) instead of regex."', 
21, TRUE, CURRENT_TIMESTAMP - INTERVAL '14 days', CURRENT_TIMESTAMP - INTERVAL '14 days', 1, 'Handmade');

-- =====================================================
-- COMMENTS
-- =====================================================

INSERT INTO comment (id, prompt_id, author, content, created_at, created_time, version, tenant) VALUES 
('comment-4', 'prompt-4', 'kevin_security', 'Make sure to use httpOnly cookies for storing refresh tokens!', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade'),
('comment-5', 'prompt-4', 'ethan_fullstack', 'Good catch! Also rotating refresh tokens on each use prevents replay attacks.', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade'),
('comment-6', 'prompt-4', 'alice_dev', 'Should we include token blacklisting for logout?', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade'),

('comment-7', 'prompt-6', 'diana_ml', 'Love this! Would be great to add AbortController for canceling requests on unmount.', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade'),
('comment-8', 'prompt-6', 'bob_builds', 'Absolutely, preventing memory leaks is crucial. Will add that to the prompt.', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade'),

('comment-9', 'prompt-8', 'ethan_fullstack', 'For production web scraping, consider using playwright for JS-heavy sites.', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 1, 'Handmade'),
('comment-10', 'prompt-8', 'charlie_data', 'Great suggestion! I''ll create a follow-up prompt for Playwright.', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 1, 'Handmade'),

('comment-11', 'prompt-12', 'ivan_backend', 'Don''t forget resource quotas to prevent one pod from hogging all resources!', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),
('comment-12', 'prompt-12', 'fiona_devops', 'Added! Also including PodDisruptionBudget for high availability.', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),

('comment-13', 'prompt-16', 'diana_ml', 'For financial data, you might want to compare Prophet with ARIMA/SARIMA too.', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),
('comment-14', 'prompt-16', 'george_data', 'Good point! Prophet is easier but traditional methods can be more accurate for certain patterns.', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),

('comment-15', 'prompt-18', 'kevin_security', 'Consider using a distributed rate limiter if you have multiple app instances.', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade'),
('comment-16', 'prompt-18', 'ivan_backend', 'Exactly why I chose Redis - works perfectly across all instances!', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade'),

('comment-17', 'prompt-22', 'hannah_mobile', 'How would you handle reconnection on mobile with spotty networks?', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade'),
('comment-18', 'prompt-22', 'ethan_fullstack', 'Socket.io has built-in reconnection, but I''d add exponential backoff and queue messages offline.', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade'),

('comment-19', 'prompt-25', 'george_data', 'Pandas can be slow on large datasets. Consider Polars for better performance.', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),
('comment-20', 'prompt-25', 'charlie_data', 'True! For datasets > 10M rows, I''d switch to Polars or Dask.', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade');

-- =====================================================
-- VOTES (Upvotes/Downvotes)
-- =====================================================

INSERT INTO prompt_vote (id, user_id, prompt_id, vote_type, created_time, version, tenant) VALUES 
-- Prompt 4 votes (JWT Auth) - very popular
('vote-4', 'user-alice', 'prompt-4', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),
('vote-5', 'user-bob', 'prompt-4', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),
('vote-6', 'user-charlie', 'prompt-4', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade'),
('vote-7', 'user-diana', 'prompt-4', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade'),
('vote-8', 'user-george', 'prompt-4', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade'),

-- Prompt 6 votes (React Hooks)
('vote-9', 'user-alice', 'prompt-6', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade'),
('vote-10', 'user-hannah', 'prompt-6', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade'),
('vote-11', 'user-diana', 'prompt-6', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade'),

-- Prompt 9 votes (ML Deployment)
('vote-12', 'user-charlie', 'prompt-9', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '9 days', 1, 'Handmade'),
('vote-13', 'user-george', 'prompt-9', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '8 days', 1, 'Handmade'),
('vote-14', 'user-alice', 'prompt-9', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '7 days', 1, 'Handmade'),
('vote-15', 'user-ethan', 'prompt-9', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '7 days', 1, 'Handmade'),

-- Prompt 12 votes (Kubernetes)
('vote-16', 'user-ethan', 'prompt-12', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '6 days', 1, 'Handmade'),
('vote-17', 'user-ivan', 'prompt-12', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),
('vote-18', 'user-alice', 'prompt-12', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),

-- Prompt 16 votes (Time Series)
('vote-19', 'user-charlie', 'prompt-16', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),
('vote-20', 'user-diana', 'prompt-16', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),
('vote-21', 'user-george', 'prompt-16', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),
('vote-22', 'user-ethan', 'prompt-16', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),

-- Prompt 18 votes (Rate Limiting)
('vote-23', 'user-alice', 'prompt-18', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade'),
('vote-24', 'user-kevin', 'prompt-18', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade'),
('vote-25', 'user-fiona', 'prompt-18', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '3 days', 1, 'Handmade'),

-- Prompt 21 votes (Data Viz)
('vote-26', 'user-george', 'prompt-21', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),
('vote-27', 'user-charlie', 'prompt-21', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '4 days', 1, 'Handmade'),

-- Prompt 25 votes (Pandas)
('vote-28', 'user-diana', 'prompt-25', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '2 days', 1, 'Handmade'),
('vote-29', 'user-george', 'prompt-25', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade'),
('vote-30', 'user-alice', 'prompt-25', 'UPVOTE', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade')
ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- USER REPUTATION
-- =====================================================

INSERT INTO user_reputation (user_id, total_reputation, created_time, version, tenant) VALUES 
('user-diana', 420, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-ethan', 385, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-fiona', 340, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-george', 290, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-hannah', 185, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-ivan', 355, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-julia', 310, CURRENT_TIMESTAMP, 1, 'Handmade'),
('user-kevin', 270, CURRENT_TIMESTAMP, 1, 'Handmade')
ON CONFLICT (user_id) DO NOTHING;

-- =====================================================
-- FLAGS (Content Moderation)
-- =====================================================

INSERT INTO flag (id, entity_type, entity_id, user_id, reason, details, status, created_at, created_time, version, tenant) VALUES 
('flag-2', 'PROMPT', 'prompt-8', 'user-kevin', 'NEEDS_IMPROVEMENT', 'Should mention robots.txt compliance and ethical scraping practices.', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 1, 'Handmade'),
('flag-3', 'COMMENT', 'comment-9', 'user-fiona', 'SPAM', 'Looks like advertising for a paid service.', 'REVIEWED', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 1, 'Handmade'),
('flag-4', 'PROMPT', 'prompt-14', 'user-julia', 'DUPLICATE', 'Similar to an existing React Navigation prompt.', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day', 1, 'Handmade')
ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- BADGE AWARDS
-- =====================================================

INSERT INTO user_badges (user_id, badge_id, awarded_at) VALUES 
('user-alice', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '100 days'),
('user-alice', 'badge-prolific-prompter', CURRENT_TIMESTAMP - INTERVAL '20 days'),
('user-bob', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '90 days'),
('user-charlie', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '85 days'),
('user-diana', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '90 days'),
('user-diana', 'badge-prolific-prompter', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('user-ethan', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '120 days'),
('user-ethan', 'badge-prolific-prompter', CURRENT_TIMESTAMP - INTERVAL '40 days'),
('user-fiona', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '60 days'),
('user-george', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '45 days'),
('user-hannah', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('user-ivan', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '75 days'),
('user-julia', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '50 days'),
('user-kevin', 'badge-first-prompt', CURRENT_TIMESTAMP - INTERVAL '100 days')
ON CONFLICT (user_id, badge_id) DO NOTHING;

-- =====================================================
-- SUMMARY STATISTICS
-- =====================================================

-- Total Prompts: 25 (3 original + 22 new)
-- Total Users: 13 (5 original + 8 new)
-- Total Answers: 7 high-quality answers
-- Total Comments: 20 realistic discussions
-- Total Votes: 30 upvotes across different prompts
-- Total Tags: 23 diverse technology tags
-- Total Flags: 4 moderation cases
-- States: OPEN (15), ANSWERED (7), VALIDATED (3)