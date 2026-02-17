@Feature_Tracking_Parameters_SearchAPI @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Add tracking parameters channelId, clientId to Search API

  @TestApi_Search
  Scenario Outline: Check if the SearchAPI accepts mandatory parameter <channelId> and <clientId> in requests
    Given I request the search API with channelId as <channelId> and clientId as <clientId> for <psf.id>
    Then the search api should return 200 statusCode

    Examples:
      | channelId    | clientId        | psf.id     |
      | mobile       | test            | PSF_430864 |
      | desktop      | automation      | PSF_430864 |
      | quoteManager | perftest        | PSF_430864 |
      | eProc        | predictive      | PSF_430864 |
      | quoteManager | onpremMobile    | PSF_430864 |
      | desktop      | mobileApi       | PSF_430864 |
      | mobile       | taxonomyService | PSF_430864 |

  @TestApi_Search
  Scenario Outline: Check if invalid clientId <clientId> or channelId <channelId> will return 400 response
    Given I request the search API with channelId as <channelId> and clientId as <clientId> for <psf.id>
    Then the search api should return 400 statusCode

    Examples:
      | channelId  | clientId     | psf.id     |
      | test       | desktop      | PSF_430864 |
      | mobileApi  | mobile       | PSF_430864 |
      | perftest   | quoteManager | PSF_430864 |
      | predictive | eProc        | PSF_430864 |