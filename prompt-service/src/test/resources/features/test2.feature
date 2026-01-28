Feature: Testcase ID 2
Tests the prompt Workflow Service using a REST client. Prompt service exists and is under test.
It helps to create a prompt and manages the state of the prompt as documented in states xml
Scenario: Create a new prompt
Given that "flowName" equals "prompt-flow"
And that "initialState" equals "DRAFT"
When I POST a REST request to URL "/prompt" with payload
"""json
{
    "description": "Description"
}
"""
Then the REST response contains key "mutatedEntity"
And store "$.payload.mutatedEntity.id" from response to "id"
And the REST response key "mutatedEntity.currentState.stateId" is "${initialState}"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"
And the REST response key "mutatedEntity.description" is "Description"

Scenario: Retrieve the prompt that just got created
When I GET a REST request to URL "/prompt/${id}"
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "${currentState}"

Scenario: Submit the prompt
    When I PATCH a REST request to URL "/prompt/${id}/submit" with payload
    """json
    {
        "comment": "Submitting"
    }
    """
    Then the REST response contains key "mutatedEntity"
    And the REST response key "mutatedEntity.currentState.stateId" is "OPEN"
    And store "$.payload.mutatedEntity.currentState.stateId" from response to "currentState"

Scenario: Send the validate event to the prompt with comments
Given that "comment" equals "Comment for validate"
And that "event" equals "validate"
When I PATCH a REST request to URL "/prompt/${id}/${event}" with payload
"""json
{
    "comment": "${comment}",
    "score": 5
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "VALIDATED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"

Scenario: Send the deprecate event to the prompt with comments
Given that "comment" equals "Comment for deprecate"
And that "event" equals "deprecate"
When I PATCH a REST request to URL "/prompt/${id}/${event}" with payload
"""json
{
    "comment": "${comment}"
}
"""
Then the REST response contains key "mutatedEntity"
And the REST response key "mutatedEntity.id" is "${id}"
And the REST response key "mutatedEntity.currentState.stateId" is "DEPRECATED"
And store "$.payload.mutatedEntity.currentState.stateId" from response to "finalState"
