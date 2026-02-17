@Feature_Exclude_Products_from_ResultsList @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Exclude products from results list

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" excluded brands should not be returned in the results list
    Given I request search api with <searchTerm> as search term for customer <CustomerFilterId>
    Then <RestrictedBrand> brand should not be returned in the results list for <searchTerm>, with <Interface>, for <CustomerFilterId>

    Examples:
      | searchTerm                   | Interface                    | CustomerFilterId                   | RestrictedBrand         |
      | products.excluded.searchTerm | products.excluded.searchType | products.excluded.customerFilterId | products.excluded.brand |

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" total number of record count should not include excluded products
    Given I request search api with <searchTerm> as search term for customer <CustomerFilterId>
    Then total number of record count should not include excluded products for <searchTerm>, with <Interface>, for <CustomerFilterId>

    Examples:
      | searchTerm                   | Interface                    | CustomerFilterId                   |
      | products.excluded.searchTerm | products.excluded.searchType | products.excluded.customerFilterId |

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" L2 Category bin counts should not include excluded products
    Given I request search api with <searchTerm> as search term for customer <CustomerFilterId>
    Then L2 Category bin counts should not include excluded products for <searchTerm>, with <Interface>, for <CustomerFilterId>

    Examples:
      | searchTerm                   | Interface                    | CustomerFilterId                   |
      | products.excluded.searchTerm | products.excluded.searchType | products.excluded.customerFilterId |

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" L3 Category bin counts should not include excluded products
    Given I request search api with <searchTerm> as search term for customer <CustomerFilterId>
    Then L3 Category bin counts should not include excluded products for <searchTerm>, with <Interface>, for <CustomerFilterId>

    Examples:
      | searchTerm                   | Interface                    | CustomerFilterId                   |
      | products.excluded.searchTerm | products.excluded.searchType | products.excluded.customerFilterId |

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I search with "<searchTerm>" excluded brands should not be returned in the Terminal Node Page
    Given I request search api to get level.three.category
    When I get all the level three category ids with the searchTerm <searchTerm> and with a min 100 binCount for customer <CustomerFilterId>
    Then I look for a L3 category with <searchTerm>, <Interface> whose results list should not have excluded products from <RestrictedBrand>

    Examples:
      | searchTerm       | Interface         | CustomerFilterId | RestrictedBrand |
      | Cartridge%20Fuse | I18NSearchGeneric | 4350526          | Phoenix Contact |

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I search with "<searchTerm>" total number of results count should not include excluded products in the Terminal Node Page
    Given I request search api to get level.three.category
    When I get all the level three category ids with the searchTerm <searchTerm> and with a min 100 binCount for customer <CustomerFilterId>
    Then total number of record count should not include excluded products for terminal node page with <searchTerm>, <Interface>

    Examples:
      | searchTerm                   | Interface                    | CustomerFilterId                   |
      | products.excluded.searchTerm | products.excluded.searchType | products.excluded.customerFilterId |

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I search with "<searchTerm>" Result list is sorted as per the search relevancy ranking set-up - Search Page
    Given I request search api with <searchTerm> as search term
    Then results list is sorted as per the search relevancy ranking set-up for search page with <searchTerm>, <Interface>

    Examples:
      | searchTerm              | Interface         |
      | Cartridge%20Fuse        | I18NSearchGeneric |

  @SearchApi_Experience_Manager_Tests  @TestApi_Search
  Scenario Outline: When I search with "<searchTerm>" Result list is sorted as per the search relevancy ranking set-up - Terminal Node Page
    Given I request search api to get level.three.category
    When I get all the level three category ids with the searchTerm <searchTerm> and with a min 100 binCount for customer <CustomerFilterId>
    Then results list is sorted as per the search relevancy ranking set-up for Terminal Node page with <searchTerm>, <Interface>

    Examples:
      | searchTerm       | Interface         | CustomerFilterId |
      | Cartridge%20Fuse | I18NSearchGeneric | 4350526          |