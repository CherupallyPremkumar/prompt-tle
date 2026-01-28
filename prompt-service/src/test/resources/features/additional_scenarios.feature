Feature: Additional Test Scenarios for Prompt Service
  Background functionality for testing prompt workflow transitions,
  commenting, tagging, and answer management

  Scenario: Create a prompt and add tags
    Given that "flowName" equals "prompt-flow"
    And that "initialState" equals "DRAFT"
    When I POST a REST request to URL "/prompt" with payload
    """json
    {
        "title": "How to center a div",
        "description": "Seeking best practices for centering elements",
        "tags": ["css", "html", "layout"]
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.id" from response to "promptId"
    And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
    And the REST response key "mutatedEntity.title" is "How to center a div"

  Scenario: Validate a prompt
    When I PATCH a REST request to URL "/prompt/${promptId}/submit" with payload
    """json
    {
        "comment": "Submitting for review"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "OPEN"
    When I PATCH a REST request to URL "/prompt/${promptId}/validate" with payload
    """json
    {
        "comment": "Prompt validated by moderator",
        "score": 5
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "VALIDATED"
    And the REST response key "mutatedEntity.validationScore" is "5"

  Scenario: Add an answer to the prompt
    When I PATCH a REST request to URL "/prompt/${promptId}/addAnswer" with payload
    """json
    {
        "body": "Use flexbox with justify-content and align-items set to center",
        "authorUsername": "css_expert"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And store "$.payload.mutatedEntity.answers[0].id" from response to "answerId"
    And the REST response key "mutatedEntity.answerCount" is "1"

  Scenario: Accept an answer
    When I PATCH a REST request to URL "/prompt/${promptId}/acceptAnswer" with payload
    """json
    {
        "answerId": "${answerId}",
        "comment": "This solution works perfectly"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.acceptedAnswerId" is "${answerId}"

  Scenario: Upvote a prompt
    When I PATCH a REST request to URL "/prompt/${promptId}/upvote" with payload
    """json
    {
        "userId": "user123"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.score" is "1"

  Scenario: Add a comment to the prompt
    When I PATCH a REST request to URL "/prompt/${promptId}/addComment" with payload
    """json
    {
        "content": "Please add more context about browser compatibility",
        "authorUsername": "reviewer1"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.commentCount" is "1"

  Scenario: Create a revision of the prompt
    When I PATCH a REST request to URL "/prompt/${promptId}/addRevision" with payload
    """json
    {
        "changeComment": "Added browser compatibility requirements",
        "newContent": "Seeking best practices for centering elements across all major browsers"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.revisionNumber" is "2"

  Scenario: Mark prompt as duplicate
    Given I POST a REST request to URL "/prompt" with payload
    """json
    {
        "title": "Another centering question",
        "description": "How do I center things?"
    }
    """
    And store "$.payload.mutatedEntity.id" from response to "duplicateId"
    When I PATCH a REST request to URL "/prompt/${duplicateId}/submit" with payload
    """json
    {
        "comment": "Submitting"
    }
    """
    Then the REST response key "mutatedEntity.currentState.stateId" is "OPEN"
    When I PATCH a REST request to URL "/prompt/${duplicateId}/markDuplicate" with payload
    """json
    {
        "duplicateOfPromptId": "${promptId}",
        "comment": "This is a duplicate of existing question"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.duplicateOfPromptId" is "${promptId}"

  Scenario: Close a prompt with reason
    When I PATCH a REST request to URL "/prompt/${duplicateId}/close" with payload
    """json
    {
        "reason": "Duplicate question",
        "comment": "Closed as duplicate"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "CLOSED"
    And the REST response key "mutatedEntity.closeReason" is "Duplicate question"

  Scenario: Attempt to add answer to closed prompt (should fail)
    When I PATCH a REST request to URL "/prompt/${duplicateId}/addAnswer" with payload
    """json
    {
        "body": "Spam answer",
        "authorUsername": "spammer"
    }
    """
    Then the http status code is 422
