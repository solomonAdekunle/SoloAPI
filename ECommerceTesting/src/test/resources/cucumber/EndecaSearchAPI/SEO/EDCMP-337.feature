@Feature_Return_category_hierarchy_productId @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Return category hierarchy for specified product id (Temporary Disabling Due to SER-581, Excessive Datadog logs Due to Incorrect Sitemaps)

  @TestApi_Search
  Scenario: Category hierarchy information upto the L1 node is returned for the specified product id
    Given I request search api to get level.three.category
    Then I request search api with a product Id and validate Category hierarchy information

  @TestApi_Search
  Scenario: Appropriate error code is returned if the product id is invalid
    Given When I request search api with an invalid product Id
    Then the search api should return 200 statuscode