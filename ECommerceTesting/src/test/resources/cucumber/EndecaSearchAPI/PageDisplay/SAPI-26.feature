@Feature_Ability_to_configure_sort_order_in_result_list_XM_cartridge @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:SAPI-26

  @SAPI-26
  Scenario Outline:  RSPro products are boosted at the top of the result set when browsing to a l3 category page for <seo url>
  #Precondition: A rule has been created for a term to promote RsPro brand
    Given I send a request to search api to get the level three category using <seo.url>
    Then the correct path for boosting RS PRO bias is triggered for <seo.url>
    And all RS PRO products should be boosted at the top of the records for <seo.url>

    Examples:
      | seo.url         |
      | seo.url.example |