@Feature_Search_within_L1_L2_Category @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Search within L1 / L2 category

  @TestApi_Search
  Scenario:  When L1 category is specified and search input parameters, the result set should only include products belonging to specified L1 category
    Given I request search api to get level.three.category
    When I request search API with L1 categoryId and a search term
    Then The result set should only include products belonging to specified category

  @TestApi_Search
  Scenario:  When L1 category seoUrl is specified and search input parameters, the result set should only include products belonging to specified L1 category
    Given I request search api to get level.three.category
    When I request search API with L1 seoUrl and a search term
    Then The result set should only include products belonging to specified category

  @TestApi_Search
  Scenario:  When L1 category internalId is specified and search input parameters, the result set should only include products belonging to specified L1 category
    Given I request search api to get level.three.category
    When I request search API with L1 internalId and a search term
    Then The result set should only include products belonging to specified category

  @TestApi_Search
  Scenario:  When L2 category is specified and search input parameters, the result set should only include products belonging to specified L1 category
    Given I request search api to get level.three.category
    When I request search API with L2 categoryId and a search term
    Then The result set should only include products belonging to specified category

  @TestApi_Search
  Scenario:  When L2 category seoUrl is specified and search input parameters, the result set should only include products belonging to specified L1 category
    Given I request search api to get level.three.category
    When I request search API with L2 seoUrl and a search term
    Then The result set should only include products belonging to specified category

  @TestApi_Search
  Scenario:  When L2 category internalId is specified and search input parameters, the result set should only include products belonging to specified L1 category
    Given I request search api to get level.three.category
    When I request search API with L2 internalId and a search term
    Then The result set should only include products belonging to specified category