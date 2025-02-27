
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

  Scenario: CNFT1-1281 Address – Search with Invalid Address Data
    Then I navigate the  basic info and address
    When I enter "&*(#))#)@)" as zip code
    Then I should see "Please enter a valid ZIP code (XXXXX) using only numeric characters (0-9)."

  Scenario: CNFT1-1282 Address - Search with No Matching Address Records
    Then I navigate the  basic info and address
    When I search for "$%(5" Street address
    Then I should see "No results found. Try refining your search, or add a new patient"

  Scenario: CNFT1-1283 Address – Search by Multiple Address Criteria
    Then I navigate the  basic info and address
    When I search for Street Address "8554 Derek Crossing" City "Akron" State "Ohio" Zip code "44329"
    Then I should see "Eulalie Schuchmacher Pudan, II / The Second"

  Scenario: CNFT1-1284 Address – Search with Partial Address
    Then I navigate the  basic info and address
    When I search for "4563 Melrose Point" Street address
    Then I should see "Kamillah Greenshiels Cushe, Junior"
  