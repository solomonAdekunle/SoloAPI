@Feature_Declarative_fetching_search_endpoint @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests


Feature:SAPI-49

  @SAPI-49 @TestApi_Search
  Scenario Outline:  Verify same properties are returned when using productFields=* in a searchQuery a <search.term>
    Given I send a request to search api to get all the attibutes for the products using productFields as a parameter for a <search.term>
    And I get all the properties for the first product for <search.term>
    Then the properties from the latest query should match those in the original query for <search.term>

    Examples:
      | search.term       |
      | generic.search.term |


  @SAPI-49 @TestApi_Search
  Scenario Outline: Verify that the correct attibutes are returned when using the declarative fetching for a <search.term>
    Given I send a request to the search api for search term <search.term>
    Then the same specification attributes should be using declarative fetching should be returned for <search.term>
    Examples:
      | search.term  |
      | search.term.declarative.fetching |