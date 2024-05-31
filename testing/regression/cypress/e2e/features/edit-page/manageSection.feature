Feature: Page Builder - User can verify manage page here.

  Background:
    Given I am logged in as "superuser" and password ""
    When User navigates to Edit page and views Manage section pop-up window

  Scenario Outline: Manage sections modal to display key elements
    Then User will see the following by "<Content>" "<Type>" "<Description>"
    Examples:
      | Content         | Type    | Description                      |
      | Manage sections | title   | main title                       |
      | Add new section | button  | to add new section               |
      | Patient         | heading | tab heading                      |
      | Six dots        | icon    | with other available sections    |
      | Pencil          | icon    | for editing                      |
      | Trash can       | icon    | for deleting a section           |
      | Cross-eye       | icon    | for not visible / visible        |
      | Close           | button  | to close the pop-up modal window |

  Scenario: Edit / rename an existing section
    And User views the pencil icon to the right of the section name
    When User clicks the pencil icon
    Then Edit section modal window displays
    When User modifies the section name
    And clicks the Save button
    Then Edit section modal closes
    And Inline confirmation message "Your changes have been saved successfully" displays under the Manage sections heading at the top

