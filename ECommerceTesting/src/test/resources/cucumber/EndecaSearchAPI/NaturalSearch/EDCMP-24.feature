@Feature_Multivariate_Testing_Capability @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Support existing Search Multivariate testing capability

  @EDCMP-24 @TestApi_Search
  Scenario: Relevant search configuration set-up / path is triggered based on the search_config parameter value passed by the client
    Given I send a request with Search Config 1 to the search api for search term l3.category.details.term
    Then the search config of 1 passed in will be reflected in the Search API response for l3.category.details.term


  @EDCMP-24 @TestApi_Search
  Scenario: If invalid search_config parameter is passed then default search configuration set-up / path is triggered
    Given I send a request with Search Config 7 to the search api for search term l3.category.details.term
    Then the search config of 0 passed in will be reflected in the Search API response for l3.category.details.term
