@Feature_Details_TerminalNode_Page_Search @All_Acceptance_Tests

Feature:Get details for Terminal Node Page - Search

  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" the following category details should be displayed
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then the category Details should be displayed for the records with a term <searchTerm>, <Interface>

    Examples:
      | searchTerm       | Interface         |
      | singleSearchTerm | I18NSearchGeneric |
      | multiSearchTerm  | I18NSearchGeneric |
      | mpnValue         | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: Verify bin count in the search api response, for a searchTerm "<searchTerm>" that excludes discontinued and production pack products
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then The search api bin count results should match the assembler response count psf.id details

    Examples:
      | searchTerm       |
      | singleSearchTerm |
      | multiSearchTerm  |
      | mpnValue         |
      | stockNumber      |

  @TestApi_Search
  Scenario Outline: For a specified search term "<searchTerm>" return initial set of 20 products with details for all products belonging to L3 category
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then the specification attribute details for initial list of 20 products should be displayed using a search term <searchTerm>, <Interface>

    Examples:
      | searchTerm      | Interface         |
      | multiSearchTerm | I18NSearchGeneric |
      | mpnValue        | I18NSearchGeneric |
      | stockNumber     | I18NRSStockNumber |

  @TestApi_Search
  Scenario Outline: When I request search API for an L3 Category with a search term "<searchTerm>" column headers for specification attributes should be in the same order as returned in the facet list
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then the product list column headers for specification attributes should be returned in the same order as in the facet list

    Examples:
      | searchTerm       |
      | singleSearchTerm |

  @TestApi_Search
  Scenario Outline: Verify Product list in the search api response, for an L3 Category with a search term "<searchTerm>", excludes discontinued and production pack products
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then The Product list bin count results should match the assembler response count with a search term <searchTerm>, <Interface>

    Examples:
      | searchTerm       | Interface         |
      | singleSearchTerm | I18NSearchGeneric |
      | multiSearchTerm  | I18NSearchGeneric |
      | mpnValue         | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: When I request the search API for an L3 Category with a search term "<searchTerm>",  the list of sort options available to sort the product list are returned
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then relevance,popularity,Price High to Low, Price Low to High sort by options should be displayed
      | Relevance          |
      | Popularity         |
      | Price: Low to High |
      | Price: High to Low |

    Examples:
      | searchTerm       |
      | singleSearchTerm |
      | multiSearchTerm  |
      | mpnValue         |

  @TestApi_Search
  Scenario Outline: For the specified search term "<searchTerm>", the product list in the SAPI response should be sorted in ascending order on price
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    Given I request the search API end point to sort the product list for an L3 Category with a search term <searchTerm> in the ascending order of price
    Then The list of products should be sorted in ascending order of price with a search term <searchTerm>

    Examples:
      | searchTerm       |
      | singleSearchTerm |
      | multiSearchTerm  |
      | mpnValue         |

  @TestApi_Search
  Scenario Outline: For the specified search term "<searchTerm>", the product list in the SAPI response should be sorted in descending order on price
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    Given I request the search API end point to sort the product list for an L3 Category with a search term <searchTerm> in the descending order of price
    Then The list of products should be sorted in descending order of price with a search term <searchTerm>

    Examples:
      | searchTerm       |
      | singleSearchTerm |
      | multiSearchTerm  |
      | mpnValue         |

  @TestApi_Search
  Scenario Outline: For the specified search term "<searchTerm>", all dimension ids and bin count for each facet values including isNew and Lead Time should be returned
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then I send a request to the assembler API with a search term <searchTerm> to get the bin count and dimension ids for, <Interface>
    Examples:
      | searchTerm       | Interface         |
      | singleSearchTerm | I18NSearchGeneric |
      | multiSearchTerm  | I18NSearchGeneric |
      | mpnValue         | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: Brand as a facet should be returned when I request for a SAPI response with the search term "<searchTerm>"
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then I get the list of brand attribute values from the search API response with a search term <searchTerm> with <Interface>
    Examples:
      | searchTerm       | Interface         |
      | singleSearchTerm | I18NSearchGeneric |
      | multiSearchTerm  | I18NSearchGeneric |
      | mpnValue         | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" for an L3 Category, production packed and discontinued products should be excluded from the facet value bin count for <psf.id>
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then the production packed products should be excluded from the facet value bin count with a search term <searchTerm>, <Interface>
    Examples:
      | searchTerm       | Interface         |
      | singleSearchTerm | I18NSearchGeneric |
      | multiSearchTerm  | I18NSearchGeneric |
      | mpnValue         | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: When SAPI requested with a search term "<searchTerm>" the ordering of the attribute values within the individual attribute filters should be alphanumeric or ascending order
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for a Terminal Node with a term <searchTerm>
    Then the ordering of attribute values of the facet with alphanumerics should be in ascending order
    Examples:
      | searchTerm       |
      | singleSearchTerm |
      | multiSearchTerm  |
      | mpnValue         |

  @TestApi_Search
  Scenario Outline: When I request the search API for an L3 Category, then all specification attribute facets should be returned in same order as in facet list for "<searchTerm>"
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for Terminal Node with a term <searchTerm> for data layer details
    Then I get the column headers for specification attributes should be returned in same order as in facet list

    Examples:
      | searchTerm       |
      | singleSearchTerm |
      | multiSearchTerm  |
      | mpnValue         |
      | stockNumber      |

  @TestApi_Search
  Scenario Outline: When I request the search API for an L3 Category with a search term "<searchTerm>", then all data layer attributes should be returned
    Given I request search api to get level.three.category
    When I get all the level three category ids with a min 20 binCount
    And I request search API end point for Terminal Node with a term <searchTerm> for data layer details
    Then I get all the data layer attributes under queryInfo with search term <searchTerm>, <pattern>,<searchType>,<interface>,<matchMode>,<cascadeOrder>,<autoCorrected>,<spellCorrectApplied>,<wildCarding>

    Examples:
      | searchTerm       | pattern                                                                      | searchType                   | interface         | matchMode       | cascadeOrder | autoCorrected | spellCorrectApplied | wildCarding |
      | singleSearchTerm | ^[\\p{L}\\p{Nd}-,/%\\.]+$                                                    | KEYWORD_SINGLE_ALPHA_NUMERIC | I18NSearchGeneric | matchallpartial | 1            | false         | true                | NONE        |
      | multiSearchTerm  | ^.*$                                                                         | CATCH_ALL_DEFAULT            | I18NSearchGeneric | matchallpartial | 1            | false         | true                | NONE        |
      | mpnValue         | null                                                                         | MPN                          | I18NManPartNumber | matchallpartial | 1            | false         | false               | BOTH        |
      | stockNumber      | ^((((rs\|RS)[ ]?)?(\d{3}[\-\s]?\d{3,4}[pPaA]?))\|(25(\d{8}\|\d{1}\-\d{7})))$ | RS_STOCK_NUMBER              | I18NRSStockNumber | matchall        | 1            | false         | false               | NONE        |