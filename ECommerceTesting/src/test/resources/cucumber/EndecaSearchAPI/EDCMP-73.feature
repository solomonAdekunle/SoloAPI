@Feature_Attribute_Filter_Category @All_Acceptance_Tests

Feature:Get L3 Category Attribute Filters Details

  @TestApi_Search
  Scenario Outline: Return product details for all products belonging to L3 category and whose attributes match the filter values for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get all the attributes and its values for the applied filter for <psf.id>
    Then Then I get the product details for the following details of the initial list of 20 products
    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_421210 |

  @TestApi_Search
  Scenario Outline: Return facet details for all products belonging to L3 category and whose attributes match the filter values for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get all the attributes and its values for the applied filter for <psf.id>
    Then I get the facet details for all the attributes that are applicable to the filtered list of products
    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_430764 |
      | PSF_430885 |

  @TestApi_Search
  Scenario Outline: Return filter details for all the selected filters attributes and filter attribute values for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get all the names and its values for the applied filter for <psf.id>
    Then I get the facet details for all the attributes that are applicable to the filtered list of products
    Examples:
      | psf.id     |
      | PSF_430764 |
      | PSF_430885 |
      | PSF_430995 |
      | PSF_432816 |

  @TestApi_Search
  Scenario Outline: Selected filters should be returned in the same order as listed in the facets for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then I get all the attributes and its values for the applied filter for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_430885 |
      | PSF_430995 |

  @TestApi_Search
  Scenario Outline: Support removal of all applied filter and filter values in the breadbox for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then I check the values in the remove all link in the breadbox
    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_430885 |
      | PSF_430995 |

  @TestApi_Search
  Scenario Outline: Support removal of specific filter in the breadbox for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get all the attributes and its values for the applied filter for <psf.id>
    Then I remove a specific filter from the breadBox for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_430885 |
      | PSF_430995 |

  @TestApi_Search
  Scenario Outline: Support removal of specific filter value in the breadbox for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get all the attributes and its values for the applied filter for <psf.id>
    Then I remove a specific filter value from the breadBox for <psf.id>
    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_430885 |
      | PSF_430995 |

  @TestApi_Search
  Scenario Outline: Return updated record count / Total number of products returned for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get all the names and its values for the applied filter for <psf.id>
    Then I get the updated record count of products returned for <psf.id>
    And the correct bin counts are returned for applied refinements
    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_430885 |
      | PSF_430995 |