@Feature_Enable_Keyword_Redirect_Web_Client @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Disable keyword redirect for mobile (search query, web client)

  @SearchApi_Experience_Manager_Tests @TestApi_Search
  Scenario: Redirects are not triggered if the client actively passes parameter to disable them
    Given I request search api with a valid search term and with redirects disabled
    Then I ensure the user queries return results and do not redirect users to other pages

  @SearchApi_Experience_Manager_Tests @TestApi_Search
  Scenario: Redirects are triggered and Response_Type attribute is set to "Redirect" if the client actively passes parameter to enable them
    Given I request search api with a valid search term and with redirects enabled
    Then I check if the user queries redirect users to other pages