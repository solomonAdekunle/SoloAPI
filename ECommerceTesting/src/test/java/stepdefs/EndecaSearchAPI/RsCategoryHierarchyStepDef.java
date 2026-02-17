package stepdefs.EndecaSearchAPI;

import config.PropertiesReader;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import pages.Page;
import pages.Search.SeachAPI.*;
import pages.Search.SeachAPI.Models.SearchAPIModel;
import pages.Search.SeachAPI.Models.TermNodeFacetValues;
import pages.Search.SeachAPI.Models.TermNodeRecordModel;
import stepdefs.SharedDriver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class RsCategoryHierarchyStepDef {

    private static final int EXPECTED_OK_STATUS_CODE = 200;

    private final CommonHelpers commonHelpers;
    private final StepsHelper stepsHelper;
    private final TerminalNodeHelper termNodeHelper;
    private final ResultsListHelpers resultsListHelper;
    private static PropertiesReader props = new PropertiesReader();
    public RsCategoryHierarchyStepDef(
            CommonHelpers commonHelpers, StepsHelper stepsHelper, TerminalNodeHelper terminalNodeHelper,
            ResultsListHelpers resultsListHelper, SharedDriver webDriver) {
        this.commonHelpers = commonHelpers;
        this.stepsHelper = stepsHelper;
        this.termNodeHelper = terminalNodeHelper;
        this.resultsListHelper = resultsListHelper;
    }


    @Given("^I request search api to get (.*)$")
    public void i_request_search_api_to_get(String categoryName) {
        new StepsHelper().getSearchHierarchyResults(categoryName);
    }

    @Given("^I send a request to search api to get the level three parent heirarchy category details using (.+)$")
    public void i_send_a_request_search_api_to_get_the_level_three_parent_heirarchy_category_details_using(String psfID) {
        new StepsHelper().getSearchAPIPSFAncestorsHierarchy(psfID);
    }

    @Given("^I request search api using (.+)$")
    public void i_request_search_api_using(String psfID) throws Throwable {
        new StepsHelper().getSearchAPIPSFUsingSeoURL(psfID);
    }

    @Given("^I send a request to search api to get the level three category details using (.+)$")
    public void i_send_a_request_search_api_to_get_the_level_three_category_details_using(String psfID) throws Throwable {
        new StepsHelper().getSearchAPIPSFCategory(psfID);
    }

    @Given("^I send a request to search api to get all level three category details using (.+)$")
    public void i_send_a_request_search_api_to_get_all_level_three_category_details_using(String psfID) throws Throwable {
        new StepsHelper().getSAPIPSFCategory(psfID);
    }

    @Given("^I send a request with a limit of (\\d+) to get the level three category details using (.+)$")
    public void iSendARequestWithALimitOfToGetTheLevelThreeCategoryDetailsUsingPsfId(int pageLimit,String psfID ) {
        String psfParam = props.getProperty(psfID);
        stepsHelper.getSearchAPIWithPageLimit(psfParam, pageLimit);
    }

    @Given("^I send a request to search api end point for an L3 Category page (.+)$")
    public void iSendARequestToSearchApiEndPointForAnL3CategoryPage(String psfID) throws Throwable {
        new StepsHelper().getSearchAPIPSFRecordIds(psfID);
    }

    @Then("^the search api should return (\\d+) statuscode with level zero categories in json response$")
    public void the_search_api_should_return_statuscode_with_level_zero_categories_in_json_response(int responseCode) throws Throwable {
        Response response = ScenarioContext.getData("level.zero.category");
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                responseCode, response.getStatusCode()
        );
        commonHelpers.compareLevelZeroSearchApiWithAssemblerResponse(response);
    }

    @Then("^the search api should return (\\d+) statuscode for (.+) with valid json response$")
    public void the_search_api_should_return_statuscode_for_categories_with_valid_json_response(int responseCode, String categoryName) throws Throwable {
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                responseCode, response.getStatusCode()
        );
    }

    @Then("^the search api should return (.+) response for the request send with an invalid category id (.+)$")
    public void the_Search_Api_Should_Return_Response_For_The_Request_Send_With_An_Invalid_Category_Id(int responseCode, String psfID) throws Throwable {
        Response response = ScenarioContext.getData(psfID);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                responseCode, response.getStatusCode()
        );
    }

    @Then("^the search api results for (.+) should match with assembler response$")
    public void the_search_api_results_for_should_match_with_assembler_response(String categoryName) {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiWithAssemblerResponse(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiWithAssemblerResponse(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiWithAssemblerResponse(response, assemblerResponse);
                break;
        }
    }

    @Then("^the search api should return all child (.+) in alphanumeric order within their parent category$")
    public void the_SearchApi_Should_Return_All_Child_Categories_In_Alphanumeric_Order_Within_Their_Parent_Category(String categoryName) throws Throwable {
        Response response = ScenarioContext.getData(categoryName);
        switch (categoryName) {
            case "level.zero.category":
                commonHelpers.assertLevelZeroCategorySortingOrder(response);
                break;
            case "level.one.category":
                commonHelpers.assertLevelOneCategorySortingOrder(response);
                break;
            case "level.two.category":
                commonHelpers.assertLevelTwoCategorySortingOrder(response);
                break;
            case "level.three.category":
                commonHelpers.assertLevelThreeCategorySortingOrder(response);
                break;
        }
    }

    @And("^each category node in search api should get dimensionId information for (.+) matching assembler response$")
    public void each_Category_Node_In_Search_Api_Should_Get_Dimension_Id_Information_For_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response searchApiResponse = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiInternalIdWithAssemblerDimensionId(searchApiResponse, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiInternalIdWithAssemblerDimensionId(searchApiResponse, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiInternalIdWithAssemblerDimensionId(searchApiResponse, assemblerResponse);
                break;
        }
    }

    @And("^the search api total (.+) should match with assembler response bin count$")
    public void the_Search_Api_Total_Count_Should_Match_With_Assembler_Response_Bin_Count(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response searchApiResponse = ScenarioContext.getData(categoryName);
        Long searchApiBinCount = commonHelpers.getSearchApiBinCount(commonHelpers.getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerCategoryCountList = commonHelpers.getLevelOneAssemblerResponseList(assemblerResponse, "count");
        List<Long> longList = new ArrayList<>();
        for (String count : assemblerCategoryCountList) longList.add(Long.valueOf(count));
        Long assemblerBinCount = longList.stream().mapToLong(Long::longValue).sum();
        assertEquals(
                "SearchAPI and Assembler bin counts should have matched",
                assemblerBinCount,
                searchApiBinCount
        );
    }

    @And("^each category node in search api should get RSID information for (.+) matching assembler response$")
    public void each_Category_Node_In_Search_Api_Should_Get_RSID_Information_For_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiIdWithAssemblerHirerarchyId(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiIdWithAssemblerHirerarchyId(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiIdWithAssemblerHirerarchyId(response, assemblerResponse);
                break;
        }
    }

    @And("^each category node in search api should get CategoryName information for (.+) matching assembler response$")
    public void each_Category_Node_In_Search_Api_Should_Get_CategoryName_Information_For_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiCategoryWithAssemblerCategoryName(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiCategoryWithAssemblerCategoryName(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiCategoryWithAssemblerCategoryName(response, assemblerResponse);
                break;
        }
    }

    @And("^each category level item in search api should get seo category name information for (.+) matching assembler response$")
    public void each_Category_Level_Item_In_Search_Api_Should_Get_Seo_Category_Name_Information_For_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiSeoCategoryWithAssemblerSeoCategoryName(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiSeoCategoryWithAssemblerSeoCategoryName(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiSeoCategoryWithAssemblerSeoCategoryName(response, assemblerResponse);
                break;
        }
    }


    @And("^each category node in search api should get seoMetadataDescription information for (.+) matching assembler response$")
    public void each_Category_Node_In_Search_Api_Should_Get_SeoMetadataDescription_Information_For_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiMetaDescriptionWithAssemblerMetaDescription(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiMetaDescriptionWithAssemblerMetaDescription(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiMetaDescriptionWithAssemblerMetaDescription(response, assemblerResponse);
                break;
        }
    }

    @And("^each category node in search api should get totalNumberOfProducts information for (.+) matching assembler response$")
    public void each_Category_Node_In_Search_Api_Should_Get_TotalNumberOfProducts_Information_For_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApisTotalCountWithAssemblerTotalCount(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApisTotalCountWithAssemblerTotalCount(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApisTotalCountWithAssemblerTotalCount(response, assemblerResponse);
                break;
        }
    }


    @And("^each category node in search api should get seoPageTitle information for (.+) matching assembler response$")
    public void each_Category_Node_In_Search_Api_Should_Get_SeoPageTitle_Information_For_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiSeoPageTitleWithAssemblerSeoPageTitle(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiSeoPageTitleWithAssemblerSeoPageTitleList(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiSeoPageTitleWithAssemblerSeoPageTitleList(response, assemblerResponse);
                break;
        }
    }


    @Then("^each category node in search api should get seoUrl information for (.+) matching assembler response$")
    public void each_Category_Node_In_Search_Api_Should_Get_SeoUrl_Information_For_Categories_Matching_Assembler_Response(String categoryName) throws Throwable {
        Response assemblerResponse = StepsHelper.getAssemblerResponse();
        Response response = ScenarioContext.getData(categoryName);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        switch (categoryName) {
            case "level.one.category":
                commonHelpers.compareLevelOneSearchApiSeoUrlWithAssemblerSeoUrl(response, assemblerResponse);
                break;
            case "level.two.category":
                commonHelpers.compareLevelTwoSearchApiSeoUrlWithAssemblerSeoUrl(response, assemblerResponse);
                break;
            case "level.three.category":
                commonHelpers.compareLevelThreeSearchApiSeoUrlWithAssemblerSeoUrl(response, assemblerResponse);
                break;
        }
    }

    @Then("^the assembler should return 200 statuscode and the search api should return (\\d+) statuscode for (.+)$")
    public void theSearchApiShouldReturnStatuscodeForPsfId(int responseCode, String psfID) {
        Response assemblerResponse = StepsHelper.getPSFAncestorsHierarchyAssemblerResponse(psfID);
        Response response = ScenarioContext.getData(psfID);

        // The Assembler request always returns a status code of 200 for any kind value of psfID (category)
        // and the result it sends is a skeleton data (as JSON) without any category data for INVALID code
        // and a populated json response for a VALID code.
        assertEquals(
                String.format(
                        "Assembler has returned an unexpected status code, when requesting Level 3 categories using '%s'",
                        psfID
                ),
                EXPECTED_OK_STATUS_CODE, assemblerResponse.getStatusCode()
        );

        // Here we are expecting status code of 404 as the psfID is an invalid category
        // and no body is returned (as expected of unsuccessful calls)
        assertEquals(
                String.format(
                        "Search API has returned an unexpected status code, when requesting Level 3 categories using '%s'",
                        psfID
                ),
                responseCode, response.getStatusCode()
        );
    }

    @Then("^the search api parent hierarchy category details should match with assembler response ancestor hierarchy (.+) details$")
    public void the_search_api_parent_hierarchy_category_details_should_match_with_assembler_response_ancestor_hierarchy(String psfID) {
        Response assemblerResponse = StepsHelper.getPSFAncestorsHierarchyAssemblerResponse(psfID);
        Response response = ScenarioContext.getData(psfID);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        Map<String, String> assemblerAncestorDetails = commonHelpers.getAssemblerAncestorDetails(assemblerResponse);
        assertEquals("Level 3 Navigation state between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelThreeNavigationState"), response.path("category.internalId"));
        assertEquals("Level 3 SEO Category name between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelThreeSeoCategoryName"), response.path("category.seoCategoryName"));
        assertEquals("Level 3 Heirarchy Id between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelThreeHeirarchyId"), response.path("category.id"));
        assertEquals("Level 3 SEO Url between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelThreeSeoURL"), response.path("category.seoUrl"));

        assertEquals("Level 2 Navigation state between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelTwoNavigationState"), response.path("category.parent.internalId"));
        assertEquals("Level 2 SEO Category Name between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelTwoSeoCategoryName"), response.path("category.parent.seoCategoryName"));
        assertEquals("Level 2 Heirachy Id between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelTwoHeirarchyId"), response.path("category.parent.id"));
        assertEquals("Level 2 SEO Url between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelTwoSeoURL"), response.path("category.parent.seoUrl"));

        assertEquals("Level 1 Navigation state between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelOneNavigationState"), response.path("category.parent.parent.internalId"));
        assertEquals("Level 1 SEO Category Name between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelOneSeoCategoryName"), response.path("category.parent.parent.seoCategoryName"));
        assertEquals("Level 1 Heirarchy Id state between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelOneHeirarchyId"), response.path("category.parent.parent.id"));
        assertEquals("Level 1 SEO Url state between SearchAPI and Assembler should have matched",
                assemblerAncestorDetails.get("levelOneSeoURL"), response.path("category.parent.parent.seoUrl"));
    }

    @Given("^I send a request to search api to get the child level one heirarchy details using (.+)$")
    public void i_send_a_request_search_api_to_get_the_child_level_one_heirarchy_details_using(String psssID) throws Throwable {
        new StepsHelper().getSearchAPIPSSSChildHierarchy(psssID);
    }

    @Then("^the search api should return (\\d+) statuscode and child heirarchy details for (.+) with valid json response$")
    public void the_search_api_should_return_statuscode_and_child_heirarchy_details_for_with_valid_json_response(int responseCode, String psssID) throws Throwable {
        Response response = ScenarioContext.getData(psssID);
        assertEquals(
                String.format(
                        "The SearchAPI request should have come back with the expected status code of '%s'", responseCode
                ),
                responseCode,
                response.getStatusCode()
        );
    }

    @Then("^search api child heirarchy category details should match with assembler response child heirarchy (.+) details$")
    public void search_api_child_heirarchy_category_details_should_match_with_assembler_response_child_heirarchy_details(String psssID) throws Throwable {
        Response assemblerResponse = StepsHelper.getPSSSAncestorsHierarchyAssemblerResponse(psssID);
        Response response = ScenarioContext.getData(psssID);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );

        Map<String, List<SearchAPIModel>> assemblerCategoriesMap = commonHelpers.getAssemblerCategories(assemblerResponse);
        Map<String, List<SearchAPIModel>> searchApiCategoriesMap = commonHelpers.getSearchApiCategories(response);

        List<SearchAPIModel> levelTwoAssemblerCategoriesList = assemblerCategoriesMap.get("levelTwoList");
        List<SearchAPIModel> levelThreeAssemblerCategoriesList = assemblerCategoriesMap.get("levelThreeList");

        List<SearchAPIModel> levelTwoSearchApiCategoriesList = searchApiCategoriesMap.get("levelTwoSearchApiList");
        List<SearchAPIModel> levelThreeSearchApiCategoriesList = searchApiCategoriesMap.get("levelThreeSearchApiList");

        for (int index = 0; index < levelTwoAssemblerCategoriesList.size(); index++) {
            assertEquals(
                    "The Level 2 categories between the SearchAPI and Assembler should have matched",
                    levelTwoAssemblerCategoriesList.get(index),
                    levelTwoSearchApiCategoriesList.get(index)
            );
        }

        for (int index = 0; index < levelThreeAssemblerCategoriesList.size(); index++) {
            assertEquals(
                    "The Level 3 categories between the SearchAPI and Assembler should have matched",
                    levelThreeAssemblerCategoriesList.get(index),
                    levelThreeSearchApiCategoriesList.get(index)
            );
        }
    }

    @Then("^the search api should return (.+) response for (.+) with invalid locale id$")
    public void the_Search_Api_Should_Return_Response_For_With_Invalid_LocaleId(int responseCode, String categoryName) throws Throwable {
        Response response = ScenarioContext.getData(categoryName);
        switch (categoryName) {
            case "invalid.level.one.category.localeId":
                assertEquals(
                        String.format(
                                "Level 1 category: the SearchAPI request should have come back with the expected status code of '%s'",
                                responseCode
                        ),
                        responseCode,
                        response.getStatusCode()
                );
                break;
            case "invalid.level.two.category.localeId":
                assertEquals(
                        String.format(
                                "Level 2 category: the SearchAPI request should have come back with the expected status code of '%s'",
                                responseCode
                        ),
                        responseCode,
                        response.getStatusCode()
                );
                break;
            case "invalid.level.three.category.localeId":
                assertEquals(
                        String.format(
                                "Level 3 category: the SearchAPI request should have come back with the expected status code of '%s'",
                                responseCode
                        ),
                        responseCode,
                        response.getStatusCode()
                );
                break;
        }
    }

    @Then("^relevance,popularity,Price High to Low, Price Low to High sort by options should be displayed$")
    public void theFollowingSortOptionsByShouldBeDisplayedForPsfId(List<String> SortOptions) throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> SortByOptionsList = commonHelpers.getSortByOptionsFromL3Category(response);
        System.out.println("My SortByOptionsList " + SortByOptionsList);
        System.out.println("My SortOptions " + SortOptions);
        assertEquals(
                "The categories should have been displaying in the expected sort order",
                SortByOptionsList,
                SortOptions
        );
    }

    @Then("^the sorting by relevance matches the assembler$")
    public void theSortingByRelevanceMatchesTheAssemblerFor() throws Throwable {
        assertEquals(
                "The relevance matches from the SearchAPI should have been in the expected sort order",
                ScenarioContext.getAssemblerPRecordIds(),
                ScenarioContext.getSearchApiRecordIds()
        );
    }

    @And("^I send a request to the assembler API to get the records for (.+$)")
    public void iSendARequestToTheAssemblerAPIToGetTheRecords(String psfid) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfid);
        List<String> searchApiRecordIds = CommonHelpers.getRecordIds(searchApiResponse, "resultsList.records.id");

        String internald = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAsseblerRelevanceResponse(internald);
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        ScenarioContext.setSearchApiRecordIds(searchApiRecordIds);
        ScenarioContext.setAssemblerPRecordIds(assemblerPRecordIds);
    }

    @Given("^I request the search API end point sorted with Popularity as a parameter for L3 Category page for (.+)$")
    public void iRequestTheSearchAPIEndPointSortedWithPopularityAsAParameterForLCategoryPagePsfId(String psfid) throws Throwable {
        new StepsHelper().getSAPIPopularityResponse(psfid);
    }

    @And("^I send a request to the assembler API to get the popularity for (.+)$")
    public void iSendARequestToTheAssemblerAPIToGetThePopularityForPsfId(String psfId) throws Throwable {
        Response searchApiPopularityReponse = ScenarioContext.getData(psfId);
        List<String> ids = CommonHelpers.getRecordIds(searchApiPopularityReponse, "resultsList.records.id");

        String internald = commonHelpers.getIds(searchApiPopularityReponse, "category.internalId");
        Response assemblerPopularityResponse = StepsHelper.getAssemblerPopularityResponse(internald);
        List<String> assemblerids = CommonHelpers.getPRecordIds(assemblerPopularityResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        ScenarioContext.setSearchApiRecordIds(ids);
        ScenarioContext.setAssemblerPRecordIds(assemblerids);
    }

    @Given("^I request the search API end point sorted with Price High to Low as a parameter for L3 Category page for (.+)$")
    public void iRequestTheSearchAPIEndPointSortedWithPriceHighToLowAsAParameterForLCategoryPageForPsfId(String psfId) throws Throwable {
        new StepsHelper().getSearchApiHIghToLowResponse(psfId);
    }

    @And("^I send a request to the assembler API to get the records in descending order for (.+)$")
    public void iSendARequestToTheAssemblerAPIToGetTheRecordsInDescendingOrderForPsfId(String psfId) throws Throwable {
        Response searcApiDescendingResponse = ScenarioContext.getData(psfId);
        List<String> ids = CommonHelpers.getRecordIds(searcApiDescendingResponse, "resultsList.records.id");

        String internalId = commonHelpers.getIds(searcApiDescendingResponse, "category.internalId");
        Response assemblerHighToLowResponse = StepsHelper.getAssemblerHighToLowResponse(internalId);
        List<String> assemblerids = CommonHelpers.getPRecordIds(assemblerHighToLowResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        ScenarioContext.setSearchApiRecordIds(ids);
        ScenarioContext.setAssemblerPRecordIds(assemblerids);
    }

    @Given("^I request the search API end point sorted with Price Low to High as a parameter for L3 Category page for(.+)$")
    public void iRequestTheSearchAPIEndPointSortedWithPriceLowToHighAsAParameterForLCategoryPageFor(String psfId) throws Throwable {
        new StepsHelper().getSAPILowToHighResponse(psfId.trim());
    }

    @And("^I send a request to the assembler API to get the records in ascending order for (.+)$")
    public void iSendARequestToTheAssemblerAPIToGetTheRecordsInAscendingOrderForPsfId(String psfId) throws Throwable {
        Response searchApiAscendingResponse = ScenarioContext.getData(psfId);
        List<String> ids = CommonHelpers.getRecordIds(searchApiAscendingResponse, "resultsList.records.id");

        String internalId = commonHelpers.getIds(searchApiAscendingResponse, "category.internalId");
        Response assemblerLowToHighResponse = StepsHelper.getAssemblerLowToHighResponse(internalId);
        List<String> assemblerIds = CommonHelpers.getPRecordIds(assemblerLowToHighResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        ScenarioContext.setSearchApiRecordIds(ids);
        ScenarioContext.setAssemblerPRecordIds(assemblerIds);
    }

    @Then("^results list is sorted by ([^\"]*$)")
    public void resultsListIsSortedByPopularity(String sortOption) throws Throwable {
        switch (sortOption) {
            case "popularity":
                assertEquals(
                        "The results list should have been sorted by popularity",
                        ScenarioContext.getAssemblerPRecordIds(),
                        ScenarioContext.getSearchApiRecordIds()
                );
                break;
            case "Price High to Low":
                assertEquals(
                        "The results list should have been sorted by Price: High to Low order",
                        ScenarioContext.getAssemblerPRecordIds(),
                        ScenarioContext.getSearchApiRecordIds()
                );
                break;
            case "Price Low to High":
                assertEquals(
                        "The results list should have been sorted by Price: Low to High order",
                        ScenarioContext.getAssemblerPRecordIds(),
                        ScenarioContext.getSearchApiRecordIds()
                );
                break;
            case "relevance":
                assertEquals(
                        "The results list should have been sorted by relevance",
                        ScenarioContext.getAssemblerPRecordIds(),
                        ScenarioContext.getSearchApiRecordIds()
                );
                break;
            case "Capacity":
                assertEquals(
                        "The results list should have been sorted by Capacity",
                        ScenarioContext.getSearchApiRecordIds(),
                        ScenarioContext.getAssemblerPRecordIds()
                );
                break;
            default:
                fail("Results list not sorted by: " + sortOption);
        }
    }

    @Then("^search api category details should match with assembler response (.+) details$")
    public void search_api_category_details_should_match_with_assembler_response_details(String psfID) throws Throwable {
        Response response = ScenarioContext.getData(psfID);
        Response assemblerResponse = commonHelpers.getAssemblerResponse(response);

        Map<String, Object> searchResponsemap = termNodeHelper.getSAPITermNodeCatDetails(response);
        Map<String, Object> assemblerTermNodemap = termNodeHelper.getAssemblerTermNodeCatDetails(assemblerResponse);
        Map<String, String> termNodeParentMap = termNodeHelper.getl3ParentProperties(assemblerResponse);
        assertEquals("Level 3 label between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3label"), searchResponsemap.get("l3label"));
        assertEquals("Level 3 SEO Url between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3seoUrl"), searchResponsemap.get("l3seoUrl"));
        assertEquals("Level 3 bin count between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3binCount"), searchResponsemap.get("l3binCount"));
        assertEquals("Level 3 SEO Page Title between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3seoPageTitle"), searchResponsemap.get("l3seoPageTitle"));
        assertEquals("Level 3 Image Url between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3imgUrl"), searchResponsemap.get("l3imgUrl"));
        assertEquals("Level 3 SEO Catgory Name between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3seoCategoryName"), searchResponsemap.get("l3seoCategoryName"));
        assertEquals("Level 3 SEO MEta Description between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3seoMetaDescription"), searchResponsemap.get("l3seoMetaDescription"));

        assertEquals("Level 2 SEO Category Name between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l2seoCategoryName"), searchResponsemap.get("l2seoCategoryName"));
        assertEquals("Level 2 label between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l2label"), searchResponsemap.get("l2label"));
        assertEquals("Level 2 RS Id between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l2rsId"), searchResponsemap.get("l2rsId"));

        assertEquals("Level 1 label between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l1label"), searchResponsemap.get("l1label"));
        assertEquals("Level 1 RS id between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l1rsId"), searchResponsemap.get("l1rsId"));
        assertEquals("Level 1 SEO Category Name between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l1seoCategoryName"), searchResponsemap.get("l1seoCategoryName"));
    }

    @Then("^The search api bin count results should match the assembler response count (.+) details$")
    public void the_search_api_count_should_match_with_assembler_count_details(String psfID) throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = commonHelpers.getAssemblerResponse(response);

        Map<String, Object> searchResponsemap = termNodeHelper.getSAPITermNodeCatDetails(response);
        Map<String, Object> assemblerTermNodemap = termNodeHelper.getAssemblerTermNodeCatDetails(assemblerResponse);
        assertEquals(
                "The Level 3 bin count bwtween SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3binCount"),
                searchResponsemap.get("l3binCount")
        );
    }

    @Then("^Then the following details for an initial list of 20 products should be displayed$")
    public void the_following_details_for_the_initial_twenty_products_should_be_displayed() throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = commonHelpers.getAssemblerResponse(response);
        termNodeHelper.assertTermNodePageAttributes(response, assemblerResponse);
    }

    @Then("^The search api bin count results should match the assembler totalNumRecs (.+) details$")
    public void the_search_api_count_should_match_with_assembler_totalNumRecs_details(String psfID) throws Throwable {
        Response response = ScenarioContext.getData(psfID);
        Response assemblerResponse = commonHelpers.getAssemblerResponse(response);

        Map<String, Object> searchResponsemap = termNodeHelper.getSAPITermNodeCatDetails(response);
        Map<String, String> assemblerTermNodemap = termNodeHelper.getAssemblerTermNodetotalNumRecsDetails(assemblerResponse);
        assertEquals(
                "The Level 3 bin count between SearchAPI and Assembler should have matched",
                assemblerTermNodemap.get("l3binCount"),
                searchResponsemap.get("l3binCount")
        );
    }

    @Given("^I request the search API end point sorted in (.+) order with Capacity as a parameter for L3 Category page for(.+)$")
    public void iRequestTheSearchAPIEndPointSortedWithCapacityAsAParameterForLCategoryPageFor(String order, String psfId) throws Throwable {
        new StepsHelper().getSAPICapacityResponse(psfId.trim(), order);
    }

    @And("^I send a request to the assembler API to get the (.+) order of the Capacity records in ascending order for (.+)$")
    public void iSendARequestToTheAssemblerAPIToGetTheCapacityRecordsInAscendingOrderForPsfId(String order, String psfId) throws Throwable {
        Response searchApiAscendingResponse = ScenarioContext.getData(psfId);
        List<String> ids = CommonHelpers.getRecordIds(searchApiAscendingResponse, "resultsList.records.id");

        String internalId = commonHelpers.getIds(searchApiAscendingResponse, "category.internalId");
        Response assemblerCapacityResponse = StepsHelper.getAssemblerCapacityResponse(internalId, order);
        List<String> assemblerIds = CommonHelpers.getPRecordIds(assemblerCapacityResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        ScenarioContext.setSearchApiRecordIds(ids);
        ScenarioContext.setAssemblerPRecordIds(assemblerIds);
    }

    @And("^the product list column headers for specification attributes should be returned in the same order as in the facet list$")
    public void i_get_the_specificationAttributes_from_the_searchAPIResponse() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String id = searchApiResponse.path("category.id");
        List<String> columnHeadersList = CommonHelpers.getRecordIds(searchApiResponse, "resultsList.columnHeaders");
        new StepsHelper().getl3CategorySearchResponse(id);
        List<String> CategoryMetaData = termNodeHelper.getCategoryProperties(ScenarioContext.getData(id));
        assertEquals(
                "The meta data/description between SearchAPI and Assembler should have matched",
                CategoryMetaData,
                columnHeadersList
        );
    }

    @Given("^I request the search API end point to request available facets for (.+)$")
    public void iRequestTheSearchAPIEndPointToRequestAvailableFacetsForPsfId(String psfId) throws Throwable {
        new StepsHelper().getSearchApiFacets(psfId);
    }

    @Given("^I request the search API end point for an L3 Category for (.+)$")
    public void iRequestTheSearchAPIEndPointForAnLCategoryForPsf(String psfId) throws Throwable {
        StepsHelper.getSearchApiResponse(psfId);
    }

    @And("^I send a request to the assembler API to get the facet bin count for (.+)$")
    public void iSendARequestToTheAssemblerAPIToGetTheFacetBinCountForPsfId(String psfId) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        List<String> whiteList = Arrays.asList(UrlBuilder.getDimensionsWhiteList().split(","));
        List<String> blackList = Arrays.asList(UrlBuilder.getDimensionsBlackList().split(","));
        List<String> columnHeadersList = CommonHelpers.getRecordIds(searchApiResponse, "refinements.key");
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        new StepsHelper().getl3CategorySearchResponse(psfId);
        List<String> categoryMetaData = termNodeHelper.getCategoryProperties(ScenarioContext.getData(psfId));
        Response assemblerResponse = StepsHelper.getAssemblerTermNodeResponse(internalId);
        columnHeadersList.removeIf((String str) -> !(whiteList.contains(str) || categoryMetaData.contains(str) || (!blackList.contains(str))));
        termNodeHelper.assertRefinementsBinCount(searchApiResponse, assemblerResponse, columnHeadersList);
    }

    @And("^I get the specification attributes list from the search Api response$")
    public void iGetTheSpecificationAttributesListFromTheSearchApiResponse() throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> specAttributesList = CommonHelpers.getSpecAttributesList(searchApiResponse);
        ScenarioContext.setsAPIattributesList(specAttributesList);
    }

    @And("^I get the specification attributes from the search API response for (.+)$")
    public void i_get_the_specificationAttributes_from_the_searchAPIResponse_for(String psfId) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        List<String> columnHeadersList = CommonHelpers.getRecordIds(searchApiResponse, "refinements.key");
        new StepsHelper().getl3CategorySearchResponse(ScenarioContext.getIds());
        List<String> categorySpecifAttributes = termNodeHelper.getCategoryProperties(ScenarioContext.getData(ScenarioContext.getIds()));
        columnHeadersList.removeIf((String str) -> !categorySpecifAttributes.contains(str));
        categorySpecifAttributes.removeIf((String str) -> !columnHeadersList.contains(str));
        assertEquals(
                "The Level 3 category specific attributes between SearchAPI and Assembler should have matched",
                columnHeadersList,
                categorySpecifAttributes
        );
    }

    @And("^I send a request to the assembler API to get the bin count and dimension ids for (.+)$")
    public void iSendARequestToTheAssemblerAPIToGetTheBinCountAndDimensionIdsForPsfId(String psfId) throws Throwable {
        Response searchApiPopularityReponse = ScenarioContext.getData(psfId);
        Response assemblerResponse = commonHelpers.getAssemblerResponse(searchApiPopularityReponse);
        assertTrue(
                "The bin count and dimensions between SearchAPI and Assembler should have matched",
                termNodeHelper.assertBinCountAndDimensions(searchApiPopularityReponse, assemblerResponse)
        );
    }

    @Then("^I get the list of brand attribute values from the search API response for (.+)$")
    public void iGetTheListOfBrandAttributeValuesFromTheSearchAPIResponseForPsfId(String psfId) throws Throwable {
        Response searchAPIResponse = ScenarioContext.getData(psfId);
        Response assemblerResponse = commonHelpers.getAssemblerResponse(searchAPIResponse);
        assertEquals(
                "The list of brand attribute values between SearchAPI and Assembler should have matched",
                termNodeHelper.getFacetBrandValues(searchAPIResponse),
                termNodeHelper.getAssemblerBrandList(assemblerResponse)
        );
    }

    @Then("^the ordering of attribute values of the facet with alphanumerics should be in ascending order$")
    public void theOrderingOfAttributeValuesOfTheFacetShouldBeAlphanumeric() throws Throwable {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        assertTrue(
                "The facet attribute values should have been ordered in an alphanumeric order",
                termNodeHelper.assertLabelOrder(searchAPIResponse)
        );
    }

    @Given("^I send a request to search api to get the level three category details with an invalid localeId for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithAnInvalidLocaleIdForPsfId(String psfId) throws Throwable {
        new StepsHelper().getSAPIInvalidReponse(psfId);
    }

    @Then("^(\\d+) status code should be returned for (.+)$")
    public void statuscodeShouldBeReturned(int statuscode, String psfId) throws Throwable {
        Response response = ScenarioContext.getData(psfId);
        assertEquals(
                String.format(
                        "The SearchAPI request should have come back with the expected status code of '%s'", statuscode
                ),
                statuscode,
                response.getStatusCode()
        );
    }

    @Then("^(\\d+) status code is returned")
    public void statuscodeIsReturned(int statuscode) throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        assertEquals(
                String.format(
                        "The SearchAPI request should have come back with the expected status code of '%s'", statuscode
                ),
                statuscode,
                response.getStatusCode()
        );
    }

    @Then("^(\\d+) status code returned for (.+) API response")
    public void statuscodeIsReturned(int statuscode, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        assertEquals(
                String.format(
                        "The SearchAPI request should have come back with the expected status code of '%s'", statuscode
                ),
                statuscode,
                searchApiResponse.getStatusCode()
        );
    }

    @Given("^I send a request to search api to get the level three category details without limit as parameter for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithoutLimitAsParameterUsingPsfId(String psfId) throws Throwable {
        new StepsHelper().getSearchAPIPSFCategory(psfId);
    }

    @Then("^the result list should default to (\\d+) for (.+)$")
    public void theResultListShouldDefaultTo(int listNumber, String psfId) throws Throwable {
        Response response = ScenarioContext.getData(psfId);
        List<String> responseList = response.getBody().prettyPeek().jsonPath().getList("resultsList.records.id");
        int resultList = responseList.size();
        assertEquals(
                "The result list should have defaulted to an expected number of items",
                listNumber,
                resultList
        );
    }

    @When("^I send a request to search api to get the level three category details but the limit parameter is not a number for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsButTheLimitParameterIsNotANumberForPsfId(String psfId) throws Throwable {
        new StepsHelper().getSearchAPIPSFInvalidLimitCategory(psfId);
    }

    @Then("^the search api bin count retrieved using psf_id should match with the bin counts retrieved using seoUrl namespace details for(.+)$")
    public void theSearchApiBinCountResultsShouldMatchSeoUrlNamespaceDetailsForPsfId(String psfId) throws Throwable {
        Response sapiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String seoUrl = commonHelpers.getSeoUrl(sapiResponse);
        Response seoUrlResponse = StepsHelper.getSeoUrlApi(seoUrl, ";targetState=1");
        ScenarioContext.setData(seoUrl, seoUrlResponse);
        Map<String, Object> searchResponsemap = termNodeHelper.getSAPITermNodeCatDetails(sapiResponse);
        Map<String, Object> searchSeoUrlmap = termNodeHelper.getSAPITermNodeCatDetails(seoUrlResponse);
        assertEquals(
                "Search API response should have the same bin count as seoUrl Request",
                searchResponsemap.get("l3binCount"),
                searchSeoUrlmap.get("l3binCount")
        );
    }

    @Then("^the search api bin count retrieved using psf_id should match with the bin counts retrieved using internalId namespace details for (.+)$")
    public void theSearchApiBinCountRetrievedUsingPsf_idShouldMatchWithTheBinCountsRetrievedUsingInternalIdNamespaceDetailsForPsfId(String psfId) throws Throwable {
        Response sapiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(sapiResponse, "category.internalId");

        Response internalIdResponse = StepsHelper.getInternalIdApi(internalId, ";targetState=1");
        ScenarioContext.setData(internalId, internalIdResponse);

        Map<String, Object> searchResponsemap = termNodeHelper.getSAPITermNodeCatDetails(sapiResponse);
        Map<String, Object> searchInternalIdMap = termNodeHelper.getSAPITermNodeCatDetails(internalIdResponse);
        assertEquals(
                "Search API response should have the same bin count as Internal ID Request",
                searchResponsemap.get("l3binCount"),
                searchInternalIdMap.get("l3binCount")
        );
    }

    @When("^I send a request to search api to get the level three category details with an invalid seoUrl for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithAnInvalidSeoUrlForPsfId(String seoUrl) throws Throwable {
        new StepsHelper().getInvalidSeoUrlResponse(seoUrl);
    }

    @When("^I send a request to search api to get the level three category details with an invalid internalId for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithAnInvalidInternalIdForInternalId(String internalId) throws Throwable {
        new StepsHelper().getInvalidInternalIdResponse(internalId);
    }

    @Then("^I get all the attributes and its values for the applied filter for (.+)$")
    public void iGetTheProductDetailsSearchAPIResponseForPsfId(String psfId) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        termNodeHelper.setAppliedDimensionDetails(searchApiResponse);
        String descriptors = termNodeHelper.getSAPIDescriptorsString();
        stepsHelper.getSAPIResponseForAttrFilters(psfId, descriptors);
        termNodeHelper.assertAppliedDimensions(ScenarioContext.getData(psfId));
    }

    @Then("^I get all the names and its values for the applied filter for (.+)$")
    public void i_get_the_name_details_for_PsfId(String psfId) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        termNodeHelper.setAppliedDimension(searchApiResponse);
        String descriptors = termNodeHelper.getSAPIDescriptorsString();
        stepsHelper.getSAPIResponseForAttrFilters(psfId, descriptors);
        termNodeHelper.assertAppliedDimensions(ScenarioContext.getData(psfId));
    }

    @Then("^Then I get the product details for the following details of the initial list of 20 products$")
    public void i_get_the_following_details_for_the_initial_twenty_products_should_be_displayed() throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(response, "category.internalId");
        String descriptors = termNodeHelper.getAssemblerDescriptorsString();
        Response assemblerResponse = stepsHelper.getAssemblerResponseForAttrFilters(internalId, descriptors);
        termNodeHelper.assertTermNodePageAttributes(response, assemblerResponse);
    }

    @Then("^I get the facet details for all the attributes that are applicable to the filtered list of products$")
    public void i_get_the_facet_details_for_all_the_attributes_of_the_products() throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(response, "category.internalId");
        String descriptors = termNodeHelper.getAssemblerDescriptorsString();
        Response assemblerResponse = stepsHelper.getAssemblerResponseForAttrFilters(internalId, descriptors);
        termNodeHelper.assertRefinementsForAppliedDimensions(response, assemblerResponse);
    }

    @Then("^I get the updated record count of products returned for (.+)$$")
    public void i_get_the_updated_record_count_returned(String psfId) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        String descriptors = termNodeHelper.getAssemblerDescriptorsString();
        Response assemblerResponse = stepsHelper.getAssemblerResponseForAttrFilters(internalId, descriptors);
        termNodeHelper.assertUpdateRecordCount(searchApiResponse, assemblerResponse);
    }

    @Then("^I remove a specific filter from the breadBox for (.+)$")
    public void i_remove_specific_filter_from_the_breadBox(String psfId) throws Throwable {
        LinkedHashMap<String, List<TermNodeFacetValues>> refinementsMap = ScenarioContext.getPropertiesMap();
        Object[] keys = refinementsMap.keySet().toArray();
        Object key = keys[new Random().nextInt(keys.length)];
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> descriptorsList = termNodeHelper.getRemovalDescriptorsList(response, key.toString(), null);
        String descriptors = "";
        for (String value : descriptorsList) {
            descriptors = descriptors.concat(value).concat(",");
        }
        List<String> desBeforeRemoval = CommonHelpers.getRecordIds(response, "breadbox.breadcrumbs.key");
        assertTrue(
                "The specific filter should have been present in the breadcrumbs",
                desBeforeRemoval.contains(key.toString())
        );
        stepsHelper.getSAPIResponseForAttrFilters(psfId, descriptors);
        List<String> desAfterRemoval = CommonHelpers.getRecordIds(ScenarioContext.getData(ScenarioContext.getIds()), "breadbox.breadcrumbs.key");
        assertFalse(
                "The specific filter should have been removed from the breadcrumbs",
                desAfterRemoval.contains(key.toString())
        );
    }

    @Then("^I remove a specific filter value from the breadBox for (.+)$")
    public void i_remove_specific_filter_value_from_the_breadBox(String psfId) throws Throwable {
        LinkedHashMap<String, List<TermNodeFacetValues>> refinementsMap = ScenarioContext.getPropertiesMap();
        Object[] keys = refinementsMap.keySet().toArray();
        Object key = keys[new Random().nextInt(keys.length)];
        String label = refinementsMap.get(key).get(0).getLabel();
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> descriptorsList = termNodeHelper.getRemovalDescriptorsList(response, key.toString(), label);
        String descriptors = "";
        for (String value : descriptorsList) {
            descriptors = descriptors.concat(value).concat(",");
        }
        List<String> desBeforeRemoval = CommonHelpers.getPRecordIds(response, "breadbox.breadcrumbs.value.label");
        assertTrue(
                "The specific filter should have been present in the breadcrumbs",
                desBeforeRemoval.contains(label)
        );
        stepsHelper.getSAPIResponseForAttrFilters(psfId, descriptors);
        List<String> desAfterRemoval = CommonHelpers.getPRecordIds(ScenarioContext.getData(ScenarioContext.getIds()), "breadbox.breadcrumbs.value.label");
        assertFalse(
                "The specific filter should have been removed from the breadcrumbs",
                desAfterRemoval.contains(label)
        );
    }

    @Then("^I check the values in the remove all link in the breadbox$")
    public void i_check_the_values_in_the_removeAll_link_in_the_breadbox() throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        String descriptors = response.path("breadbox.removeAll.descriptors");
        assertNull("The remove all link in the breadbox should be null or absent", descriptors);
    }

    @Then("^Then I get the Result count showing the total number of products returned for (.+)$")
    public void i_get_the_result_count_showing_the_total_number_of_products(String psfId) throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        int resultCount = response.path("queryInfo.results_count");
        Response assemblerResponse = commonHelpers.getAssemblerResponse(response);
        int assemblerTotalCount = assemblerResponse.path("mainContent[0].contents[0].totalNumRecs");
        assertEquals(
                "The total number of products returned by the Assembler should match that of SearchAPI",
                assemblerTotalCount,
                resultCount
        );
    }

    @Given("^I send a request to search api to get the level three category details and no limit is specified by client for (.+)")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsAndNoLimitIsSpecifiedByClientForPsfId(String psfId) throws Throwable {
        new StepsHelper().getSearchAPIPSFCategory(psfId);
    }

    @And("^the default page Pagination number is (\\d+) for (.+)$")
    public void theDefaultPagePaginationNumberIs(String page, String psfId) throws Throwable {
        Response searchApiAscendingResponse = ScenarioContext.getData(psfId);
        String pageNumber = commonHelpers.getIds(searchApiAscendingResponse, "resultsList.pagination.page");
        assertEquals("The default page number should have been as expected", pageNumber, page);
    }

    @Given("^I send a request to search api to get the level three category details and the limit is more than (\\d+) products for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsAndTheLimitIsMoreThanProductsForPsfId(int limit, String psfId) throws Throwable {
        new StepsHelper().getSAPIAboveLimitResponse(limit, psfId);
    }

    @Then("^the number of products returned is (\\d+) for (.+)$")
    public void theAbsoluteMaximumNumberOfProductsReturnedIs(String limit, String psdId) throws Throwable {
        Response spfResponse = ScenarioContext.getData(psdId);
        String actualLimitNumber = commonHelpers.getIds(spfResponse, "resultsList.pagination.limit");
        assertEquals(
                "Total number of products returned should have been within the limit for the specific category",
                limit,
                actualLimitNumber
        );
    }

    @Given("^I request an end-point for an L3 Category with a specified limit of (\\d+) products for (.+)$")
    public void iRequestAnEndPointForAnLCategoryWithASpecifiedLimitOfProductsFor(String limit, String psfId) throws Throwable {
        new StepsHelper().getValidLimitPSFResponse(limit, psfId);
    }

    @When("^I send a request to search api to get the level three category details with pagination page (\\d+) specified for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithPaginationPageSpecifiedForPsfId(int pagPage, String psfId) throws Throwable {
        new StepsHelper().getPaginationPageResponse(pagPage, psfId);
    }

    @Then("^last page pagination number should be returned for (.+)$")
    public void lastPagePaginationNumberShouldBeReturned(String psfId) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(psfId);
        String lastPaginationNumber = commonHelpers.getIds(SAPIResponse, "resultsList.pagination.lastPage");
        double lastDoublePaginationNumber = Double.parseDouble(lastPaginationNumber);

        String internald = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAsseblerPaginationResponse(internald);
        String assemblerStringLastRecNum = commonHelpers.getIds(assemblerResponse, "mainContent[0].contents[0].totalNumRecs");

        double currentPage = Math.ceil(Double.parseDouble(assemblerStringLastRecNum) / 20.0);
        assertEquals(
                "The last page number (pagination) returned by Assembler should match that of SearchAPI",
                String.valueOf(currentPage),
                String.valueOf(lastDoublePaginationNumber)
        );
    }

    @And("^the current page pagination number should be returned for(.+)$")
    public void theCurrentPagePaginationNumberShouldBeReturned(String psfId) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(psfId.trim());
        String SAPICurrentPaginationPage = commonHelpers.getIds(SAPIResponse, "resultsList.pagination.page"); //2

        String internald = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAsseblerPaginationResponse(internald);
        String assemblerStringLastRecNum = commonHelpers.getIds(assemblerResponse, "mainContent[0].contents[0].lastRecNum"); //40
        int currentPage = Integer.parseInt(assemblerStringLastRecNum) / 20;
        assertEquals(
                "The current page number (pagination) returned by Assembler should match that of SearchAPI",
                String.valueOf(currentPage),
                SAPICurrentPaginationPage
        );
    }

    @Then("^the total number of products in the results set should be returned$")
    public void theTotalNumberOfProductsInTheResultsSetShouldBeReturnedForPsfId() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());

        String totalProducts = commonHelpers.getIds(SAPIResponse, "resultsList.pagination.count");

        String internald = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAsseblerPaginationResponse(internald);
        String assemblerTotalProducts = commonHelpers.getIds(assemblerResponse, "mainContent[0].contents[0].totalNumRecs"); //255

        String SAPIResultsCount = commonHelpers.getIds(SAPIResponse, "queryInfo.results_count");

        assertEquals(
                "The category specific product count between SearchAPI and Assembler should have matched",
                assemblerTotalProducts,
                SAPIResultsCount
        );
        assertEquals(
                "The category specific product count between SearchAPI and Assembler should have matched",
                assemblerTotalProducts,
                totalProducts
        );
    }

    @And("^the limit of records per page should be returned$")
    public void theLimitOfRecordsPerPageShouldBeReturnedForPsfId() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());

        String totalProducts = commonHelpers.getIds(SAPIResponse, "resultsList.pagination.limit");

        String internald = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAsseblerPaginationResponse(internald);
        String assemblerLimit = commonHelpers.getIds(assemblerResponse, "'endeca:assemblerRequestInformation'.requestQueryString");
        String lastLimit = assemblerLimit.split("Nrpp=")[1];
        String lastAssemblerLimit = lastLimit.substring(0, lastLimit.indexOf('&'));
        assertEquals(
                "The category limit of records per page between SearchAPI and Assembler should have matched",
                lastAssemblerLimit,
                totalProducts
        );
    }

    @Given("^I request an end-point for an L3 Category for pagination page ([-+]?\\d*) and limit ([-+]?\\d*)$")
    public void iRequestAnEndPointForAnLCategoryForPaginationPageAndLimitForPsfId(int pageNumber, int limit) throws Throwable {
        new StepsHelper().getParamPaginationPSFResponse(pageNumber, limit);
    }

    @And("^the products returned match the products from the assembler for page (\\d+) and limit (\\d+)$")
    public void theProductsReturnedMatchTheProductsFromTheAssemblerForPageAndLimitForTheSamePsfId(int pageNumber, int limit) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> listOfRecords = CommonHelpers.getRecordIds(SAPIResponse, "resultsList.records.id");
        Collections.sort(listOfRecords);

        String internald = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerParameterisedResponse(pageNumber, limit, internald);
        Map<String, Map<String, List<String>>> mapOfRecords = termNodeHelper.getAssemblerTermNodeProp(assemblerResponse);
        List<String> pRecordIdList = new ArrayList<>();
        Set<String> pRecordIdSet = mapOfRecords.keySet();
        pRecordIdList.addAll(pRecordIdSet);
        Collections.sort(pRecordIdList);

        assertEquals(
                "The products returned by Assembler should have matched SearchAPI (page limit related)",
                pRecordIdList,
                listOfRecords
        );
    }

    @When("^I send a request to search api to get the level three category details with a pagination page (.+) specified for (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithAPaginationPageSpecifiedForPsfId(String invalidPage, String psfId) throws Throwable {
        new StepsHelper().getInvalidPaginationPageResponse(invalidPage, psfId);
    }

    @When("^I send a request to search api to get the level three category details with a limit (.*) specified for (.*)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithALimitJunkSpecifiedForPsfId(String invalidLimit, String psfId) throws Throwable {
        new StepsHelper().getInvalidLimitSAPIResponse(invalidLimit, psfId);
    }

    @Then("^I get all the level three category ids with minimum count (\\d+) and maximum (\\d+) for (.+)$")
    public void i_get_all_the_level_three_category_ids(int minCount, int maxCount, String categoryName) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(categoryName);
        List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(searchApiResponse,
                "category.children.children.children.children");
        ScenarioContext.setIds(commonHelpers.getLevelThreeIdsList(levelThreeIds, minCount, maxCount).get(0));
    }

    @Given("^I request an end-point for an L3 Category for out of bounds pagination page (\\d+) and limit (\\d+)$")
    public void iRequestAnEndPointForAnLCategoryForOUtOfBoundsPaginationPageAndLimitForAnyPsfId(int pageNumber, int limit) throws Throwable {
        new StepsHelper().getOutOfBoundsPageSAPIResponse(pageNumber, limit);
    }

    @When("^I send a request to search api to get the level three category details with a pagination page (\\d+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryDetailsWithAPaginationPage(int pageNumber) throws Throwable {
        new StepsHelper().getPageNumberResponse(pageNumber);
    }

    @Then("^(\\d+) status code should be returned$")
    public void statusCodeShouldBeReturned(int statuscode) throws Throwable {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        assertEquals(
                String.format(
                        "The SearchAPI request should have come back with the expected status code of '%s'", statuscode
                ),
                statuscode,
                response.getStatusCode()
        );
    }

    @When("^I request search API to get results count for (.+)$")
    public void iRequestSearchAPIToGet(String categories) throws Throwable {
        stepsHelper.getCategorySearchResponse(categories);
    }

    @Then("^the results count returned should match the assembler for (.+)$")
    public void theResultsCountShouldBeReturned(String categories) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(categories.trim());
        String resultsCount = commonHelpers.getIds(SAPIResponse, "queryInfo.results_count");
        String internald = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAsseblerCategoryResponse(internald);
        String assemblerTotalCount = commonHelpers.getIds(assemblerResponse, "mainContent[0].contents[0].totalNumRecs");

        assertEquals(
                "The total number of records or result count between Assembler and SearchAPI should have matched",
                assemblerTotalCount,
                resultsCount
        );
    }

    @Then("^the special characters are removed from the search query (.+) before it gets executed to match (.+)$")
    public void theSpecialCharactersAreRemovedFromTheSearchQueryBeforeItGetsExecuted(String searchTerm, String expectedTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        String strippedQuery = Page.getProperty(expectedTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        String appliedSearchTerm = commonHelpers.getIds(searchApiResponse, "queryInfo.search_keyword_app");

        assertEquals(
                "Search Keyword Applied does not equal original search term with special characters removed",
                strippedQuery,
                appliedSearchTerm
        );
    }

    @Then("^the search_keyword is returned in the data layer for (.+)$")
    public void theSearch_keywordIsReturnedInTheDataLayer(String searchTerm) {
        String searchQueryTerm = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQueryTerm);
        String searchQuery = commonHelpers.getIds(searchApiResponse, "queryInfo.search_keyword");
        assertFalse("Search Query is not returned", searchQuery.isEmpty());
    }

    @And("^the applied search keyword is returned in the data layer for (.+)$")
    public void theAppliedSearchKeywordIsReturnedInTheDataLayer(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        String appliedSearchTerm = commonHelpers.getIds(searchApiResponse, "queryInfo.search_keyword_app");
        assertFalse("Applied Search Keyword was not returned", appliedSearchTerm.isEmpty());
    }

    @Then("^status code is (\\d+) for the (.+) search query for markets other than JP and CN$")
    public void verifyStatusCodeIsForSearchTermQuery(int statuscode, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        String market = System.getProperty("country");
        if (market.equalsIgnoreCase("cn") || market.equalsIgnoreCase("jp")) {
            Response response = ScenarioContext.getData(searchQuery);
            assertEquals(
                    "SearchAPI request should have returned the expected status code",
                    EXPECTED_OK_STATUS_CODE,
                    response.getStatusCode()
            );
        } else {
            Response response = ScenarioContext.getData(searchQuery);
            assertEquals(
                    String.format(
                            "The SearchAPI request should have come back with the expected status code of '%s'", statuscode
                    ),
                    statuscode,
                    response.getStatusCode()
            );
        }
    }

    @Given("^I request the search API end point for an L3 Category page with a (.+) brand for (.+)$")
    public void iRequestTheSearchAPIEndPointForAnLCategoryPageWithABrandForPsfId(String type, String psfId) throws Throwable {
        switch (type) {
            case "boosted":
                stepsHelper.getResultsListResponse(psfId);
                break;
            case "buried":
                stepsHelper.getResultsListResponse(psfId);
                break;
        }
    }

    @Given("^I request the search API end point for a Termninal node page with a searchTerm for a boosted brand for psfId$")
    public void iRequestTheSearchAPIEndPointForATermninalNodePageWithASearchTermForABoostedBrandFor() throws Throwable {
        stepsHelper.getBoostedResultsListResponsePSFWithTerm();
    }

    @Given("^I request the search API end point for a Termninal node page with a searchTerm for a buried brand for psf.id$")
    public void iRequestTheSearchAPIEndPointForATermninalNodePageWithASearchTermTermForABuriedBrandFor() throws Throwable {
        stepsHelper.getBuriedResultsListResponsePSFWithTerm();
    }

    @Given("^I request search api with (.*) as search term with product field equal (.*)$")
    public void i_request_search_api_with_search_term_by_mpn_as_search_term_with_product_field_equal_P_manufacturerPartNumber(String searchQuery, String productField) {
        String searchTerm = Page.getProperty(searchQuery);
        stepsHelper.getSAPISearchQueryProductsFieldResponse(searchTerm, productField);
    }

    @Given("^I request search api with (.*) as search term for terminal node with product field equal (.*)$")
    public void i_request_search_api_with_search_term_psfID_as_search_term_for_terminal_node_with_product_field_equal_specified_key(String psf_Id, String expectedKeys) {
        String searchTerm = Page.getProperty(psf_Id);
        assertNotNull(
                String.format(
                        "No valid search term (%s) is present for the terminal node with the product field '%s'. " +
                                "Check config file for it's setting",
                        searchTerm, psf_Id
                ),
                searchTerm
        );
        stepsHelper.getResponseForPSF_IDWithProductsFieldParams(searchTerm, expectedKeys);
    }

    @Given("^I request search api with (.*) as search term with Products field equal (.*),(.*)$")
    public void i_request_search_api_with_search_term_by_mpn_as_search_term_with_Products_field_equal_specified_param(String product_id, String productFieldParam, String ProductFieldParam1) {
        stepsHelper.getResponseWithProductIdWithProductsFieldWithMultipleParam(product_id, productFieldParam, ProductFieldParam1);
    }

    @Then("^the boosted products are returned at the top of the results list for (.+)$")
    public void theBoostedProductsAreReturnedAtTheTopOfTheResultsListForPsfId(String psfId) throws Throwable {
        stepsHelper.assertBoostedProducts(psfId);
    }

    @Then("^the boosted products are returned at the top of the list for term")
    public void theBoostedProductsAreReturnedAtTheTopOfTheResultListForPsfId() throws Throwable {
        stepsHelper.assertBoostedProductsForPSFWithSearchTerm();
    }

    @Then("^the buried products are returned in the the results list for (.+)$")
    public void theBuriedProductsAreReturnedAtTheBottomOfTheResultsListForPsfId(String psfId) throws Throwable {
        stepsHelper.assertBuriedProducts(psfId);
    }

    @Then("^the buried products are returned for psfid at the top of the list for term$")
    public void theBuriedProductsAreReturnedAtTheTopOfTheListForTerm() throws Throwable {
        stepsHelper.assertBuriedProductsForPSFWithSearchTerm();
    }

    @Then("^the name and path of the triggered bury rule should be returned for (.+)$")
    public void theNameAndPathOfTheTriggeredRuleShouldBeReturned(String psfId) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(psfId);
        String rulesMap = commonHelpers.getIds(SAPIResponse, "queryInfo.rulesFired").replace("}", "");
        String rule = rulesMap.split("content/")[1];
        String expectedRule = UrlBuilder.getExpectedBuriedPath();
        assertEquals("The expected rule should have been returned by SearchAPI", expectedRule, rule);
    }

    @Then("^the products that are not part of boost rule are sorted as per the browse relevancy ranking set-up for (.+)$")
    public void theProductsThatAreNotPartOfBoostRuleAreSortedAsPerTheBrowseRelevancyRankingSetUpForPsfId(String psfId) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(psfId);
        termNodeHelper.getSearchApiRefinementProperties(SAPIResponse);
        String boostedL3Brand = Page.getProperty("l3.boosted.brand");
        String infineonBinCount = termNodeHelper.getRefinementsBinCount("I18NsearchBybrand", boostedL3Brand);

        Double infineonStoppingPage = Double.parseDouble(infineonBinCount) / 20.0;
        int pageNumber = 0;
        Integer recorsIndexOnStoppingPage = Integer.parseInt(infineonBinCount) % 20;
        if (recorsIndexOnStoppingPage > 0) {
            pageNumber = infineonStoppingPage.intValue() + 1;
        } else {
            pageNumber = infineonStoppingPage.intValue();
        }

        stepsHelper.getSAPIEndOfBoostResponse(pageNumber, psfId);
        Response lastBrandPageResponse = ScenarioContext.getData(psfId);
        List<TermNodeRecordModel> records = termNodeHelper.getTermNodeProperties(lastBrandPageResponse);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : records) {
            if (!model.getPropertiesMap().get("P_brand").equals(boostedL3Brand)) {
                recordIdList.add(model.getPropertiesMap().get("P_recordID"));
            }
        }
        int listSize = recordIdList.size();

        String internalId = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Response assemblerLastBrandPageResponse = StepsHelper.getAssemblerResponseWitBoostedRecords(internalId, listSize);
        List<List<String>> list = assemblerLastBrandPageResponse.getBody().jsonPath().prettyPeek().getList("mainContent[0].contents[0].records.attributes.P_recordID");
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : list) {
            for (String id : idList) {
                pRecordIds.add(id);
            }
        }
        assertEquals(
                "The products that are not part of boost rule should have been sorted as per the browse relevancy ranking " +
                        "set-up for the specific category",
                pRecordIds,
                recordIdList
        );
    }

    @Then("^the products that are not part of bury rule are sorted as per the browse relevancy ranking set-up for (.+)$")
    public void theProductsThatAreNotPartOfBuryRuleAreSortedAsPerTheBrowseRelevancyRankingSetUpForPsfId(String psfId) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(psfId);
        Integer totalCount = Integer.parseInt(commonHelpers.getIds(SAPIResponse, "resultsList.pagination.count"));
        String buriedL3Brand = Page.getProperty("l3.buried.brand");

        termNodeHelper.getSearchApiRefinementProperties(SAPIResponse);
        Integer buriedProductsBinCount = Integer.parseInt(termNodeHelper.getRefinementsBinCount("I18NsearchBybrand", buriedL3Brand));

        Integer nonBuriedProductCount = totalCount - buriedProductsBinCount;

        Integer pageNumber = 0;
        Double buriedProductsStartingPage = nonBuriedProductCount.doubleValue() / 20;
        int recordsIndexOnSStartingPage = (nonBuriedProductCount) % 20;
        if (recordsIndexOnSStartingPage > 0) {
            pageNumber = buriedProductsStartingPage.intValue() + 1;
        } else {
            pageNumber = buriedProductsStartingPage.intValue();
        }
        stepsHelper.getSAPIStartOfBuryResponse(pageNumber, psfId);
        Response lastBrandPageResponse = ScenarioContext.getData(psfId);
        List<TermNodeRecordModel> records = termNodeHelper.getTermNodeProperties(lastBrandPageResponse);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : records) {
            if (!model.getPropertiesMap().get("P_brand").equals(buriedL3Brand)) {
                recordIdList.add(model.getPropertiesMap().get("P_recordID"));
            }
        }
        int listSize = recordIdList.size();

        String internalId = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Integer No = 20 * (pageNumber - 1);

        Response assemblerLastBrandPageResponse = StepsHelper.getAssemblerResponseWithBuriedRecords(internalId, listSize, No);
        List<List<String>> list = assemblerLastBrandPageResponse.getBody().jsonPath().getList("mainContent[0].contents[0].records.attributes.P_recordID");
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : list) {
            for (String id : idList) {
                pRecordIds.add(id);
            }
        }
        assertEquals(
                "The products that are not part of bury rule should have been sorted as per the browse relevancy ranking " +
                        "set-up for the specific category",
                pRecordIds,
                recordIdList
        );
    }

    @Then("^the name and path of the triggered boost rule should be returned for (.+)$")
    public void theNameAndPathOfTheTriggeredBoostRuleShouldBeReturnedForPsfId(String psfId) throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(psfId);
        String rulesMap = commonHelpers.getIds(SAPIResponse, "queryInfo.rulesFired").replace("}", "");
        String rule = rulesMap.split("content/")[1];
        String expectedRule = UrlBuilder.getExpectedBoostedPath();
        assertEquals(
                "The triggered boost rule should have returned the name and path of the specific category",
                expectedRule,
                rule
        );
    }

    @Then("^the name and path of the triggered boost rule is returned$")
    public void theNameAndPathOfTheTriggeredBoostRuleIsReturnedForTerm() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(UrlBuilder.getBoostedTerm());
        String rulesMap = commonHelpers.getIds(SAPIResponse, "queryInfo.rulesFired.ResultsList");
        String rule = rulesMap.split("content/")[1];
        String expectedRule = UrlBuilder.getExpectedBoostedRuleWithSearchTermPath();
        assertEquals(
                "The triggered boost rule should have returned the name and path of the search term",
                expectedRule,
                rule
        );
    }

    @Then("^the name and path of the triggered bury rule is returned$")
    public void theNameAndPathOfTheTriggeredBuryRuleIsReturnedForTerm() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(UrlBuilder.getBuriedTerm());
        String rulesMap = commonHelpers.getIds(SAPIResponse, "queryInfo.rulesFired.ResultsList");
        String rule = rulesMap.split("content/")[1];
        String expectedRule = UrlBuilder.getExpectedBuriedRuleWithSearchTermPath();
        assertEquals(
                "The triggered bury rule should have returned the name and path of the search term",
                expectedRule,
                rule
        );
    }

    @Then("^the products which are not part of boost rule are sorted as per the browse relevancy ranking setup for term and psfId$")
    public void theProductsWhichAreNotPartOfBoostRuleAreSortedAsPerTheBrowseRelevancyRankingSetUpForTerm() throws Throwable {
        Response searchAPI = ScenarioContext.getData(UrlBuilder.getBoostedTerm());

        String psfId = UrlBuilder.getBoostedPsfId();
        String term = UrlBuilder.getBoostedTerm();
        List<String> recordIdList = termNodeHelper.getPageNumberBoostedProducts(searchAPI, term, psfId);

        String internalId = commonHelpers.getIds(searchAPI, "category.internalId");
        Response assemblerLastBrandPageResponse = StepsHelper.getAssemblerResponsePSFWithSearchTermWithBoostedRecords(term, internalId, recordIdList.size());
        List<List<String>> list = assemblerLastBrandPageResponse.getBody().jsonPath().prettyPeek().getList("mainContent[0].contents[0].records.attributes.P_recordID");
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : list) {
            for (String id : idList) {
                pRecordIds.add(id);
            }
        }
        assertEquals(
                "The products that are not part of boost rule should have been sorted as per the browse relevancy " +
                        "ranking set-up for the specific category",
                pRecordIds,
                recordIdList
        );
    }

    @Then("^the products which are not part of bury rule are sorted as per the browse relevancy ranking setup for psf.id and term$")
    public void theProductsWhichAreNotPartOfBuryRuleAreSortedAsPerTheBrowseRelevancyRankingSetUpForPsfId() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(UrlBuilder.getBuriedTerm());
        String psfId = UrlBuilder.getBuriedPsfId();
        String term = UrlBuilder.getBuriedTerm();
        Integer totalCount = Integer.parseInt(commonHelpers.getIds(SAPIResponse, "resultsList.pagination.count"));

        termNodeHelper.getSearchApiRefinementProperties(SAPIResponse);
        String buriedBrand = UrlBuilder.getBuriedBrandPSFWithSearchTerm();
        Integer buriedProductsBinCount = Integer.parseInt(termNodeHelper.getRefinementsBinCount("I18NsearchBybrand", buriedBrand));

        Integer nonBuriedProductCount = totalCount - buriedProductsBinCount;

        Integer pageNumber = 0;
        Double buriedProductsStartingPage = nonBuriedProductCount.doubleValue() / 20;
        int recordsIndexOnSStartingPage = (nonBuriedProductCount) % 20;
        if (recordsIndexOnSStartingPage > 0) {
            pageNumber = buriedProductsStartingPage.intValue() + 1;
        } else {
            pageNumber = buriedProductsStartingPage.intValue();
        }
        stepsHelper.getSAPIStartOfBuriedResponse(term, pageNumber, psfId);
        Response lastBrandPageResponse = ScenarioContext.getData(term);
        List<TermNodeRecordModel> records = termNodeHelper.getTermNodeProperties(lastBrandPageResponse);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : records) {
            if (!model.getPropertiesMap().get("P_brand").equals(buriedBrand)) {
                recordIdList.add(model.getPropertiesMap().get("P_recordID"));
            }
        }
        int listSize = recordIdList.size();

        String internalId = commonHelpers.getIds(SAPIResponse, "category.internalId");
        Integer No = 20 * (pageNumber - 1);

        Response assemblerLastBrandPageResponse = StepsHelper.getAssemblerResponseWithBuriedRecordsPSFWithSearchTerm(term, internalId, totalCount, No);
        List<Map<String, List<String>>> assemblerRecords = termNodeHelper.getPreservedOrderAssemblerTermNodeProps(assemblerLastBrandPageResponse);
        List<String> pRecordIds = assemblerRecords.stream()
                .filter(record -> !record.get("P_brand").get(0).equals(buriedBrand))
                .map(record -> record.get("P_recordID").get(0))
                .collect(Collectors.toList());

        assertEquals(
                "Non buried results from assembler do not match non buried results from Search API",
                pRecordIds,
                recordIdList
        );
    }

    @Given("^I request the search API end point for a search results page with a searchTerm for a boosted brand$")
    public void iRequestTheSearchAPIEndPointForASearchResultsPageWithASearchTermTermForABoostedBrand() throws Throwable {
        stepsHelper.getBoostedResultsListResponseWithTerm();
    }

    @Then("^the boosted products are being returned at the top of the list for term$")
    public void theBoostedProductsAreReturnedAtTheTopOfTheResultListForSaerchTerm() throws Throwable {
        stepsHelper.assertBoostedProductsWithSearchTerm();
    }

    @Given("^I request the search API end point for a search results page with a searchTerm for a buried brand$")
    public void iRequestTheSearchAPIEndPointForASearchResultsPageWithASearchTermForABuriedBrand() throws Throwable {
        stepsHelper.getBuriedResultsListResponseWithTerm();
    }

    @Then("^the buried products are being returned at the top of the list for term$")
    public void theBuriedProductsAreBeingReturnedAtTheTopOfTheListForTerm() throws Throwable {
        stepsHelper.assertBuriedProductsWithSearchTerm();
    }

    @Then("^the triggered boost rule should return the name and path for term$")
    public void theTheTriggeredBoostRuleShouldReturnTheNameAndPathForTerm() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(UrlBuilder.getBoostedSearchResultsTerm());
        String rulesMap = commonHelpers.getIds(SAPIResponse, "queryInfo.rulesFired.ResultsList");
        String rule = rulesMap.split("content/")[1];
        String expectedRule = UrlBuilder.getExpectedBoostedWithSearchTermPath();
        assertEquals(
                "The triggered boost rule should have returned the name and path of the search term",
                expectedRule,
                rule
        );
    }

    @Then("^the triggered bury rule should return the name and path for term$")
    public void theTriggeredBuryRuleShouldReturnTheNameAndPathForTerm() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(UrlBuilder.getBuriedSearchResultsTerm());
        String rulesMap = commonHelpers.getIds(SAPIResponse, "queryInfo.rulesFired.ResultsList");
        String rule = rulesMap.split("content/")[1];
        String expectedRule = UrlBuilder.getExpectedBuriedWithSearchTermPath();
        assertEquals(
                "The triggered bury rule should have returned the name and path of the search term",
                expectedRule,
                rule
        );
    }

    @Then("^the products which are not part of boost rule are sorted as per the browse relevancy ranking set_up for term$")
    public void theProductsWhichAreNotPartOfBoostRuleAreSortedAsPerTheBrowseRelevancyRankingSet_UpForTerm() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(UrlBuilder.getBoostedSearchResultsTerm());
        String term = UrlBuilder.getBoostedSearchResultsTerm();

        Map<String, Integer> brandMap = termNodeHelper.getSAPIBrandBinCounts(SAPIResponse);
        String boostedBrand = UrlBuilder.getBoostedBrandWithSearchTerm();
        String boostedBrandCount = brandMap.getOrDefault(boostedBrand, 0).toString();

        Double boostedProductsStoppingPage = Double.parseDouble(boostedBrandCount) / 20;
        int pageNumber = 0;
        Integer recordsIndexOnStoppingPage = Integer.parseInt(boostedBrandCount) % 20;
        if (recordsIndexOnStoppingPage > 0) {
            pageNumber = boostedProductsStoppingPage.intValue() + 1;
        } else {
            pageNumber = boostedProductsStoppingPage.intValue();
        }

        stepsHelper.getSAPIEndOfBoostedSearchResultsResponse(pageNumber, term);
        Response lastBrandPageResponse = ScenarioContext.getData(term);
        List<TermNodeRecordModel> records = termNodeHelper.getTermNodeProperties(lastBrandPageResponse);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : records) {
            if (!model.getPropertiesMap().get("P_brand").equals(boostedBrand)) {
                recordIdList.add(model.getPropertiesMap().get("P_recordID"));
            }
        }
        int listSize = recordIdList.size();

        Response assemblerLastBrandPageResponse = StepsHelper.getAssemblerResponseWithSearchTermWithBoostedRecords(term, listSize);
        List<List<String>> list = assemblerLastBrandPageResponse.getBody().jsonPath().prettyPeek().getList("mainContent[0].contents[0].records.attributes.P_recordID");
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : list) {
            for (String id : idList) {
                pRecordIds.add(id);
            }
        }
        assertEquals(
                "The products which are not part of boost rule should have been are sorted as per the browse " +
                        "relevancy ranking setup for the search term",
                pRecordIds,
                recordIdList
        );
    }

    @Then("^the products which are not part of bury rule are sorted as per the browse relevancy ranking set-up for term$")
    public void theProductsWhichAreNotPartOfBuryRuleAreSortedAsPerTheBrowseRelevancyRankingSetUpForTerm() throws Throwable {
        Response SAPIResponse = ScenarioContext.getData(UrlBuilder.getBuriedSearchResultsTerm());
        String term = UrlBuilder.getBuriedSearchResultsTerm();
        Integer totalCount = Integer.parseInt(commonHelpers.getIds(SAPIResponse, "resultsList.pagination.count"));
        Map<String, Integer> brandMap = termNodeHelper.getSAPIBrandBinCounts(SAPIResponse);
        String buriedBrand = UrlBuilder.getBuriedBrandWithSearchTerm();
        Integer count = brandMap.getOrDefault(buriedBrand, 0);

        int nonBuriedProductCount = totalCount - count;
        int pageNumber = 0;
        double buriedProductsStartingPage = (double) nonBuriedProductCount / 20;
        int recordsIndexOnSStartingPage = (nonBuriedProductCount) % 20;
        if (recordsIndexOnSStartingPage > 0) {
            pageNumber = (int) buriedProductsStartingPage + 1;
        } else {
            pageNumber = (int) buriedProductsStartingPage;
        }

        stepsHelper.getStartOfBuryResponseSAPI(term, pageNumber);
        Response lastBrandPageResponse = ScenarioContext.getData(term);
        List<TermNodeRecordModel> records = termNodeHelper.getTermNodeProperties(lastBrandPageResponse);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : records) {
            if (!model.getPropertiesMap().get("P_brand").equals(buriedBrand)) {
                recordIdList.add(model.getPropertiesMap().get("P_recordID"));
            }
        }
        int listSize = recordIdList.size();
        Integer No = 20 * (pageNumber - 1);

        Response assemblerLastBrandPageResponse = StepsHelper.getAssemblerResponseWithBuriedRecordsWithSearchTerm(term, listSize, No);
        List<List<String>> list = assemblerLastBrandPageResponse.getBody().jsonPath().prettyPeek().getList("mainContent[0].contents[0].records.attributes.P_recordID");
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : list) {
            for (String id : idList) {
                pRecordIds.add(id);
            }
        }
        assertEquals(
                "The products which are not part of bury rule should have been are sorted as per the browse " +
                        "relevancy ranking setup for the search term",
                pRecordIds,
                recordIdList
        );
    }

    @Given("^I send request to search api to get the level three category details using (.+)$")
    public void i_send_request_search_api_to_get_the_level_three_category_details_using(String psfID) throws Throwable {
        new StepsHelper().getSearchAPIForIncorrectCategory(psfID);
    }

    @Given("^I send a request for a user segment (.+) to get the level three category details using (.+)$")
    public void iSendARequestForAUserSegmentEDCMPToGetTheLevelThreeCategoryDetailsUsingBoostedPsfId(String userSegment, String psfID) {
        String psfParam = Page.getProperty(psfID);
        stepsHelper.getSearchAPIWithUserSegment(psfParam, userSegment);
    }

    @Given("^I send a request to search api to get the level three category details for (.+)$")
    public void i_send_a_request_search_api_to_get_the_level_three_category_details_for(String type) throws Throwable {
        new StepsHelper().getSearchAPIPSFTerminalFor(type);
    }

    @And("^the correct bin counts are returned for applied refinements$")
    public void theCorrectBinCountsAreReturnedForAppliedRefinements() {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        termNodeHelper.assertRefinementBinCount(response);
    }

    @Then("^the sortSpec for (.+) should be displayed for (.+)$")
    public void theSortSpecShouldBeDisplayed(String sortOption, String psfId) throws Throwable {
        Response sapiResponse = ScenarioContext.getData(psfId);
        switch (sortOption) {
            case ("relevance"):
                String relevanceSortSpec = commonHelpers.getIds(sapiResponse, "resultsList.sortOptions[0].sortSpec");
                assertTrue("The relevance sort spec should have been empty", relevanceSortSpec.isEmpty());
                break;
            case ("popularity"):
                String expectedPopularitySortSpec = UrlBuilder.getPopularitySortSpec();
                String popularitySortSpec = commonHelpers.getIds(sapiResponse, "resultsList.sortOptions[1].sortSpec");
                assertEquals(
                        "The popularity sort spec should have matched",
                        expectedPopularitySortSpec,
                        popularitySortSpec
                );
                break;
            case ("Price: Low to High"):
                String expectedLowToHighSortSpec = UrlBuilder.getLowToHighSortSpec();
                String ascendingSortSpec = commonHelpers.getIds(sapiResponse, "resultsList.sortOptions[2].sortSpec");
                assertEquals(
                        "The Price: Low to High sort spec should have matched",
                        ascendingSortSpec,
                        expectedLowToHighSortSpec
                );
                break;
            case ("Price: High to Low"):
                String expectedHighToLowSortSpec = UrlBuilder.getHighToLowSortSpec();
                String expectedDescendingSortSpec = commonHelpers.getIds(sapiResponse, "resultsList.sortOptions[3].sortSpec");
                assertEquals(
                        "The Price: High to Low sort spec should have matched",
                        expectedHighToLowSortSpec,
                        expectedDescendingSortSpec
                );
                break;
        }
    }

    @Given("^I send a request to search api to get all the attibutes for the products using productFields as a parameter for a (.+)$")
    public void iSendARequestToSearchApiToGetAllThePropertiesForTheRecordsUsingProductFieldsForASearchTerm(String term) throws Throwable {
        String searchQuery = Page.getProperty(term);
        stepsHelper.getSAPIResponseWithProductFieldsParam(searchQuery);
    }

    @And("^I get all the properties for the first product for (.+)$")
    public void iGetAllThePropertiesForTheFirstProduct(String term) throws Throwable {
        String searchQuery = Page.getProperty(term);
        Response sapiResponse = ScenarioContext.getData(searchQuery);
        List<String> propertiesList = CommonHelpers.getRecordIds(sapiResponse, "resultsList.records[0].properties.key");
        ScenarioContext.setData("list", propertiesList);
    }

    @Then("^the properties from the latest query should match those in the original query for (.+)$")
    public void the_properties_from_the_latest_query_should_match_those_in_the_original_query_for(String term) throws Throwable {
        String searchQuery = Page.getProperty(term);
        List<String> propertiesList = ScenarioContext.getData("list");
        String resultsString = String.join(",", propertiesList);
        stepsHelper.getSAPIResponseWithAllTheProperties(resultsString, searchQuery);
        Response latestResponse = ScenarioContext.getData(resultsString);
        List<String> lastPropertiesList = CommonHelpers.getRecordIds(latestResponse, "resultsList.records[0].properties.key");
        assertEquals(
                "The properties from the latest query should have matched that in the original query for the search term",
                lastPropertiesList,
                propertiesList
        );
    }

    @Then("^I should see (.*) within the SAPI response for (.*)$")
    public void i_should_see_P_recordID_within_the_SAPI_response_specify_fields(String term, String recordPropertiesValue) {
        String searchQuery = Page.getProperty(term);
        Response sapiResponse = ScenarioContext.getData(searchQuery);
        assertNotNull(
                "SearchAPI request should have come back with a valid response, the response was 'null'",
                sapiResponse
        );
        assertEquals(
                "The REST call was NOT successful, came back with an unexpected status code.",
                EXPECTED_OK_STATUS_CODE, sapiResponse.statusCode()
        );
        List<String> propertiesList = CommonHelpers.getRecordIds(sapiResponse, "resultsList.records[0].properties.key");
        assertTrue(
                "Product record id should have been present within the SearchAPI response",
                propertiesList.stream().anyMatch(x -> x.contains(recordPropertiesValue))
        );
    }

    @Then("^I should see (.*) within the PSFID response for (.*)$")
    public void i_should_see_search_term_psfID_within_the_PSFID_response_for_P_manufacturerPartNumber(String term, String recordPropertiesValue) {
        String searchQuery = Page.getProperty(term);
        Response sapiResponse = ScenarioContext.getData(searchQuery);
        assertNotNull(
                "SearchAPI request should have come back with a valid response, the response was 'null'",
                sapiResponse
        );
        List<String> propertiesList = sapiResponse.path("resultsList.records.properties.key");
        List<String> str = Stream.of(propertiesList).map(Objects::toString).collect(Collectors.toList());
        assertTrue(
                "Search term for specific category should have been found in the response (MPN related)",
                str.stream().anyMatch(x -> x.contains(recordPropertiesValue))
        );
    }

    @Then("^(.*) should be displayed as key within the SAPI response for (.*)$")
    public void specified_value_should_be_displayed_as_key_within_the_SAPI_response_for_search_term_by_mpn(String key, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response sapiResponse = ScenarioContext.getData(searchQuery);
        assertNotNull(
                "SearchAPI request should have come back with a valid response, the response was 'null'",
                sapiResponse
        );
        List<String> propertiesList = CommonHelpers.getRecordIds(sapiResponse, "resultsList.records[0].properties.key");
        assertTrue(
                "Key should have been found within the SearchAPI response",
                propertiesList.stream().anyMatch(x -> x.contains(key))
        );
    }

    @Then("^(.*) should have (.*) value to equal (.*) within the response$")
    public void specified_search_term_should_have_specified_value_key_to_equal_specify_value_within_the_response(String searchTerm, String key, String value) {
        String searchQuery = Page.getProperty(searchTerm);
        Response sapiResponse = ScenarioContext.getData(searchQuery);
        assertNotNull(
                "SearchAPI request should have come back with a valid response, the response was 'null'",
                sapiResponse
        );
        List<String> propertiesList = CommonHelpers.getPRecordIds(sapiResponse, "resultsList.records.properties.key");
        List<String> getPropertiesValue = CommonHelpers.getRecordIds(sapiResponse, "resultsList.records.properties.value");
        List<String> getPropertiesList = new ArrayList<>();
        for (String s : propertiesList) {
            getPropertiesList.add(s);
            if (s.equalsIgnoreCase(key)) {
                String getvalue = getPropertiesValue.get(1);
                assertTrue(
                        String.format(
                                "The value corresponding to the key '%s' should have matched for the specified search term",
                                key
                        ),
                        getvalue.equalsIgnoreCase(value)
                );
            }
        }
    }

    @Then("^I (.*) see product response with stock number (.*) returning (.*)$")
    public void i_should_see_product_response_with_stock_number_returning_expected_keys(String ShouldOrShouldNot, String productID, String expected_Keys) {
        Response sapiResponse = ScenarioContext.getData(productID);
        assertNotNull(
                "SearchAPI request should have come back with a valid response, the response was 'null'",
                sapiResponse
        );
        List<String> propertiesList = CommonHelpers.getRecordIds(sapiResponse, "product.properties.key");
        if (ShouldOrShouldNot.equalsIgnoreCase("should")) {
            assertTrue(
                    "Should have returned the expected product",
                    propertiesList.stream().anyMatch(x -> x.contains(expected_Keys))
            );
        } else {
            assertFalse(
                    "Should have NOT returned the specific product",
                    propertiesList.stream().anyMatch(x -> x.contains(expected_Keys))
            );
        }
    }

}
