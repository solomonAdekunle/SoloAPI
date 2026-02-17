@Feature_sort_spec_for_sort_options @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Return sort spec for sort options

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category based on relevance, sort spec for sort options are returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the sortSpec for relevance should be displayed for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category based on popularity, sort spec for sort options are returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the sortSpec for popularity should be displayed for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category based on Low to High, sort spec for sort options are returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the sortSpec for Price: Low to High should be displayed for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category based on High to Low, sort spec for sort options are returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then the sortSpec for Price: High to Low should be displayed for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |