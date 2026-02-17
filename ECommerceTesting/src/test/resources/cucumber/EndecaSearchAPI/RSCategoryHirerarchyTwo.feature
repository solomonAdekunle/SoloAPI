@Feature_SearchAPI_Return_RS_Category_Hierarchy @All_Acceptance_Tests

Feature:EDCMP-162-Get-details-for-L1&L2-CategoryPages

  @TestApi_Search
  Scenario Outline: Verify RSIDS are returned in the search api response for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category node in search api should get RSID information for <Categories> matching assembler response

    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |

  @TestApi_Search
  Scenario Outline: Verify Category names are returned in the search api response for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category node in search api should get CategoryName information for <Categories> matching assembler response

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
  Scenario Outline: Verify SeoPageTitle are returned in the search api response for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category node in search api should get seoPageTitle information for <Categories> matching assembler response
    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |

  @TestApi_Search
  Scenario Outline: Verify SeoUrl are returned in the search api response for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category node in search api should get seoUrl information for <Categories> matching assembler response
    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |

  @TestApi_Search
  Scenario Outline: Verify SeoMetadataDescription are returned in the search api response for <Categories>
    Given I request search api to get <Categories>
    Then the search api should return 200 statuscode for <Categories> with valid json response
    And each category node in search api should get seoMetadataDescription information for <Categories> matching assembler response

    Examples:
      | Categories           |
      | level.one.category   |
      | level.two.category   |
      | level.three.category |

  @TestApi_Search
  Scenario Outline: Verify search api response parent category details (rs.id,seocategoryname,dimensionid,seoUrl) using <psf.id> matches with the assembler response ancestor category details
    Given I send a request to search api to get the level three parent heirarchy category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>
    And  the search api parent hierarchy category details should match with assembler response ancestor hierarchy <psf.id> details
    Examples:
      | psf.id     |
      | PSF_432244 |
      | PSF_421172 |
      | PSF_409430 |

  @TestApi_Search
  Scenario Outline: Verify search api response using <SeoUrls> matches assembler response ancestor category details when seoUrl is passed as part of request
    Given I send a request to search api to get the level three parent heirarchy category details using <SeoUrls>
    Then I request search api using <SeoUrls>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <SeoUrls>
    And  the search api parent hierarchy category details should match with assembler response ancestor hierarchy <SeoUrls> details
    Examples:
      | SeoUrls    |
      | PSF_432244 |
      | PSF_421172 |
      | PSF_409430 |
      | PSF_421171 |

  @TestApi_Search
   Scenario Outline: Verify search api error response for an invalid category id request <psf.id>
    Given I send a request to search api to get the level three parent heirarchy category details using <psf.id>
    Then the search api should return 404 response for the request send with an invalid category id <psf.id>
    Examples:
      | psf.id      |
      | sdhfklsdf   |
      | PSF_42$5&)  |
      | PSF_47s434  |
      | PSFeererrer |

  @TestApi_Search
  Scenario Outline: Verify search api response child category details (seocategoryname,imageurl,dimensionid,bincount,seoURL) using <psss.id> matches with the assembler response child category details
    Given I send a request to search api to get the child level one heirarchy details using <psss.id>
    Then the search api should return 200 statuscode and child heirarchy details for <psss.id> with valid json response
    And  search api child heirarchy category details should match with assembler response child heirarchy <psss.id> details
    Examples:
      | psss.id     |
      | PSSS_432064 |
      | PSSS_438300 |
      | PSSS_114891 |

