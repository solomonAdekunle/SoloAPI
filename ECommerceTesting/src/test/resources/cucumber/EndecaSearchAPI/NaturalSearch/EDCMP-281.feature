@Feature_Applied_Brand_Filter_SR_Page @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Applied Brand filter on Search Results Page

  @TestApi_Search
  Scenario: Return initial set of 20 products are relevant to the search term and specific brand
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then I check if the initial set of products are relevant to the search term and specific brand

  @TestApi_Search
  Scenario: Verify bin count in the search api response, with a search term and specific brand, excludes discontinued and production pack products
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then The search api bin count results should match the assembler response count

  @TestApi_Search
  Scenario: When I request search API with a search term and specific brand, Products are returned as per the search relevancy ranking set-up
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then Products are returned as per the search relevancy ranking set-up

  @TestApi_Search
  Scenario: Return the list of sort options available to sort the product list by Relevance, Popularity, Price: Low to High, Price: High to Low
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then relevance,popularity,Price High to Low, Price Low to High sort by options should be displayed
      | Relevance          |
      | Popularity         |
      | Price: Low to High |
      | Price: High to Low |

  @TestApi_Search
  Scenario: Return Category Name and Bin Count for all L2 categories that are part of the search result
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then Return category name and bin count for all L2 category that are part of search result

  @TestApi_Search
  Scenario: Return Category Name, SEO URL and Bin Count for all L3 categories that are part of the search result
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then Return category name, SEO URL and bin count for all L2 category that are part of search result

  @TestApi_Search
  Scenario: Return search related details for adobe tagging and front end display
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then I get all the data layer attributes under queryInfo with search term

  @TestApi_Search
  Scenario: Return Brand name for the selected brand filter
    Given I request search api to get level.three.category
    When I request search api with a searchTerm and brand dimension Id
    Then I check if the brand name for all the products P_brand property is equal to the brand filter provided
