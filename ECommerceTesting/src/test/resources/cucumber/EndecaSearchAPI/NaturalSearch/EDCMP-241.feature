@Feature_Natural_Search @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:EDCMP-241

  @EDCMP-241 @TestApi_Search
  Scenario: Product with relevant unit attribute returned for combined.attribute.term
    Given I send a request to the search api for search term attribute.term.1
    And I send a request to the search api for search term attribute.term.2
    And I send a request to the search api for search term combined.attribute.term
    Then The common products from the attribute.term.1 and attribute.term.2 record lists are returned when searching for combined.attribute.term


  @EDCMP-241 @TestApi_Search
  Scenario Outline: Relevant product is returned when using variations of units of measurement in <search.term>
    Given I send a request to the search api for search term <search.term>
    Then The Search API and Assembler responses for the <search.term> contain the desired product
    Examples:
      | search.term           |
      | attribute.variation.1 |
      | attribute.variation.2 |
      | attribute.variation.3 |
      | attribute.variation.4 |
      | attribute.variation.5 |
      | attribute.variation.6 |

  @EDCMP-241 @TestApi_Search
  Scenario Outline: Variations in units of measurement in <search.queries> return the same results
    Given I send a request to the search api for search term <search.term1>
    * I send a request to the search api for search term <search.term2>
    Then The Search API and Assembler responses for <search.term1> and <search.term2> are in the same order
    Examples:
      | search.queries              | search.term1     | search.term2               |
      | search.with.varying.units.1 | attribute.term.2 | attribute.term.2.variation |


  @EDCMP-241  @TestApi_Search
  Scenario: Unit of measurement <search.term> separated by dot or comma return the same results
    Given I send a request to the search api for search term attribute.variation.1
    * I send a request to the search api for search term unit.of.measurement.comma.separated
    Then The Search API and Assembler responses for attribute.variation.1 and unit.of.measurement.comma.separated are in the same order


  @EDCMP-241 @TestApi_Search
  Scenario: Attribute units are localized to specific markets
    Given I send a request to the search api for search term localised.attribute.search.term
    Then the attribute units returned localised.attribute.search.term are localised to specific markets

