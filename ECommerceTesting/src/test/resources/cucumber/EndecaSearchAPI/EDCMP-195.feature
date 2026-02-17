@Feature_SAPI_Sort_By_Search_Context @All_Acceptance_Tests

Feature:Sort_By_Search_Context

  @TestApi_Search  @SAPI_Sort_By_Search_ContextTest
  Scenario Outline: When I request the search API end point for an L3 Category with a search term, relevance, popularity, Price Low to High and Price High to Low sort options are returned for <term>
    Given I request the search API end point for an L3 Category page with a term <term> for <psf.id>
    Then relevance,popularity,Price High to Low, Price Low to High sort by options should be displayed
      | Relevance          |
      | Popularity         |
      | Price: Low to High |
      | Price: High to Low |
    Examples:
      | psf.id     | term               |
      | PSF_430864 | search.single.term |
      | PSF_430864 | sapi.stock.number  |
      | PSF_430864 | search.mpn.value   |
      | PSF_430864 | search.multi.term  |


  @TestApi_Search @SAPI_Sort_By_Search_ContextTest
  Scenario Outline: When I request the search API end point for an L3 Category page with a search term, then result list is sorted by Relevance for <term>
    Given I request the search API end point for a Terminal Node with a term <term> for <psf.id>
    Then the results from SAPI with a term <term> sorted by relevance match the assembler for <psf.id>,<interface>
    Examples:
      | psf.id     | term               | interface         |
      | PSF_430864 | search.single.term | I18NSearchGeneric |
      | PSF_430864 | sapi.stock.number  | I18NRSStockNumber |
      | PSF_430864 | search.mpn.value   | I18NSearchGeneric |
      | PSF_430864 | search.multi.term  | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for an L3 Category page with a search term and popularity as a parameter, then result list is sorted by Popularity for <term>
    Given I request the search API end point for a Terminal Node sorted by popularity with a term <term> for <psf.id>
    Then the results from SAPI with a term <term> sorted by popularity match the assembler for <psf.id>,<interface>
    Examples:
      | psf.id     | term               | interface         |
      | PSF_430864 | search.single.term | I18NSearchGeneric |
      | PSF_430864 | sapi.stock.number  | I18NRSStockNumber |
      | PSF_430864 | search.mpn.value   | I18NSearchGeneric |
      | PSF_430864 | search.multi.term  | I18NSearchGeneric |

  @TestApi_Search @SAPI_Sort_By_Search_ContextTest
  Scenario Outline: When I request the search API end point for an L3 Category page with a search term and Price High to Low as a parameter, then result list is sorted by descending order for <term>
    Given I request the search API end point for a Terminal Node sorted by descending order with a term <term> for <psf.id>
    Then the results from SAPI with a term <term> sorted by descending order match the assembler for <psf.id>,<interface>
    Examples:
      | psf.id     | term               | interface         |
      | PSF_430864 | search.single.term | I18NSearchGeneric |
      | PSF_430864 | sapi.stock.number  | I18NRSStockNumber |
      | PSF_430864 | search.mpn.value   | I18NSearchGeneric |
      | PSF_430864 | search.multi.term  | I18NSearchGeneric |

  @TestApi_Search @SAPI_Sort_By_Search_ContextTest
  Scenario Outline: When I request the search API end point for an L3 Category page with a search term and Price Low to High as a parameter, then result list is sorted by descending order for <term>
    Given I request the search API end point for a Terminal Node sorted by ascending order with a term <term> for <psf.id>
    Then the results from SAPI with a term <term> sorted by ascending order match the assembler for <psf.id>,<interface>
    Examples:
      | psf.id     | term               | interface         |
      | PSF_430864 | search.single.term | I18NSearchGeneric |
      | PSF_430864 | sapi.stock.number  | I18NRSStockNumber |
      | PSF_430864 | search.mpn.value   | I18NSearchGeneric |
      | PSF_430864 | search.multi.term  | I18NSearchGeneric |

  @TestApi_Search @SAPI_Sort_By_Search_ContextTest
  Scenario Outline: When I request the search API end point for a search results page with a search term, relevance, popularity, Price: Low to High and Price: High to Low sort options are returned for <term>
    Given I request the search API end point for a search results page with a term <term>
    Then relevance,popularity,Price High to Low, Price Low to High sort by options should be displayed
      | Relevance          |
      | Popularity         |
      | Price: Low to High |
      | Price: High to Low |
    Examples:
      | term               |
      | search.single.term |
      | sapi.stock.number  |
      | search.mpn.value   |
      | search.multi.term  |


  @TestApi_Search @SAPI_Sort_By_Search_ContextTest
  Scenario Outline: When I request the search API end point for a search results page with a search term, then result list is sorted by Relevance for <term>
    Given I request the search API end point for a search results page with a term <term>
    Then the results from SAPI with a search term <term> ordered by relevance match the results from the assembler for <interface>
    Examples:
      | term               | interface         |
      | search.single.term | I18NSearchGeneric |
      | sapi.stock.number  | I18NRSStockNumber |
      | search.mpn.value   | I18NSearchGeneric |
      | search.multi.term  | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: When I request the search API end point for a search results page with a search term and popularity as a parameter, then result list is sorted by Popularity for <term>
    Given I request the search API end point for a search results page sorted by popularity with a term <term>
    Then the results from SAPI with a search term <term> ordered by popularity match the results from the assembler for <interface>
    Examples:
      | term               | interface         |
      | search.single.term | I18NSearchGeneric |
      | sapi.stock.number  | I18NRSStockNumber |
      | search.mpn.value   | I18NSearchGeneric |
      | search.multi.term  | I18NSearchGeneric |


  @TestApi_Search
  Scenario Outline: When I request the search API end point for a search results page with a search term and Price High to Low as a parameter, then result list is sorted by descending order for <term>
    Given I request the search API end point for a search results page in descending order with a term <term>
    Then the results from SAPI with a search term <term> ordered by descending order match the results from the assembler for <interface>
    Examples:
      | term               | interface         |
      | search.single.term | I18NSearchGeneric |
      | sapi.stock.number  | I18NRSStockNumber |
      | search.mpn.value   | I18NSearchGeneric |
      | search.multi.term  | I18NSearchGeneric |

  @TestApi_Search
  Scenario Outline: When I request the search API end point fora search results page with a search term and Price Low to High as a parameter, then result list is sorted by descending order for <term>
    Given I request the search API end point for a search results page in ascending order with a term <term>
    Then the results from SAPI with a search term <term> ordered by ascending order match the results from the assembler for <interface>
    Examples:
      | term               | interface         |
      | search.single.term | I18NSearchGeneric |
      | sapi.stock.number  | I18NRSStockNumber |
      | search.mpn.value   | I18NSearchGeneric |
      | search.multi.term  | I18NSearchGeneric |



