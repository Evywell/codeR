Feature: Retrieves all characters of a user

  Scenario: As authenticated user with characters, I should retrieve my character stand
    Given I am logged as user 1
    When I send a character stand packet
    Then I should receive a packet with opcode 1
    And the characters count should be 1
    * the main character id should be 1
    * the main character name should be "Evywell"

  Scenario: As authenticated user with no characters, I should have an empty character stand
    Given I am logged as user 2
    When I send a character stand packet
    Then I should receive a packet with opcode 1
    And the characters count should be 0
    And the character stand should be empty
