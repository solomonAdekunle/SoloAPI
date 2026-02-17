@Feature_Alternative_Product @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Expose the record detail for alternative products for given product id

  @TestApi_Search
  Scenario: Product attribute details are returned when one alternative product is available
    Given I send a request to the Search API alternatives end point for stock number stock.number.with.alternative.product with a limit of 1
    Then 1 alternative record is returned in the Search API response for stock.number.with.alternative.product

  @TestApi_Search
  Scenario: Product attribute details are returned when more than one alternative product is available
    Given I send a request to the Search API alternatives end point for stock number stock.number.with.alternative.product with a limit of 2
    Then 2 alternative record is returned in the Search API response for stock.number.with.alternative.product

  @TestApi_Search
  Scenario: No product attribute details are returned when an alternative is not available
    Given I send a request to the Search API alternatives end point for stock number stock.number.with.no.alternative.product with a limit of 1
    Then no alternatives are returned and an appropriate error message is returned for stock.number.with.no.alternative.product

  @TestApi_Search
  Scenario: No product attribute details are returned when the request is made with an invalid stock number
    Given I send a request to the Search API alternatives end point for stock number invalid.stock.number with a limit of 1
    Then an appropriate error message is returned for invalid.stock.number

