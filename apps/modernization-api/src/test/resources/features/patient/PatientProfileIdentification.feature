@patient-profile-identifications
  Feature: Patient Profile Identifications

    Background:
      Given I have a patient

    Scenario: I cannot retrieve patient identifications for a patient
      Given I have the authorities: "FIND-PATIENT" for the jurisdiction: "ALL" and program area: "STD"
      Then the profile has no associated identifications

    Scenario: I cannot retrieve patient identifications without proper authorities
      Given I have the authorities: "OTHER" for the jurisdiction: "ALL" and program area: "STD"
      Then the profile identifications are not accessible

    Scenario: I can retrieve patient identifications for a patient
      Given I have the authorities: "FIND-PATIENT" for the jurisdiction: "ALL" and program area: "STD"
      And the patient has 5 identifications
      When the profile requests at least 3 identifications
      Then the profile has 5 associated identifications
      And the profile shows 3 associated identifications
