Feature: Account has no meter readings

  Scenario: Call the REST API with account ID 1
    When the client calls endpoint "/api/smart/reads/1"
    Then response status code is 200
    And response body should contain an empty response for account 1