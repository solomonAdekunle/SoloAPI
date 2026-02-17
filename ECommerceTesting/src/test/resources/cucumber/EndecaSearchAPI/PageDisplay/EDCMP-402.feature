@Feature_Return_Implicit_Refinements @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Return implicit refinements in SAPI response

  @TestApi_Search
  Scenario: Return implicit refinements in the refinements block of the SAPI response
    Given I request search api to get level.three.category
    Then I apply dimension to a l3 category and I check for implicit refinements

  @TestApi_Search
  Scenario: The order of implicit refinements should follow the same logic as other refinements
    Given I request search api to get level.three.category
    When I apply dimension to a l3 category and I check for implicit refinements
    Then I check if the order of the implicit refinements follow the same logic as other refinements

  @TestApi_Search
  Scenario: Return bin counts for implicit refinement
    Given I request search api to get level.three.category
    When I apply dimension to a l3 category and I check for implicit refinements
    Then Bin count for implicit refinement should match the total number of records in the result set

  @TestApi_Search
  Scenario: Ability to configure whether or not implicit refinements are returned in SAPI on per query basis
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    Then I request search API without implicit refinements query so no implicit refinements are returned