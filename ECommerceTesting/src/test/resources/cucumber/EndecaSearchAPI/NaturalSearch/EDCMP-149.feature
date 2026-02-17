@Feature_DYM_Suggestions_Zero_Results_Page @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Get Did You Mean suggestions on the zero results page

  @TestApi_Search
  Scenario Outline: DYM suggestions, regardless of number, that closely match the search term <searchTerm> are returned where relevant
    Given I request search api with <searchTerm> as search term
    Then the DYM suggestions <didYouMean> that closely match the search term are returned

    Examples:
      | searchTerm | didYouMean |
      | elephant   | elegant    |
      | IRLZ34BF   | irlz34     |

  @TestApi_Search
  Scenario Outline: Return searchResults, cascadeOrder, searchKeyword, searchKeywordApp, InterfaceName, MatchMode and searchType for <searchTerm> for adobe tagging and front end display
    Given I request search api with <searchTerm> as search term
    Then the search related details <searchTerm>, <searchCascade>,<searchInterface>,<keywordApp>,<matchMode>,<searchType> <searchResults> are returned

    Examples:
      | searchTerm | searchCascade | searchInterface   | keywordApp | matchMode       | searchType                   | searchResults |
      | elephant   | 1             | I18NSearchGeneric | elephant   | matchallpartial | KEYWORD_SINGLE_ALPHA_NUMERIC | 0             |
      | trapaline  | 1             | I18NSearchGeneric | trapaline  | matchallpartial | KEYWORD_SINGLE_ALPHA_NUMERIC | 0             |
      | IRLZ34BF   | 1             | I18NSearchGeneric | IRLZ34BF   | matchallpartial | KEYWORD_SINGLE_ALPHA_NUMERIC | 0             |

  @TestApi_Search
  Scenario Outline: Return pattern, spellCorrectApplied and wildCardMode for <searchTerm> for adobe tagging and front end display
    Given I request search api with <searchTerm> as search term
    Then the search related details <pattern> <spellCorrApplied>,<wildCardMode> <searchInterface> are returned for adobe tagging

    Examples:
      | pattern                   | spellCorrApplied | wildCardMode | searchTerm |searchInterface   |
      | ^[\\p{L}\\p{Nd}-,/%\\.]+$ | true             | NONE         | elephant   |I18NSearchGeneric |
      | ^[\\p{L}\\p{Nd}-,/%\\.]+$ | true             | NONE         | trapaline  |I18NSearchGeneric |
      | ^[\\p{L}\\p{Nd}-,/%\\.]+$ | true             | NONE         | IRLZ34BF   |I18NSearchGeneric |

  @TestApi_Search 
  Scenario Outline: Get the search_alternative_offered attribute response for <searchTerm>
    Given I request search api with <searchTerm> as search term
    Then I get the search alternative offered attribute response as <alternativeOffered> for search term <searchTerm>

    Examples:
      | searchTerm     | alternativeOffered     |
      | elephant       | alternative offered    |
      | mosfet%20istor | no alternative offered |