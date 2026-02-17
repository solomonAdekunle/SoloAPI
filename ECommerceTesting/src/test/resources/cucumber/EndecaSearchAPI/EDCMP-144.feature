@Feature_AutoCorrection @AutoCorrection_Regression @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Get the misSpelt query terms to be AutoCorrected

  @TestApi_Search @AutoCorrection_Regression
  Scenario Outline: Get the misSpelt <searchTerm> AutoCorrected so the results are returned for the corrected term
    Given I request search api with <searchTerm> as search term
    Then I check if the search term is autocorrected to <autoCorrected> unless the localId is jp

    Examples:
      | autoCorrected      | searchTerm         |
      | rectangular        | rectanguler        |
      | IRLZ34NPBF         | IRLZ34NPBC         |
      | 541-1247           | 541-1247           |
      | mosfet transistors | mosfet%20transisto |

  @TestApi_Search @AutoCorrection_Regression
  Scenario Outline: Return additional information for <searchTerm> details for relevant page
    Given I request search api with <searchTerm> as search term
    Then I check additional details required along with autoCorrected term <autoCorrectedTerm>, <spellCorrectApplied>, <autoCorrected>

    Examples:
      | autoCorrectedTerm  | searchTerm         | spellCorrectApplied | autoCorrected |
      | rectangular        | rectanguler        | true                | true          |
      | IRLZ34NPBF         | IRLZ34NPBC         | true                | true          |
      | 541-1247           | 541-1247           | false               | false         |
      | mosfet transistors | mosfet%20transisto | true                | true          |

  @TestApi_Search @AutoCorrection_Regression
  Scenario Outline: Negative Testing for <searchTerm> details for relevant page
    Given I request search api with <searchTerm> as search term
    Then I check the autoCorrected keyword is null and spellCorrectApplied is true when a correct <searchTerm>, <autoCorrectedTerm> is provided

    Examples:
      | autoCorrectedTerm   | searchTerm            |
      | rectangular         | rectangular           |
      | ADAFRUIT INDUSTRIES | ADAFRUIT%20INDUSTRIES |

  @TestApi_Search
  Scenario Outline: Check if the AutoCorrect is returned with same SearchTerm as Original if the only difference is the searchTerm <searchTerm> is in different case to the CorrectTerm
    Given I request search api with <searchTerm> as search term
    Then I check the spellCorrected keyword is null and spellCorrectApplied is true when a correct <searchTerm> is provided

    Examples:
      | searchTerm            |
      | rectANgular           |
      | ADAFRuiT%20INDUSTRIES |


