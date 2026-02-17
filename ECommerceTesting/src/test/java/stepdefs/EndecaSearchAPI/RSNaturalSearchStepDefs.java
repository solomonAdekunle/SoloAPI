package stepdefs.EndecaSearchAPI;

import config.PropertiesReader;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;
import pages.Page;
import pages.Search.SeachAPI.*;
import stepdefs.SharedDriver;

import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;
import static pages.Search.SeachAPI.CommonHelpers.getRecordIds;

public class RSNaturalSearchStepDefs {
    private static final String COUNTRY = "country";
    private static final String CHINA_MARKET = "cn";
    private static final String JAPAN_MARKET = "jp";
    private static final String UK_MARKET = "uk";
    private static final String ENVIRONMENT = "env";
    private static final String ST1_ENV = "st1";
    private static final String ST2_ENV = "st2";
    private static final int EXPECTED_OK_STATUS_CODE = 200;
    private final String searchApiRecordIdPath = "resultsList.records.id";
    private final StepsHelper stepsHelper;
    private final CommonHelpers commonHelpers;
    private final NaturalSearchHelpers naturalSearchHelpers;
    private final TerminalNodeHelper termNodeHelper;
    private static PropertiesReader props = new PropertiesReader();

    public RSNaturalSearchStepDefs(CommonHelpers commonHelpers, SharedDriver webDriver, StepsHelper stepsHelper,
                                   NaturalSearchHelpers naturalSearchHelpers, TerminalNodeHelper termNodeHelper) {
        this.stepsHelper = stepsHelper;
        this.commonHelpers = commonHelpers;
        this.naturalSearchHelpers = naturalSearchHelpers;
        this.termNodeHelper = termNodeHelper;
    }


    @Given("^I send a (.*) only request to the search api for search term (.*)$")
    public void iSendAMPNOnlyRequestToTheSearchApiForSearchTermSearchTerm(String searchType, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        stepsHelper.setSearchAPIMpnOnlyNaturalSearchRequest(searchType, searchQuery);
    }

    @Given("^I send a request to the search api for search term (.*)")
    public void iSendARequestToTheSearchApiForSearchTermSearchTerm(String searchTerm) {
        String searchQuery = props.getProperty(searchTerm);
        stepsHelper.setSearchAPINaturalSearchRequest(searchQuery);
    }

    @Given("^I send a request for (.*) with a limit of (\\d+)$")
    public void iSendARequestForSearchTermWithLimitParameters(String searchTerm, int limit) {
        String searchQuery = Page.getProperty(searchTerm);
        stepsHelper.setSearchAPIRequestWithLimit(searchQuery, limit);
    }


    @Given("^I send a request for a user segment (.*) to the search api for search term (.*)$")
    public void iSendARequestForASpecificUserSegmentToTheSearchApiForSearchTermBoostedSearchTerm(String userSegment, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        stepsHelper.setSearchAPIRequestWithUserSegment(searchQuery, userSegment);
    }

    @Given("^I send a special character request to the search api for (.*)")
    public void iSendARequestToTheSearchApiForSearchTermSearchTermWithSpecialCharacters(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        stepsHelper.setSearchAPINaturalSearchRequestWithSpecialCharacters(searchQuery);
    }

    @Given("^I send a request to the search api to get (.*) with (.*)$")
    public void i_send_a_request_to_the_search_api_to_get_search_term_with_cust_specified(String searchTerm, String customerFilterIdProperty) {
        String searchQuery = Page.getProperty(searchTerm);
        String customerFilter = Page.getProperty(customerFilterIdProperty);
        stepsHelper.setSearchAPINaturalSearchWithCustomerFilterRequest(searchQuery, customerFilter);
    }

    @Given("^I send a search request to the search api to get (.*) with (.*) for search by stock number$")
    public void i_send_a_search_request_to_the_search_api_to_get_search_term_with_customer_specified_for_search_by_stock(String searchTerm, String customerFilterIdProperty) {
        String searchQuery = Page.getProperty(searchTerm);
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        stepsHelper.setSearchAPINaturalSearchWithCustomerFilterRequest(searchQuery, customerFilterId);
    }

    @Given("^I send a search request to the search api to get (.*) with (.*) for search by MPN$")
    public void i_send_a_search_request_to_the_search_api_to_get_search_by_MPN_with_Customer_Filter_id(String searchTerm, String customerFilterIdProperty) {
        String searchQuery = Page.getProperty(searchTerm);
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        stepsHelper.setSearchAPIByMPNWithCustomerFilterRequest(searchQuery, customerFilterId);
    }

    @Given("^I send a search request to the search api to get (.+) with (.+) for hidden Level two and Level three categories$")
    public void i_send_a_search_request_to_the_search_api_to_get_search_term_restricted_with_Customer_Filter_id_for_hidden_Level_two_and_Level_three_categories(String searchTerm, String customerFilterIdProperty) {
        String searchQuery = Page.getProperty(searchTerm);
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        stepsHelper.setSearchAPINaturalSearchWithCustomerFilterRequest(searchQuery, customerFilterId);

    }

    @Given("^I send a search request to the search api to get (.+) with (.*) for hidden Level three categories$")
    public void i_send_a_search_request_to_the_search_api_to_get_search_term_restricted_L_with_Customer_Filter_id_for_hidden_Level_three_categories(String searchTerm, String customerFilterIdProperty) {
        String searchQuery = Page.getProperty(searchTerm);
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        stepsHelper.setSearchAPINaturalSearchWithCustomerFilterRequest(searchQuery, customerFilterId);
    }

    @Given("^I request the search API end point for an Terminal node with a term (.*) with (.*) for (.*)$")
    public void i_request_the_search_API_end_point_for_an_Terminal_node_with_a_term_with(String psfId, String searchTerm, String customerFilterIdProperty) {
        String getPsfId = Page.getProperty(psfId);
        String getSearchTerm = Page.getProperty(searchTerm);
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        stepsHelper.setTermNodeWithCustomerFilterResponse(getSearchTerm, customerFilterId, getPsfId);
    }

    @Given("^I send a Post request to the search api with (.*)$")
    public void i_send_a_Post_request_to_the_search_api_with_parameters(String data) {
        new StepsHelper().getPostSearchApiResponse(Page.getProperty(data));
    }

    @Given("^I send a Post request to the search api slash facet parameters$")
    public void i_send_a_Post_request_to_the_search_api_slash_facet_parameters() {
        new StepsHelper().getPostSearchApiFacetResponse(Page.getProperty("post.facet.searchterm.query.cable"));
    }

    @Then("^All sort by options should be displayed For (.*)$")
    public void allSortByOptionsShouldBeDisplayedForTheSearchTerm(String searchTerm) {
        List<String> details = Arrays.asList(UrlBuilder.getSortOptions().split(","));
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        List<String> SortByOptionsList = commonHelpers.getSortByOptionsFromL3Category(searchApiResponse);
        assertEquals(
                "All sort by options should have been displayed for the search term",
                SortByOptionsList,
                details
        );
    }

    @Then("^the Search API and Assembler API results are in the same order for (.*)$")
    public void theSearchAPIAndAssemblerAPIResultsAreInTheSameOrder(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        Response assemblerResponse = null;
        List<String> searchApiRecordIds = getRecordIds(searchApiResponse, searchApiRecordIdPath);
        naturalSearchHelpers.getAssemblerSearchResponse(searchTerm);
        List<List<String>> assemblerPRecordIds = naturalSearchHelpers.getAssemblerSearchResponse(searchTerm).path("mainContent[0].contents[0].records.attributes.P_recordID");
        naturalSearchHelpers.areSearchApiAndAssemblerApiRecordsEqual(searchApiRecordIds, assemblerPRecordIds);
    }

    @And("^the query info block for (.*) should be displayed correctly for (.*)$")
    public void theQueryInfoBlockForStockNumberInterfaceShouldBeDisplayedCorrectly(String searchInterface, String searchTerm) {
        naturalSearchHelpers.assertQueryInfoBlockValues(searchInterface, searchTerm);
    }

    @Then("^the list of results should be in the same order for (.*) and (.*)$")
    public void theListOfResultsShouldBeInTheSameOrderForSearchTermWithTrademarkSymbolAndSearchTermWithoutTrademarkSymbol(String searchTerm1, String searchTerm2) {
        String searchQueryWithSymbol = Page.getProperty(searchTerm1);
        Response searchApiResponse1 = ScenarioContext.getData(searchQueryWithSymbol);
        List<String> searchApiRecordIdsWithTm = getRecordIds(searchApiResponse1, searchApiRecordIdPath);
        String searchQueryWithoutSymbol = Page.getProperty(searchTerm2);
        Response searchApiResponse2 = ScenarioContext.getData(searchQueryWithoutSymbol);
        List<String> searchApiRecordIdsWithoutTm = getRecordIds(searchApiResponse2, searchApiRecordIdPath);
        assertEquals(
                "The list of records with and without the trademark symbol should have matched",
                searchApiRecordIdsWithTm,
                searchApiRecordIdsWithoutTm
        );
    }

    @Then("^the extra Assembler API results are discontinued for (.*)$")
    public void theExtraAssemblerAPIResultsAreDiscontinued(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        naturalSearchHelpers.returnSearchApiRecords(searchQuery);
        naturalSearchHelpers.getLocaleLifecycleStatusForExtraAssemblerRecords(searchQuery);

        List<String> extraRecordLifecycleStatus = new ArrayList<>();
        for (List<String> idList : naturalSearchHelpers.getLocaleLifecycleStatusForExtraAssemblerRecords(searchQuery))
            for (String id : idList)
                extraRecordLifecycleStatus.add(id);
        for (String record : extraRecordLifecycleStatus) {
            assertEquals("The extract record lifecycle status should have matched", "90", record);
        }
    }

    @Then("^the Search API record for (.*) is returned$")
    public void theSearchAPIRecordIsReturned(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        int recordCount = searchApiResponse.path("resultsList.pagination.count");
        assertEquals("The pagination count for the search term should have matched", 1, recordCount);
    }

    @Then("^all search API attributes should be displayed for the records matching the (.*)$")
    public void allSearchAPIAttributesShouldBeDisplayedForTheRecordsMatchingTheSearchTerm(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        naturalSearchHelpers.assertSearchResultsPageDetails(searchApiResponse, assemblerResponse);
    }

    @And("^the record returned is relevant to the (.*)$")
    public void theRecordReturnedIsRelevantToTheSearchTerm(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertRelevanceOfStockNumberResponse(searchApiResponse);
    }

    @Then("^The L(\\d+) category details that match the (.*) are displayed$")
    public void theLXCategoryDetailsThatMatchTheSearchTermAreDisplayed(int categoryLevel, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        if (categoryLevel == 3) {
            naturalSearchHelpers.assertL3CategoryDetailsDisplayed(searchApiResponse, assemblerResponse);
        } else {
            naturalSearchHelpers.assertL2CategoryDetailsDisplayed(searchApiResponse, assemblerResponse);
        }
    }

    @Then("^the API record counts for (.*) with no filters applied are not equal$")
    public void theAPIRecordCountsWithNoFiltersAppliedAreNotEqual(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        naturalSearchHelpers.assertRecordCountNotEqual(searchQuery);
    }

    @And("^the API record counts for (.*) with filters applied are equal$")
    public void theAPIRecordCountsForSearchTermWithFiltersAppliedAreEqual(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        naturalSearchHelpers.assertRecordCountEqual(searchQuery);
    }

    @Then("^all Adobe tags relating to the (.*) are returned$")
    public void allAdobeTagsRelatingToTheSearchTermAreReturned(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertAdobeTagsPresent(searchApiResponse);
    }

    @And("^accurate Brand details relating to the (.*) are returned$")
    public void accurateBrandDetailsRelatingToTheSearchTermAreReturned(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        naturalSearchHelpers.assertSearchResultsPageBrandDetails(searchApiResponse, assemblerResponse);
    }

    @Then("^Category Seo Url details that relate to the (.*) are retuned$")
    public void CategorySeoUrlDetailsThatRelateToTheSearchTermAreRetuned(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        naturalSearchHelpers.assertCategorySeoDetails(searchApiResponse, assemblerResponse);
    }

    @Then("^The common products from the (.*) and (.*) record lists are returned when searching for (.*)$")
    public void theCommonProductsFromTheRecordListsAreReturnedWhenSearchingForCombinedAttributeTerm(String searchTerm1, String searchTerm2, String searchTermCombined) {
        String searchQuery1 = Page.getProperty(searchTerm1);
        String searchQuery2 = Page.getProperty(searchTerm2);
        String searchQueryCombined = Page.getProperty(searchTermCombined);
        naturalSearchHelpers.assertCommonProductsFromRecordListEqualCombinedAttributeRecordList(naturalSearchHelpers.returnSearchApiRecords(searchQuery1), naturalSearchHelpers.returnSearchApiRecords(searchQuery2), naturalSearchHelpers.returnSearchApiRecords(searchQueryCombined));
    }

    @Then("^The Search API and Assembler responses for the (.*) contain the desired product$")
    public void theSearchAPIAndAssemblerResponsesForTheSearchTermContainTheDesiredProduct(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        List<String> searchApiRecordList = naturalSearchHelpers.returnSearchApiRecords(searchQuery);
        Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        naturalSearchHelpers.assertThatResponsesContainDesiredProduct(searchApiRecordList, assemblerResponse);
    }

    @Then("^The Search API and Assembler responses for (.*) and (.*) are in the same order$")
    public void theSearchAPIAndAssemblerResponsesForSearchTermAndSearchTermAreInTheSameOrder(String searchTerm1, String searchTerm2) {
        assertEquals(naturalSearchHelpers.assertResultsListEqualAndReturnAssemblerRecordList(searchTerm1), naturalSearchHelpers.assertResultsListEqualAndReturnAssemblerRecordList(searchTerm2));
    }

    @Then("^the attribute units returned (.*) are localised to specific markets$")
    public void theAttributeUnitsReturnedLocalisedAttributeSearchTermAreLocalisedToSpecificMarkets(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        if ((System.getProperty(COUNTRY).equals("de")) || (System.getProperty(COUNTRY).equals("f1"))) {
            naturalSearchHelpers.assertAttributesContainLocalisedAttributes(searchApiResponse);
        } else {
            naturalSearchHelpers.assertAtrributesDisplayed(searchApiResponse);
        }
    }

    @Then("^The response for (.*) contains all relevant top-level attributes$")
    public void theResponseForSearchTermContainsAllRelevantTopLevelAttributes(String searchTerm) {
        Response searchApiResponse = ScenarioContext.getData(props.getProperty(searchTerm));
        naturalSearchHelpers.assertTopLevelAttributes(searchApiResponse);
    }

    @And("^The results list block for (.*) contains sortOptions and Pagination Information but not any records$")
    public void theResultsListBlockForSearchTermContainsSortOptionsAndPaginationInformationButNotAnyRecords(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        if (searchTerm.contains("PS")) {
            String internald = commonHelpers.getIds(searchApiResponse, "category.internalId");
            Response assemblerResponse = StepsHelper.getAsseblerRelevanceResponse(internald);
            naturalSearchHelpers.assertSortOption(searchApiResponse, assemblerResponse);
        } else {
            Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
            naturalSearchHelpers.assertSortOption(searchApiResponse, assemblerResponse);
        }
    }

    @And("^The pagination for (.*) will report actual results count, max page and page=(\\d+)$")
    public void thePaginationForSearchTermWillReportActualResultsCountMaxPageAndPage(String searchTerm, int pageNumber) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        if (searchQuery.contains("PS")) {
            String internald = commonHelpers.getIds(searchApiResponse, "category.internalId");
            Response assemblerResponse = StepsHelper.getAsseblerRelevanceResponse(internald);
            naturalSearchHelpers.assertResultsCount(searchApiResponse, assemblerResponse);
            naturalSearchHelpers.assertPageCount(searchApiResponse, pageNumber);
        } else {
            Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
            naturalSearchHelpers.assertResultsCount(searchApiResponse, assemblerResponse);
            naturalSearchHelpers.assertPageCount(searchApiResponse, pageNumber);
        }
    }

    @And("^The pagination in the response received for (.*) and (.*) will report actual results count, max page and page=(\\d+)$")
    public void thePaginationForSearchTermAndPsfIdWillReportActualResultsCountMaxPageAndPage(String searchTerm, String psfID, int pageNumber) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        String internald = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerLimitResponse(internald, searchQuery);
        naturalSearchHelpers.assertResultsCount(searchApiResponse, assemblerResponse);
        naturalSearchHelpers.assertPageCount(searchApiResponse, pageNumber);
    }

    @Given("^I send a search API request for (.*) in (.*) with limit set to (\\d+)$")
    public void iSendASearchAPIRequestForSearchTermInPsfIdWithLimitSetTo(String searchTerm, String psf, int limit) {
        String searchQuery = Page.getProperty(searchTerm);
        String psfID = Page.getProperty(psf);
        stepsHelper.getTerminalNodeWithSearchTermAndLimitResponse(searchQuery, psfID, limit);
    }

    @Then("^the (.*) in XM is returned first in the records list for (.*)$")
    public void theBoostedProductInXMIsReturnedFirstInTheRecordsList(String boostedProduct, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertPositionOfBoostedProduct(searchApiResponse, boostedProduct);
    }

    @And("^the results list rules for (.*) fired matches the rule set up in XM for (.*)$")
    public void theResultsListRulesForBoostedSearchTermFiredMatchesTheRuleSetUpInXM(String searchTerm, String xmRule) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertBoostRuleFired(searchApiResponse, xmRule);
    }

    @Given("^I send a request for user segment (.*) to get a search category response for (.*) and (.*)$")
    public void iSendARequestForUserSegmentEDCMPToGetASearchCategoryResponseForBoostedProductTerminalNodeSearchAndPsfId(String userSegment, String searchTerm, String psf) {
        String searchQuery = Page.getProperty(searchTerm);
        String psfID = Page.getProperty(psf);
        stepsHelper.getTerminalNodeWithSearchTermAndUserSegmentResponse(searchQuery, psfID, userSegment);
    }

    @Then("^the search API does not report the (.*) as spell corrected$")
    public void theSearchAPIDoesNotReportTheSearchTermAsSpellCorrected(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertSearchKeywordMatchesSearchTerm(searchApiResponse, searchQuery);
    }

    @Given("^I send a request with Search Config (\\d+) to the search api for search term (.*)$")
    public void iSendARequestWithSearchConfigToTheSearchApiForSearchTermSearchTerm(int searchConfig, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        stepsHelper.setSearchAPINaturalSearchRequestWithSearchConfig(searchQuery, searchConfig);
    }

    @Then("^the search config of (\\d+) passed in will be reflected in the Search API response for (.*)$")
    public void theSearchConfigOfPassedInWillBeReflectedInTheSearchAPIResponseForLCategoryDetailsTerm(int searchConfig, String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        if ((System.getProperty(COUNTRY).equalsIgnoreCase("de")) || (System.getProperty(COUNTRY).equalsIgnoreCase("uk")) | (System.getProperty(COUNTRY).equalsIgnoreCase("jp"))) {
            naturalSearchHelpers.assertMvtConfigID(searchApiResponse, searchConfig);
        } else {
            naturalSearchHelpers.assertMvtConfigWithNoSetup(searchApiResponse);
        }
    }

    @Then("^the search keyword applied should match the (.*) with designated characters removed$")
    public void theSearchKeywordAppliedShouldMatchTheSearchTermWithDesignatedCharactersRemoved(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertStockNumberPatternMatched(searchApiResponse, searchQuery);
    }

    @Then("^the (.*) and search keyword applied are the same$")
    public void theSearchKeywordAppliedShouldMatchTheSearchTerm(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertSearchKeywordMatchesSearchTerm(searchApiResponse, searchQuery);
    }

    @Then("^the (.*) should have designated characters removed and be converted to (.*)$")
    public void theSearchTermShouldHaveDesignatedCharactersRemovedAndBeConvertedToSingleByte(String searchTerm, String singleByteTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        naturalSearchHelpers.assertDoubleByteStockNoPattern(searchApiResponse, singleByteTerm);
    }

    @Given("^I request search api for (.+) that matches mpnType$")
    public void iRequestSearchApiForReservedCharacterTermThatMatchesMpnType(String SearchTerm) throws Throwable {
        String searchQuery = Page.getProperty(SearchTerm);
        stepsHelper.getSearchAPIRequestWithReservedCharacters(searchQuery);
    }

    @Then("^the reserved characters should be removed from the query for (.+)$")
    public void theReservedCharactersShouldBeRemovedFromTheQuery(String reservedCharTerm) throws Throwable {
        String reservedCharacterTerm = Page.getProperty(reservedCharTerm);
        Response response = ScenarioContext.getData(reservedCharacterTerm);
        String expectedCorrectedTerm = UrlBuilder.getExpectedUrlPath();
        assertEquals(
                "The reserved characters should have been removed from the query",
                expectedCorrectedTerm,
                response.path("queryInfo.search_keyword_app")
        );
    }

    @Then("^the stock number is padded to 7 digits with 0s for (.+)$")
    public void theStockNumberIsPaddedToDigitsWithS(String searchQuery) throws Throwable {
        String term = Page.getProperty(searchQuery);
        Response response = ScenarioContext.getData(term);
        String paddedStockNumber = UrlBuilder.getPaddedStockNumber();
        assertEquals(
                "The stock number should have been padded for the search query",
                paddedStockNumber,
                response.path("queryInfo.search_keyword_app")
        );
    }

    @And("^the original search query should be returned for (.+)$")
    public void theOriginalSearchQueryShouldBeReturnedForStockNumberExample(String searchQuery) throws Throwable {
        String term = Page.getProperty(searchQuery);
        Response response = ScenarioContext.getData(term);
        String originalSearchQuery = response.path("queryInfo.search_keyword");
        assertEquals(
                "The original search query should have been returned",
                term,
                originalSearchQuery
        );
    }

    @Then("^I request search api with (.+) in single byte as search term to check if the same results are returned$")
    public void i_check_if_the_conversion_is_applied(String searchTerm) {
        if (System.getProperty(COUNTRY).equals(JAPAN_MARKET) || System.getProperty(COUNTRY).equals(CHINA_MARKET)) {
            Response doubleByteResponse = ScenarioContext.getData(ScenarioContext.getIds());
            String doubleByteResultsCount = doubleByteResponse.path("queryInfo.results_count").toString();
            stepsHelper.getSAPIResponseWithSearchQueryParam(searchTerm);
            Response singleByteResponse = ScenarioContext.getData(searchTerm);
            String singleByteResultsCount = singleByteResponse.path("queryInfo.results_count").toString();
            assertEquals(
                    "Results returned from Single Byte and double byte search term vary",
                    doubleByteResultsCount, singleByteResultsCount
            );
        }
    }

    @Given("^I request search api with a (.+) byte search term and with redirects enabled")
    public void i_request_search_api_with_a_search_term_regardless_of_byte_format(String byteFormat) {
        if (System.getProperty(COUNTRY).equals(JAPAN_MARKET) || System.getProperty(COUNTRY).equals(CHINA_MARKET)) {
            String searchTerm;
            if ("single".equals(byteFormat))
                searchTerm = UrlBuilder.getRedirectSearchTerm();
            else
                searchTerm = UrlBuilder.getRedirectDoubleByteSearchTerm();
            stepsHelper.getSAPIResponseWithKeywordRedirect(searchTerm);
        }
    }

    @Then("^the special characters are removed from the search term (.+)$")
    public void theSpecialCharactersAreRemovedFromTheSearchQueryBeforeItGetsExecuted(String searchTerm) {
        if (System.getProperty(COUNTRY).equals(JAPAN_MARKET) || System.getProperty(COUNTRY).equals(CHINA_MARKET)) {
            String searchQuery = Page.getProperty(searchTerm);
            Response searchApiResponse = ScenarioContext.getData(searchQuery);
            String appliedSearchTerm = commonHelpers.getIds(searchApiResponse, "queryInfo.search_keyword_app");
            String searchTermWithSpecialCharactersRemoved = searchQuery.replaceAll("[＼＼＄£！＾＆＊（）［］｛｝＠＂＃？＿＋＝；：～＇＜＞｜¬¦℗©®℠™]", "");
            if (searchTermWithSpecialCharactersRemoved.contains("\\") || searchTermWithSpecialCharactersRemoved.contains("[") || searchTermWithSpecialCharactersRemoved.contains("]")) {
                String searchTermFinal = searchTermWithSpecialCharactersRemoved.replaceAll("\\\\", "").replaceAll("\\[", "").replaceAll("\\]", "");
                assertEquals("Search Keyword Applied does not equal original search term with special characters removed", searchTermFinal, appliedSearchTerm);
            } else {
                assertEquals("Search Keyword Applied does not equal original search term with special characters removed", searchTermWithSpecialCharactersRemoved, appliedSearchTerm);
            }
        }
    }

    @Given("^I request search api with (.+) as search term in JP or CN$")
    public void i_request_search_api_with_a_search_term_in_jp_cn(String searchTerm) {
        if (System.getProperty(COUNTRY).equals(JAPAN_MARKET) || System.getProperty(COUNTRY).equals(CHINA_MARKET)) {
            stepsHelper.getSAPIResponseWithSearchQueryParam(searchTerm);
        }
    }

    @Then("^I check if the results are returned for the search term irrespective of the byte format they are entered in$")
    public void i_check_results_are_returned() {
        if (System.getProperty(COUNTRY).equals(JAPAN_MARKET) || System.getProperty(COUNTRY).equals(CHINA_MARKET)) {
            Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
            int count = searchApiResponse.path("queryInfo.results_count");
            assertTrue(
                    "The result count should have been a numeric value",
                    termNodeHelper.isNumber(String.valueOf(count))
            );
            assertTrue("no results found", count > 0);
        }
    }

    @Then("^I check if the same results are returned for the search term irrespective of the byte format they are entered in$")
    public void i_check_same_results_are_returned() {
        if (System.getProperty(COUNTRY).equals(JAPAN_MARKET) || System.getProperty(COUNTRY).equals(CHINA_MARKET)) {
            Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
            int count = searchApiResponse.path("queryInfo.results_count");
            String searchTerm = ScenarioContext.getIds();
            switch (searchTerm) {
                case "10kΩ":
                    stepsHelper.getSAPIResponseWithSearchQueryParam("１０ｋΩ");
                    break;
                case "+70°C":
                    stepsHelper.getSAPIResponseWithSearchQueryParam("＋７０°Ｃ");
                    break;
                case "±20%25":
                    stepsHelper.getSAPIResponseWithSearchQueryParam("±２０%EF%BC%85");
                    break;
                case "12.5Ω":
                    stepsHelper.getSAPIResponseWithSearchQueryParam("12．5Ω");
                    break;
                case "12,5":
                    stepsHelper.getSAPIResponseWithSearchQueryParam("12，5");
                    break;
                default:
                    fail(searchTerm + " search term can not be found");
            }
            Response doubleByteResponse = ScenarioContext.getData(ScenarioContext.getIds());
            int doubleByteCount = doubleByteResponse.path("queryInfo.results_count");
            assertEquals(
                    "Results returned from Single Byte and double byte search term vary",
                    count, doubleByteCount
            );
        }
    }

    @Then("^I check when the customer filter is passed restricted L1 categories are not returned$")
    public void i_check_when_customer_filter_is_passed__restricted_L1_categories_are_not_returned() {
        if ((System.getProperty(COUNTRY).equals(UK_MARKET) && !System.getProperty(ENVIRONMENT).equals(ST1_ENV))
                || System.getProperty(COUNTRY).equals(JAPAN_MARKET)) {
            Response response = ScenarioContext.getData(ScenarioContext.getIds());
            List<Map<String, String>> levelOneCat = commonHelpers.getLevelOneSearchApiResponse(response, "category.children.children");
            Response customerFilterResponse = stepsHelper.getCustomerFilterResponse(ScenarioContext.getIds());
            List<Map<String, String>> customerFilterL1Cat = commonHelpers.getLevelOneSearchApiResponse(
                    customerFilterResponse, "category.children.children"
            );
            assertTrue(
                    "The list of Level 1 categories without the customer filter should have excluded one or more " +
                            "categories when then customer filter was applied",
                    levelOneCat.size() > customerFilterL1Cat.size());
        }
    }

    @Then("^I check when the customer filter is passed restricted L2 categories are not returned$")
    public void i_check_when_customer_filter_is_passed_restricted_L2_categories_are_not_returned() {
        if ((System.getProperty(COUNTRY).equals(UK_MARKET) && !System.getProperty(ENVIRONMENT).equals(ST1_ENV))
                || System.getProperty(COUNTRY).equals(JAPAN_MARKET)) {
            Response response = ScenarioContext.getData(ScenarioContext.getIds());
            List<Map<String, String>> levelTwoCat = commonHelpers.getLevelTwoSearchApiResponse(response, "category.children.children.children");
            Response customerFilterResponse = stepsHelper.getCustomerFilterResponse(ScenarioContext.getIds());
            List<Map<String, String>> customerFilterL2Cat = commonHelpers.getLevelTwoSearchApiResponse(
                    customerFilterResponse, "category.children.children.children");
            assertTrue(
                    "The list of Level 2 categories without the customer filter should have excluded one or more " +
                            "categories when then customer filter was applied",
                    levelTwoCat.size() > customerFilterL2Cat.size()
            );
        }
    }

    @Then("^I check when the customer filter is passed restricted L3 categories are not returned$")
    public void i_check_when_customer_filter_is_passed_restricted_L3_categories_are_not_returned() {
        if (System.getProperty(COUNTRY).equals(UK_MARKET) || System.getProperty(COUNTRY).equals(JAPAN_MARKET)) {
            Response response = ScenarioContext.getData(ScenarioContext.getIds());
            List<Map<String, String>> levelThreeCat = commonHelpers.getLevelThreeSearchApiResponse(response, "category.children.children.children.children");
            Response customerFilterResponse = stepsHelper.getCustomerFilterResponse(ScenarioContext.getIds());
            List<Map<String, String>> customerFilterL3Cat = commonHelpers.getLevelThreeSearchApiResponse(
                    customerFilterResponse, "category.children.children.children.children");
            assertTrue(
                    "The list of Level 3 categories without the customer filter should have excluded one or more " +
                            "categories when then customer filter was applied",
                    levelThreeCat.size() > customerFilterL3Cat.size()
            );
        }
    }

    @Then("^I check when the customer filter is passed restricted book is not returned$")
    public void i_check_when_customer_filter_is_passed_restricted_book_is_not_returned() {
        if (System.getProperty(ENVIRONMENT).equals(ST2_ENV) && (System.getProperty(COUNTRY).equals(UK_MARKET) ||
                System.getProperty(COUNTRY).equals(JAPAN_MARKET))) {
            Response response = ScenarioContext.getData(ScenarioContext.getIds());
            List<String> booksList = response.path("category.children.id");
            Response customerFilterResponse = stepsHelper.getCustomerFilterResponse(ScenarioContext.getIds());
            List<String> filteredBooksList = customerFilterResponse.path("category.children.id");
            assertTrue(
                    "The restricted book should not have been returned when the customer filter was applied",
                    booksList.size() > filteredBooksList.size()
            );
        }
    }

    @Then("^I ensure the L1 category hidden products are not returned in the bin count$")
    public void i_ensure_the_L1_category_hidden_products_are_not_returned_in_the_bin_count() {
        if (System.getProperty(COUNTRY).equals(UK_MARKET) || System.getProperty(COUNTRY).equals(JAPAN_MARKET)) {
            Response response = ScenarioContext.getData(ScenarioContext.getIds());
            List<Map<String, String>> levelOneCat = commonHelpers.getLevelOneSearchApiResponse(response, "category.children.children");
            int countBeforeCustomerFilter = commonHelpers.getTotalBinCount(levelOneCat);
            Response customerFilterResponse = stepsHelper.getCustomerFilterResponse(ScenarioContext.getIds());
            List<Map<String, String>> customerFilterL1Cat = commonHelpers.getLevelOneSearchApiResponse(
                    customerFilterResponse, "category.children.children");
            int countAfterCustomerFilter = commonHelpers.getTotalBinCount(customerFilterL1Cat);
            assertTrue(
                    "The Level 1 category hidden products should NOT have been returned in the bin count",
                    countBeforeCustomerFilter > countAfterCustomerFilter
            );
        }
    }

    @Then("^I ensure the L2 category hidden products are not returned in the bin count$")
    public void i_ensure_the_L2_category_hidden_products_are_not_returned_in_the_bin_count() {
        if (System.getProperty(COUNTRY).equals(UK_MARKET) || System.getProperty(COUNTRY).equals(JAPAN_MARKET)) {
            Response response = ScenarioContext.getData(ScenarioContext.getIds());
            List<Map<String, String>> levelTwoCat = commonHelpers.getLevelTwoSearchApiResponse(response, "category.children.children.children");
            int countBeforeCustomerFilter = commonHelpers.getTotalBinCount(levelTwoCat);
            Response customerFilterResponse = stepsHelper.getCustomerFilterResponse(ScenarioContext.getIds());
            List<Map<String, String>> customerFilterL2Cat = commonHelpers.getLevelTwoSearchApiResponse(
                    customerFilterResponse, "category.children.children.children");
            int countAfterCustomerFilter = commonHelpers.getTotalBinCount(customerFilterL2Cat);
            assertTrue(
                    "The Level 2 category hidden products should NOT have been returned in the bin count",
                    countBeforeCustomerFilter > countAfterCustomerFilter
            );
        }
    }

    @Then("^I ensure the L3 category hidden products are not returned in the bin count$")
    public void i_ensure_the_L3_category_hidden_products_are_not_returned_in_the_bin_count() {
        if (System.getProperty(COUNTRY).equals(UK_MARKET) || System.getProperty(COUNTRY).equals(JAPAN_MARKET)) {
            Response response = ScenarioContext.getData(ScenarioContext.getIds());
            List<Map<String, String>> levelThreeCat = commonHelpers.getLevelThreeSearchApiResponse(response, "category.children.children.children.children");
            int countBeforeCustomerFilter = commonHelpers.getTotalBinCount(levelThreeCat);
            Response customerFilterResponse = stepsHelper.getCustomerFilterResponse(ScenarioContext.getIds());
            List<Map<String, String>> customerFilterL3Cat = commonHelpers.getLevelThreeSearchApiResponse(customerFilterResponse, "category.children.children.children.children");
            int countAfterCustomerFilter = commonHelpers.getTotalBinCount(customerFilterL3Cat);
            assertTrue(
                    "The Level 3 category hidden products should NOT have been returned in the bin count",
                    countBeforeCustomerFilter > countAfterCustomerFilter
            );
        }
    }

    @Then("^the same specification attributes should be using declarative fetching should be returned for (.+)$")
    public void theSameSpecificationAttributesShouldBeUsingDeclarativeFetchingShouldBeReturnedForSearchTerm(String term) throws Throwable {
        String searchQuery = Page.getProperty(term);
        Response sapiResponse = ScenarioContext.getData(searchQuery);
        List<Map<String, String>> thrirdProductSpecAttributesMap = sapiResponse.path("resultsList.records[2].specificationAttributes");
        stepsHelper.getDeclarativeFetchingSAPIResponse(searchQuery);
        Response fetchingResponse = ScenarioContext.getData(searchQuery);
        List<Map<String, String>> thirdProductFetchingSpecAttributesMap = fetchingResponse.path("resultsList.records[2].specificationAttributes");
        naturalSearchHelpers.assertSameSpeficicationAttributesAreReturned(thrirdProductSpecAttributesMap, thirdProductFetchingSpecAttributesMap);
    }

    @Then("^excluded one record should not be returned in the results list for (.+), with (.+), for (.+)$")
    public void excluded_record_should_not_be_returned_in_the_results_lists(
            String searchTermProperty, String interfaceNameProperty, String customerFilterProperty) {
        String searchTerm = Page.getProperty(searchTermProperty);
        String interfaceName = Page.getProperty(interfaceNameProperty);
        String customerFilter = Page.getProperty(customerFilterProperty);

        Response searchApiResponse = ScenarioContext.getData(customerFilter);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExclusionSearch(searchTerm, "0", interfaceName, "20");

        List<String> searchAPIRecords = searchApiResponse.path("resultsList.records.id");
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(
                assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        String excludedProductRecord = UrlBuilder.getExcludedRecords();
        assertFalse(
                "Excluded record is present in the SAPI record list", searchAPIRecords.contains(excludedProductRecord));
        assertTrue(
                "Excluded record is NOT present in the Assembler record list", assemblerPRecordIds.contains(excludedProductRecord));
    }

    @Given("^I send a request to search api to get the level three category using (.+)$")
    public void iSendARequestToSearchApiToGetTheLevelThreeCategoryUsingSeoUrl(String seoUrl) throws Throwable {
        stepsHelper.getL3CategoryWithSeoUrlResponse(seoUrl);
    }

    @And("^all RS PRO products should be boosted at the top of the records for (.+)$")
    public void allRSPROProductsShouldBeDisplayedAtTheTopOfTheRecordsForSeoUrl(String seoUrl) throws Throwable {
        Response sapiResponse = ScenarioContext.getData(seoUrl);
        List<String> searchApiRecordIdsList = CommonHelpers.getRecordIds(sapiResponse, "resultsList.records.id");
        naturalSearchHelpers.assertFirstRecordBrand(sapiResponse);
        String internalId = commonHelpers.getIds(sapiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getSeoUrlAssemblerL3Response(internalId);
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "All RS Pro products should have been displayed at the top of the record for the SEO Url",
                assemblerPRecordIds,
                searchApiRecordIdsList
        );
    }

    @Then("^the correct path for boosting RS PRO bias is triggered for (.+)$")
    public void theCorrectRuleForBoostingRSPROBiasIsTriggered(String seoUrl) throws Throwable {
        Response sapiResponse = ScenarioContext.getData(seoUrl);
        assertNotNull(
                String.format(
                        "The REST call using the value set for '%s' has come back with an " +
                                "invalid response. Check the config file for it's setting", seoUrl
                ),
                sapiResponse
        );
        assertEquals(
                String.format(
                        "The REST call was NOT successful, came back with an unexpected status code. " +
                                "Check config file for '%s' setting", seoUrl
                ), EXPECTED_OK_STATUS_CODE, sapiResponse.statusCode()
        );
        String rulePathMap = sapiResponse.path("queryInfo.rulesFired.ResultsList");
        String rulePath = rulePathMap.split("content/")[1];
        String expectedRulePath = Page.getProperty("expected.rs.pro.bias.rule.path");
        assertEquals("We got a different rule-path than expected.", expectedRulePath, rulePath);
    }

    @Then("^I check when the customer filter is passed restricted products are not returned for (.+)$")
    public void i_check_when_the_customer_filter_is_passed_restricted_products_are_not_returned_for(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        String restrictedProduct = Page.getProperty("restricted.product");
        List<String> sapiRecordIds = getRecordIds(sapiResponse, "resultsList.records.id");
        assertFalse(
                "Hidden record was returned in Search API query", sapiRecordIds.contains(restrictedProduct)
        );
    }

    @Then("^Hidden level two category should not returned as part of refinement option for (.*) with (.*)$")
    public void hidden_level_two_category_should_not_returned_as_part_of_refinement_option_for_Customer_Filter_id_with_search_term_example(String customerFilterIdProperty, String searchterm) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        List<String> getCustomWithFilterLevelTwoRefineLabels = sapiResponse.path("refinements[0].value.label");
        String term = Page.getProperty(searchterm);
        Response response = StepsHelper.getSearchWithoutCustomerFilterApiResponse(term);
        List<String> getCustomWithOutFilterLevelTwoRefineLabels = response.path("refinements[0].value.label");
        assertNotEquals(
                "The Level 2 category hidden products should NOT have been returned as part of the retirement " +
                        "option when when the customer filter is applied",
                getCustomWithOutFilterLevelTwoRefineLabels.size(), getCustomWithFilterLevelTwoRefineLabels.size()
        );
    }

    @Then("^Level two categories super section counts exclude hidden categories in query info block for (.*) with (.*)$")
    public void level_two_categories_super_section_counts_exclude_hidden_categories_in_query_info_block_for_Customer_Filter_id_with_search_term_example(String customerFilterIdProperty, String searchTerm) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        String customerFilterIdSupersection = sapiResponse.path("queryInfo.search_super_sections");
        String term = Page.getProperty(searchTerm);
        Response response = StepsHelper.getSearchWithoutCustomerFilterApiResponse(term);
        String getSupersectionWithoutFilter = response.path("queryInfo.search_super_sections");
        assertNotEquals(
                "The Level 2 category super section should have excluded the hidden categories in query info block " +
                        "when customer filter will be applied",
                customerFilterIdSupersection,
                getSupersectionWithoutFilter
        );
    }

    @Then("^I check when the (.*) is passed restricted products are not returned for for stock number search$")
    public void i_check_when_the_is_passed_restricted_products_are_not_returned_for_for_stock_number_search(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        String restrictedProduct = Page.getProperty("search.by.stockNumber.restricted.product");
        List<String> sapiPrecordIds = getRecordIds(sapiResponse, "resultsList.records.id");
        assertFalse("Hidden record was returned in Search API query", sapiPrecordIds.contains(restrictedProduct));
    }

    @Then("^I should see record count excludes hidden products for search term by stock number for Customer.Filter$")
    public void i_should_see_record_count_excludes_hidden_products_for_search_term_by_stock_number_for_Customer_Filter_id() {
        Response sapiResponse = ScenarioContext.getData(Page.getProperty("Customer.Filter.id"));
        int countWithCustomerFilter = sapiResponse.path("resultsList.pagination.count");
        String term = Page.getProperty("search.term.by.stock.number");
        Response customerWithoutFilterResponse = StepsHelper.getSearchWithoutCustomerFilterApiResponse(term);
        int countBeforeCustomerFilter = customerWithoutFilterResponse.path("resultsList.pagination.count");
        assertTrue(
                "The products hidden by the customer filter should have NOT been returned",
                countBeforeCustomerFilter > countWithCustomerFilter
        );
    }

    @Then("^I should see record count excludes hidden products for (.+) with (.+)$")
    public void i_should_see_record_count_excludes_hidden_products_for(String customerFilterIdProperty, String searchTerm) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        assertNotNull(
                "SearchAPI query to fetch records excluding hidden products should have come back with a valid response",
                sapiResponse
        );
        int countWithCustomerFilter = sapiResponse.path("resultsList.pagination.count");
        Response customerWithoutFilterResponse = StepsHelper.getSearchWithoutCustomerFilterApiResponse(Page.getProperty(searchTerm));
        int countBeforeCustomerFilter = customerWithoutFilterResponse.path("resultsList.pagination.count");
        assertTrue(
                "The products hidden by the customer filter should have NOT been returned",
                countBeforeCustomerFilter > countWithCustomerFilter
        );
    }

    @Then("^I should see Level one and Level two bin count exclude hidden product for (.*)$")
    public void i_should_see_Level_one_and_Level_two_bin_count_exclude_hidden_product_for_Specify(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        List<String> categoryAndBinCounts = sapiResponse.path("refinements[0].value.binCount");
        int sumOfCategoryBinCounts = categoryAndBinCounts.stream().map(Integer::parseInt).mapToInt(Integer::intValue).sum();
        int resultCounts = sapiResponse.path("queryInfo.results_count");
        assertEquals(
                "The Level 1 and Level 2 bin counts should have excluded the hidden products after the customer filter " +
                        "have being applied",
                sumOfCategoryBinCounts,
                resultCounts
        );
    }

    @Then("^I should get status code (.*) for (.*) with valid json response$")
    public void i_should_get_status_code(int statusCode, String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response response = ScenarioContext.getData(customerFilterId);
        assertEquals(
                String.format(
                        "Should have received a status code of '%s', after customer filter has been applied", statusCode
                ),
                statusCode,
                response.getStatusCode()
        );
    }

    @Then("^I should not see hidden level two and level three categories returned for (.*)$")
    public void i_should_not_see_hidden_level_two_and_level_three_categories_returned_for_Customer_Filter_id(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response response = ScenarioContext.getData(customerFilterId);
        List<String> catCountL2 = CommonHelpers.getRecordIds(response, "refinements[0].value.label");
        String getHiddenCategoryLevelTwo = Page.getProperty("Hidden.Leveltwo.categories.labels");
        assertTrue(
                "Hidden Level 2 or Level 3 categories after applying customer filter should NOT have been returned",
                catCountL2.stream().noneMatch(x -> x.contains(getHiddenCategoryLevelTwo))
        );
    }

    @Then("^I should not see hidden level three categories returned for (.*)$")
    public void i_should_not_see_hidden_level_three_categories_returned_for_Customer_Filter_id(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response response = ScenarioContext.getData(customerFilterId);
        List<String> catCountL3 = CommonHelpers.getPRecordIds(response, "refinements[0].value.categories.label");
        String getHiddenCategoryLevelThree = Page.getProperty("hidden.LevelThree.categories.labels");
        assertTrue(
                "Hidden Level 3 categories after applying customer filter should NOT have been returned",
                catCountL3.stream().noneMatch(x -> x.contains(getHiddenCategoryLevelThree))
        );
    }

    @Then("^I should not see hidden level three categories returned in the topCategories block for (.*)$")
    public void i_should_not_see_hidden_level_three_categories_returned_in_the_topCategories_block_for_Customer_Filter_id(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response response = ScenarioContext.getData(customerFilterId);
        List<String> getTopCatLables = CommonHelpers.getRecordIds(response, "topCategories.categoryName");
        String getHiddenCategoryLevelThree = Page.getProperty("hidden.LevelThree.categories.labels");
        assertTrue(
                "Hidden Level 3 categories should NOT have been returned in the top categories block",
                getTopCatLables.stream().noneMatch(x -> x.contains(getHiddenCategoryLevelThree))
        );
    }

    @Then("^I should hidden level two and level three categories should not returned for search by MPN with (.*)$")
    public void i_should_hidden_level_two_and_level_three_categories_should_not_returned_for_search_by_MPN_with_Customer_Filter_id(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response response = ScenarioContext.getData(customerFilterId);
        List<String> binCountL1 = CommonHelpers.getPRecordIds(response, "refinements[0].value.categories.label");
        String getHiddenCategoryLevelTwo = Page.getProperty("hidden.l2.and.l3.category.lable.search.by.MPN");
        assertTrue(
                "Hidden Level 2 and 3 categories should NOT have been returned for the MPN search after " +
                        "applying customer filter",
                binCountL1.stream().noneMatch(x -> x.contains(getHiddenCategoryLevelTwo))
        );
    }

    @Then("^I should see results count in queryInfo block excludes hidden products for customer filter by stock number search$")
    public void i_should_see_results_count_in_queryInfo_block_excludes_hidden_products_for_customer_filter_by_stock_number_search() {
        Response response = ScenarioContext.getData(Page.getProperty("Customer.Filter.id"));
        int totalNumOfQueryBlockCount = response.path("queryInfo.results_count");
        Response customerWithoutFilterResponse = StepsHelper.getSearchWithoutCustomerFilterApiResponse(Page.getProperty("search.term.by.stock.number"));
        int totalNumOfQueryBlockCountWithoutFilter = customerWithoutFilterResponse.path("queryInfo.results_count");
        assertTrue(
                "Hidden products should NOT have been returned after applying custom filter with stock number search",
                totalNumOfQueryBlockCountWithoutFilter > totalNumOfQueryBlockCount
        );
    }

    @Then("^I should see the super section category hidden restricted products not returned$")
    public void i_should_see_the_super_section_category_hidden_restricted_products_not_returned() {
        Response response = ScenarioContext.getData(Page.getProperty("Customer.Filter.id"));
        String customerFilterSearchSuperSections = response.path("queryInfo.search_super_sections");
        String term = Page.getProperty("search.term.by.mpn");
        Response sapiResponse = StepsHelper.getSearchByMPNWithOutCustomerFilterApiResponse(term);
        String SearchSuperSections = sapiResponse.path("queryInfo.search_super_sections");
        assertNotEquals(
                "The super section category should have been hidden and NOT returned",
                SearchSuperSections,
                customerFilterSearchSuperSections
        );
    }

    @Then("^I should see the search_categories queryInfo block excludes hidden products$")
    public void i_should_see_the_search_categories_queryInfo_block_excludes_hidden_products() {
        Response response = ScenarioContext.getData(Page.getProperty("Customer.Filter.id"));
        String customerFilterSearchCategorySections = response.path("queryInfo.search_categories");
        String term = Page.getProperty("search.term.by.mpn");
        Response sapiResponse = StepsHelper.getSearchByMPNWithOutCustomerFilterApiResponse(term);
        String SearchCategorySections = sapiResponse.path("queryInfo.search_categories");
        assertNotEquals(
                "The search categories query block should have excluded hidden products",
                SearchCategorySections,
                customerFilterSearchCategorySections
        );
    }

    @Then("^I should see record count excludes hidden products for search by mpn$")
    public void i_should_see_record_count_excludes_hidden_products_for_search_by_mpn() {
        Response response = ScenarioContext.getData(Page.getProperty("Customer.Filter.id"));
        int customerFilterResultCounts = response.path("resultsList.pagination.count");
        String term = Page.getProperty("search.term.by.mpn");
        Response sapiResponse = StepsHelper.getSearchByMPNWithOutCustomerFilterApiResponse(term);
        int resultCount = sapiResponse.path("resultsList.pagination.count");
        assertTrue(
                "Search by MPN should have excluded hidden products",
                resultCount > customerFilterResultCounts
        );
    }

    @Then("^I should see results count in queryInfo block excludes hidden products for (.+) with (.+)$")
    public void i_should_see_results_count_in_queryInfo_block_excludes_hidden_products(String customerFilterIdProperty, String searchTerm) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        int totalNumOfQueryBlockCount = sapiResponse.path("queryInfo.results_count");
        String term = Page.getProperty(searchTerm);
        Response customerWithoutFilterResponse = StepsHelper.getSearchWithoutCustomerFilterApiResponse(term);
        int totalNumOfQueryBlockCountWithoutFilter = customerWithoutFilterResponse.path("queryInfo.results_count");
        assertTrue(
                "Search query should have excluded hidden products after applying customer filter to it",
                totalNumOfQueryBlockCountWithoutFilter > totalNumOfQueryBlockCount
        );
    }

    @Then("^I should see all categories excluding hidden products as part of the refinement options for (.+) with (.+)$")
    public void i_should_see_all_categories_excluding_hidden_products_as_part_of_the_refinement_options_for_with_search_term_example(String customerFilterIdProperty, String searchTerm) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        String totalNumOfSearchCategoriesCustomerFilter = sapiResponse.path("queryInfo.search_categories");
        String term = Page.getProperty(searchTerm);
        Response customerWithoutFilterResponse = StepsHelper.getSearchWithoutCustomerFilterApiResponse(term);
        String totalNumOfSearchCategoriesCountWithoutFilter = customerWithoutFilterResponse.path("queryInfo.search_categories");
        assertNotEquals(
                "Hidden products should NOT have been returned as part of the refinement options after " +
                        "customer filter has been applied",
                totalNumOfSearchCategoriesCountWithoutFilter,
                totalNumOfSearchCategoriesCustomerFilter
        );
    }

    @Then("^I check when the Customer_Filter is passed (.*) are not returned on terminal node page$")
    public void i_check_when_the_customer_filter_is_passed_restricted_products_are_not_returned_on_terminal_node_page(String getRestrictedProduct) {
        String terminalNodePsfId = Page.getProperty("terminal.node.psf.id");
        Response sapiResponse = ScenarioContext.getData(terminalNodePsfId);
        assertNotNull(
                String.format(
                        "An invalid SearchAPI response was returned when requesting data for " +
                                "category '%s' (terminal node)", terminalNodePsfId
                ),
                sapiResponse
        );

        List<String> sapiProductRecordIds = CommonHelpers.getRecordIds(sapiResponse, "resultsList.records.id");
        String restrictedProduct = Page.getProperty(getRestrictedProduct);
        assertFalse(
                "Hidden record was returned in Search API query", sapiProductRecordIds.contains(restrictedProduct)
        );
    }

    @Then("^The bin count in breadcrumb block excludes hidden products on terminal node for (.+) with (.*)$")
    public void the_bin_count_in_breadcrumb_block_excludes_hidden_products_on_terminal_node_for(String psfId, String searchTerm) {
        String getPsfId = Page.getProperty(psfId);
        Response response = ScenarioContext.getData(getPsfId);
        assertNotNull("REST call returned an invalid response.", response);
        List<Integer> breadcrumbBinCountWithCustomerFilter = response.path("breadbox.breadcrumbs[0].value.binCount");
        int breadcrumbBinCountWithCustomerFilterValue = breadcrumbBinCountWithCustomerFilter.get(0);
        String getSearchTerm = Page.getProperty(searchTerm);
        Response sapiResponse = StepsHelper.getTerminalNodeDesktopWithoutCustomerFilterResponse(getSearchTerm, getPsfId);
        List<Integer> breadcrumbBinCountWithoutCustomerFilter = sapiResponse.path("breadbox.breadcrumbs[0].value.binCount");
        int breadcrumbBinCountWithoutCustomerFilterValue = breadcrumbBinCountWithoutCustomerFilter.get(0);
        assertTrue(
                "Hidden products are NOT excluded: the bincount without restriction is NOT more than with restriction",
                breadcrumbBinCountWithoutCustomerFilterValue > breadcrumbBinCountWithCustomerFilterValue
        );
    }

    @Then("^I should see record count excludes hidden products on terminal node for (.*) with (.*)$")
    public void i_should_see_record_count_excludes_hidden_products_on_terminal_node_for_terminal_node_psfId_with_CustomerFilter_id_by_Terminal_node(String psfId, String searchTerm) {
        String getPsfId = Page.getProperty(psfId);
        Response response = ScenarioContext.getData(getPsfId);
        int totalNumOfRecordsWithCustomerField = response.path("resultsList.pagination.count");
        String getSearchTerm = Page.getProperty(searchTerm);
        Response sapiResponse = StepsHelper.getTerminalNodeDesktopWithoutCustomerFilterResponse(getSearchTerm, getPsfId);
        assertNotNull(
                "SearchAPI request to fetch products for terminal node should have had a valid response",
                sapiResponse
        );
        int totalNumOfRecordsWithOutCustomerFilterValue = sapiResponse.path("resultsList.pagination.count");
        assertTrue(
                "Should have excluded hidden product(s) for the termninal node after applying customer filter to it",
                totalNumOfRecordsWithOutCustomerFilterValue > totalNumOfRecordsWithCustomerField
        );
    }

    @Then("^I should see results count in queryInfo block Terminal node excludes hidden products for (.*) with (.*)$")
    public void i_should_see_results_count_in_queryInfo_block_Terminal_node_excludes_hidden_products_for_Specify_psfId_with_specify_term(String psfId, String searchTerm) {
        String getPsfId = Page.getProperty(psfId);
        Response response = ScenarioContext.getData(getPsfId);
        int totalNumOfQueryBlockCountCustomerId = response.path("queryInfo.results_count");
        String getSearchTerm = Page.getProperty(searchTerm);
        Response sapiResponse = StepsHelper.getTerminalNodeDesktopWithoutCustomerFilterResponse(getSearchTerm, getPsfId);
        int totalNumOfQueryBlockWithOutCustomerId = sapiResponse.path("queryInfo.results_count");
        assertTrue(
                "Hidden products at the terminal node should have been excluded after applying customer filter to it",
                totalNumOfQueryBlockWithOutCustomerId > totalNumOfQueryBlockCountCustomerId
        );
    }

    @Then("^No results should returned for (.*)$")
    public void no_results_should_returned_for_Customer_Filter_id(String customerFilterIdProperty) {
        String customerFilterId = Page.getProperty(customerFilterIdProperty);
        Response sapiResponse = ScenarioContext.getData(customerFilterId);
        assertNotNull(
                "SearchAPI query (after the applying customer filter) should have come back with a valid response",
                sapiResponse
        );
        int countWithCustomerFilter = sapiResponse.path("resultsList.pagination.count");
        assertEquals(
                "No results should have been returned after applying customer filter",
                0, countWithCustomerFilter
        );
    }

    @Given("^I request search api with (.+) as search term for customer (.+)$")
    public void searchAPIRequestWithSearchTermForGivenCustomer(String searchTermProp, String customerFilterProp) {
        String searchTerm = Page.getProperty(searchTermProp);
        String customerFilterId = Page.getProperty(customerFilterProp);
        stepsHelper.setSearchAPINaturalSearchWithCustomerFilterRequest(searchTerm, customerFilterId);
    }

    @Then("^total number of record count should not include excluded products for (.+), with (.+), for (.+)$")
    public void totalNumberOfRecordsShouldNotIncludeExcludedProducts(String searchTermProp, String interfaceNameProp, String customerFilterIdProp) {
        String searchTerm = Page.getProperty(searchTermProp);
        String interfaceName = Page.getProperty(interfaceNameProp);
        String customerFilterId = Page.getProperty(customerFilterIdProp);

        Response searchApiResponse = ScenarioContext.getData(customerFilterId);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExclusionSearch(searchTerm, "0", interfaceName, "20");

        Integer assemblerRecordCount = assemblerResponse.path("'endeca:assemblerRequestInformation'.'endeca:numRecords'");
        Integer searchAPIRecordCount = searchApiResponse.path("queryInfo.results_count");

        assertEquals(
                "SearchAPI should return a 200 status code",
                EXPECTED_OK_STATUS_CODE,
                searchApiResponse.getStatusCode());
        assertTrue(
                "SearchAPI is expected to return at least 1 record for testing",
                searchAPIRecordCount > 0);
        assertTrue(
                "Assembler response bincount is same or less than SAPI",
                assemblerRecordCount > searchAPIRecordCount);
    }


    @Then("^L2 Category bin counts should not include excluded products for (.+), with (.+), for (.+)$")
    public void l2_category_bin_counts_should_not_include_excluded_products(String searchTermProp, String interfaceNameProp, String customerFilterProp) {
        String searchTerm = Page.getProperty(searchTermProp);
        String interfaceName = Page.getProperty(interfaceNameProp);
        String customerFilter = Page.getProperty(customerFilterProp);
        Response searchApiResponse = ScenarioContext.getData(customerFilter);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExclusionSearch(searchTerm, "0", interfaceName, "20");
        termNodeHelper.assertExcludedProductsNotReturnedL2BinCount(searchApiResponse, assemblerResponse);
        naturalSearchHelpers.assertExcludedProductsNotReturnedWithIndividualL2BinCounts(searchApiResponse, assemblerResponse);
    }

    @Then("^The page Pagination number is (\\d+) for the searchTerm$")
    public void The_page_Pagination_number_is_for_the_search_Term_with(int pageNumber) {
        naturalSearchHelpers.assertSapiElementCountEqual(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.pagination.page", pageNumber
        );
    }

    @Then("^The lastPage count should be less than (\\d+) for the searchTerm$")
    public void the_lastPage_count_should_be_less_than_for_the_searchTerm(int expectedLastPageCount) {
        int actualLastPageNumber = naturalSearchHelpers.sapiLastPageCount(
                ScenarioContext.getData(ScenarioContext.getIds())
        );
        assertTrue("Last page number are not equal",
                actualLastPageNumber < expectedLastPageCount);

    }

    @Then("^The lastPage count should be null$")
    public void the_lastPage_count_should_be_null() {
        String actualLastPageNumber = naturalSearchHelpers.sapiGetElement(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.pagination.lastPage"
        );
        assertNull(actualLastPageNumber);
    }

    @Then("^The available navigation list should be less or (\\d+) for the searchTerm$")
    public void the_available_navigation_list_should_be_less_or_equal_for_the_searchTerm(int listNumber) {
        naturalSearchHelpers.assertSapiCountFromListLessOrEqual
                (ScenarioContext.getData(ScenarioContext.getIds()),
                        "availableNavigation.key",
                        listNumber
                );
    }

    @Then("^The selected navigation list should be (\\d+) for the searchTerm$")
    public void the_selected_navigation_list_should_be_for_the_searchTerm(int listNumber) {
        naturalSearchHelpers.assertSapiCountFromListLessOrEqual(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "selectedNavigation.key",
                listNumber
        );
    }

    @Then("^The result count should be (\\d+) for the searchTerm$")
    public void the_result_count_should_be_for_the_searchTerm_with_parameters(int expectedResultCountNumber) {
        naturalSearchHelpers.assertSapiElementCountEqual(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.pagination.count",
                expectedResultCountNumber
        );
    }

    @Then("^The result count should be greater than (\\d+) for the searchTerm$")
    public void the_result_count_should_be_greater_than_for_the_searchTerm_with(int expectedCountResults) {
        naturalSearchHelpers.assertSapiElementCountGreaterThan(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.pagination.count",
                expectedCountResults
        );
    }

    @Then("^I should see page count to be (\\d+) for the searchTerm$")
    public void i_should_see_page_count_to_be_for_the_searchTerm(int expectedPageCountNumber) {
        naturalSearchHelpers.
                assertSapiElementCountEqual(ScenarioContext.getData(ScenarioContext.getIds()),
                        "resultsList.pagination.page",
                        expectedPageCountNumber
                );
    }


    @Then("^I should see sortOption set as (.*) for Ascending for the searchTerm$")
    public void i_should_see_sortOption_set_as_priceLowHigh_for_Ascending_for_the_search_with_parameter(String priceLowHighSortOption) {
        String thirdSortOption = naturalSearchHelpers.
                sapiSortByIndex(ScenarioContext.getData(ScenarioContext.getIds()),
                        "resultsList.sortOptions.sortOption",
                        2
                );
        assertEquals(thirdSortOption,
                priceLowHighSortOption
        );
    }

    @Then("^I should see the sortOption selected set as (.*) for Relevance for the searchTerm$")
    public void i_should_see_the_sortOption_selected_set_as_parameter_for_Relevance_for_the_searchTerm(String sortSelectedOption) {
        String firstSortOptionSelected = naturalSearchHelpers.
                SapiSortByIndexByConvertingBooleanToString(ScenarioContext.getData(ScenarioContext.getIds()),
                        "resultsList.sortOptions.selected",
                        0
                );
        assertEquals("Selected value is set to false",
                firstSortOptionSelected,
                sortSelectedOption
        );
    }

    @Then("^I should see sortOption selected set as (.*) for Ascending for the searchTerm$")
    public void i_should_see_sortOption_selected_set_as_true_for_Ascending_for_the_searchTerm(String sortSelectedOption) {
        String thirdSortOptionSelected = naturalSearchHelpers.SapiSortByIndexByConvertingBooleanToString(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.sortOptions.selected",
                2
        );
        assertEquals("Selected value is set to false",
                thirdSortOptionSelected,
                sortSelectedOption
        );
    }

    @Then("^I should see sortOption selected set as (.*) for Descending for the searchTerm$")
    public void i_should_see_sortOption_selected_set_as_parameter_for_Descending_for_the_searchTerm(String sortSelectedOption) {
        String sortOptionSelected = naturalSearchHelpers.SapiSortByIndexByConvertingBooleanToString(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.sortOptions.selected", 1);
        assertEquals("Selected value is set to false",
                sortOptionSelected,
                sortSelectedOption
        );
    }

    @Then("^I should see see (.*) as a value for KEY for the searchTerm$")
    public void i_should_see_see_attributes_Length_as_a_value_for_KEY_for_the_searchTerm_with(String attributeLength) {
        String avaiNavigation_keyValue = naturalSearchHelpers.sapiGetElement(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "availableNavigation.key");
        assertEquals(
                avaiNavigation_keyValue,
                attributeLength
        );
    }

    @Then("^The refinement count should be (\\d+) for the searchTerm$")
    public void the_refinement_count_should_be_for_the_searchTerm_with(int refinementListCount) {
        naturalSearchHelpers.assertSapiCountFromListLessOrEqual(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "availableNavigation.refinements",
                refinementListCount
        );
    }

    @Then("^the recordId count should be less or equal (\\d+) for the searchTerm$")
    public void the_recordId_count_should_be_less_or_equal_for_the_searchTerm(int listNumber) {
        naturalSearchHelpers.assertSapiCountFromListLessOrEqual(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.records.id", listNumber
        );
    }

    @Then("^the limit of records per page should be (\\d+) for the searchTerm$")
    public void the_limit_of_records_per_page_should_be_for_the_searchTerm_with_parameters(int expectedLimit) {
        naturalSearchHelpers.assertSapiElementCountEqual(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.pagination.limit", expectedLimit
        );
    }

    @Then("^(.*) is displayed as displayName from the responseBody$")
    public void parameter_is_displayed_as_displayName_from_the_responsebody(String expectedValue) {
        List<String> actualValue = naturalSearchHelpers.sapiListElements(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.records[0].properties.key");
        assertTrue("The expected value is not among the list of elements", actualValue.stream().anyMatch(x -> x.contains(expectedValue)));
    }

    @Then("^(.*) is displayed as value under brand refinements$")
    public void parameter_is_displayed_as_value_under_brand_refinements(String expectedValue) {
        List<String> actualValue = naturalSearchHelpers.sapiListElements(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.records[0].properties.value");
        assertTrue("The expected value is not equal to elements", actualValue.stream().
                anyMatch(x -> x.equals(expectedValue))
        );
    }

    @Then("^I should see the prices lists displayed as expected based on (.*)$")
    public void i_should_see_the_prices_lists_displayed_as_expected_based_on_parameters(String sortBy) {
        if (sortBy.equalsIgnoreCase("ASC")) {
            naturalSearchHelpers.
                    assertSapiAscendingPricesOrderList(
                            ScenarioContext.getData(ScenarioContext.getIds())
                    );
        } else if (sortBy.equalsIgnoreCase("DESC")) {
            naturalSearchHelpers.
                    assertSapiDescendingPricesOrderList(
                            ScenarioContext.getData(ScenarioContext.getIds())
                    );
        } else if (sortBy.equalsIgnoreCase("Relevance")) {
            naturalSearchHelpers.
                    assertSapiRelevancePriceorder(
                            ScenarioContext.getData(ScenarioContext.getIds())
                    );
        } else {
            Assert.fail("None of the above tests are verified");
        }
    }

    @Then("^The recordId count should be empty$")
    public void the_recordId_count_should_be_empty() {
        List<String> recordIdCount = naturalSearchHelpers.sapiListElements(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "resultsList.records.id"
        );
        assertTrue("The recordId list is not empty",
                recordIdCount.isEmpty()
        );
    }

    @Then("^I should see from the refinement list value contains (.*) from the response$")
    public void i_should_see_from_the_refinement_list_value_contains_parameter_from_the_response(String containValue) {
        List<String> refinementsCount = naturalSearchHelpers.sapiListElements(
                ScenarioContext.getData(ScenarioContext.getIds()),
                "availableNavigation.refinements.value"
        );
        assertTrue("The expected value is not among the list of elements", refinementsCount.stream().anyMatch(x -> x.contains(containValue)));
    }
}