@Feature_Return_Attribute_ID_For_Selected_Attribute_on_TN @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Return Attribute ID for the selected attribute on TN page

  @TestApi_Search
  Scenario: Attribute ID of the dimensions that are not part of specification attributes (e.g.: "brand" ,isNew) should be returned
    Given I send a request to search api to get the level three category details for IsNew
    And I query the applied dimensions for IsNew
    And I verify the facet applied ids are correctly displayed for isNew

  @TestApi_Search
  Scenario: Attribute ID of the dimensions that are not part of specification attributes (e.g.: "brand" ,Lead Time) should be returned
    Given I send a request to search api to get the level three category details for LeadTime
    And I query the applied dimensions for LeadTime
    And I verify the facet applied ids are correctly displayed for leadTime

  @TestApi_Search
  Scenario Outline: Return dimension IDs of the all applied attribute filter values for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get the attributes and its values for the applied filter for <psf.id>
    Then the dimension ids of the applied attribute values match the assembler
    Examples:
      | psf.id     |
      | PSF_430864 |
      | PSF_421210 |

  @TestApi_Search
  Scenario Outline: Return Attribute ID of the all dimensions which have refinements selected for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    And I get the attributes and its values for the applied filter for <psf.id>
    Then the attribute ids of the applied facets match the assembler
    Examples:
      | psf.id     |
      | PSF_430864 |
      | PSF_421210 |

