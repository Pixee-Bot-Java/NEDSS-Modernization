@open_investigations
Feature: Open Investigations

  Background:
    Given I have the authorities: "VIEWWORKUP-PATIENT,VIEW-INVESTIGATION" for the jurisdiction: "ALL" and program area: "STD"
    And there are 2 patients

  Scenario: I can retrieve open investigations for a particular patient
    Given a patient has open investigations
    When I search for open investigations for a patient
    Then I receive a list of open investigations

  Scenario: If a patient has no open investigations, none are returned
    Given a patient does not have open investigations
    When I search for open investigations for a patient
    Then no open investigations are returned
