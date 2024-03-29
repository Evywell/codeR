Feature: Authentication using a user id

  Scenario: Authenticate with a valid user id
    When I send an authentication packet with a user id 1
    Then I should receive a packet with opcode 0
    And the authentication packet should contain a success result

  Scenario: Authenticate with an invalid user id
    When I send an authentication packet with a user id 0
    Then I should receive a packet with opcode 0
    And the authentication packet should contain an error result

  Scenario: Authenticate with a user without any account
    Given the user 4 does not have an account
    When I send an authentication packet with a user id 4
    Then I should receive a packet with opcode 0
    And the authentication packet should contain a success result
    And the user 4 should have an account
