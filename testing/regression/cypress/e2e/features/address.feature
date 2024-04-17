
Feature: Patient Search by Address

  Background:
    Given I am logged in as "superuser" and password ""

  Scenario: Address - Search by City
    Then I navigate the  basic info and address
    When I search for "Newyork" city
    Then I should see No Results found text

  Scenario: Address - Search by State
    Then I navigate the  basic info and address
    Then I select "Hawaii" state
    Then I should found result patient profile

  Scenario: Address - Search by Zip Code
    Then I navigate the  basic info and address
    When I search for "90001" zip code
    Then I should found result patient profile

  Scenario: Address - Search by Street address
    Then I navigate the  basic info and address
    When I search for "90 SE Panda Cutten KY 25783" Street address
    Then I should found result patient profile