@Feature_Exclude_Records_from_ResultsList @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Exclude record from results list

  @SearchApi_Experience_Manager_Tests
  Scenario Outline: When I request the search API with a search term "<searchTerm>" excluded records should not be returned in the results list
    Given I request search api with <searchTerm> as search term for customer <CustomerFilterId>
    Then excluded one record should not be returned in the results list for <searchTerm>, with <Interface>, for <CustomerFilterId>

    Examples:
      | searchTerm                            | Interface                    | CustomerFilterId                   |
      | products.excluded.targeted.searchTerm | products.excluded.searchType | products.excluded.customerFilterId |