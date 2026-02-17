@Feature_Map_Double_Single_Byte_Characters @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:JP/CN - Map Double and Single Byte Characters in search query

  @TestApi_Search
  Scenario Outline: Search terms "<searchTerm>" entered in Kanji and Hiragana single and double byte characters return the same results
    Given I request search api with <searchTerm> as search term in JP or CN
    Then I check if the results are returned for the search term irrespective of the byte format they are entered in

    Examples:
      | searchTerm |
      | 固定抵抗器      |
      | D-subコネクタ  |
      | メス9極       |

  @TestApi_Search
  Scenario Outline: Special characters are removed for search term: <search.term>
    Given I send a special character request to the search api for <search.term>
    Then the special characters are removed from the search term <search.term>
    Examples:
      | search.term                                |
      | valid.length.term.with.special.characters4 |

  @TestApi_Search
  Scenario Outline: Search terms "<searchTerm>" entered in Katakana single and double byte characters return the same results
    Given I request search api with <searchTerm> as search term in JP or CN
    Then I check if the results are returned for the search term irrespective of the byte format they are entered in

    Examples:
      | searchTerm |
      | D-Subｺﾈｸﾀ  |
      | ﾒｽ%20９極    |

  @TestApi_Search
  Scenario Outline: Search terms "<searchTerm>" with special characters when entered in double or single byte should return the same results on the Chinese and Japanese
    Given I request search api with <searchTerm> as search term in JP or CN
    Then I check if the same results are returned for the search term irrespective of the byte format they are entered in

    Examples:
      | searchTerm |
      | 10kΩ       |
      | +70°C      |
      | ±20%25     |
      | 12.5Ω      |
      | 12,5       |

  @TestApi_Search
  Scenario: English search terms entered in  single and double byte characters return the same results on the Chinese and Japanese sites
    Given I request search api with ＬＥＤ as search term in JP or CN
    Then I request search api with LED in single byte as search term to check if the same results are returned

  @SearchApi_Experience_Manager_Tests
  Scenario: Redirects are triggered for single byte search query that match the XM entry
    Given I request search api with a single byte search term and with redirects enabled
    Then I check if the keyword redirects are triggered for search term regardless of byte format they are entered

  @SearchApi_Experience_Manager_Tests
  Scenario: Redirects are triggered for double byte search query that match the XM entry
    Given I request search api with a double byte search term and with redirects enabled
    Then I check if the keyword redirects are triggered for search term regardless of byte format they are entered
