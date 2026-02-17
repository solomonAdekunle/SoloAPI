@Feature_Filter_Production_Pack_Products_For_Stock_Pattern @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Filter production pack and discontinued products in Search Generic interface for Stock Pattern

  @TestApi_Search
  Scenario: Filter discontinued products in Search Generic interface for Stock Pattern
    Given I request search api with a search term of the stock pattern to check if there are any discontinued products
    Then I want production pack and discontinued products filtered from the search generic interface of the Stock pattern

  @TestApi_Search
  Scenario: Filter production pack in Search Generic interface for Stock Pattern
    Given I request search api with a search term of the stock pattern to check if there are any production packed products
    Then I want production pack and discontinued products filtered from the search generic interface of the Stock pattern

