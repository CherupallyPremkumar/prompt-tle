Feature: Tests the Security of Prompt Query Service

  Background:
    Given the security is enabled

  Scenario: Unauthorized access to prompt search should fail
    When I POST a REST request to URL "/q/prompt.search" with payload
    """json
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 302

  Scenario: Authorized access to prompt search should succeed
    Given I am logged in as "google-user" with role "ROLE_USER"
    When I POST a REST request to URL "/q/prompt.search" with payload
    """json
    {
      "pageNum": 1,
      "numRowsInPage": 10
    }
    """
    Then the http status code is 200
    And success is true
