@All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests @GroupBy_Acceptance_Tests

Feature: SearchQueriesForGroupBy

  @TestApi_Search
  Scenario: Validate that SAPI Response will return expected data when minimal queries are sent
    Given I send a Post request to the search api with post.minimal.searchterm.query.led
    Then The page Pagination number is 1 for the searchTerm
    And The result count should be greater than 1000 for the searchTerm

  @TestApi_Search
  Scenario Outline: Validate that SAPI Response will return expected data when NavigationName parameter is included in the queries sent
    Given I send a Post request to the search api with <data>
    Then The result count should be greater than <expectedResultCountNumber> for the searchTerm
    And The available navigation list should be less or <navigationlistCount> for the searchTerm
    And The selected navigation list should be <selectednavigationlistCount> for the searchTerm
    Examples:
      | data                                                       | expectedResultCountNumber | navigationlistCount | selectednavigationlistCount |
      | post.navigateName.attributes.level1.searchterm.query.led   | 2                         | 20                  | 1                           |
      | post.navigateName.attributes.level2.searchterm.query.led   | 2                         | 20                  | 1                           |
      | post.navigateName.attributes.level3.searchterm.query.led   | 2                         | 20                  | 1                           |
      | post.navigateName.attributes.packSize.searchterm.query.led | 2                         | 50                  | 1                           |


  @TestApi_Search
  Scenario Outline: Validate that SAPI Response will return expected data when each of the sortDirection parameter is set among the queries sent
    Given I send a Post request to the search api with <data>
    Then I should see the sortOption selected set as <sortSelectedOption> for Relevance for the searchTerm
    And I should see sortOption selected set as <sortSelectedOption1> for Descending for the searchTerm
    And I should see sortOption selected set as <sortSelectedOption2> for Ascending for the searchTerm
    And The refinement count should be <refinementListCount> for the searchTerm
    And I should see the prices lists displayed as expected based on <sortBy>
    Examples:
      | data                                                             | sortSelectedOption | sortSelectedOption1 | sortSelectedOption2 | refinementListCount | sortBy    |
      | post.sortDirection.ASC.searchterm.without.queryType.offers       | false              | false               | true                | 50                  | ASC       |
      | post.sortDirection.DESC.searchterm.without.queryType.offers      | false              | true                | false               | 50                  | DESC      |
      | post.sortDirection.Relevance.searchterm.without.queryType.offers | true               | false               | false               | 50                  | Relevance |


  @TestApi_Search
  Scenario: Validate that SAPI Response will return expected data when facet parameter are included in the queries sent
    Given I send a Post request to the search api slash facet parameters
    Then I should see see attributes.Length as a value for KEY for the searchTerm
    And The refinement count should be 50 for the searchTerm
    And I should see from the refinement list value contains 1m from the response

  @TestApi_Search
  Scenario Outline: Validate that SAPI Response will return expected data for customer with filter Id and customer without filter Id
    Given I send a Post request to the search api with <data>
    Then The result count should be greater than <expectedResultCountNumber> for the searchTerm
    And the recordId count should be less or equal <recordIdCounts> for the searchTerm
    And the limit of records per page should be <expectedLimitCount> for the searchTerm
    Examples:
      | data                                               | expectedResultCountNumber | recordIdCounts | expectedLimitCount |
      | post.customerWithFilter.searchterm.query.Gloves    | 2                         | 40             | 20                 |
      | post.customerWithoutFilter.searchterm.query.Gloves | 300                       | 40             | 20                 |

  @TestApi_Search
  Scenario Outline: Validate that SAPI Response will return expected data for customer with filter Id for MPN and Stock Number
    Given I send a Post request to the search api with <data>
    Then The result count should be <expectedResultCountNumber> for the searchTerm
    And The lastPage count should be null
    And The recordId count should be empty
    Examples:
      | data                                                   | expectedResultCountNumber |
      | post.customerWithFilter.searchterm.queryType.MPN       | 0                         |
      | post.customerWithFilter.searchterm.query.rsStockNumber | 0                         |

  @TestApi_Search
  Scenario Outline: Validate that SAPI Response will return expected data when RS stock number is set as query among the parameters sent
    Given I send a Post request to the search api with <data>
    Then The result count should be <expectedResultCountNumber> for the searchTerm
    And The page Pagination number is <pageNumber> for the searchTerm
    And the recordId count should be less or equal <recordIdCounts> for the searchTerm
    And The lastPage count should be less than <expectedLastPageCount> for the searchTerm
    Examples:
      | data                                        | expectedResultCountNumber | pageNumber | recordIdCounts | expectedLastPageCount |
      | post.minimal.searchterm.query.rsStockNumber | 1                         | 1          | 20             | 2                     |
      | post.mpn.searchterm.queryType.MPN           | 1                         | 1          | 20             | 2                     |


  @TestApi_Search
  Scenario: Validate that SAPI Response will return expected data when BrandName is set as query among the parameters sent
    Given I send a Post request to the search api with post.minimal.searchterm.query.brandName
    Then The result count should be greater than 100 for the searchTerm
    And brand is displayed as displayName from the responseBody
    And Kingbright is displayed as value under brand refinements

  @TestApi_Search
  Scenario Outline: Validate that SAPI Response will return expected data when PageSize Number changes
    Given I send a Post request to the search api with <data>
    Then The result count should be greater than <expectedCountResults> for the searchTerm
    And the limit of records per page should be <expectedLimitCount> for the searchTerm
    And The lastPage count should be less than <expectedLastPageCount> for the searchTerm
    Examples:
      | data                                        | expectedCountResults | expectedLimitCount | expectedLastPageCount |
      | post.pagination.pageSize.20.searchterm.led  | 200                  | 20                 | 5000                  |
      | post.pagination.pageSize.15.searchterm.led  | 200                  | 20                 | 5000                  |
      | post.pagination.pageSize.50.searchterm.led  | 200                  | 50                 | 3000                  |
      | post.pagination.pageSize.59.searchterm.led  | 200                  | 20                 | 5000                  |
      | post.pagination.pageSize.100.searchterm.led | 200                  | 100                | 1000                  |
      | post.pagination.pageSize.101.searchterm.led | 200                  | 20                 | 5000                  |


  @TestApi_Search
  Scenario: Validate that SAPI Response will return expected data when Page Number sent is greater than the LastPage Number
    Given I send a Post request to the search api with post.pagination.page.10000.searchterm.led
    Then The result count should be 0 for the searchTerm
    And The lastPage count should be null