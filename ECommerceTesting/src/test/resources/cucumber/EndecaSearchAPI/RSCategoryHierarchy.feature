@Feature_SearchAPI_Return_RS_Category_Hierarchy @All_Acceptance_Tests

Feature:EDCMP-72-SearchAPI-Return-RS-Category-Hierarchy

  @TestApi_Search
  Scenario: Verify category response for L0 category for whole RS category hierarchy
    Given I request search api to get level.zero.category
    Then the search api should return 200 statuscode with level zero categories in json response

  @TestApi_Search
  Scenario Outline: Verify category response for whole hierarchy <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And  the search api results for <Categories> should match with assembler response
    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |

  @TestApi_Search
  Scenario Outline: Verify SEOCategoryNames are returned in the search api response for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category level item in search api should get seo category name information for <Categories> matching assembler response
    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |

  @TestApi_Search
  Scenario Outline: Verify Dimensionids are returned in the search api response for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category node in search api should get dimensionId information for <Categories> matching assembler response

    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |


  @TestApi_Search
  Scenario Outline: Verify the correct Total number of products for <Categories> within the category are returned in the search api response
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category node in search api should get totalNumberOfProducts information for <Categories> matching assembler response

    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |


  @TestApi_Search
  Scenario Outline: Verify all child categories are in alphanumeric order within their parent category for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return all child <Categories> in alphanumeric order within their parent category
    Examples:
      | Categories           |
      | level.zero.category  |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |

  @TestApi_Search
  Scenario: Verify bin count in the search api response categories excludes discontinued and production pack products
    Given I request search api to get bin.count
    Then the search api should return 200 statuscode for bin.count with valid json response
    And  the search api total bin.count should match with assembler response bin count

  @TestApi_Search
  Scenario Outline: Verify the error response for the <Categories> with invalid localeid request parameters
    Given I request search api to get <Categories>
    Then the search api should return 400 response for <Categories> with invalid locale id
    Examples:
      | Categories                            |
      | invalid.level.one.category.localeId   |
      | invalid.level.two.category.localeId   |
      | invalid.level.three.category.localeId |


