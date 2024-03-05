@delete_draft_page
Feature: Delete Draft Page

    Background:
        Given pages exist
        And I am logged in
        And I can "LDFAdministration" any "System"

    Scenario: I can delete draft page
        Given I create a delete page request with draft page
        When I send a delete page request
        Then the draft is deleted and the page changed to published

