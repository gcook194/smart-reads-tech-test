Feature: Validation on post endpoint

  Scenario: Call the REST API with no account ID
    When the client posts to endpoint "/api/smart/reads" with no account ID
    Then error response status code is 400
    And response body should contain the error message "Account ID must not be null" for field "accountId"

  Scenario: Call the REST API with no meter ID
    When the client posts to endpoint "/api/smart/reads" with no meter ID
    Then error response status code is 400
    And response body should contain the error message "Meter ID must not be null" for field "gasReadings[0]"

  Scenario: Call the REST API with no meter reading
    When the client posts to endpoint "/api/smart/reads" with no meter reading
    Then error response status code is 400
    And response body should contain the error message "Meter reading must not be null" for field "gasReadings[0]"

  Scenario: Call the REST API with no meter reading date
    When the client posts to endpoint "/api/smart/reads" with no meter reading date
    Then error response status code is 400
    And response body should contain the error message "Date must not be null" for field "gasReadings[0]"