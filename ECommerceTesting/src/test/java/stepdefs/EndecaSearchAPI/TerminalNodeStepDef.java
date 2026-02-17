package stepdefs.EndecaSearchAPI;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import pages.Page;
import pages.Search.SeachAPI.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TerminalNodeStepDef {

    private final CommonHelpers commonHelpers;
    private final StepsHelper stepsHelper;
    private final TerminalNodeHelper termNodeHelper;

    public TerminalNodeStepDef(CommonHelpers commonHelpers, StepsHelper stepsHelper, TerminalNodeHelper termNodeHelper) {
        this.commonHelpers = commonHelpers;
        this.stepsHelper = stepsHelper;
        this.termNodeHelper = termNodeHelper;
    }

    @Then("^Then I get the InternalId for all the refinements for (.+)$")
    public void i_get_the_internalId_for_all_the_refinements(String psfId) throws Exception {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        termNodeHelper.assertInternalIdNotNull(searchApiResponse);
    }

    @Given("^I request search api that has no targetState block for (.+)$")
    public void i_request_search_api_with_a_targetState_parameter(String psfID) {
        Response response = stepsHelper.getSAPIResponseWithTrackingParameters("mobile", "automation", psfID);
        ScenarioContext.setData(psfID, response);
        ScenarioContext.setIds(psfID);
    }

    @Then("^I get the targetState block details collapsed for all the refinements for (.+)$")
    public void i_get_the_targetState_block_collapsed_for_all_the_refinements(String psfId) throws Exception {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        assertTrue(
                "Target state block should have been collapsed for all the refinement",
                termNodeHelper.assertTargetStateBlock(searchApiResponse, false)
        );
    }

    @Then("^I get the targetState block details for all the refinements for (.+)$")
    public void i_get_the_targetState_for_all_the_refinements(String psfId) throws Exception {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        assertTrue(
                "Target state block should have been returned for all the refinements",
                termNodeHelper.assertTargetStateBlock(searchApiResponse, true)
        );
    }

    @Given("^I request search api with (.+) as search term$")
    public void i_request_search_api_with_a_misSpelt_search_term(String searchTerm) {
        stepsHelper.getSAPIResponseWithSearchQueryParam(searchTerm);
    }

    @Then("^I check if the search term is autocorrected to (.+) unless the localId is (.+)$")
    public void i_get_the_misspelt_searchTerm_autoCorrected(String correctTerm, String localId) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String autoCorrectedTerm = searchApiResponse.path("queryInfo.search_keyword_spell_corrected");
        String interfaceName = searchApiResponse.path("queryInfo.search_interface_name");
        if (!"I18NRSStockNumber".equals(interfaceName)) {
            if (!System.getProperty("country").equals(localId))
                assertTrue(
                        "The correct term should have been unchanged (local id not the same as current country)",
                        correctTerm.toUpperCase().contains(autoCorrectedTerm.toUpperCase())
                );
            else {
                assertNotEquals(
                        "The correct term should have been autocorrected (current locale used)",
                        correctTerm,
                        autoCorrectedTerm
                );
                assertNull(
                        "Autocorrected term should have NOT been set (current locale used)",
                        autoCorrectedTerm
                );
            }
        } else {
            assertNull(
                    "I18NRSStockNumber: autocorrected term should have NOT been set",
                    autoCorrectedTerm
            );
        }
    }

    @Then("^I check additional details required along with autoCorrected term (.+), (.+), (.+)$")
    public void i_check_additional_details_along_with_searchTerm_autoCorrected(String correctTerm, String spellCorrect, String autoCorrected) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String searchKeyWord = ScenarioContext.getIds().replaceAll("%20", " ");
        String misSpeltSearchTerm = searchApiResponse.path("queryInfo.search_keyword");
        String autoCorrectedTerm = searchApiResponse.path("queryInfo.search_keyword_spell_corrected");
        String interfaceName = searchApiResponse.path("queryInfo.search_interface_name");
        boolean searchAutoCorrected = Boolean.parseBoolean(searchApiResponse.path("queryInfo.search_autocorrected"));
        boolean spellCorrectApplied = Boolean.parseBoolean(searchApiResponse.path("queryInfo.search_spell_correct_applied"));
        if (!System.getProperty("country").equals("jp") && !"I18NRSStockNumber".equals(interfaceName)) {
            assertTrue(
                    "The correct term should have been unchanged (country: not jp)",
                    correctTerm.toUpperCase().contains(autoCorrectedTerm.toUpperCase())
            );
            assertEquals(
                    "The search term should have remained unchanged (country: not jp)",
                    searchKeyWord.toUpperCase(), misSpeltSearchTerm.toUpperCase()
            );
            assertEquals(
                    "The autocorrection of misspelt term should have occurred (country: not jp)",
                    autoCorrected, searchApiResponse.path("queryInfo.search_autocorrected")
            );
            assertEquals(
                    "The spell correction of misspelt term should have been applied (country: not jp)",
                    spellCorrect, searchApiResponse.path("queryInfo.search_spell_correct_applied")
            );
        } else {
            assertNotEquals(
                    "The correct term should have been unchanged (country: jp)",
                    correctTerm, autoCorrectedTerm
            );
            assertNull(
                    "The not autocorrected term should have been created (country: jp)",
                    autoCorrectedTerm
            );
            assertEquals(
                    "The search term should have remained unchanged (country: jp)",
                    searchKeyWord, misSpeltSearchTerm
            );
            assertFalse(
                    "The autocorrection of misspelt term should have NOT occurred (country: jp)",
                    searchAutoCorrected
            );
            assertFalse(
                    "The spell correction of misspelt term should have been NOT been applied (country: jp)",
                    spellCorrectApplied
            );
        }
    }

    @Given("^I request the search API end point for an L3 Category page with a term (.+) for (.+)$")
    public void iRequestTheSearchAPIEndPointForAnLCategoryPageWithATermMosfetForPsfId(String term, String psfId) {
        stepsHelper.getTermNodeWithSearchTermResponse(UrlBuilder.getSearchTerm(term), psfId);
    }

    @Given("^I request the search API end point for a search results page with a term (.+)$")
    public void iRequestTheSearchAPIEndPointForASearchResultsPageWithATermMosfet(String term) {
        stepsHelper.getSearchTermResponse(UrlBuilder.getSearchTerm(term));
    }

    @Then("^the results from SAPI with a search term (.+) ordered by descending order match the results from the assembler for (.+)$")
    public void the_results_from_SAPI_with_a_search_term_mosfet_ordered_by_descending_order_match_the_results_from_the_assembler(String term, String interfaceName) {
        Response searchApiAscendingResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> ids = CommonHelpers.getRecordIds(searchApiAscendingResponse, "resultsList.records.id");

        Response assemblerHighToLowResponse = StepsHelper.getAssemblerHighToLowWithSearchTermResponse(ScenarioContext.getIds(), interfaceName);
        List<String> assemblerids = CommonHelpers.getPRecordIds(assemblerHighToLowResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        assertEquals(
                "Search term results (by descending order) between SearchAPI and Assembler should have matched",
                assemblerids,
                ids
        );
    }

    @Given("^I request the search API end point for a search results page in descending order with a term (.+)$")
    public void iRequestTheSearchAPIEndPointForASearchResultsPageInDescendingOrderWithATerm(String term) {
        stepsHelper.getDescendingResponseWithSearchTerm(UrlBuilder.getSearchTerm(term));
    }

    @Then("^the results from SAPI with a search term (.+) ordered by ascending order match the results from the assembler for(.+)$")
    public void the_results_from_SAPI_with_a_search_term_mosfet_ordered_by_ascending_order_match_the_results_from_the_assembler(String term, String interfaceName) {
        Response searchApiAscendingResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> ids = CommonHelpers.getRecordIds(searchApiAscendingResponse, "resultsList.records.id");

        Response assemblerHighToLowResponse = StepsHelper.getAssemblerLowToHighWithSearchTermResponse(ScenarioContext.getIds(), interfaceName);
        List<String> assemblerids = CommonHelpers.getPRecordIds(assemblerHighToLowResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        assertEquals(
                "The search term results should have matched and been sorted in ascending order for the search",
                assemblerids,
                ids
        );
    }

    @Given("^I request the search API end point for a search results page in ascending order with a term (.+)$")
    public void iRequestTheSearchAPIEndPointForASearchResultsPageInAscendingOrderWithATermMosfet(String term) {
        stepsHelper.getAscendingResponseWithSearchTerm(UrlBuilder.getSearchTerm(term));
    }

    @Given("^I request the search API end point for a search results page sorted by popularity with a term (.+)$")
    public void iRequestTheSearchAPIEndPointForASearchResultsPageSortedByPopularityWithATerm(String term) {
        stepsHelper.getSAPIPopularityResponseWithSearchTerm(UrlBuilder.getSearchTerm(term));
    }

    @Then("^the results from SAPI with a search term (.+) ordered by popularity match the results from the assembler for (.+)$")
    public void the_results_from_SAPI_with_a_search_term_mosfet_ordered_by_popularity_match_the_results_from_the_assembler(String term, String interfaceName) {
        Response searchApiAscendingResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> ids = CommonHelpers.getRecordIds(searchApiAscendingResponse, "resultsList.records.id");

        Response assemblerHighToLowResponse = StepsHelper.getAssemblerPopularityWithSearchTermResponse(ScenarioContext.getIds(), interfaceName);
        List<String> assemblerids = CommonHelpers.getPRecordIds(assemblerHighToLowResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        assertEquals(
                "The search term results ordered by popularity should have matched",
                assemblerids,
                ids
        );
    }

    @And("^the results from SAPI with a search term (.+) ordered by relevance match the results from the assembler for (.+)$")
    public void the_results_from_SAPI_with_a_search_term_mosfet_ordered_by_relevance_match_the_results_from_the_assembler(String term, String interfaceName) {
        Response searchApiAscendingResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> ids = CommonHelpers.getRecordIds(searchApiAscendingResponse, "resultsList.records.id");

        Response assemblerRelevanceResponse = StepsHelper.getAssemblerRelevanceWithSearchTermResponse(ScenarioContext.getIds(), interfaceName);
        List<String> assemblerids = CommonHelpers.getPRecordIds(assemblerRelevanceResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        assertEquals(
                "The search term results ordered by relevance should have matched",
                assemblerids,
                ids
        );
    }

    @And("^the results from SAPI with a term (.+) sorted by relevance match the assembler for (.+),(.+)$")
    public void the_results_from_SAPI_with_a_term_mosfet_sorted_by_relevance_match_the_assembler(String term, String psfId, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> searchApiRecordIds = CommonHelpers.getRecordIds(searchApiResponse, "resultsList.records.id");

        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTerminalNodeRelevanceWithInterfaceResponse(internalId, ScenarioContext.getIds(), interfaceName);
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "The search term results ordered by relevance should have matched",
                assemblerPRecordIds,
                searchApiRecordIds
        );
    }

    @Given("^I request the search API end point for a Terminal Node with a term (.+) for (.+)$")
    public void iRequestTheSearchAPIEndPointForATerminalNodeWithATermMosfetForPsfId(String term, String psfId) {
        stepsHelper.getTerminalNodeWithSearchTermResponse(UrlBuilder.getSearchTerm(term), psfId);
    }

    @Given("^I request the search API end point for a Terminal Node sorted by popularity with a term (.+) for (.+)$")
    public void iRequestTheSearchAPIEndPointForATerminalNodeSortedByPopularityWithATermMosfetForPsfId(String term, String psfId) {
        stepsHelper.getTerminalNodeByPopularityWithSearchTermResponse(UrlBuilder.getSearchTerm(term), psfId);
    }

    @Then("^the results from SAPI with a term (.+) sorted by popularity match the assembler for (.+),(.+)$")
    public void the_results_from_SAPI_with_a_term_mosfet_sorted_by_popularity_match_the_assembler(String term, String psfId, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> searchApiRecordIds = CommonHelpers.getRecordIds(searchApiResponse, "resultsList.records.id");

        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTerminalNodePopularityWithInterfaceResponse(internalId, ScenarioContext.getIds(), interfaceName);
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "The search term results ordered by popularity should have matched",
                assemblerPRecordIds,
                searchApiRecordIds
        );
    }

    @Then("^the category Details should be displayed for the records with a term (.+), (.+)$")
    public void the_category_details_should_be_displayed(String searchTerm, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(
                ScenarioContext.getIds(), internalId, "0", interfaceName, "20"
        );
        termNodeHelper.assertCategoryDetails(searchApiResponse, assemblerResponse);
    }

    @Then("^the specification attribute details for an initial list of 20 products should be displayed using a search term (.+), (.+)$")
    public void theSpecificationAttributeDetailsForAnInitialListOfProductsShouldBeDisplayed(String searchTerm, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        String searchStr = ScenarioContext.getSearchTermMap().get(searchTerm);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchStr, internalId, "0", interfaceName, "20");
        termNodeHelper.assertTermNodePageAttributes(searchApiResponse, assemblerResponse);
    }

    @Given("^I request the search API end point for a Terminal Node sorted by descending order with a term (.+) for (.+)$")
    public void iRequestTheSearchAPIEndPointForATerminalNodeSortedByDescendingOrderWithATermMosfetForPsfId(String term, String psfId) {
        stepsHelper.getTerminalNodeByDescendingOrderWithSearchTermResponse(UrlBuilder.getSearchTerm(term), psfId);
    }

    @Then("^the results from SAPI with a term (.+) sorted by descending order match the assembler for (.+),(.+)$")
    public void the_results_from_SAPI_with_a_term_mosfet_sorted_by_descending_order_match_the_assembler(String term, String psfId, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> searchApiRecordIds = CommonHelpers.getRecordIds(searchApiResponse, "resultsList.records.id");

        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTerminalNodeDescendingOrderWithInterfaceResponse(internalId, ScenarioContext.getIds(), interfaceName);
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "The search term results by descending order should have matched",
                assemblerPRecordIds,
                searchApiRecordIds
        );
    }

    @Given("^I request the search API end point for a Terminal Node sorted by ascending order with a term (.+) for (.+)$")
    public void iRequestTheSearchAPIEndPointForATerminalNodeSortedByAscendingOrderWithATermMosfetForPsfId(String term, String psfId) throws Throwable {
        stepsHelper.getTerminalNodeByAscendingOrderWithSearchTermResponse(UrlBuilder.getSearchTerm(term), psfId);
    }

    @And("^the results from SAPI with a term (.+) sorted by ascending order match the assembler for (.+),(.+)$")
    public void the_results_from_SAPI_with_a_term_mosfet_sorted_by_ascending_order_match_the_assembler(String term, String psfId, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> searchApiRecordIds = CommonHelpers.getRecordIds(searchApiResponse, "resultsList.records.id");

        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTerminalNodeAscendingOrderWithInterfaceResponse(internalId, ScenarioContext.getIds(), interfaceName);
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "The search term results by ascending order should have matched",
                assemblerPRecordIds,
                searchApiRecordIds
        );
    }

    @And("^the production packed products should be excluded from the facet value bin count with a search term (.+), (.+)$")
    public void the_production_packed_products_should_be_excluded_from_facet_value(String searchTerm, String interfaceName) throws Exception {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> whiteList = Arrays.asList(UrlBuilder.getDimensionsWhiteList().split(","));
        List<String> columnHeadersList = CommonHelpers.getRecordIds(searchApiResponse, "refinements.key");
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        String psfId = searchApiResponse.path("category.id");
        new StepsHelper().getl3CategorySearchResponse(psfId);
        String searchStr = ScenarioContext.getSearchTermMap().get(searchTerm);
        List<String> categoryMetaData = termNodeHelper.getCategoryProperties(ScenarioContext.getData(psfId));
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchStr, internalId, "0", interfaceName, "20");
        columnHeadersList.removeIf((String str) -> !(whiteList.contains(str) || categoryMetaData.contains(str)));
        termNodeHelper.assertRefinementsBinCount(searchApiResponse, assemblerResponse, columnHeadersList);
    }

    @Then("^The Product list bin count results should match the assembler response count with a search term (.+), (.+)")
    public void the_product_list_search_api_count_should_match_with_assembler_count_details(String searchTerm, String interfaceName) {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(response, "category.internalId");
        String searchStr = ScenarioContext.getSearchTermMap().get(searchTerm);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchStr, internalId, "0", interfaceName, "20");

        String searchAPIProductCount = response.path("queryInfo.results_count").toString();
        String assemblerProductCount = assemblerResponse.path("secondaryContent[0].refinementCrumbs[0].count").toString();
        assertEquals(
                "The product list count between SearchAPI and Assembler should have matched",
                assemblerProductCount,
                searchAPIProductCount
        );
    }

    @And("^The list of products should be sorted in (.+) order of price with a search term (.+)$")
    public void iSendARequestToTheAssemblerAPIToGetTheCapacityRecordsInAscendingOrderForPsfId(String order, String term) {
        Response searchApiAscendingResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> recordIds = CommonHelpers.getRecordIds(searchApiAscendingResponse, "resultsList.records.id");
        String searchTerm = ScenarioContext.getSearchTermMap().get(term);
        String internalId = commonHelpers.getIds(searchApiAscendingResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTermNodeProductOrderResponse(internalId, searchTerm, order);
        List<String> assemblerIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");

        assertEquals(
                "The capacity records (in ascending order) for the specific category should have matched (by ids)",
                assemblerIds,
                recordIds
        );
    }

    @Given("^I request the search API end point to sort the product list for an L3 Category with a search term (.+) in the (.+) order of price$")
    public void iRequestTheSearchAPIEndPointSortedWithCapacityAsAParameterForLCategoryPageFor(String searchTerm, String order) {
        Map<String, String> searchTermMap = termNodeHelper.getSearchTerms(ScenarioContext.getData(ScenarioContext.getIds()));
        stepsHelper.getTermNodeForProductOrderResponse(searchTermMap.get(searchTerm), order, ScenarioContext.getIds());
    }

    @And("^I send a request to the assembler API with a search term (.+) to get the bin count and dimension ids for, (.+)$")
    public void i_send_request_assemblerAPI_get_binCount_dimensionIds(String searchTerm, String interfaceName) throws Exception {
        Response searchApiReponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchApiReponse, "category.internalId");
        String searchStr = ScenarioContext.getSearchTermMap().get(searchTerm);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchStr, internalId, "0", interfaceName, "20");
        assertTrue(
                "The bin count and dimensions between SearchAPI and Assembler should have matched",
                termNodeHelper.assertBinCountAndDimensions(searchApiReponse, assemblerResponse)
        );
    }

    @Then("^I get the list of brand attribute values from the search API response with a search term (.+) with (.+)$")
    public void i_get_list_of_brand_attribute_values_from_searchAPI_response(String searchTerm, String interfaceName) {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchAPIResponse, "category.internalId");
        String searchStr = ScenarioContext.getSearchTermMap().get(searchTerm);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchStr, internalId, "0", interfaceName, "20");
        assertEquals(
                "The brand list (brand attribute values) between SearchAPI and Assembler should have matched",
                termNodeHelper.getAssemblerBrandList(assemblerResponse),
                termNodeHelper.getFacetBrandValues(searchAPIResponse)
        );
    }

    @And("^I get the column headers for specification attributes should be returned in same order as in facet list$")
    public void i_get_the_column_headers_specificationAttributes_from_the_searchAPIResponse_for() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> columnHeadersList = CommonHelpers.getRecordIds(searchApiResponse, "refinements.key");
        String psfId = searchApiResponse.path("category.id");
        List<String> removeList = new ArrayList<>();
        columnHeadersList.remove("I18NsearchBybrand");
        columnHeadersList.remove("I18NsearchByStockPolicy");
        columnHeadersList.remove("I18NIsNew");
        new StepsHelper().getl3CategorySearchResponse(psfId);
        List<String> categorySpecifAttributes = termNodeHelper.getCategoryProperties(ScenarioContext.getData(psfId));
        for (String facet : categorySpecifAttributes) {
            if (!columnHeadersList.contains(facet))
                removeList.add(facet);
        }
        categorySpecifAttributes.removeAll(removeList);
        assertEquals(
                "The column headers headers and the category specific attributes should have matched",
                categorySpecifAttributes,
                columnHeadersList
        );
    }

    @Then("^I get all the data layer attributes under queryInfo with search term (.+), (.+),(.+),(.+),(.+),(.+),(.+),(.+),(.+)$")
    public void i_get_attribute_values_from_searchAPI_response(String searchTerm, String pattern, String searchType,
                                                               String interfaceName, String matchMode, String cascade,
                                                               String autoCorrect, String spellCorrrectApp, String wildCarding) {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String searchStr = searchTerm;
        if (ScenarioContext.getSearchTermMap() != null && ScenarioContext.getSearchTermMap().get(searchTerm) != null)
            searchStr = ScenarioContext.getSearchTermMap().get(searchTerm);
        searchStr = searchStr.replaceAll("[+]", "");
        String searchKeyword = searchStr.replaceAll("%20", " ");
        String searchKeywordApp = "";
        if (searchType.equals("MPN"))
            searchKeywordApp = searchStr.replaceAll("%20", " ").replaceAll("-", "");
        else
            searchKeywordApp = searchStr.replaceAll("%20", " ");
        String spellCorrect = "false";
        if (!System.getProperty("country").equals("jp")) {
            spellCorrect = spellCorrrectApp;
        } else {
            autoCorrect = "false";
        }
        assertEquals(
                "The pagination count and result count should have matched",
                searchAPIResponse.path("resultsList.pagination.count").toString(),
                searchAPIResponse.path("queryInfo.results_count").toString()
        );
        assertEquals(
                "The search keyword/terms should have returned results (ids)",
                searchKeyword, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword").trim()
        );
        assertEquals(
                "Attribute cascade should have been found in the SearchAPI response",
                cascade, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_cascade_order")
        );
        assertEquals(
                "Attribute interfaceName should have been found in the SearchAPI response",
                interfaceName, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_interface_name")
        );
        assertEquals(
                "Attribute searchKeywordApp name should have been found in the SearchAPI response",
                searchKeywordApp, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword_app")
        );
        assertEquals(
                "Attribute matchMode should have been found in the SearchAPI response",
                matchMode, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_match_mode")
        );
        if (!searchTerm.equals("mpnValue"))
            assertEquals(
                    "Attribute search_pattern_matched should have been found in the SearchAPI response",
                    pattern, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_pattern_matched")
            );
        else if (searchAPIResponse.path("queryInfo.search_pattern_matched") != null)
            assertEquals(
                    "Attribute search_pattern_matched should have been found in the SearchAPI response",
                    pattern, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_pattern_matched")
            );
        else
            assertNull(
                    "Attribute search_pattern_matched should have contained a valid value",
                    searchAPIResponse.path("queryInfo.search_pattern_matched")
            );
        assertEquals(
                "Attribute autoCorrect should have been found in the SearchAPI response",
                autoCorrect, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_autocorrected")
        );
        assertEquals(
                "Attribute spellCorrect should have been found in the SearchAPI response",
                spellCorrect, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_spell_correct_applied")
        );
        assertEquals(
                "Attribute searchType should have been found in the SearchAPI response",
                searchType, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_search_type")
        );
        assertEquals(
                "Attribute wildCarding should have been found in the SearchAPI response",
                wildCarding, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_wild_carding_mode")
        );
    }

    @Then("^I check the autoCorrected keyword is null and spellCorrectApplied is true when a correct (.+), (.+) is provided$")
    public void i_check_autoCorrected_keyword_null_spellCorrectApplied_true(String searchTerm, String correctTerm) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String searchKeyWord = searchTerm.replaceAll("%20", " ");
        String misSpeltSearchTerm = searchApiResponse.path("queryInfo.search_keyword");
        String autoCorrectedTerm = searchApiResponse.path("queryInfo.search_keyword_spell_corrected");
        String interfaceName = searchApiResponse.path("queryInfo.search_interface_name");
        boolean searchAutoCorrected = Boolean.parseBoolean(searchApiResponse.path("queryInfo.search_autocorrected"));
        boolean spellCorrectApplied = Boolean.parseBoolean(searchApiResponse.path("queryInfo.search_spell_correct_applied"));
        if (!System.getProperty("country").equals("jp") && !"I18NRSStockNumber".equals(interfaceName)) {
            assertNull(
                    "The correct term should have been unchanged (country: not jp)",
                    autoCorrectedTerm
            );
            assertEquals(
                    "The search term should have remained unchanged (country: not jp)",
                    searchKeyWord.toUpperCase(), misSpeltSearchTerm.toUpperCase()
            );
            assertFalse(
                    "The autocorrection of misspelt term should have occurred (country: not jp)",
                    searchAutoCorrected
            );
            assertTrue(
                    "The spell correction of misspelt term should have been applied (country: not jp)",
                    spellCorrectApplied
            );
        } else {
            assertNotEquals(
                    "The correct term should have been unchanged (country: jp)",
                    correctTerm, autoCorrectedTerm
            );
            assertNull(
                    "The not autocorrected term should have been created (country: jp)",
                    autoCorrectedTerm
            );
            assertEquals(
                    "The search term should have remained unchanged (country: jp)",
                    searchKeyWord, misSpeltSearchTerm
            );
            assertFalse(
                    "The autocorrection of misspelt term should have NOT occurred (country: jp)",
                    searchAutoCorrected
            );
            assertFalse(
                    "The spell correction of misspelt term should have been NOT been applied (country: jp)",
                    spellCorrectApplied
            );
        }
    }

    @Given("^I request the search API with channelId as (.+) and clientId as (.+) for (.+)$")
    public void i_request_the_searchAPI_with_channelId_and_clientId(String channelId, String clientId, String psfId) {
        Response response = stepsHelper.getSAPIResponseWithTrackingParameters(channelId, clientId, psfId);
        ScenarioContext.setData(channelId, response);
        ScenarioContext.setIds(channelId);
    }

    @Then("^the search api should return (\\d+) statusCode$")
    public void the_searchAPI_should_return_desired_statusCode(int statusCode) {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        assertEquals(
                String.format(
                        "The SearchAPI request should have come back with the expected status code of '%s'", statusCode
                ),
                statusCode,
                response.getStatusCode()
        );
    }

    @Given("^the DYM suggestions (.+) that closely match the search term are returned$")
    public void the_DYM_suggestions_are_returned(String dymSuggestions) {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        String searchAlternatives = response.path("queryInfo.search_alternatives");
        assertEquals(
                "The dynamic suggestion should have closely matched the search term",
                dymSuggestions, searchAlternatives
        );
    }

    @Then("^the search related details (.+), (.+),(.+),(.+),(.+),(.+) (.+) are returned$")
    public void the_search_related_details_returned(String searchTerm, String cascade, String interfaceName,
                                                    String keywordApp, String matchMode,
                                                    String searchType, String searchResults) {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        assertEquals(
                "Search term should have been found in the SearchAPI response",
                searchTerm, response.path("queryInfo.search_keyword")
        );
        assertEquals(
                "Search keyword app should have been found in the SearchAPI response",
                keywordApp, response.path("queryInfo.search_keyword_app")
        );
        assertEquals(
                "Results count should have been found in the SearchAPI response",
                searchResults, response.path("queryInfo.results_count").toString()
        );
        assertEquals(
                "Interface name should have been found in the SearchAPI response",
                interfaceName, response.path("queryInfo.search_interface_name")
        );
        assertEquals(
                "Search cascade order should have been found in the SearchAPI response",
                cascade, response.path("queryInfo.search_cascade_order")
        );
        assertEquals(
                "Search type should have been found in the SearchAPI response",
                searchType, response.path("queryInfo.search_search_type")
        );
        assertEquals(
                "Match mode should have been found in the SearchAPI response",
                matchMode, response.path("queryInfo.search_match_mode")
        );
    }

    @Then("^the search related details (.+) (.+),(.+) (.+) are returned for adobe tagging$")
    public void the_search_related_details_returned_for_adobe_tagging(String pattern, String spellCorrectApplied,
                                                                      String wildCardMode, String interfaceName) {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        if (!"I18NRSStockNumber".equals(interfaceName)) {
            if (!System.getProperty("country").equals("jp")) {
                assertEquals(
                        "Spell corrected applied should have been found in the SearchAPI response",
                        spellCorrectApplied, response.path("queryInfo.search_spell_correct_applied"));
                assertEquals(
                        "Search pattern should have been found in the SearchAPI response",
                        pattern, response.path("queryInfo.search_pattern_matched"));
                assertEquals(
                        "Wild carding mode should have been found in the SearchAPI response",
                        wildCardMode, response.path("queryInfo.search_wild_carding_mode"));
            } else {
                assertNotEquals(
                        "Spell correct applied should have NOT been found in the SearchAPI response",
                        spellCorrectApplied, response.path("queryInfo.search_spell_correct_applied")
                );
            }
        }
    }

    @Then("^I get the search alternative offered attribute response as (.+) for search term (.+)$")
    public void I_get_the_search_alternative_attribute_response(String alternativeOffered, String searchTerm) {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        assertEquals(
                "Search alternative attribute should have been found in the SearchAPI response",
                alternativeOffered, response.path("queryInfo.search_alternative_offered")
        );
    }

    @Then("^I check if the product is commercially sensitive$")
    public void the_following_details_for_the_initial_products_should_be_displayed() {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = commonHelpers.getAssemblerResponse(response);
        termNodeHelper.assertCommerciallySensitiveProducts(response, assemblerResponse);
    }

    @Then("^I check if the search term returns products that are commercially sensitive with (.+)$")
    public void check_the_following_details_are_commercially_sensitive(String interfaceName) {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = stepsHelper.getAssemblerResponseForSearchResults(ScenarioContext.getIds(), "0", interfaceName, "20");
        termNodeHelper.assertCommerciallySensitiveProducts(response, assemblerResponse);
    }

    @Given("^I request search api with (.+) as search term belong to single L3 category$")
    public void i_request_search_api_with_a_search_term(String searchTerm) {
        stepsHelper.getSAPIResponseWithSearchQueryParam(UrlBuilder.getSearchTerm(searchTerm));
    }

    @Then("^the L3 category Details should be displayed for the records with a term (.+), (.+)$")
    public void the_l3_category_details_should_be_displayed(String searchTerm, String interfaceName) {
        String searchId = ScenarioContext.getIds();
        Response searchApiResponse = ScenarioContext.getData(searchId);
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchId, internalId, "0", interfaceName, "20");
        termNodeHelper.assertCategoryDetails(searchApiResponse, assemblerResponse);
    }

    @Then("^I get all the level three category ids with a min (\\d+) binCount$")
    public void i_get_all_the_level_three_category_ids(int binCount) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(searchApiResponse,
                "category.children.children.children.children");
        termNodeHelper.setL3CategoryId(levelThreeIds, binCount);
    }

    @Given("^I request search API end point for a Terminal Node with a term (.+)$")
    public void i_request_searchAPI_for_terminalNode_with_searchTerm(String term) {
        Map<String, String> searchTermMap = termNodeHelper.getSearchTerms(ScenarioContext.getData(ScenarioContext.getIds()));
        stepsHelper.getTerminalNodeWithSearchTermResponse(searchTermMap.get(term), ScenarioContext.getIds());
    }

    @Then("^the specification attribute details for initial list of 20 products should be displayed using a search term (.+), (.+)$")
    public void the_specificationAttribute_details_for_list_products(String searchTerm, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        String searchStr = ScenarioContext.getSearchTermMap().get(searchTerm);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchStr, internalId, "0", interfaceName, "20");
        termNodeHelper.assertTermNodePageAttributes(searchApiResponse, assemblerResponse);
    }

    @Given("^I request search API end point for Terminal Node with a term (.+) for data layer details$")
    public void i_request_searchAPI_terminalNode_with_searchTerm(String term) {
        Map<String, String> searchTermMap = termNodeHelper.getSearchTerms(ScenarioContext.getData(ScenarioContext.getIds()));
        if (!term.equals("mpnValue"))
            stepsHelper.getTerminalNodeWithSearchTermResponse(searchTermMap.get(term), ScenarioContext.getIds());
        else {
            String searchTerm = searchTermMap.get(term) + ";searchType=mpn";
            stepsHelper.getTerminalNodeWithSearchTermResponse(searchTerm, ScenarioContext.getIds());
        }
    }

    @Then("^L3 Category bin counts should not include excluded products for (.+), with (.+), for (.+)$")
    public void l3_category_bin_counts_should_not_include_excluded_products(String searchTermProp, String interfaceNameProp, String customerFilterProp) {
        String searchTerm = Page.getProperty(searchTermProp);
        String interfaceName = Page.getProperty(interfaceNameProp);
        String customerFilter = Page.getProperty(customerFilterProp);
        Response searchApiResponse = ScenarioContext.getData(customerFilter);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExclusionSearch(searchTerm, "0", interfaceName, "20");
        termNodeHelper.assertExcludedProductsNotReturnedL3BinCount(searchApiResponse, assemblerResponse);
        termNodeHelper.assertExcludedProductsNotReturnedWithIndividualL3BinCount(searchApiResponse, assemblerResponse);
    }

    @Then("^I check the spellCorrected keyword is null and spellCorrectApplied is true when a correct (.+) is provided$")
    public void i_check_spellCorrected_keyword_null_spellCorrectApplied_true(String searchTerm) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String searchKeyWord = searchTerm.replaceAll("%20", " ");
        String misSpeltSearchTerm = searchApiResponse.path("queryInfo.search_keyword");
        String autoCorrectedTerm = searchApiResponse.path("queryInfo.search_keyword_spell_corrected");
        String interfaceName = searchApiResponse.path("queryInfo.search_interface_name");
        boolean searchAutoCorrected = Boolean.parseBoolean(searchApiResponse.path("queryInfo.search_autocorrected"));
        boolean spellCorrectApplied = Boolean.parseBoolean(searchApiResponse.path("queryInfo.search_spell_correct_applied"));
        if (!System.getProperty("country").equals("jp") && !"I18NRSStockNumber".equals(interfaceName)) {
            assertNull(
                    "The correct term should have been unchanged (country: not jp)",
                    autoCorrectedTerm
            );
            assertEquals(
                    "The search term should have remained unchanged (country: not jp)",
                    searchKeyWord.toUpperCase(), misSpeltSearchTerm.toUpperCase()
            );
            assertFalse(
                    "The autocorrection of misspelt term should have occurred (country: not jp)",
                    searchAutoCorrected
            );
            assertTrue(
                    "The spell correction of misspelt term should have been applied (country: not jp)",
                    spellCorrectApplied
            );
        } else {
            assertNull(
                    "The not autocorrected term should have been created (country: jp)",
                    autoCorrectedTerm
            );
            assertEquals(
                    "The search term should have remained unchanged (country: jp)",
                    searchKeyWord, misSpeltSearchTerm
            );
            assertFalse(
                    "The autocorrection of misspelt term should have NOT occurred (country: jp)",
                    searchAutoCorrected
            );
            assertFalse(
                    "The spell correction of misspelt term should have been NOT been applied (country: jp)",
                    spellCorrectApplied
            );
        }
    }

    @Given("^I request search api with a (.+) with results belong to single L3 category$")
    public void i_request_search_api_with_search_term_resulting_l3_category(String searchTerm) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(searchApiResponse,
                "category.children.children.children.children");
        Map<String, String> searchTermMap = termNodeHelper.setL3CategorySearchTerms(levelThreeIds);
        stepsHelper.getSAPIResponseWithSearchQueryParam(searchTermMap.get(searchTerm));
    }

    @Then("^total number of record count should not include excluded products for (.+), with (.+), for (\\d+)$")
    public void total_number_of_record_count_should_not_include_excluded_products(String searchTermProp, String interfaceNameProp, String customerFilterIdProp) {
        String searchTerm = Page.getProperty(searchTermProp);
        String interfaceName = Page.getProperty(interfaceNameProp);
        String customerFilterId = Page.getProperty(customerFilterIdProp);
        Response searchApiResponse = ScenarioContext.getData(customerFilterId);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExclusionSearch(searchTerm, "0", interfaceName, "20");
        Integer assemblerLimit = assemblerResponse.path("'endeca:assemblerRequestInformation'.'endeca:numRecords'");
        Integer searchAPI = searchApiResponse.path("queryInfo.results_count");

        assertTrue("Assembler response bincount is same or less than SAPI", assemblerLimit > searchAPI);
    }

    @Then("^(.+) brand should not be returned in the results list for (.+), with (.+), for (.+)$")
    public void excluded_brands_should_not_be_returned_in_the_results_lists(
            String restrictedBrandProp, String searchTermProp, String interfaceNameProp, String customerFilterProp) {
        String restrictedBrand = Page.getProperty(restrictedBrandProp);
        String searchTerm = Page.getProperty(searchTermProp);
        String interfaceName = Page.getProperty(interfaceNameProp);
        String customerFilter = Page.getProperty(customerFilterProp);

        Response searchApiResponse = ScenarioContext.getData(customerFilter);
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExclusionSearch(searchTerm,"0", interfaceName,"20");
        Map<String, Integer> assemblerBrands = termNodeHelper.getAssemblerBrandBincounts(assemblerResponse);

        assertNotNull(
                "A restricted brand must be provided for comparison (Check property in gherkin example)",
                restrictedBrand);
        assertNotNull(
                String.format("Assembler query should include %s to make this a valid test", restrictedBrand),
                assemblerBrands.get(restrictedBrand));
        termNodeHelper.assertProductListDoesNotIncludeRestrictedBrandNames(searchApiResponse, restrictedBrand);
    }

    @Then("^I get all the level three category ids with the searchTerm (.+) and with a min (\\d+) binCount for customer (.+)$")
    public void i_get_all_the_level_three_category_ids_searchTerm(String searchTerm, int binCount, String customerFilter) {
            List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(ScenarioContext.getData(ScenarioContext.getIds()),
                "category.children.children.children.children");
        termNodeHelper.setLargestL3CategoryIdWithSearchTerm(levelThreeIds, binCount, searchTerm, customerFilter);
    }

    @Then("^I look for a L3 category with (.+), (.+) whose results list should not have excluded products from (.+)$")
    public void i_request_search_api_with_search_resulting_l3_category(String searchTerm, String interfaceName, String excludedBrand) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExcludedProductsSearch(searchTerm, internalId, "0", interfaceName, "20");

        Map<String, Integer> searchAPIBrands = termNodeHelper.getSAPIBrandBinCounts(searchApiResponse);
        Map<String, Integer> assemblerBrands = termNodeHelper.getAssemblerBrandBincounts(assemblerResponse);

        assertFalse(
                String.format(
                        "Search API response should not include results for %s brand when restrictions are applied",
                        excludedBrand
                ),
                searchAPIBrands.containsKey(excludedBrand));
        assertTrue(
                String.format(
                        "Assembler response without exclusions should include %s brand within result set", excludedBrand
                ),
                assemblerBrands.containsKey(excludedBrand));
    }

    @Then("^total number of record count should not include excluded products for terminal node page with (.+), (.+)$")
    public void total_number_of_record_cound_not_include_excluded_products(String searchTermProp, String interfaceNameProp) {
        String searchTerm = Page.getProperty(searchTermProp);
        String interfaceName = Page.getProperty(interfaceNameProp);
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExcludedProductsSearch(searchTerm, internalId, "0", interfaceName, "20");
        Integer assemblerLimit = assemblerResponse.path("'endeca:assemblerRequestInformation'.'endeca:numRecords'");
        Integer searchAPI = searchApiResponse.path("queryInfo.results_count");
        assertTrue("Assembler response bincount is same or less than SAPI", assemblerLimit > searchAPI);
    }

    @Then("^results list is sorted as per the search relevancy ranking set-up for search page with (.+), (.+)$")
    public void results_list_sorted_relevancy_reanking_setUp(String searchTerm, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = stepsHelper.getAssemblerResponseForSearchResults(searchTerm, "0", interfaceName, "20");
        termNodeHelper.assertSearchRelevancyRanking(searchApiResponse, assemblerResponse);
    }

    @Then("^results list is sorted as per the search relevancy ranking set-up for Terminal Node page with (.+), (.+)$")
    public void results_list_sorted_relevancy_reanking_setUp_terminal_node(String searchTerm, String interfaceName) {
       Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchTerm, internalId, "0", interfaceName, "20");
        termNodeHelper.assertSearchRelevancyRanking(searchApiResponse, assemblerResponse);
    }

    @Then("^Return the name and path of the rule been triggered in the data layer of search page with (.+), (.+)$")
    public void return_name_path_of_the_rule_fired_data_layer_search_page(String searchTerm, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String actualRuleFired = searchApiResponse.path("queryInfo.rulesFired.Exclusions");
        String expectedRulesTriggered = UrlBuilder.getExclusionsRuleFired();
        assertEquals(
                "actual rule fired does not match the expected rule fired", expectedRulesTriggered, actualRuleFired
        );
    }

    @Given("^I request search api with a valid search term and with redirects (.+)")
    public void i_request_search_api_with_a_valid_search_term(String redirects) {
        String searchTerm = UrlBuilder.getRedirectSearchTerm();
        if ("disabled".equals(redirects))
            searchTerm += UrlBuilder.getRedirectParams();
        stepsHelper.getSAPIResponseWithKeywordRedirect(searchTerm);
    }

    @Then("^I ensure the user queries return results and do not redirect users to other pages$")
    public void i_ensure_user_queries_return_results() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        int count = searchApiResponse.path("queryInfo.results_count");
        assertTrue(
                "The results count in the SearchAPI response should have been numeric",
                termNodeHelper.isNumber(String.valueOf(count))
        );
        assertNull("redirectUrl is found", searchApiResponse.path("redirectUrl"));
    }

    @Then("^I check if the user queries redirect users to other pages$")
    public void i_ensure_user_queries_does_not_return_results() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        assertNotNull(
                "SearchAPI response should have contained a redirect Url",
                searchApiResponse.path("redirectUrl")
        );
        assertNull("redirectUrl is not found", searchApiResponse.path("queryInfo.results_count"));
        assertEquals("Redirect", searchApiResponse.path("queryInfo.response_type"));
    }

    @Given("^I request search api with a searchTerm and brand dimension Id$")
    public void i_request_search_api_with_search_term_resulting_l3_category() throws Exception {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(searchApiResponse,
                "category.children.children.children.children");
        termNodeHelper.setSearchTermMap(levelThreeIds, 50);
    }

    @Given("^I check if the initial set of products are relevant to the search term and specific brand$")
    public void i_check_if_the_products_relevant_to_searchTerm_brand() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = termNodeHelper.getAssemblerResponseWithSearchTermAndDimensionId();
        termNodeHelper.assertTermNodePageAttributes(searchApiResponse, assemblerResponse);
    }

    @Then("^The search api bin count results should match the assembler response count$")
    public void the_search_api_count_should_match_with_assembler_count_details() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = termNodeHelper.getAssemblerResponseWithSearchTermAndDimensionId();
        Integer assemblerLimit = assemblerResponse.path("'endeca:assemblerRequestInformation'.'endeca:numRecords'");
        Integer searchAPI = searchApiResponse.path("queryInfo.results_count");
        assertEquals("Assembler response binCount is not equal to SAPI", assemblerLimit.intValue(), searchAPI.intValue());
    }

    @Then("^Products are returned as per the search relevancy ranking set-up$")
    public void products_sorted_relevancy_reanking_setUp() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = termNodeHelper.getAssemblerResponseWithSearchTermAndDimensionId();
        termNodeHelper.assertSearchRelevancyRanking(searchApiResponse, assemblerResponse);
    }

    @Then("^Return category details for all L2 categories that are part of the search result$")
    public void return_l2_category_details_from_search_results(String searchTerm, String interfaceName) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = termNodeHelper.getAssemblerResponseWithSearchTermAndDimensionId();
        termNodeHelper.assertExcludedProductsNotReturnedL2BinCount(searchApiResponse, assemblerResponse);
    }

    @Then("^Return category name and bin count for all L2 category that are part of search result$")
    public void return_categoryName_binCount_for_all_L2_category_that_are_part_of_search_result() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = termNodeHelper.getAssemblerResponseWithSearchTermAndDimensionId();
        termNodeHelper.assertCategoryDetailsForL2Category(searchApiResponse, assemblerResponse);
    }

    @Then("^Return category name, SEO URL and bin count for all L2 category that are part of search result$")
    public void return_categoryName_seoUrl_binCount_for_all_L2_category_that_are_part_of_search_result() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = termNodeHelper.getAssemblerResponseWithSearchTermAndDimensionId();
        termNodeHelper.assertCategoryDetailsForL3Category(searchApiResponse, assemblerResponse);
    }

    @Then("^I get all the data layer attributes under queryInfo with search term$")
    public void i_get_attribute_values_from_searchAPI_response() {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String searchStr = ScenarioContext.getSearchTermMap().get("searchTerm");
        String spellCorrect = "false";
        String autoCorrect = "false";
        if (!System.getProperty("country").equals("jp")) {
            spellCorrect = "true";
        } else {
            autoCorrect = "false";
        }
        assertEquals(
                "The pagination and results count values in the SearchAPI response should have matched",
                searchAPIResponse.path("resultsList.pagination.count").toString(),
                searchAPIResponse.path("queryInfo.results_count").toString()
        );
        assertEquals(
                "The search keyword value should have been found in the SearchAPI response",
                searchStr, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword")
        );
        assertEquals(
                "The search cascade order value should have been found in the SearchAPI response",
                "1", commonHelpers.getIds(searchAPIResponse, "queryInfo.search_cascade_order")
        );
        assertEquals(
                "The search interface name value should have been found in the SearchAPI response",
                "I18NSearchGeneric",
                commonHelpers.getIds(searchAPIResponse, "queryInfo.search_interface_name")
        );
        assertEquals(
                "The search keyword app value should have been found in the SearchAPI response",
                searchStr, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword_app")
        );
        assertEquals(
                "The search match mode value should have been found in the SearchAPI response",
                "matchallpartial", commonHelpers.getIds(searchAPIResponse, "queryInfo.search_match_mode")
        );
        assertEquals(
                "The search autocorrected flag should have been found in the SearchAPI response",
                autoCorrect, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_autocorrected")
        );
        assertEquals(
                "The search spell correct applied flag should have been found in the SearchAPI response",
                spellCorrect, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_spell_correct_applied")
        );
        assertEquals(
                "The search type flag should have been found in the SearchAPI response",
                "KEYWORD_SINGLE_ALPHA_NUMERIC",
                commonHelpers.getIds(searchAPIResponse, "queryInfo.search_search_type")
        );
        assertEquals(
                "The search wild carding mode flag should have been found in the SearchAPI response",
                "NONE", commonHelpers.getIds(searchAPIResponse, "queryInfo.search_wild_carding_mode")
        );
        int search_super_sections = termNodeHelper.getCategoryCount("search_super_sections", searchAPIResponse);
        int search_categories = termNodeHelper.getCategoryCount("search_categories", searchAPIResponse);
        assertEquals(
                "The search categories should have been found in the SearchAPI response",
                String.valueOf(search_categories),
                commonHelpers.getIds(searchAPIResponse, "queryInfo.search_categories")
        );
        assertEquals(
                "The search super sections should have been found in the SearchAPI response",
                String.valueOf(search_super_sections),
                commonHelpers.getIds(searchAPIResponse, "queryInfo.search_super_sections"));
    }

    @Given("^I check if the brand name for all the products P_brand property is equal to the brand filter provided$")
    public void i_get_the_brand_name_for_products_returned() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        termNodeHelper.assertBrandNameForReturnedProducts(searchApiResponse);
    }

    @Then("^the dimension ids of the applied attribute values match the assembler$")
    public void the_dimension_ids_of_the_applied_attribute_values_match_the_assembler() {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> facetsAppliedIdsList = response.path("queryInfo.facets_applied.id");

        String internalId = commonHelpers.getIds(response, "category.internalId");
        String descriptors = termNodeHelper.getAssemblerDescriptorsString();
        Response assemblerResponse = stepsHelper.getAssemblerResponseForAttrFilters(internalId, descriptors);
        List<String> analyticsList = termNodeHelper.getAssemblerAnalyticsList(assemblerResponse);
        assertEquals("Dimension ids of the applied attribute values should have matched the Assembler",
                analyticsList, facetsAppliedIdsList
        );
    }

    @And("^I get the attribute ids of the dimensions that are not part of the specification attributes for (.+)$")
    public void i_get_the_attribute_ids_of_the_dimensions_that_are_not_part_of_the_specification_attributes(String psfId) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        Map<String, List<String>> searchAPIProperties = termNodeHelper.getDimensionIds(searchApiResponse);

        Response assemblerResponse = commonHelpers.getAssemblerResponse(searchApiResponse);
        Map<String, List<String>> navigationStateMap = termNodeHelper.getAssemblerAttributeIdsFromSpecificDimensions(assemblerResponse);

        termNodeHelper.assertAttributeIdMaps(searchAPIProperties, navigationStateMap);
    }

    @And("^I query the applied dimensions for (.+)$")
    public void iQueryTheAppliedDimensionsForIsNew(String type) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        termNodeHelper.selectAppliedDimensionDetails(searchApiResponse);

        if (type.equals("IsNew")) {
            termNodeHelper.selectIsNewDimensionDetails(searchApiResponse);
        } else {
            termNodeHelper.selectLeadTimeDimensionDetails(searchApiResponse);
        }
        String internalIds = termNodeHelper.getSAPIIntenalIdsString();
        stepsHelper.getSAPIResponseForSelectedAttrFilters(ScenarioContext.getIds(), internalIds);
    }

    @And("^I verify the facet applied ids are correctly displayed for isNew$")
    public void iVerifyTheFacetAppliedIdsAreCorrectlyDisplayedForIsNew() {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(response, "category.internalId");
        String internalIds = termNodeHelper.getAssemblerInternalIdsString();
        Response assemblerResponse = stepsHelper.getAssemblerResponseForAttributeFilters(internalId, internalIds);
        List<String> facetsAppliedIds = response.path("queryInfo.facets_applied.id");
        List<String> expectedIds = Arrays.asList(UrlBuilder.getFacetAppliedIsNewIds().split(","));

        String id = "IsNew";
        assertTrue(
                String.format("%s was not among the reference (expected) Ids", id),
                expectedIds.contains(id)
        );
        assertTrue(
                "The target Ids list is invalid or empty",
                (facetsAppliedIds != null) && (facetsAppliedIds.size() > 0)
        );
        assertTrue(
                "Did not find the Id in the reference (expected) Ids list",
                expectedIds.contains(facetsAppliedIds.get(0))
        );
    }

    @And("^I get the attributes and its values for the applied filter for (.+)$")
    public void iGetTheAttributesAndItsValuesForTheAppliedFilterForPsfId(String psfId) throws Throwable {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        termNodeHelper.setSpecificAppliedDimensionDetails(searchApiResponse);
        String descriptors = termNodeHelper.getSAPIDescriptorsString();
        stepsHelper.getSAPIResponseForAttrFilters(psfId, descriptors);
        termNodeHelper.assertAppliedDimensions(ScenarioContext.getData(psfId));
    }

    @Then("^the attribute ids of the applied facets match the assembler$")
    public void the_attribute_ids_of_the_applied_facets_match_the_assembler() {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        List<String> attributeValuesIdsList = response.path("queryInfo.attribute_values.id");

        String internalId = commonHelpers.getIds(response, "category.internalId");
        String descriptors = termNodeHelper.getAssemblerDescriptorsString();

        Response assemblerResponse = stepsHelper.getAssemblerResponseForAttrFilters(internalId, descriptors);
        String assemblerUrl = internalId + descriptors;
        List<String> navStateList = termNodeHelper.getAssemblerNavigationAssemblerList(assemblerUrl, assemblerResponse);
        assertEquals(
                "The attribute ids of the applied facets should have matched the Assembler",
                navStateList, attributeValuesIdsList
        );
    }

    @And("^I verify the facet applied ids are correctly displayed for leadTime$")
    public void iVerifyTheFacetAppliedIdsAreCorrectlyDisplayedForLeadTime() {
        Response response = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = commonHelpers.getIds(response, "category.internalId");
        String internalIds = termNodeHelper.getAssemblerInternalIdsString();
        Response assemblerResponse = stepsHelper.getAssemblerResponseForAttributeFilters(internalId, internalIds);
        List<String> facetsAppliedIds = response.path("queryInfo.facets_applied.id");
        List<String> expectedIds = Arrays.asList(UrlBuilder.getFacetAppliedLeadTimeIds().split(","));
        assertEquals(
                "The facet applied ids should have been correctly displayed for leadTime",
                expectedIds, facetsAppliedIds
        );
    }

    @Then("^I apply dimension to a l3 category and I check for implicit refinements$")
    public void i_get_all_the_l3_category_ids() throws Exception {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(searchApiResponse,
                "category.children.children.children.children");
        assertTrue(
                "Level 3 implicit refinements should have been the expected count",
                termNodeHelper.hasImplicitRefinement(levelThreeIds, 50)
        );
    }

    @Then("^I check if the order of the implicit refinements follow the same logic as other refinements$")
    public void i_check_the_order_of_the_implicit_refinements() {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        assertTrue(
                "The implicit refinements should have been in the expected order",
                termNodeHelper.assertLabelOrder(searchAPIResponse)
        );
    }

    @Then("^Bin count for implicit refinement should match the total number of records in the result set$")
    public void binCounts_for_implicit_refinements_should_match_total_number_of_records() throws Exception {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        termNodeHelper.validateImplicitRefinementBinCounts(searchAPIResponse);
    }

    @Then("^I request search API without implicit refinements query so no implicit refinements are returned$")
    public void i_request_search_api_without_implicit_refinements_query() throws Exception {
        String psfId = ScenarioContext.getIds();
        termNodeHelper.setAppliedDimension(ScenarioContext.getData(psfId));
        String descriptors = termNodeHelper.getSAPIDescriptorsString();
        stepsHelper.getSAPIResponseForAttrFilters(psfId, descriptors);
        assertFalse(
                "implicit refinement found when implicit refinement param not passed",
                termNodeHelper.hasImplicitRefinement(ScenarioContext.getData(psfId))
        );
    }

    @Then("^I check if InternalId is returned for each top level dimension in refinement section$")
    public void i_check_for_InternalId_in_refinement_section() throws Exception {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(searchApiResponse,
                "category.children.children.children.children");
        assertNotNull(
                "InternalId should have been is returned for each of the top level dimension in refinement section",
                termNodeHelper.isInternalIdPresentInTopLevelDimension(levelThreeIds, 50)
        );
    }

    @Then("^I check if InternalId is returned for each top level dimension from breadbox$")
    public void i_get_ll_the_l3_category_ids() throws Exception {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<Map<String, String>> levelThreeIds = commonHelpers.getLevelThreeSearchApiResponse(searchApiResponse,
                "category.children.children.children.children");
        Response response = termNodeHelper.isInternalIdPresentInTopLevelDimension(levelThreeIds, 50);
        termNodeHelper.assertTopLevelInternalIdInBreadbox(response);
    }

    @Then("^I check if the keyword redirects are triggered for search term regardless of byte format they are entered$")
    public void i_check_if_the_keyword_redirect_are_triggered() {
        if (System.getProperty("country").equals("jp") || System.getProperty("country").equals("de")) {
            Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
            assertNotNull(
                    "SearchAPI response should have contained a redirect Url",
                    searchApiResponse.path("redirectUrl")
            );
            assertNull("redirectUrl is not found", searchApiResponse.path("queryInfo.results_count"));
            assertEquals("The response type should have been as expected",
                    "Redirect", searchApiResponse.path("queryInfo.response_type")
            );
        }
    }

    @Then("^I check no conversion is applied to byte format of the search term$")
    public void i_check_if_the_special_characters_have_been_removed() {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        assertEquals(
                "The search keywords should have remained unchanged in the SearchAPI response (search keyword)",
                ScenarioContext.getIds(),
                commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword").trim()
        );
        assertEquals(
                "The search keywords should have remained unchanged in the SearchAPI response (search keyword app)",
                ScenarioContext.getIds(), commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword_app")
        );
    }

    @Then("^I check the conversion is applied from double to Single (.+) on stock number pattern$")
    public void i_check_if_the_conversion_is_applied(String expectedTerm) {
        Response searchAPIResponse = ScenarioContext.getData(ScenarioContext.getIds());
        assertEquals(
                "The search keywords should have remained unchanged in the SearchAPI response (search keyword)",
                ScenarioContext.getIds(),
                commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword").trim()
        );
        assertEquals(
                "The search keywords should have remained unchanged in the SearchAPI response (search keyword app)",
                expectedTerm, commonHelpers.getIds(searchAPIResponse, "queryInfo.search_keyword_app")
        );
    }

    @Given("^I request search api with a search term of the stock pattern to check if there are any (.+) products$")
    public void i_request_search_api_with_search_term(String products) {
        String searchTerm;
        if ("discontinued".equals(products))
            searchTerm = UrlBuilder.getDiscontinuedProduct();
        else
            searchTerm = UrlBuilder.getProductionPackedProduct();
        stepsHelper.getSAPIResponseWithSearchQueryParam(searchTerm);
    }

    @Then("^I want production pack and discontinued products filtered from the search generic interface of the Stock pattern$")
    public void the_total_number_of_record_count_should_not_include_excluded_products() {
        String searchTerm = ScenarioContext.getIds();
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Response assemblerResponse = stepsHelper.getAssemblerResponseForExclusionSearch(searchTerm, "0", "I18NSearchGeneric", "20");
        Integer assemblerLimit = assemblerResponse.path("'endeca:assemblerRequestInformation'.'endeca:numRecords'");
        Integer searchAPI = searchApiResponse.path("queryInfo.results_count");
        assertEquals("Assembler response bincount is not same as SAPI", searchAPI, assemblerLimit);
        Response assemblerResponseWithDiscontProducts = stepsHelper.getFilterProductionPackedAssemblerResponse(searchTerm, "0", "I18NSearchGeneric", "20");
        Integer assemblerLimitWithDiscontProducts = assemblerResponseWithDiscontProducts.path("'endeca:assemblerRequestInformation'.'endeca:numRecords'");
        assertTrue("Assembler response bincount is not same as SAPI", searchAPI < assemblerLimitWithDiscontProducts);
    }
}
