Feature: Google Search

  Background:
    Given I navigate to google home page

  Scenario: Search for term
    When I search for term "example"
    Then I should see search results
     And Results page search field should contain "example"

  Scenario: Feeling lucky
    When I use feel lucky feature with term "example"
    Then I should see a non-google page
