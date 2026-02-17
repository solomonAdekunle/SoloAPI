@Feature_Terminal_Node_Browse @All_Acceptance_Tests

Feature:Get Terminal Node Details

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category, then the following category details should be displayed for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>
    Then search api category details should match with assembler response <psf.id> details

    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: Verify bin count in the search api response, for an L3 Category, excludes discontinued and production pack products for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>
    Then The search api bin count results should match the assembler response count <psf.id> details

    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category, a set of 20 products with the following Details should be displayed for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>
    Then Then the following details for an initial list of 20 products should be displayed

    Examples:
      | psf.id     |
      | PSF_421172 |

  @TestApi_Search
  Scenario Outline: Verify Product List in the search api response, for an L3 Category, excludes discontinued and production pack products for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>
    Then The search api bin count results should match the assembler totalNumRecs <psf.id> details

    Examples:
      | psf.id     |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category page with Capacity as a parameter, then result list is sorted by ascending order for <psf.id>
    Given I request the search API end point sorted in ascending order with Capacity as a parameter for L3 Category page for <psf.id>
    And I send a request to the assembler API to get the ascending order of the Capacity records in ascending order for <psf.id>
    Then results list is sorted by Capacity
    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_409430 |
      | PSF_421171 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category page with Capacity as a parameter, then result list is sorted by descending order for <psf.id>
    Given I request the search API end point sorted in descending order with Capacity as a parameter for L3 Category page for <psf.id>
    And I send a request to the assembler API to get the descending order of the Capacity records in ascending order for <psf.id>
    Then results list is sorted by Capacity
    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_409430 |
      | PSF_421171 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category column headers for specification attributes should be in the same order as returned in the facet list <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the product list column headers for specification attributes should be returned in the same order as in the facet list
    Examples:
      | psf.id     |
      | PSF_409469 |

  @TestApi_Search
  Scenario Outline:When  I send a request to search api to get the level three category details with an invalid localeId, then 400 response should be returned for <psf.id>
    Given I send a request to search api to get the level three category details with an invalid localeId for <psf.id>
    Then 400 status code should be returned for <psf.id>
    Examples:
      | psf.id     |
      | PSF_409469 |
      | PSF_409430 |
      | PSF_421171 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an invalid L3 Category, then 404 error code should be returned for <psf.id>
    Given I send request to search api to get the level three category details using <psf.id>
    Then 404 status code should be returned for <psf.id>
    Examples:
      | psf.id                |
      | PSF_40946966666446666 |
      | PSF_33333333333333333 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an invalid L3 Category,with limit  missing as a paramater, then the result list should default to 20 for <psf.id>
    Given I send a request to search api to get the level three category details without limit as parameter for <psf.id>
    Then the result list should default to 20 for <psf.id>
    Examples:
      | psf.id     |
      | PSF_430764 |
      | PSF_421210 |
      | PSF_421231 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category with the limit parameter not being a number, then error code 400 should be returned for <psf.id>
    When I send a request to search api to get the level three category details but the limit parameter is not a number for <psf.id>
    Then 400 status code should be returned for <psf.id>
    Examples:
      | psf.id     |
      | PSF_430764 |
      | PSF_409430 |
      | PSF_421171 |

  @TestApi_Search
  Scenario Outline: Verify the search api bin count retrieved using psf_id should match with the bin counts retrieved using seoUrl namespace details for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>
    Then the search api bin count retrieved using psf_id should match with the bin counts retrieved using seoUrl namespace details for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: Verify the search api bin count retrieved using psf_id should match with the bin counts retrieved using internalId namespace details for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>
    Then the search api bin count retrieved using psf_id should match with the bin counts retrieved using internalId namespace details for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: When I send a request to search api to get the level three category details with an invalid seoUrl, then 404 status code should be returned for <seoUrl>
    When I send a request to search api to get the level three category details with an invalid seoUrl for <seoUrl>
    Then 404 status code should be returned for <seoUrl>
    Examples:
      | seoUrl                                                                         |
      | automation-control-gear/circuit-protection-circuit-breakers/auto-recloserjunk/ |

  @TestApi_Search
  Scenario Outline: When I send a request to search api to get the level three category details with an invalid internalId, then 404 status code should be returned for <internalId>
    When I send a request to search api to get the level three category details with an invalid internalId for <internalId>
    Then 404 status code should be returned for <internalId>
    Examples:
      | internalId          |
      | 3525325334343434342 |

