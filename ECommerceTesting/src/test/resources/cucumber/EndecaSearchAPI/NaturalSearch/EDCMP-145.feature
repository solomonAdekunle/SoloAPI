@Feature_Natural_Search @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:EDCMP-145

  @EDCMP-145 @TestApi_Search
  Scenario: Search query is not executed for search queries with an invalid length and appropriate error code is returned
    Given I send a request to the search api for search term invalid.length.term
    Then status code is 400 for the invalid.length.term search query for markets other than JP and CN

  @EDCMP-145 @TestApi_Search
  Scenario: Search query is executed for search queries with a valid length
    Given I send a request to the search api for search term search.term
    Then 200 status code returned for search.term API response

  @EDCMP-145 @TestApi_Search
  Scenario: Search terms matching RS stock number pattern,should trigger use of RS Stock number interface with the 'matchall' match mode and data layer attributes should be returned
    Given I send a request to the search api for search term simplified.search.term
    Then the Search API and Assembler API results are in the same order for simplified.search.term
    And the query info block for stock.number.interface.1 should be displayed correctly for simplified.search.term

  @EDCMP-145 @TestApi_Search
  Scenario: Search terms containing single alphanumeric search term that does not match RS stock number pattern should trigger use of Simplified search interface with the 'matchallpartial' match mode and data layer attributes should be returned
    Given I send a request to the search api for search term keyword.interface.term
    Then the Search API and Assembler API results are in the same order for keyword.interface.term
    And the query info block for keyword.interface should be displayed correctly for keyword.interface.term

  @EDCMP-145 @TestApi_Search
  Scenario: Search terms containing multiple alphanumeric search terms should trigger use of Simplified search interface with the 'matchallpartial' match mode and data layer attributes should be returned
    Given I send a request to the search api for search term simplified.search.term.cascade
    Then the Search API and Assembler API results are in the same order for simplified.search.term.cascade
    And the query info block for stock.number.interface.2 should be displayed correctly for simplified.search.term.cascade

  @EDCMP-145 @TestApi_Search
  Scenario: Stock number searches triggering RS Stock Number interface and not returning any results should cascade to simplified search interface with the 'matchallpartial' match mode and data layer attributes should be returned
    Given I send a request to the search api for search term catch.all.default.term
    Then the Search API and Assembler API results are in the same order for catch.all.default.term
    And the query info block for catch.all.default.interface should be displayed correctly for catch.all.default.term

  @EDCMP-145 @TestApi_Search
  Scenario Outline: Stock number searches should return "<type>" products in the result set
    Given I send a request to the search api for search term <search.term>
    Then the Search API record for <search.term> is returned
    Examples:
      | type              | search.term              |
      | Discontinued      | discon.stock.number      |
      | Production Packed | prod.packed.stock.number |


  @EDCMP-145 @TestApi_Search
    Scenario Outline: <search.term> searches should not return discontinued and production pack products in the result set
    Given I send a request to the search api for search term <search.term>
    Then the extra Assembler API results are discontinued for <search.term>
    Examples:
      | search.term         |
      | mpn.search.term     |
      | keyword.search.term |

    @EDCMP-145 @TestApi_Search
      Scenario Outline: Stock number searches for <search.term> with spaces return relevant results
      Given I send a request to the search api for search term <search.term>
      Then the Search API record for <search.term> is returned
      And the record returned is relevant to the <search.term>
      Examples:
        | search.term                          |
        | stock.number.with.space.at.beginning |
        | stock.number.with.space.at.end       |



