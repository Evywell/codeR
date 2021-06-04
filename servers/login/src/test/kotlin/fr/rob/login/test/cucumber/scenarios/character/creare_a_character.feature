Feature: Create a simple character

  Scenario: Create a character with valid parameters
    Given I am logged as user 1
    When I send a create character packet with name "Imnotused"
    Then I should receive a packet with opcode 2
    And the packet should be a success
    And the created character should be level 1
    And the created character name should be "Imnotused"

  Scenario: Attempt to create a character with a name already taken
    Given I am logged as user 1
    When I send a create character packet with name "Evywell"
    Then I should receive a packet with opcode 2
    And the packet should be an error
    And the error message should be "err_character_name_already_taken"

  Scenario: Attempt to create a character with a name already taken by another player
    Given I am logged as user 1
    When I send a create character packet with name "Tarthas"
    Then I should receive a packet with opcode 2
    And the packet should be an error
    And the error message should be "err_character_name_already_taken"

  Scenario Outline: Scenario: Attempt to create a character with a short name
    Given I am logged as user 1
    When I send a create character packet with name "<name>"
    Then I should receive a packet with opcode 2
    And the packet should be an error
    And the error message should be "err_character_name_too_small"
    Examples:
      | name |
      | a   |
      | aa  |

  Scenario: Attempt to create a character with a long name
    Given I am logged as user 1
    When I send a create character packet with name "Mysuperlongnameandco"
    Then I should receive a packet with opcode 2
    And the packet should be an error
    And the error message should be "err_character_name_too_big"

  Scenario: Attempt to create a character with already max characters
    Given I am logged as user 1
    Given I have 9 characters
    When I send a create character packet with name "Imstillnotused"
    Then I should receive a packet with opcode 2
    And the packet should be an error
    And the error message should be "err_max_characters_per_user"

  Scenario Outline: Attempt to create a character with speciel characters in name
    Given I am logged as user 1
    When I send a create character packet with name "<name>"
    Then I should receive a packet with opcode 2
    And the packet should be an error
    And the error message should be "err_invalid_character_name"
    Examples:
      | name |
      |      |
      | Invaélid   |
      | 9NotValid  |
      | Hey!!55 |
      | ^^^^^^^^^^^^^^; |
