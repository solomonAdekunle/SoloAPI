@Feature_MPN_Commercially_Sensitive_Products @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Do not return MPN for commercially sensitive product

  @TestApi_Search
  Scenario Outline: MPN should not be returned as part of the SAPI response for <psf.id> if a product is commercially sensitive
    Given I send a request to search api to get the level three category details using <psf.id>
    Then I check if the product is commercially sensitive

    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_412614 |

  @TestApi_Search
  Scenario Outline: MPN should not be returned as part of the SAPI response for <searchTerm> for a stock number search
    Given I request search api with <searchTerm> as search term
    Then I check if the search term returns products that are commercially sensitive with <Interface>

    Examples:
      | searchTerm | Interface         |
      | 734-8885   | I18NRSStockNumber |

  @TestApi_Search
  Scenario Outline: MPN should not be returned for commercially sensitive products for <searchTerm>
    Given I request search api with <searchTerm> as search term
    Then I check if the search term returns products that are commercially sensitive with <Interface>

    Examples:
      | searchTerm            | Interface         |
      | power%20drills%20mini | I18NSearchGeneric |
      | VDE/1000%20V          | I18NSearchGeneric |



  @TestApi_Search
  Scenario: P_commerciallySensitive Key should returned value as Y as part of the SAPI response when product field is specified for search.term.by.mpn if a product is commercially sensitive
    Given I request search api with search.term.by.commerciallysensitive as search term with product field equal P_manufacturerPartNumber
    Then I should see search.term.by.commerciallysensitive within the SAPI response for P_recordID
    And P_commerciallySensitive should be displayed as key within the SAPI response for search.term.by.commerciallysensitive
    And search.term.by.commerciallysensitive should have P_commerciallySensitive value to equal Y within the response

  @TestApi_Search
  Scenario: validate that SAPI will return three keys when product field is specified for search.by.leds
    Given I request search api with search.term.leds as search term with product field equal P_manufacturerPartNumber
    Then I should see search.term.leds within the SAPI response for P_recordID
    And P_commerciallySensitive should be displayed as key within the SAPI response for search.term.leds
    And I should see search.term.leds within the SAPI response for P_manufacturerPartNumber

  @TestApi_Search
  Scenario: validate that SAPI will return three keys when product field is specified for search by psf_id
    Given I request search api with search.term.psfID as search term for terminal node with product field equal P_manufacturerPartNumber
    Then I should see search.term.psfID within the SAPI response for P_recordID
    And P_commerciallySensitive should be displayed as key within the SAPI response for search.term.psfID
    And I should see search.term.psfID within the PSFID response for P_manufacturerPartNumber

  @TestApi_Search @low1236
  Scenario: Validate that SAPI Response will return expected keys when product field is specified for non commerically sensitive product
    Given I request search api with 7790713 as search term with Products field equal P_manufacturerPartNumber,availableForSale
    Then I should see product response with stock number 7790713 returning P_familyID
     And I should see product response with stock number 7790713 returning P_recordID
     And I should see product response with stock number 7790713 returning P_commerciallySensitive
     And I should see product response with stock number 7790713 returning P_unpriced
     And I should see product response with stock number 7790713 returning P_breakPrice1
     And I should see product response with stock number 7790713 returning availableForSale
     And I should see product response with stock number 7790713 returning P_manufacturerPartNumber

  @TestApi_Search
  Scenario: Validate that Manufacturing Part Number will not be returned as part of the expected key for a commercially sensitive product when product field is specified
    Given I request search api with 0355063 as search term with Products field equal P_manufacturerPartNumber,availableForSale
    Then I should see product response with stock number 0355063 returning P_familyID
     And I should see product response with stock number 0355063 returning P_recordID
     And I should see product response with stock number 0355063 returning P_commerciallySensitive
     And I should see product response with stock number 0355063 returning P_unpriced
     And I should see product response with stock number 0355063 returning P_breakPrice1
     And I should see product response with stock number 0355063 returning availableForSale
     And I should not see product response with stock number 0355063 returning  P_manufacturerPartNumber
