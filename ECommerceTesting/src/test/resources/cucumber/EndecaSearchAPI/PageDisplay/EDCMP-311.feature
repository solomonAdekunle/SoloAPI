@Feature_PageDisplay_API @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:PageDisplay_API

  @EDCMP-311 @TestAPI_Search @SAPI_Quick_Acceptance_Tests
  Scenario Outline: The Search Results Page response for <search.term> will contain all relevant top-level attributes with a limit of 0
    Given I send a request for <search.term> with a limit of 0
    Then The response for <search.term> contains all relevant top-level attributes
    And The results list block for <search.term> contains sortOptions and Pagination Information but not any records
    And The pagination for <search.term> will report actual results count, max page and page=1
    Examples:
      | search.term            |
      | keyword.interface.term |


  @EDCMP-311 @TestAPI_Search
  Scenario Outline: The Terminal Node Page response for <psf.id> will contain all relevant top-level attributes with a limit of 0
    Given I send a request with a limit of 0 to get the level three category details using <psf.id>
    Then The response for <psf.id> contains all relevant top-level attributes
    And The results list block for <psf.id> contains sortOptions and Pagination Information but not any records
    And The pagination for <psf.id> will report actual results count, max page and page=1
    Examples:
      | psf.id                       |
      | terminal.node.psf.param.term |

  @EDCMP-311 @TestAPI_Search
    Scenario Outline: The Terminal Node Page response with <search.term> and <psf.id> will contain all relevant top-level attributes with a limit of 0
    Given I send a search API request for <search.term> in <psf.id> with limit set to 0
    Then The response for <search.term> contains all relevant top-level attributes
    And The results list block for <search.term> contains sortOptions and Pagination Information but not any records
    And The pagination in the response received for <search.term> and <psf.id> will report actual results count, max page and page=1
    Examples:
      | search.term    | psf.id    |
      | l3.search.term | l3.psf.id |


