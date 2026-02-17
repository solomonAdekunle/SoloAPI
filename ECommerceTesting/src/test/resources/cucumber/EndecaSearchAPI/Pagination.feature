@Feature_Pagination_SAPI @All_Acceptance_Tests

Feature: Pagination_SAPI

  @TestApi_Search
  Scenario Outline: When I request an end-point for an L3 Category, then by default the number of products returned is 20 and the default pagination number is 1 for <psf.id>
    Given I send a request to search api to get the level three category details and no limit is specified by client for <psf.id>
    Then the result list should default to 20 for <psf.id>
    And the default page Pagination number is 1 for <psf.id>

    Examples:
      | psf.id     |
      | PSF_430764 |
      | PSF_421210 |
      | PSF_421231 |

  @TestApi_Search
  Scenario Outline: When I request an end-point for an L3 Category for more than a 100 products, then the absolute maximum number of products returned is 100 for <psf.id>
    Given I send a request to search api to get the level three category details and the limit is more than 101 products for <psf.id>
    Then the number of products returned is 100 for <psf.id>

    Examples:
      | psf.id     |
      | PSF_430864 |
      | PSF_438287 |
      | PSF_421231 |

  @TestApi_Search
  Scenario Outline: When I request an end-point for an L3 Category with an overridden limit, then the new limit should be returned (99) for <psf.id>
    Given I request an end-point for an L3 Category with a specified limit of 99 products for <psf.id>
    Then the number of products returned is 99 for <psf.id>

    Examples:
      | psf.id     |
      | PSF_430864 |
      | PSF_438287 |
      | PSF_421231 |

  @TestApi_Search
  Scenario Outline: When I request an end-point for an L3 Category with an overridden limit, then the new limit should be returned (100) for <psf.id>
    Given I request an end-point for an L3 Category with a specified limit of 100 products for <psf.id>
    Then the number of products returned is 100 for <psf.id>

    Examples:
      | psf.id     |
      | PSF_430864 |
      | PSF_438287 |
      | PSF_421231 |

  @TestApi_Search
  Scenario Outline: When I request an end-point for an L3 Category, then last and the current page pagination number should be returned for <psf.id>
    When I send a request to search api to get the level three category details with pagination page 2 specified for <psf.id>
    And the current page pagination number should be returned for <psf.id>
    Then last page pagination number should be returned for <psf.id>

    Examples:
      | psf.id     |
      | PSF_421231 |
      | PSF_430864 |
      | PSF_438287 |

  @TestApi_Search
  Scenario: When I request an end-point for an L3 Category, then the total number of products in the results set and the limit should be returned
    Given I request search api to get level.three.category
    Then I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    When I send a request to search api to get the level three category details with a pagination page 2
    Then the total number of products in the results set should be returned
    And the limit of records per page should be returned

  @TestApi_Search
  Scenario: Client can set the current page pagination number to return products for that page
    Given I request search api to get level.three.category
    Then I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    Given I request an end-point for an L3 Category for pagination page 3 and limit 20
    Then the products returned match the products from the assembler for page 40 and limit 20

  @TestApi_Search
  Scenario: Given I have requested an end-point for an L3 Category, when there are results after the current page, then the next set of products should be returned for <psf.id>
    Given I request search api to get level.three.category
    And I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    And I request an end-point for an L3 Category for pagination page 3 and limit 50
    And the products returned match the products from the assembler for page 100 and limit 50
    When I request an end-point for an L3 Category for pagination page 4 and limit 50
    Then the products returned match the products from the assembler for page 150 and limit 50

  @TestApi_Search
  Scenario: Given I have requested an end-point for an L3 Category, when there are results after the current page, then the previous set of products should be returned for <psf.id>
    Given I request search api to get level.three.category
    And I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    And I request an end-point for an L3 Category for pagination page 3 and limit 100
    And the products returned match the products from the assembler for page 200 and limit 100
    When I request an end-point for an L3 Category for pagination page 2 and limit 100
    Then the products returned match the products from the assembler for page 100 and limit 100

  @TestApi_Search
  Scenario: Client has requested to get the results for the first page for <psf.id>
    Given I request search api to get level.three.category
    And I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    And I request an end-point for an L3 Category for pagination page 3 and limit 100
    And the products returned match the products from the assembler for page 200 and limit 100
    When I request an end-point for an L3 Category for pagination page 1 and limit 100
    Then the products returned match the products from the assembler for page 0 and limit 100

  @TestApi_Search
  Scenario: Client has requested to get the results for the last page for <psf.id>
    Given I request search api to get level.three.category
    And I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    And I request an end-point for an L3 Category for pagination page 1 and limit 100
    And the products returned match the products from the assembler for page 0 and limit 100
    When I request an end-point for an L3 Category for pagination page 3 and limit 100
    Then the products returned match the products from the assembler for page 200 and limit 100

  @TestApi_Search
  Scenario: When page number is out of bounds, greater than the lastPage, return 400 status code
    Given I request search api to get level.three.category
    And I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    When I request an end-point for an L3 Category for out of bounds pagination page 7 and limit 50
    Then 400 status code is returned

  @TestApi_Search
  Scenario: When page number is an integer < 1, set page number to 1 (and return first page of results) for <psf.id>
    Given I request search api to get level.three.category
    And I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    When I request an end-point for an L3 Category for pagination page -1 and limit 20
    Then the products returned match the products from the assembler for page 0 and limit 20

  @TestApi_Search
  Scenario: When limit is an integer <0, set limit to 20 for <psf.id>
    Given I request search api to get level.three.category
    And I get all the level three category ids with minimum count 200 and maximum 300 for level.three.category
    When I request an end-point for an L3 Category for pagination page -2 and limit 20
    Then the products returned match the products from the assembler for page 0 and limit 20

  @TestApi_Search
  Scenario Outline: When a page is not a a number (NaN), return status code 400 for <psf.id>
    When I send a request to search api to get the level three category details with a pagination page junk specified for <psf.id>
    Then 400 status code should be returned for <psf.id>

    Examples:
      | psf.id     |
      | PSF_421231 |
      | PSF_438287 |

  @TestApi_Search
  Scenario Outline: When a limit is not a a number (NaN), return status code 400 for <psf.id>
    And I send a request to search api to get the level three category details with a limit junk specified for <psf.id>
    Then 400 status code should be returned for <psf.id>

    Examples:
      | psf.id     |
      | PSF_421231 |
      | PSF_438287 |


