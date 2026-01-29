Feature: Tests the Prompt Query Service entity-specific queries with full field support.

  Background:
     Given I am logged in as "user-alice" with role "ROLE_USER"

  Scenario: Search prompts by status and verify all fields for p1
    When I POST a REST request to URL "/q/prompt.search" with payload
    """json
    { "filters": { "stateId": "VALIDATED" } }
    """
    Then the http status code is 200
    And success is true
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "prompt-3"
    And the REST response key "list[0].row.slug" is "python-pandas-agg"
    And the REST response key "list[0].row.title" is "Pandas Aggregation Prompt"
    And the REST response key "list[0].row.validationScore" is "98"
    And the REST response key "list[0].row.semanticVersion" is "1.0.0"
    And the REST response key "list[0].row.authorUsername" is "charlie_data"
    And the REST response key "list[0].row.usageCount" is "0"
    And the REST response key "list[0].row.viewCount" is "10"
    And the REST response key "list[0].row.favoriteCount" is "0"
    And the REST response key "list[0].row.score" is "0"
    And the REST response key "list[0].row.answerCount" is "0"
    And the REST response key "list[0].row.commentCount" is "0"
    And the REST response key "list[0].row.revisionNumber" is "1"
    And the REST response key "list[0].row.tenant" is "Handmade"

  Scenario: Get answers for a specific prompt
    When I POST a REST request to URL "/q/prompt.getAnswers" with payload
    """json
    { "filters": { "promptId": "prompt-2" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "answer-1"
    And the REST response key "list[0].row.authorUsername" is "alice_dev"
    And the REST response key "list[0].row.score" is "10"
    And the REST response key "list[0].row.isAccepted" is "true"

  Scenario: Get comments for a specific prompt
    When I POST a REST request to URL "/q/prompt.getComments" with payload
    """json
    { "filters": { "promptId": "prompt-1" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "2"
    And the REST response key "list[0].row.id" is "comment-1"
    And the REST response key "list[0].row.content" is "Make sure to handle nulls!"
    And the REST response key "list[0].row.author" is "bob_builds"

  Scenario: Get pending flags
    When I POST a REST request to URL "/q/prompt.getFlags" with payload
    """json
    { "filters": { "status": "PENDING" } }
    """
    Then the http status code is 200
    And the REST response key "numRowsReturned" is "1"
    And the REST response key "list[0].row.id" is "flag-1"
    And the REST response key "list[0].row.reason" is "NEEDS_IMPROVEMENT"
