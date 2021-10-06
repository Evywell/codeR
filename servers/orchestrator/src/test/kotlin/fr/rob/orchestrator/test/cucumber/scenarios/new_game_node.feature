Feature: Assign instance to new game node

  Scenario: A new game node appears with default instances
    When I authenticate with game agent "Test"
    And the agent "Test" registers new game node with name="A" and port=1234
    Then I should re

