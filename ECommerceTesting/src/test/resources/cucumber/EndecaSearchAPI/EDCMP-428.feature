@Feature_TopLevel_Dimension_Ids @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Return top level dimension with an internalId in SAPI response

  @TestApi_Search
  Scenario: Return internalId for top level dimension from Refinement section
    Given I request search api to get level.three.category
    Then I check if InternalId is returned for each top level dimension in refinement section

  @TestApi_Search
  Scenario: Return internalId for top level dimension from breadbox
    Given I request search api to get level.three.category
    Then I check if InternalId is returned for each top level dimension from breadbox