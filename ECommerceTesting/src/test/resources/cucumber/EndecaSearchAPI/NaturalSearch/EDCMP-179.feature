@Feature_Natural_Search @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:EDCMP-179

  @EDCMP-179 @TestApi_Search
  Scenario Outline: Queries for <search.term> executed with searchType=MPN parameter should trigger use of MPN interface
    Given I send a MPN only request to the search api for search term <search.term>
    And the query info block for mpn.only.term should be displayed correctly for mpn.only.term
    Examples:
      | search.term   |
      | mpn.only.term |


