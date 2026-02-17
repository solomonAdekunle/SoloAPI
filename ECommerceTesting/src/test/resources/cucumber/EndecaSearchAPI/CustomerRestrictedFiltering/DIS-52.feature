@Feature_Customer_Restricted_Filtering_Search_EndPoint @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Customer restricted filtering on the Search Endpoint

  @dis-52333 @TestApi_Search
  Scenario Outline: Ensure restricted products are not returned when <Customer_Filter> is applied <search.term> by search by name
    Given I send a request to the search api to get <search.term> with <Customer_Filter>
    Then I should get status code 200 for <Customer_Filter> with valid json response
    Then I check when the customer filter is passed restricted products are not returned for <Customer_Filter>
    Then I should see record count excludes hidden products for <Customer_Filter> with <search.term>
    Then I should see Level one and Level two bin count exclude hidden product for <Customer_Filter>
    And I should see results count in queryInfo block excludes hidden products for <Customer_Filter> with <search.term>
    And I should see all categories excluding hidden products as part of the refinement options for <Customer_Filter> with <search.term>
    And Hidden level two category should not returned as part of refinement option for <Customer_Filter> with <search.term>
    And Level two categories super section counts exclude hidden categories in query info block for <Customer_Filter> with <search.term>
    Examples:
      | search.term                 | Customer_Filter    |
      | search.term.example.1       | Customer.Filter.id |

  @dis-52333 @TestApi_Search
  Scenario: Ensure restricted products are not returned for hidden L3 categories when Customer_Filter is applied search by name
    Given I send a search request to the search api to get search.term.restricted.L3 with CustomerFilter.id.hidden.l3 for hidden Level three categories
    Then I should not see hidden level three categories returned for CustomerFilter.id.hidden.l3
    Then I should not see hidden level three categories returned in the topCategories block for CustomerFilter.id.hidden.l3

  @dis-52333 @TestApi_Search
  Scenario: Ensure restricted products are not returned for hidden L2 categories when Customer_Filter is applied search by name
    Given I send a search request to the search api to get search.term.restricted with Customer.Filter.id for hidden Level two and Level three categories
    Then I should not see hidden level two and level three categories returned for Customer.Filter.id

 @dis-52333 @TestApi_Search
  Scenario: Ensure restricted products are not returned when Customer_Filter is applied for search.term with stock number
    Given I send a search request to the search api to get search.term.by.stock.number with Customer.Filter.id for search by stock number
    Then I check when the Customer.Filter.id is passed restricted products are not returned for for stock number search
    Then I should see record count excludes hidden products for search term by stock number for Customer.Filter
    And I should see results count in queryInfo block excludes hidden products for customer filter by stock number search

  @dis-52333 @TestApi_Search
  Scenario: Ensure restricted products are not returned when Customer_Filter is applied for search by MPN
    Given I send a search request to the search api to get search.term.by.mpn with Customer.Filter.id for search by MPN
    Then I should hidden level two and level three categories should not returned for search by MPN with Customer.Filter.id
    Then I check when the customer filter is passed restricted products are not returned for Customer.Filter.id
    And I should see record count excludes hidden products for Customer.Filter.id with search.term.by.mpn
    And Hidden level two category should not returned as part of refinement option for Customer.Filter.id with search.term.by.mpn
    Then I should see the super section category hidden restricted products not returned
    Then I should see the search_categories queryInfo block excludes hidden products
    Then I should see record count excludes hidden products for search by mpn
    Then I should see Level one and Level two bin count exclude hidden product for Customer.Filter.id
    And I should see results count in queryInfo block excludes hidden products for Customer.Filter.id with search.term.by.mpn

  @dis-52333 @TestApi_Search
  Scenario Outline: Ensure restricted products are not returned when <Customer_Filter> is applied for <psf_id> in the Search API
    Given I request the search API end point for an Terminal node with a term <psf_id> with <search.term> for <Customer_Filter>
     Then I check when the Customer_Filter is passed hidden.product.number.by.psf are not returned on terminal node page
     And The bin count in breadcrumb block excludes hidden products on terminal node for <psf_id> with <search.term>
     And I should see record count excludes hidden products on terminal node for <psf_id> with <search.term>
     And I should see results count in queryInfo block Terminal node excludes hidden products for <psf_id> with <search.term>

    Examples:
      | search.term           | Customer_Filter                    | psf_id               |
      | search.term.by.PSF_id | CustomerFilter.id.by.Terminal.node | terminal.node.psf.id |


  @dis-52333 @TestApi_Search
  Scenario: Ensure restricted products are not returned when search by restricted product stock number
    Given I send a search request to the search api to get search.by.stockNumber.restricted.product with Customer.Filter.id for search by stock number
    Then No results should returned for Customer.Filter.id
