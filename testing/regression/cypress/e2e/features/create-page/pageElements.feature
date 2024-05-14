Feature: User can verify existing create new page elements here.

  Background:
    Given I am logged in as "superuser" and password ""
    When User navigates to Create New Page and views the page

  Scenario Outline: Verify Add new page displays required key elements
    Then User should see the following required elements by "<Items required>" "<Type>"
    Examples:
      | Items required                                                         | Type                             |
      | Page Builder                                                           | Title heading                    |
      | Page Library                                                           | Link (to return to page library) |
      | Create new page                                                        | Page heading                     |
      | Let's fill out some information about your new page before creating it | Text                             |
      | Condition(s)                                                           | Heading, drop-down box           |
      | Can't find the condition you're looking for?                           | Text                             |
      | Create a new condition here                                            | Link                             |
      | Page name                                                              | Heading, text field              |
      | Event type                                                             | Heading, drop-down box           |
      | Template                                                               | Heading, drop-down box           |
      | Can't find the template you're looking for?                            | Heading and Link                 |
      | Import a new template here                                             | Heading and Link                 |
      | Page description                                                       | Heading, text fields             |
      | Data mart name                                                         | Heading, text fields             |
      | Cancel                                                                 | Buttons                          |
      | Create page                                                            | Buttons                          |
