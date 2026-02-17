@Feature_SearchAPI_Books_Hierarchy @SearchAPI_Books_HierarchyRegression @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:EDCMP-256-SearchAPI-Books-Hierarchy

  @TestApi_Search @sdsdsd444 @sapiRegression
  Scenario: Verify books are returned based on current display order for L0 Categories
    Given I request search api to get level.zero.category
    Then the search api should return all child level.zero.category in alphanumeric order within their parent category
