package stepdefs.EndecaSearchAPI;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import pages.Page;
import pages.Search.SeachAPI.*;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductStepDefs {

    private static final int EXPECTED_OK_STATUS_CODE = 200;
    private static final int EXPECTED_404_ERROR_CODE = 404;
    private static final String SEARCHAPI_REQUEST_FAILURE_REASON =
            "The SearchAPI REST should have failed and come back with a 404 error code.";
    private final NaturalSearchHelpers naturalHelpers;
    private final StepsHelper stepsHelper;
    private final TerminalNodeHelper terminalNodeHelper;

    public ProductStepDefs(NaturalSearchHelpers naturalHelpers, StepsHelper stepsHelper, TerminalNodeHelper terminalNodeHelper) {
        this.naturalHelpers = naturalHelpers;
        this.stepsHelper = stepsHelper;
        this.terminalNodeHelper = terminalNodeHelper;
    }

    @Then("^I request search api with a product Id and validate Category hierarchy information$")
    public void i_request_searchAPI_with_a_productId() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Map<String, String> productMap = naturalHelpers.getProductId(searchApiResponse);
        assertNotNull("productId not found", productMap.get("productId"));
        Response response = stepsHelper.getSearchProductResponse(productMap.get("productId"));
        assertEquals(
                "The SearchAPI REST call was NOT successful, came back with an unexpected status code.",
                EXPECTED_OK_STATUS_CODE, response.getStatusCode()
        );
        ScenarioContext.setData(productMap.get("productId"), response);
        ScenarioContext.setIds(productMap.get("productId"));
        naturalHelpers.assertCategoryHierarchyInfo(response);
    }

    @Then("^When I request search api with an invalid product Id$")
    public void when_i_request_searchAPI_with_an_invalid_productId() {
        Response response = stepsHelper.getSearchProductResponse("invalidPId");
        ScenarioContext.setData("invalidPId", response);
        ScenarioContext.setIds("invalidPId");
        assertEquals(SEARCHAPI_REQUEST_FAILURE_REASON, EXPECTED_OK_STATUS_CODE, response.getStatusCode());
    }

    @Then("^I see if 404 error code is returned with an error message$")
    public void i_request_searchAPI_with_an_invalid_productId() {
        Response response = ScenarioContext.getData("invalidPId");
        assertEquals(SEARCHAPI_REQUEST_FAILURE_REASON, EXPECTED_404_ERROR_CODE, response.getStatusCode());
    }

    @Then("^I request search to get all the sibling categories of the L3 category that the provided product belongs to$")
    public void i_request_seFarchAPI_to_get_siblings_of_the_L3_category_for_a_given_productId() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        List<Map<String, String>> siblingsList = naturalHelpers.getCategorySiblings(searchApiResponse);
        Response productResponse = ScenarioContext.getData(ScenarioContext.getIds());
        naturalHelpers.assertSiblingCategoryDetails(productResponse, siblingsList);
    }

    @Then("^I check if sibling categories are returned in alpha numeric order$")
    public void i_check_siblings_categories_are_returned_in_alpha_numeric_order() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        naturalHelpers.getCategorySiblings(searchApiResponse);
        Response productResponse = ScenarioContext.getData(ScenarioContext.getIds());
        naturalHelpers.assertSiblingCategoryOrder(productResponse);
    }

    @Then("^I request search API with L1 (.+) and a search term$")
    public void i_get_all_the_L1_category_ids(String id) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Map<String, String> categoryMap = naturalHelpers.getCategoryDetails(searchApiResponse);
        String catId = "";
        String searchQuery = "";
        switch (id) {
            case "categoryId":
                catId = categoryMap.get("levelOneParent");
                searchQuery = categoryMap.get("levelOneLabel").split(" ")[0];
                stepsHelper.getTerminalNodeWithSearchTermResponse(searchQuery, catId);
                break;
            case "seoUrl":
                catId = categoryMap.get("levelOneSeoUrl");
                searchQuery = categoryMap.get("levelOneLabel").split(" ")[0];
                stepsHelper.getTerminalNodeWithSearchTermResponse(searchQuery, catId);
                break;
            case "internalId":
                catId = categoryMap.get("levelOneInternalId");
                searchQuery = categoryMap.get("levelOneLabel").split(" ")[0] + ";" + UrlBuilder.getNameSpaceParam() + "internalId";
                stepsHelper.getTerminalNodeWithSearchTermResponse(searchQuery, catId);
                break;
        }
    }

    @Then("^The result set should only include products belonging to specified category$")
    public void the_result_set_includes_specified_L1_category_products() {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        String internalId = searchApiResponse.path("category.internalId");
        Response assembler = stepsHelper.getAssemblerResponseForCategorySearch(ScenarioContext.getIds(), "I18NSearchGeneric", internalId);
        naturalHelpers.assertCategoryRecords(searchApiResponse, assembler);
    }

    @Then("^I request search API with L2 (.+) and a search term$")
    public void i_get_all_the_L2_category_ids(String id) {
        Response searchApiResponse = ScenarioContext.getData(ScenarioContext.getIds());
        Map<String, String> categoryMap = naturalHelpers.getCategoryDetails(searchApiResponse);
        String catId = "";
        String searchQuery = "";
        switch (id) {
            case "categoryId":
                catId = categoryMap.get("levelTwoParent");
                searchQuery = categoryMap.get("levelTwoLabel").split(" ")[0];
                stepsHelper.getTerminalNodeWithSearchTermResponse(searchQuery, catId);
                break;
            case "seoUrl":
                catId = categoryMap.get("levelTwoSeoUrl");
                searchQuery = categoryMap.get("levelTwoLabel").split(" ")[0];
                stepsHelper.getTerminalNodeWithSearchTermResponse(searchQuery, catId);
                break;
            case "internalId":
                catId = categoryMap.get("levelTwoInternalId");
                searchQuery = categoryMap.get("levelTwoLabel").split(" ")[0] + ";" + UrlBuilder.getNameSpaceParam() + "internalId";
                stepsHelper.getTerminalNodeWithSearchTermResponse(searchQuery, catId);
                break;
        }
    }

    @Given("^I send a request to the Search API alternatives end point for stock number (.+) with a limit of (\\d+)$")
    public void iSendARequestToTheSearchAPIAlternativesEndPointForStockNumberStockNoWithALimitOf(String stockNumber, int recordLimit) {
        String stockNumberWithAlternativeProduct = Page.getProperty(stockNumber);
        Response response = stepsHelper.getProductAlternativesResponse(stockNumberWithAlternativeProduct, recordLimit);
        ScenarioContext.setData(stockNumberWithAlternativeProduct, response);
        ScenarioContext.setIds(stockNumberWithAlternativeProduct);

    }

    @Then("^(\\d+) alternative record is returned in the Search API response for (.+)$")
    public void alternativeRecordIsReturnedInTheSearchAPIResponseForProduct(int recordLimit, String stockNumber) {
        String stockNumberWithAlternativeProduct = Page.getProperty(stockNumber);
        Response searchApiResponse = ScenarioContext.getData(stockNumberWithAlternativeProduct);
        List<String> recordsList = searchApiResponse.path("alternativesResultsList.records.id");
        assertEquals("an unexpected number of alternative records was returned", recordsList.size(), recordLimit);
    }

    @Then("^no alternatives are returned and an appropriate error message is returned for (.+)$")
    public void noAlternativesAreReturnedAndAnAppropriateErrorMessageIsReturnedForStockNumber(String stockNumber) {
        String stockNumberWithAlternativeProduct = Page.getProperty(stockNumber);
        Response searchApiResponse = ScenarioContext.getData(stockNumberWithAlternativeProduct);
        JsonNode response = terminalNodeHelper.parseStream(searchApiResponse.getBody().jsonPath().prettify());
        JsonNode recordsNode = response.findValue("alternativesResultsList");
        assertEquals("an unexpected number of alternative records was returned", recordsNode.asText(), "");
    }

    @Then("^an appropriate error message is returned for (.+)")
    public void anAppropriateErrorMessageIsReturnedForInvalidStockNumber(String stockNumber) {
        String stockNumberWithAlternativeProduct = Page.getProperty(stockNumber);
        Response searchApiResponse = ScenarioContext.getData(stockNumberWithAlternativeProduct);
        assertEquals(SEARCHAPI_REQUEST_FAILURE_REASON, EXPECTED_404_ERROR_CODE, searchApiResponse.getStatusCode());
    }

    @Then("^the search api should return (\\d+) statuscode$")
    public void theSearchApiShouldReturnStatuscode(int statusCode) {
        Response response = stepsHelper.getSearchProductResponse("invalidPId");
        ScenarioContext.setData("invalidPId", response);
        ScenarioContext.setIds("invalidPId");
        assertEquals(SEARCHAPI_REQUEST_FAILURE_REASON, statusCode, response.getStatusCode());
    }
}
