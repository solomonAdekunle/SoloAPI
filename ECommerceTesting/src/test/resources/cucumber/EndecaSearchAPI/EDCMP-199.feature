@Feature_FACET_DETAILS_L3_TERMINAL_NODE  @FACET_DETAILS_L3_TERMINAL_NODE @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests


Feature:EDCMP-199

  @TestApi_Search @FACET_DETAILS_L3_TERMINAL_NODE_Test
  Scenario Outline: When I request the search API end point for an L3 Category, then all specification attribute facets should be returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then I get the specification attributes from the search API response for <psf.id>
    Examples:
      | psf.id     |
      |PSF_432971  |
      | PSF_430953 |
      | PSF_430885 |

  @TestApi_Search @FACET_DETAILS_L3_TERMINAL_NODE_Test
  Scenario Outline: When I request the search API end point for an L3 Category, all dimension ids and bin count for each facet values including isNew and Lead Time should be returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I send a request to the assembler API to get the bin count and dimension ids for <psf.id>
    Examples:
      | psf.id     |
      | PSF_430764 |
      | PSF_430885 |
      | PSF_430995 |

  @TestApi_Search @FACET_DETAILS_L3_TERMINAL_NODE_Test
  Scenario Outline: When I request the search API end point for an L3 Category, brand as a facet should be returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then I get the list of brand attribute values from the search API response for <psf.id>
    Examples:
      | psf.id     |
      | PSF_430764 |
      | PSF_430885 |
      | PSF_430995 |
      | PSF_432816 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category, production packed and discontinued products should be excluded from the facet value bin count for <psf.id>
    Given I request the search API end point for an L3 Category for <psf.id>
    And I send a request to the assembler API to get the facet bin count for <psf.id>
    Examples:
      | psf.id     |
      | PSF_430697 |
      | PSF_430764 |
      | PSF_430885 |
      | PSF_430995 |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category, for each facet which does not contain a unit of measure, the ordering of attribute values should be alphanumeric for <psf.id>
    Given I request the search API end point for an L3 Category for <psf.id>
    Then the ordering of attribute values of the facet with alphanumerics should be in ascending order
    Examples:
      | psf.id     |
      | PSF_430764 |
      | PSF_430885 |
      | PSF_430995 |
