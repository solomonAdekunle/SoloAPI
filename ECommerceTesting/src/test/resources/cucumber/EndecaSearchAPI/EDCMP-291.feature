@Feature_All_Refinements_contain_internalId @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Get InternalId and TargetState from Refinements

  @TestApi_Search
  Scenario Outline: Return InternalId for all the refinements for <psf.id>
    Given I request search api that has no targetState block for <psf.id>
    Then Then I get the InternalId for all the refinements for <psf.id>

    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_432244 |
      | PSF_421210 |

  @TestApi_Search
  Scenario Outline: Check targetState block is not collapsed for all the refinements for <psf.id>
    Given I send a request to search api to get the level three category details using <psf.id>
    Then I get the targetState block details for all the refinements for <psf.id>

    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_432244 |
      | PSF_421210 |

  @TestApi_Search
  Scenario Outline: Check targetState block is collapsed for all the refinements for <psf.id>
    Given I request search api that has no targetState block for <psf.id>
    Then I get the targetState block details collapsed for all the refinements for <psf.id>

    Examples:
      | psf.id     |
      | PSF_421172 |
      | PSF_432244 |
      | PSF_421210 |
