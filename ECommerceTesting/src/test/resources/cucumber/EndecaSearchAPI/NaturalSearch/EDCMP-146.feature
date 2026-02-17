@Feature_Natural_Search @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:EDCMP-146

  @EDCMP-146 @TestApi_Search
  Scenario Outline: Special characters are removed for search term: <search.term>
    Given I send a special character request to the search api for <search.term>
    Then the special characters are removed from the search query <search.term> before it gets executed to match <search.term.expected>
    Examples:
      | search.term                                | search.term.expected |
      | valid.length.term.with.special.characters1 | valid.length.term.with.special.characters1.expected |
      | valid.length.term.with.special.characters2 | valid.length.term.with.special.characters2.expected |
      | valid.length.term.with.special.characters3 | valid.length.term.with.special.characters3.expected |


  @EDCMP-146 @TestApi_Search
  Scenario: When the length of the search query invalid.length.term.with.special.characters is invalid, then no search query gets executed and an appropriate error code is returned
    Given I send a special character request to the search api for invalid.length.term.with.special.characters
    Then status code is 400 for the invalid.length.term.with.special.characters search query for markets other than JP and CN



  @EDCMP-146 @TestApi_Search
  Scenario: Search queries of a valid length containing special characters valid.length.term.with.special.characters return the search_keyword and search_keyword_app in the data layer
    Given I send a special character request to the search api for valid.length.term.with.special.characters
    Then the search_keyword is returned in the data layer for valid.length.term.with.special.characters
    And the applied search keyword is returned in the data layer for valid.length.term.with.special.characters


  @EDCMP-146 @TestApi_Search
    Scenario Outline: Searches for product series with <symbol-type> return consistent results with and without the symbol
    Given I send a special character request to the search api for <search-term1>
    * I send a special character request to the search api for <search-term2>
    Then the list of results should be in the same order for <search-term1> and <search-term2>
    Examples:
      | symbol-type                 | search-term1                                 | search-term2                                    |
      | Trademark Symbol            | search.term.with.trademark.symbol            | search.term.without.trademark.symbol            |
      | Registered Trademark symbol | search.term.with.registered.trademark.symbol | search.term.without.registered.trademark.symbol |


