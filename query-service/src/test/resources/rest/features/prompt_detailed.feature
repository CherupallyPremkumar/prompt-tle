Feature: Tests the Prompt Query Service entity-specific queries with full field support.

  Background:
     Given I am logged in as "user1" with role "ROLE_USER"

  Scenario: Search prompts by status and verify all fields for p1
    When I POST a REST request to URL "/q/prompt.search" with payload
    """json
    { "filters": { "stateId": "VALIDATED" } }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "p1"
    And the REST response key "list[0].allowedActions[0].allowedAction" is "flag"
    And the REST response key "list[0].row.slug" is "java-streams-guide"
    And the REST response key "list[0].row.title" is "Java Streams Guide"
    And the REST response key "list[0].row.template" is "Hello ${name}"
    And the REST response key "list[0].row.systemPrompt" is "Act as a Java expert"
    And the REST response key "list[0].row.validationScore" is "85"
    And the REST response key "list[0].row.semanticVersion" is "1.0.0"
    And the REST response key "list[0].row.recommendedModel" is "gpt-4"
    And the REST response key "list[0].row.authorUsername" is "ExpertJavaCoder"
    And the REST response key "list[0].row.usageCount" is "100"
    And the REST response key "list[0].row.viewCount" is "500"
    And the REST response key "list[0].row.favoriteCount" is "50"
    And the REST response key "list[0].row.score" is "45"
    And the REST response key "list[0].row.acceptedAnswerId" is "a1"
    And the REST response key "list[0].row.answerCount" is "1"
    And the REST response key "list[0].row.commentCount" is "1"
    And the REST response key "list[0].row.revisionNumber" is "1"
    And the REST response key "list[0].row.imageUrl" is "http://example.com/java.png"
    And the REST response key "list[0].row.creatorRole" is "SENIOR_DEVELOPER"
    And the REST response key "list[0].row.tenant" is "tle-tenant"

  Scenario: Get answers for a specific prompt
    When I POST a REST request to URL "/q/prompt.getAnswers" with payload
    """json
    { "filters": { "promptId": "p1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "a1"
    And the REST response key "list[0].row.authorUsername" is "expert_coder"
    And the REST response key "list[0].row.score" is "10"
    And the REST response key "list[0].row.isAccepted" is "true"

  Scenario: Get comments for a specific prompt
    When I POST a REST request to URL "/q/prompt.getComments" with payload
    """json
    { "filters": { "promptId": "p1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "c1"
    And the REST response key "list[0].row.content" is "Very helpful guide!"
    And the REST response key "list[0].row.author" is "user4"

  Scenario: Get revision history for a specific prompt
    When I POST a REST request to URL "/q/prompt.getRevisions" with payload
    """json
    { "filters": { "promptId": "p1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "r1"
    And the REST response key "list[0].row.changeComment" is "Initial revision"
    And the REST response key "list[0].row.title" is "Java Streams Guide"

  Scenario: Get variables for a specific prompt
    When I POST a REST request to URL "/q/prompt.getVariables" with payload
    """json
    { "filters": { "promptId": "p1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "v1"
    And the REST response key "list[0].row.name" is "collection_name"
    And the REST response key "list[0].row.type" is "STRING"
    And the REST response key "list[0].row.defaultValue" is "list"

  Scenario: Get activity logs for a specific prompt
    When I POST a REST request to URL "/q/prompt.getActivities" with payload
    """json
    { "filters": { "promptId": "p1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "act1"
    And the REST response key "list[0].row.activityName" is "PUBLISH"
    And the REST response key "list[0].row.activitySuccess" is "true"

  Scenario: Get favorites for a specific user
    When I POST a REST request to URL "/q/prompt.getFavorites" with payload
    """json
    { "filters": { "userId": "user5" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "f1"
    And the REST response key "list[0].row.notes" is "Useful for interview prep"

  Scenario: Get pending flags
    When I POST a REST request to URL "/q/prompt.getFlags" with payload
    """json
    { "filters": { "status": "PENDING" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "fl1"
    And the REST response key "list[0].row.reason" is "PLAGIARISM"

  Scenario: Get active bounties
    When I POST a REST request to URL "/q/prompt.getBounties" with payload
    """json
    { "filters": { "status": "ACTIVE" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "b1"
    And the REST response key "list[0].row.amount" is "500"

  Scenario: Get attachments for a specific entity
    When I POST a REST request to URL "/q/prompt.getAttachments" with payload
    """json
    { "filters": { "entityId": "p1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "att1"
    And the REST response key "list[0].row.url" is "http://example.com/diagram.png"
    And the REST response key "list[0].row.mimeType" is "image/png"

  Scenario: Get notifications for a specific user
    When I POST a REST request to URL "/q/prompt.getNotifications" with payload
    """json
    { "filters": { "userId": "user1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "n1"
    And the REST response key "list[0].row.title" is "Welcome"
    And the REST response key "list[0].row.isRead" is "false"
