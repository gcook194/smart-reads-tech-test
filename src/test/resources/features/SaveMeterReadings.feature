Feature: Save meter readings

  Scenario: call the rest API with a valid post body
    When the client posts to endpoint "/api/smart/reads" with a valid post body
    Then post response status code is 200 with body "saved"