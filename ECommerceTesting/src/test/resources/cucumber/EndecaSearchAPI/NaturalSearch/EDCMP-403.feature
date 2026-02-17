@Feature_Remove_special_character_by_SearchPattern @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Ability to configure removal of special character by search pattern

  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" excluded brands should not be returned in the results list
    Given I request search api with <searchTerm> as search term
    Then I check no conversion is applied to byte format of the search term

    Examples:
      | searchTerm |
      | ＬＥＤ        |


  @TestApi_Search
  Scenario: When I request the search API with a search term "<searchTerm>" excluded brands should not be returned in the results list
    Given I request search api with ４４４－４４４４ as search term
    Then I check the conversion is applied from double to Single 4444444 on stock number pattern