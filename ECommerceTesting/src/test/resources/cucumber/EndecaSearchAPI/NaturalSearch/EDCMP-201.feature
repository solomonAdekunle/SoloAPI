@Feature_Disable_Keyword_Redirect @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Disable keyword redirect for mobile (search query, XM keyword)

  @SearchApi_Experience_Manager_Tests
  Scenario: Redirects are not triggered for valid search query that match the XM keyword redirect entry
    Given I request search api with a valid search term and with redirects disabled
    Then I ensure the user queries return results and do not redirect users to other pages

  @SearchApi_Experience_Manager_Tests @TestApi_Search
  Scenario: Redirects are triggered for valid search query that match the XM keyword redirect entry
    Given I request search api with a valid search term and with redirects enabled
    Then I check if the user queries redirect users to other pages