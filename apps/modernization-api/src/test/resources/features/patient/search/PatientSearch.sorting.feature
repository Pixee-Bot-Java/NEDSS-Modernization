@patient @patient-search
Feature: Patient Search Sorting

  Background:
    Given I am logged into NBS
    And I can "find" any "patient"
    And I have a patient

  Scenario: I can find the patients ordered by birthday ascending
    Given  the patient has a "birthday" of "1987-01-15"
    And I have another patient
    And the patient has a "birthday" of "1999-11-19"
    And I have another patient
    And the patient has a "birthday" of "1974-05-29"
    And patients are available for search
    And I want patients sorted by "birthday" "asc"
    When I search for patients
    And search result 1 has a "birthday" of "1974-05-29"
    And search result 2 has a "birthday" of "1987-01-15"
    And search result 3 has a "birthday" of "1999-11-19"

  Scenario: I can find the patients ordered by birthday descending
    Given  the patient has a "birthday" of "1987-01-15"
    And I have another patient
    And the patient has a "birthday" of "1999-11-19"
    And I have another patient
    And the patient has a "birthday" of "1974-05-29"
    And patients are available for search
    And I want patients sorted by "birthday" "desc"
    When I search for patients
    And search result 1 has a "birthday" of "1999-11-19"
    And search result 2 has a "birthday" of "1987-01-15"
    And search result 3 has a "birthday" of "1974-05-29"

  Scenario: I can find the most relevant patient when sorting by last name  ascending
    Given the patient has the "legal" name "Timothy" "Jackson"
    And I have another patient
    And the patient has the "legal" name "Jason" "Todd"
    And I have another patient
    And the patient has the "legal" name "Stephanie" "Brown"
    And patients are available for search
    And I want patients sorted by "last name" "asc"
    When I search for patients
    Then search result 1 has a "first name" of "Stephanie"
    And search result 1 has a "last name" of "Brown"
    And search result 2 has a "first name" of "Timothy"
    And search result 2 has a "last name" of "Jackson"
    And search result 3 has a "first name" of "Jason"
    And search result 3 has a "last name" of "Todd"

  Scenario: I can find the most relevant patient when sorting by last name descending
    Given the patient has the "legal" name "Timothy" "Jackson"
    And I have another patient
    And the patient has the "legal" name "Jason" "Todd"
    And I have another patient
    And the patient has the "legal" name "Stephanie" "Brown"
    And patients are available for search
    And I want patients sorted by "last name" "desc"
    When I search for patients
    Then search result 1 has a "first name" of "Jason"
    And search result 1 has a "last name" of "Todd"
    And search result 2 has a "first name" of "Timothy"
    And search result 2 has a "last name" of "Jackson"
    And search result 3 has a "first name" of "Stephanie"
    And search result 3 has a "last name" of "Brown"
