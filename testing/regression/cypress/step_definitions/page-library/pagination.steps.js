import {Then} from "@badeball/cypress-cucumber-preprocessor";
import {pageLibraryPaginationPage} from "@pages/page-library/pagination.page";

Then("User should see by default 10 rows of pages listed in the library", () => {
    pageLibraryPaginationPage.checkForDefaultRows();
});

Then("User select 20 left footer of the page to show the list of pages", () => {
    pageLibraryPaginationPage.selectNumberOfRows('20');
});

Then("User should see only 20 rows in the library and for each subsequent list where applicable", () => {
    pageLibraryPaginationPage.checkDisplayingNumberOfRowsSubsequently(20);
});
Then("User navigates to the Create page", () => {
    pageLibraryPaginationPage.navigateToCreatePage();
});
Then("User should only see 10 rows in the library and for each subsequent list where applicable", () => {
    pageLibraryPaginationPage.checkForDefaultRows();
});
Then("User select 30 left footer of the page to show the list of pages", () => {
    pageLibraryPaginationPage.selectNumberOfRows('30');
});
Then("User should see only 30 rows in the library and for each subsequent list where applicable", () => {
    pageLibraryPaginationPage.checkDisplayingNumberOfRowsSubsequently(30);
});
Then("User select 50 left footer of the page to show the list of pages", () => {
    pageLibraryPaginationPage.selectNumberOfRows('50');
});
Then("User should see only 50 rows in the library and for each subsequent list where applicable", () => {
    pageLibraryPaginationPage.checkDisplayingNumberOfRowsSubsequently(50);
});
