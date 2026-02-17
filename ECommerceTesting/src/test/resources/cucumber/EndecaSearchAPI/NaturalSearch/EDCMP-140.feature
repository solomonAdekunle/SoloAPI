@Feature_Natural_Search @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:EDCMP-140

  @EDCMP-140 @TestApi_Search
  Scenario Outline: When I request the search API for a search term, then the following category details should be displayed for <search.term>
    Given I send a request to the search api for search term <search.term>
    Then all search API attributes should be displayed for the records matching the <search.term>
    Examples:
      | search.term  |
      | search.term1 |

  @EDCMP-140 @TestApi_Search
  Scenario Outline: Production pack products are excluded from product list when searching for <search.term>
    Given I send a request to the search api for search term <search.term>
    Then the API record counts for <search.term> with no filters applied are not equal
    And the API record counts for <search.term> with filters applied are equal
    And accurate Brand details relating to the <search.term> are returned
    Examples:
      | search.term           |
      | search.term.pack.type |


  @EDCMP-140 @TestApi_Search
  Scenario Outline: Products returned as per the search relevancy ranking set-up when searching for <search.term>
    Given I send a request to the search api for search term <search.term>
    Then the Search API and Assembler API results are in the same order for <search.term>
    Examples:
      | search.term            |
      | keyword.interface.term |

  @EDCMP-140 @TestApi_Search
  Scenario Outline: When I request the search API with a search term "<search.term>",  the list of sort options available to sort the product list are returned
    Given I send a request to the search api for search term <search.term>
    Then All sort by options should be displayed For <search.term>
    Examples:
      | search.term    |
      | sort.options.1 |
      | sort.options.2 |
      | sort.options.3 |


  @EDCMP-140 @TestApi_Search
  Scenario Outline: All category details are returned for L2 categories that match the <search.term>
    Given I send a request to the search api for search term <search.term>
    Then The L2 category details that match the <search.term> are displayed
    Examples:
      | search.term              |
      | l2.category.details.term |

  @EDCMP-140 @TestApi_Search
  Scenario Outline: All category details are returned for L3 categories that match the <search.term>
    Given I send a request to the search api for search term <search.term>
    Then The L3 category details that match the <search.term> are displayed
    Examples:
      | search.term              |
      | l3.category.details.term |


  @EDCMP-140 @TestApi_Search
  Scenario Outline: Search related details for adobe tagging are returned for "<search.term>"
    Given I send a request to the search api for search term <search.term>
    Then all Adobe tags relating to the <search.term> are returned
    Examples:
      | search.term            |
      | keyword.interface.term |


  @EDCMP-140 @TestApi_Search
  Scenario Outline: Category URL's relating to the "<search.term>" are returned
    Given I send a request to the search api for search term <search.term>
    Then Category Seo Url details that relate to the <search.term> are retuned
    Examples:
      | search.term       |
      | category.url.term |


  @EDCMP-140 @TestApi_Search
  Scenario Outline: Searches for <search.term> using the Search API are not spell corrected
    Given I send a request to the search api for search term <search.term>
    Then the search API does not report the <search.term> as spell corrected
    Examples:
      | search.term           |
      | no.spell.correct.term |

