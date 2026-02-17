@Feature_Natural_Search @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Natural Search

  @EDCMP-392
  Scenario Outline: Stock number searches with prefix's and hyphens have these characters removed before the '<search.term>' search query is submitted to Endeca
    Given I send a request to the search api for search term <search.term>
    Then the search keyword applied should match the <search.term> with designated characters removed
    Examples:
      | search.term             |
      | stock.number.pattern.1  |
      | stock.number.pattern.2  |
      | stock.number.pattern.3  |
      | stock.number.pattern.4  |
      | stock.number.pattern.5  |
      | stock.number.pattern.6  |
      | stock.number.pattern.7  |
      | stock.number.pattern.8  |
      | stock.number.pattern.9  |
      | stock.number.pattern.10 |
      | stock.number.pattern.11 |
      | stock.number.pattern.12 |
      | stock.number.pattern.13 |
      | stock.number.pattern.14 |
      | stock.number.pattern.15 |
      | stock.number.pattern.16 |
      | stock.number.pattern.17 |
      | stock.number.pattern.18 |


  @EDCMP-392
  Scenario Outline: Stock number searches for '<search.term>' which do not match the stock number pattern are not processed before being submitted to endeca
    Given I send a request to the search api for search term <search.term>
    Then the <search.term> and search keyword applied are the same
    Examples:
      | search.term                    |
      | invalid.stock.number.pattern.1 |
      | invalid.stock.number.pattern.2 |
      | invalid.stock.number.pattern.3 |
      | invalid.stock.number.pattern.4 |


  @EDCMP-392
  Scenario Outline: Double byte stock number searches with prefix's and hyphens have these characters removed before the '<search.term>' search query is submitted to Endeca
    Given I send a request to the search api for search term <search.term>
    Then the <search.term> should have designated characters removed and be converted to <search.term.single.byte>
    Examples:
      | search.term                         | search.term.single.byte |
      | double.byte.stock.number.pattern.1  | stock.number.pattern.1  |
      | double.byte.stock.number.pattern.2  | stock.number.pattern.2  |
      | double.byte.stock.number.pattern.3  | stock.number.pattern.3  |
      | double.byte.stock.number.pattern.4  | stock.number.pattern.4  |
      | double.byte.stock.number.pattern.5  | stock.number.pattern.5  |
      | double.byte.stock.number.pattern.6  | stock.number.pattern.6  |
      | double.byte.stock.number.pattern.7  | stock.number.pattern.7  |
      | double.byte.stock.number.pattern.8  | stock.number.pattern.8  |
      | double.byte.stock.number.pattern.9  | stock.number.pattern.9  |
      | double.byte.stock.number.pattern.10 | stock.number.pattern.10 |
      | double.byte.stock.number.pattern.11 | stock.number.pattern.11 |
      | double.byte.stock.number.pattern.12 | stock.number.pattern.12 |
      | double.byte.stock.number.pattern.13 | stock.number.pattern.13 |
      | double.byte.stock.number.pattern.14 | stock.number.pattern.14 |
      | double.byte.stock.number.pattern.15 | stock.number.pattern.15 |
      | double.byte.stock.number.pattern.16 | stock.number.pattern.16 |
      | double.byte.stock.number.pattern.17 | stock.number.pattern.17 |
      | double.byte.stock.number.pattern.18 | stock.number.pattern.18 |

  @EDCMP-392
  Scenario Outline: Double Byte stock number searches for '<double.byte.search.term>' which do not match the stock number pattern are not processed before being submitted to endeca
    Given I send a request to the search api for search term <double.byte.search.term>
    Then the <double.byte.search.term> and search keyword applied are the same
    Examples:
      | double.byte.search.term                    |
      | double.byte.invalid.stock.number.pattern.1 |
      | double.byte.invalid.stock.number.pattern.2 |
      | double.byte.invalid.stock.number.pattern.3 |
      | double.byte.invalid.stock.number.pattern.4 |