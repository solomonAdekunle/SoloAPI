@Feature_Search_API_should_return_Assembler_500_error_code @TestApi_Search @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature: Search API should return appropriate status code for the VALID and INVALID category respectively

  @TestApi_Search_Manual @SAPI-107
  Scenario Outline: Search API should return a 200 OK status code for a VALID category code <psf.id>
    Given I send a request to search api to get all level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 200 statuscode for <psf.id>

    Examples:
      | psf.id     |
      | PSF_432244 |

  @TestApi_Search @SAPI-107
  Scenario Outline: Existing valid 404 scenarios should not be disrupted (if an INVALID category is requested, Search API should return a 404 response) for <psf.id>
    Given I send a request to search api to get all level three category details using <psf.id>
    Then the assembler should return 200 statuscode and the search api should return 404 statuscode for <psf.id>

    Examples:
      | psf.id            |
      | PSF_4322455555554 |