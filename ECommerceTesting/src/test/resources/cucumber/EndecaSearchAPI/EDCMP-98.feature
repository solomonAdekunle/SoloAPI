@Feature_Sort_By @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:EDCMP-98

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category, relevance, popularity, Price: Low to High and Price: High to Low sort options are returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then relevance,popularity,Price High to Low, Price Low to High sort by options should be displayed
      | Relevance         |
      | Popularity        |
      | Price: Low to High |
      | Price: High to Low |

    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_421172 |
      | PSF_409430 |


  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category page, then result list is sorted by Relevance for <psf.id>
    Given I send a request to search api end point for an L3 Category page <psf.id>
    And I send a request to the assembler API to get the records for <psf.id>
    Then results list is sorted by relevance
  Examples:
  | psf.id     |
  | PSF_421172 |
  | PSF_409430 |
  | PSF_421171 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category page with popularity as a parameter, then result list is sorted by Popularity for <psf.id>
    Given I request the search API end point sorted with Popularity as a parameter for L3 Category page for <psf.id>
    And I send a request to the assembler API to get the popularity for <psf.id>
    Then results list is sorted by popularity
    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_409430 |
      | PSF_421171 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category page with Price High to Low as a parameter, then result list is sorted by descending order for <psf.id>
    Given I request the search API end point sorted with Price High to Low as a parameter for L3 Category page for <psf.id>
    And I send a request to the assembler API to get the records in descending order for <psf.id>
    Then results list is sorted by Price High to Low
    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_409430 |
      | PSF_421171 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category page with Price Low to High as a parameter, then result list is sorted by descending order for <psf.id>
    Given I request the search API end point sorted with Price Low to High as a parameter for L3 Category page for <psf.id>
    And I send a request to the assembler API to get the records in ascending order for <psf.id>
    Then results list is sorted by Price Low to High
    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_409430 |
      | PSF_421171 |

