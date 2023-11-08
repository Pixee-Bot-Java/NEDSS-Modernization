@page-summary-search
Feature: Searching for Sorted Page Summaries

  Background:
    Given I am logged in
    And I can "LDFAdministration" any "System"
    And I am looking for page summaries that contain "sorting"

  Scenario: I can search for Page Summaries sorted by name ascending
    Given I have a page
    And the page has a "name" of "X sorting"
    And I have another page
    And the page has a "name" of "C sorting"
    And I have another page
    And the page has a "name" of "55 sorting"
    And I have another page
    And the page has a "name" of "A sorting"
    And I am looking for page summaries sorted by "name" ascending
    When I search for page summaries
    Then the 1st found page summary has the "name" "55 sorting"
    And the 2nd found page summary has the "name" "A sorting"
    And the 3rd found page summary has the "name" "C sorting"
    And the 4th found page summary has the "name" "X sorting"

  Scenario: I can search for Page Summaries sorted by name descending
    Given I have a page
    And the page has a "name" of "X sorting"
    And I have another page
    And the page has a "name" of "C sorting"
    And I have another page
    And the page has a "name" of "55 sorting"
    And I have another page
    And the page has a "name" of "A sorting"
    And I am looking for page summaries sorted by "name" descending
    When I search for page summaries
    Then the 1st found page summary has the "name" "X sorting"
    And the 2nd found page summary has the "name" "C sorting"
    And the 3rd found page summary has the "name" "A sorting"
    And the 4th found page summary has the "name" "55 sorting"

  Scenario: I can search for Page Summaries sorted by event type ascending
    Given I have a page named "sorting one"
    And the page is for a Vaccination
    And I have a page named "sorting two"
    And the page is for a Lab Susceptibility
    And I have a page named "sorting three"
    And  the page is for a Contact Record
    And I have a page named "sorting four"
    And the page is for an Interview
    And I am looking for page summaries sorted by "event type" ascending
    When I search for page summaries
    Then the 1st found page summary has the "event type" "contact record"
    And the 2nd found page summary has the "event type" "interview"
    And the 3rd found page summary has the "event type" "lab susceptibility"
    And the 4th found page summary has the "event type" "vaccination"

  Scenario: I can search for Page Summaries sorted by event type descending
    Given I have a page named "sorting one"
    And the page is for a Vaccination
    And I have a page named "sorting two"
    And the page is for a Lab Susceptibility
    And I have a page named "sorting three"
    And  the page is for a Contact Record
    And I have a page named "sorting four"
    And the page is for an Interview
    And I am looking for page summaries sorted by "event type" descending
    When I search for page summaries
    Then the 1st found page summary has the "event type" "vaccination"
    And the 2nd found page summary has the "event type" "lab susceptibility"
    And the 3rd found page summary has the "event type" "interview"
    And the 4th found page summary has the "event type" "contact record"

  Scenario: I can search for Page Summaries sorted by condition ascending
    Given I have a page named "sorting one"
    And the page is tied to the Trichinellosis condition
    And I have a page named "sorting two"
    And the page is tied to the Neurosyphilis condition
    And I have a page named "sorting three"
    And the page is tied to the Crusted Scabies condition
    And I have a page named "sorting four"
    And the page is tied to the Diphtheria condition
    And I am looking for page summaries sorted by "condition" ascending
    When I search for page summaries
    Then the 1st found page summary has the "condition" "Crusted Scabies"
    And the 2nd found page summary has the "condition" "Diphtheria"
    And the 3rd found page summary has the "condition" "Neurosyphilis"
    And the 4th found page summary has the "condition" "Trichinellosis"

  Scenario: I can search for Page Summaries sorted by condition descending
    Given I have a page named "sorting one"
    And the page is tied to the Trichinellosis condition
    And I have a page named "sorting two"
    And the page is tied to the Neurosyphilis condition
    And I have a page named "sorting three"
    And the page is tied to the Crusted Scabies condition
    And I have a page named "sorting four"
    And the page is tied to the Diphtheria condition
    And I am looking for page summaries sorted by "condition" descending
    When I search for page summaries
    Then the 1st found page summary has the "condition" "Trichinellosis"
    And the 2nd found page summary has the "condition" "Neurosyphilis"
    And the 3rd found page summary has the "condition" "Diphtheria"
    And the 4th found page summary has the "condition" "Crusted Scabies"

  Scenario: I can search for Page Summaries sorted by status ascending
    Given I have a page named "sorting one"
    And the page is Published
    And I have a page named "sorting two"
    And the page is Published with draft
    And I have a page named "sorting three"
    And the page is Initial draft
    And I have a page named "sorting four"
    And the page is a Draft
    And I am looking for page summaries sorted by "status" ascending
    When I search for page summaries
    Then the 1st found page summary has the "status" "Initial Draft"
    And the 2nd found page summary has the "status" "Initial Draft"
    And the 3rd found page summary has the "status" "Published with draft"
    And the 4th found page summary has the "status" "Published"

  Scenario: I can search for Page Summaries sorted by status descending
    Given I have a page named "sorting one"
    And the page is Published
    And I have a page named "sorting two"
    And the page is Published with draft
    And I have a page named "sorting three"
    And the page is Initial draft
    And I have a page named "sorting four"
    And the page is a Draft
    And I am looking for page summaries sorted by "status" descending
    When I search for page summaries
    Then the 1st found page summary has the "status" "Published"
    And the 2nd found page summary has the "status" "Published with draft"
    And the 3rd found page summary has the "status" "Initial Draft"
    And the 4th found page summary has the "status" "Initial Draft"

  Scenario: I can search for Page Summaries sorted by last updated by ascending
    Given the user "Paige" "Arthur" exists as "paige.arthur"
    And I have a page
    And paige.arthur changed the page name to "sorting one"
    And I have another page
    And the user "Other" "User" exists as "other.user"
    And other.user changed the page name to "sorting two"
    And I have another page
    And the user "Another" "User" exists as "another.user"
    And another.user changed the page name to "sorting three"
    And I have another page
    And the user "Other" "Clerical" exists as "other.clerical"
    And other.clerical changed the page name to "sorting four"
    And I am looking for page summaries sorted by "last updated by" ascending
    When I search for page summaries
    Then the 1st found page summary was "last updated by" "Another User"
    And the 2nd found page summary was "last updated by" "Other Clerical"
    And the 3rd found page summary was "last updated by" "Other User"
    And the 4th found page summary was "last updated by" "Paige Arthur"

  Scenario: I can search for Page Summaries sorted by last updated by descending
    Given the user "Paige" "Arthur" exists as "paige.arthur"
    And I have a page
    And paige.arthur changed the page name to "sorting one"
    And I have another page
    And the user "Other" "User" exists as "other.user"
    And other.user changed the page name to "sorting two"
    And I have another page
    And the user "Another" "User" exists as "another.user"
    And another.user changed the page name to "sorting three"
    And I have another page
    And the user "Other" "Clerical" exists as "other.clerical"
    And other.clerical changed the page name to "sorting four"
    And I am looking for page summaries sorted by "last updated by" descending
    When I search for page summaries
    Then the 1st found page summary was "last updated by" "Paige Arthur"
    And the 2nd found page summary was "last updated by" "Other User"
    And the 3rd found page summary was "last updated by" "Other Clerical"
    And the 4th found page summary was "last updated by" "Another User"

  Scenario: I can search for Page Summaries sorted by last updated on ascending
    Given the user "Paige" "Arthur" exists as "paige.arthur"
    And I have a page
    And paige.arthur changed the page name to "sorting one" 3 years ago
    And I have another page
    And the user "Other" "User" exists as "other.user"
    And other.user changed the page name to "sorting two" a day ago
    And I have another page
    And the user "Another" "User" exists as "another.user"
    And another.user changed the page name to "sorting three" 7 months ago
    And I have another page
    And the user "Other" "Clerical" exists as "other.clerical"
    And other.clerical changed the page name to "sorting four" 5 days ago
    And I am looking for page summaries sorted by "last updated on" ascending
    When I search for page summaries
    Then the 1st found page summary was "last updated by" "Paige Arthur"
    And the 2nd found page summary was "last updated by" "Another User"
    And the 3rd found page summary was "last updated by" "Other Clerical"
    And the 4th found page summary was "last updated by" "Other User"

  Scenario: I can search for Page Summaries sorted by last updated on descending
    Given the user "Paige" "Arthur" exists as "paige.arthur"
    And I have a page
    And paige.arthur changed the page name to "sorting one" 3 years ago
    And I have another page
    And the user "Other" "User" exists as "other.user"
    And other.user changed the page name to "sorting two" a day ago
    And I have another page
    And the user "Another" "User" exists as "another.user"
    And another.user changed the page name to "sorting three" 7 months ago
    And I have another page
    And the user "Other" "Clerical" exists as "other.clerical"
    And other.clerical changed the page name to "sorting four" 5 days ago
    And I am looking for page summaries sorted by "last updated on" descending
    When I search for page summaries
    Then the 1st found page summary was "last updated by" "Other User"
    And the 2nd found page summary was "last updated by" "Other Clerical"
    And the 3rd found page summary was "last updated by" "Another User"
    And the 4th found page summary was "last updated by" "Paige Arthur"
