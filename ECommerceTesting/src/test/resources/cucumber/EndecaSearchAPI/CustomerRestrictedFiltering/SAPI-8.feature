@Feature_Customer_Restricted_Filtering_Category_EndPoint @All_Acceptance_Tests

Feature: Customer restricted filtering on the Category Endpoint

  @TestApi_Search @letme
  Scenario: Ensure restricted L1 categories are not returned in the Search API
    Given I request search api to get level.one.category
    Then I check when the customer filter is passed restricted L1 categories are not returned

  @TestApi_Search @letme
  Scenario: Ensure restricted L2 categories are not returned in the Search API
    Given I request search api to get level.two.category
    Then I check when the customer filter is passed restricted L2 categories are not returned

  @TestApi_Search
  Scenario: Ensure restricted L3 categories are not returned in the Search API
    Given I request search api to get level.three.category
    Then I check when the customer filter is passed restricted L3 categories are not returned

  @TestApi_Search
  Scenario: Ensure when all L1 categories are hidden, the book section should not be returned
    Given I request search api to get level.one.category
    Then I check when the customer filter is passed restricted book is not returned

  @TestApi_Search
  Scenario: When an L1 categories is selected ensure the hidden products are not included in the bin count
    Given I request search api to get level.one.category
    Then I ensure the L1 category hidden products are not returned in the bin count

  @TestApi_Search
  Scenario: When an L2 categories is selected ensure the hidden products are not included in the bin count
    Given I request search api to get level.two.category
    Then I ensure the L1 category hidden products are not returned in the bin count

  @TestApi_Search
  Scenario: When an L3 categories is selected ensure the hidden products are not included in the bin count
    Given I request search api to get level.three.category
    Then I ensure the L1 category hidden products are not returned in the bin count