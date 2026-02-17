@Feature_Remove_reserved_characters_from_the_search_query @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Remove_reserved_characters_from_the_searchQuery

  @TestApi_Search
  Scenario: Remove reserved characters from the search term that match searchType=MPN
    Given I request search api for reserved.character.mpn that matches mpnType
    Then the reserved characters should be removed from the query for reserved.character.mpn

  @TestApi_Search
  Scenario: For search terms matching RS Stock number pattern - pad the numbers that are less than 7 digits long to 7 digits with 0's and return the original search query
    Given I send a request to the search api for search term stock.number.example
    Then the stock number is padded to 7 digits with 0s for stock.number.example
    And the original search query should be returned for stock.number.example

