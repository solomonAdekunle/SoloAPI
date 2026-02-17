@Feature_TN_Details_Search_Belongs_L3_Category @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Get TN page details if all search results belong to single L3 category

  @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<searchTerm>" the following L3 category details should be displayed
   Given I request search api to get level.three.category
   And I request search api with a <searchTerm> with results belong to single L3 category
   Then the category Details should be displayed for the records with a term <searchTerm>, <Interface>

    Examples:
     | searchTerm  | Interface         |
     | searchTerm  | I18NSearchGeneric |
     | mpnValue    | I18NSearchGeneric |
     | stockNumber | I18NRSStockNumber |

  @TestApi_Search
  Scenario Outline: For a specified search term "<searchTerm>" return initial set of 20 products with details for all products belonging to L3 category
    Given I request search api to get level.three.category
    And I request search api with a <searchTerm> with results belong to single L3 category
    Then the specification attribute details for an initial list of 20 products should be displayed using a search term <searchTerm>, <Interface>

    Examples:
      | searchTerm  | Interface         |
      | searchTerm  | I18NSearchGeneric |
      | mpnValue    | I18NSearchGeneric |
      | stockNumber | I18NRSStockNumber |

  @TestApi_Search
  Scenario Outline: When I request search API for an L3 Category with a search term "<searchTerm>" column headers for specification attributes should be in the same order as returned in the facet list
    Given I request search api to get level.three.category
    And I request search api with a <searchTerm> with results belong to single L3 category
    Then the product list column headers for specification attributes should be returned in the same order as in the facet list

    Examples:
      | searchTerm  |
      | searchTerm  |
      | stockNumber |

  @TestApi_Search
  Scenario Outline: When I request the search API for an L3 Category with a search term "<searchTerm>", then all data layer attributes should be returned
    Given I request search api to get level.three.category
    And I request search api with a <searchTerm> with results belong to single L3 category
    Then I get all the data layer attributes under queryInfo with search term <searchTerm>, <pattern>,<searchType>,<interface>,<matchMode>,<cascadeOrder>,<autoCorrected>,<spellCorrectApplied>,<wildCarding>

    Examples:
      | searchTerm  | pattern                                                                        | searchType                   | interface         | matchMode       | cascadeOrder | autoCorrected | spellCorrectApplied | wildCarding |
      | searchTerm  | ^.*$                                                                           | CATCH_ALL_DEFAULT            | I18NSearchGeneric | matchallpartial | 1            | true         | true                | NONE        |
      | stockNumber | ^((((rs\|RS)[ ]?)?(\d{3}[\-\s]?\d{3,4}[pPaA]?))\|(25(\d{8}\|\d{1}\-\d{7})))$ | RS_STOCK_NUMBER              | I18NRSStockNumber | matchall        | 1            | false         | false               | NONE        |
      | mpnValue    | ^[\\p{L}\\p{Nd}-,/%\\.]+$                                                      | KEYWORD_SINGLE_ALPHA_NUMERIC | I18NSearchGeneric | matchallpartial | 1            | false         | true                | NONE        |