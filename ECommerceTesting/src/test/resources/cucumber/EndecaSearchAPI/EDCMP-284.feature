@Feature_SearchAPI_Return_Info_Block_Results_Count @All_Acceptance_Tests @SAPI_Quick_Acceptance_Tests

Feature:Return_Info_Block_Results_List_Count

  @TestApi_Search
  Scenario Outline: Result count returned as part of the queryInfo block for <categories>
    When I request search API to get results count for <Categories>
    Then the results count returned should match the assembler for <Categories>
    Examples:
      | Categories  |
      | PSSS_436942 |
      | PSF_430764  |
      | PSS_430748  |
