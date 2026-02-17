package pages.Search.SeachAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import pages.Page;
import pages.Search.SeachAPI.Models.TermNodeRecordModel;
import stepdefs.SharedDriver;

import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;
import static pages.Search.SeachAPI.CommonHelpers.getRecordIds;
import static pages.Search.SeachAPI.StepsHelper.getAssemblerResponseWithoutRefiniements;

public class NaturalSearchHelpers {
    private static final int EXPECTED_OK_STATUS_CODE = 200;
    private static CommonHelpers commonHelpers;
    private final Page page;
    private final StepsHelper stepsHelper;
    private final TerminalNodeHelper terminalNodeHelper;
    private final String assmeblerRecordPath = "mainContent[0].contents[0].records.attributes.P_recordID";


    public NaturalSearchHelpers(CommonHelpers commonHelpers, SharedDriver webDriver, StepsHelper stepsHelper, TerminalNodeHelper terminalNodeHelper) {
        page = new Page(webDriver) {
        };
        this.stepsHelper = stepsHelper;
        NaturalSearchHelpers.commonHelpers = commonHelpers;
        this.terminalNodeHelper = terminalNodeHelper;
    }

    /**
     * Helper method to return the total bin count of an assembler l2Category
     * <p>
     * i.e this method loops through all child catagories of a given l2 category
     * and returns the sum of the bin counts
     *
     * @param assemblerL2Category
     * @return sum of all l3 category bin counts
     */
    private static Integer getL2AssemblerTotalBinCount(Map.Entry<String, List<Map<String, String>>> assemblerL2Category) {
        return assemblerL2Category.getValue()
                .stream()
                .mapToInt(
                        l2Category -> Integer.parseInt(l2Category.get("binCount"))
                ).sum();
    }

    public Response getAssemblerSearchResponse(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        Response assemblerSearchResonce = null;
        if (searchTerm.equalsIgnoreCase("simplified.search.term")) {
            assemblerSearchResonce = StepsHelper.getAssemblerSearchGenericInterfaceResponse(searchQuery);
        } else if (searchTerm.equalsIgnoreCase("keyword.interface.term")) {
            assemblerSearchResonce = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        } else if (searchTerm.equalsIgnoreCase("simplified.search.term.cascade")) {
            assemblerSearchResonce = StepsHelper.getAssemblerSearchGenericCascadeInterfaceResponse(searchQuery);
        } else if (searchTerm.equalsIgnoreCase("catch.all.default.term")) {
            assemblerSearchResonce = StepsHelper.getAssemblerCatchAllDefaultInterfaceResponse(searchQuery);
        } else if (searchTerm.equalsIgnoreCase("keyword.search.term") || (searchTerm.equalsIgnoreCase("mpn.search.term"))) {
            assemblerSearchResonce = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        } else fail("Incorrect search config specified");
        return assemblerSearchResonce;
    }

    public void assertQueryInfoBlockValues(String searchInterface, String searchTerm) {
        if (searchInterface.equalsIgnoreCase("stock.number.interface.1")) {
            String searchQuery = Page.getProperty(searchTerm);
            Response searchApiResponse = ScenarioContext.getData(searchQuery);
            assertEquals(
                    "Should have found the interface name in the SearchAPI response",
                    "I18NRSStockNumber", getSearchInterfaceFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the cascade order in the SearchAPI response",
                    "1", getCascadeOrderFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the search type in the SearchAPI response",
                    "RS_STOCK_NUMBER", getSearchTypeFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the match mode in the SearchAPI response",
                    "matchall", getMatchModeFromApiResponse(searchApiResponse)
            );
        } else if (searchInterface.equalsIgnoreCase("stock.number.interface.2")) {
            String searchQuery = Page.getProperty(searchTerm);
            Response searchApiResponse = ScenarioContext.getData(searchQuery);
            assertEquals(
                    "Should have found the interface name in the SearchAPI response",
                    "I18NSearchGeneric", getSearchInterfaceFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the cascade order in the SearchAPI response",
                    "2", getCascadeOrderFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the search type in the SearchAPI response",
                    "RS_STOCK_NUMBER", getSearchTypeFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the match mode in the SearchAPI response",
                    "matchallpartial", getMatchModeFromApiResponse(searchApiResponse)
            );
        } else if (searchInterface.equalsIgnoreCase("keyword.interface")) {
            String searchQuery = Page.getProperty(searchTerm);
            Response searchApiResponse = ScenarioContext.getData(searchQuery);
            assertEquals(
                    "Should have found the interface name in the SearchAPI response",
                    "I18NSearchGeneric", getSearchInterfaceFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the cascade order in the SearchAPI response",
                    "1", getCascadeOrderFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the search type in the SearchAPI response",
                    "KEYWORD_SINGLE_ALPHA_NUMERIC", getSearchTypeFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the match mode in the SearchAPI response",
                    "matchallpartial", getMatchModeFromApiResponse(searchApiResponse)
            );
        } else if (searchInterface.equalsIgnoreCase("catch.all.default.interface")) {
            String searchQuery = Page.getProperty(searchTerm);
            Response searchApiResponse = ScenarioContext.getData(searchQuery);
            assertEquals(
                    "Should have found the interface name in the SearchAPI response",
                    "I18NSearchGeneric", getSearchInterfaceFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the cascade order in the SearchAPI response",
                    "1", getCascadeOrderFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the search type in the SearchAPI response",
                    "CATCH_ALL_DEFAULT", getSearchTypeFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the match mode in the SearchAPI response",
                    "matchallpartial", getMatchModeFromApiResponse(searchApiResponse)
            );
        } else if (searchInterface.equalsIgnoreCase("mpn.only.term")) {
            String searchQuery = Page.getProperty(searchTerm);
            Response searchApiResponse = ScenarioContext.getData(searchQuery);
            assertEquals(
                    "Should have found the interface name in the SearchAPI response",
                    "I18NManPartNumber", getSearchInterfaceFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the cascade order in the SearchAPI response",
                    "1", getCascadeOrderFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the search type in the SearchAPI response",
                    "MPN", getSearchTypeFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the match mode in the SearchAPI response",
                    "matchallpartial", getMatchModeFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the wild carding mode in the SearchAPI response",
                    "BOTH", getWildCardingModeFromApiResponse(searchApiResponse)
            );
            assertEquals(
                    "Should have found the spell correction in the SearchAPI response",
                    "false", getSpellCorrectionApplied(searchApiResponse)
            );
        } else {
            fail("No Search Interface Specified");
        }
    }

    private String getSpellCorrectionApplied(Response searchApiResponse) {
        return commonHelpers.getIds(searchApiResponse, "queryInfo.search_spell_correct_applied");
    }

    private String getWildCardingModeFromApiResponse(Response searchApiResponse) {
        return commonHelpers.getIds(searchApiResponse, "queryInfo.search_wild_carding_mode");
    }

    private String getSearchPatternMatchedFromApiResponse(Response searchApiResponse) {
        return commonHelpers.getIds(searchApiResponse, "queryInfo.search_pattern_matched");
    }

    private String getSearchInterfaceFromApiResponse(Response searchApiResponse) {
        return commonHelpers.getIds(searchApiResponse, "queryInfo.search_interface_name");
    }

    private String getCascadeOrderFromApiResponse(Response searchApiResponse) {
        return commonHelpers.getIds(searchApiResponse, "queryInfo.search_cascade_order");
    }

    private String getSearchTypeFromApiResponse(Response searchApiResponse) {
        return commonHelpers.getIds(searchApiResponse, "queryInfo.search_search_type");
    }

    private String getMatchModeFromApiResponse(Response searchApiResponse) {
        return commonHelpers.getIds(searchApiResponse, "queryInfo.search_match_mode");
    }

    public List<String> returnSearchApiRecords(String searchQuery) {
        Response searchApiResponse = ScenarioContext.getData(searchQuery);
        return getRecordIds(searchApiResponse, "resultsList.records.id");
    }

    public List<List<String>> getLocaleLifecycleStatusForExtraAssemblerRecords(String searchQuery) {
        Response assemblerResponse = getAssemblerResponseWithoutRefiniements(searchQuery);
        List<List<String>> localeLifecycleStatus = assemblerResponse.path("mainContent[0].contents[0].records.attributes.P_localeLifecycleStatus");
        return localeLifecycleStatus.subList(returnSearchApiRecords(searchQuery).size(), localeLifecycleStatus.size());
    }

    public void areSearchApiAndAssemblerApiRecordsEqual(List<String> searchApiRecordIds, List<List<String>> assemblerPRecordIds) {
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : assemblerPRecordIds)
            for (String id : idList)
                pRecordIds.add(id);
        assertEquals(
                "The records between SearchAPI and Assembler should have matched (same order)",
                pRecordIds, searchApiRecordIds
        );
    }

    public void assertSearchResultsPageDetails(Response searchApiResponse, Response assemblerResponse) {
        List<String> details = Arrays.asList(UrlBuilder.getSearchResultsAttributesList().split(","));
        List<TermNodeRecordModel> recordList = terminalNodeHelper.getTermNodeProperties(searchApiResponse);
        Map<String, Map<String, List<String>>> records = terminalNodeHelper.getAssemblerTermNodeProp(assemblerResponse);
        terminalNodeHelper.assertAssemblerRecordSize(recordList);
        assertSpecificationAttributesDisplayed(recordList, details, records);
    }

    private void assertSpecificationAttributesDisplayed(List<TermNodeRecordModel> recordList, List<String> details, Map<String, Map<String, List<String>>> records) {
        for (TermNodeRecordModel record : recordList) {
            for (String str : details) {
                Map<String, List<String>> assemblerRecord = records.get(record.getId());
                if (str.equals("brandURL") || str.equals("categoryURL") || str.equals("specificationAttributes")) {
                    Object value = str.equals("specificationAttributes") ? record.getSpecAttributes() : record.getPropertiesMap().get(str);
                    assertNotNull(
                            String.format("%s should have had a valid value", str),
                            value
                    );
                } else {
                    if (!str.equals("P_manufacturerPartNumber") || !assemblerRecord.get("P_commerciallySensitive").contains("Y"))
                        if ((!str.equals("P_imagePrimary") || record.getPropertiesMap().get(str) != null))
                            assertTrue(
                                    String.format(
                                            "The SearchAPI and Assembler property maps should have been the same, " +
                                                    "differs for '%s'", str
                                    ),
                                    assemblerRecord.get(str).contains(record.getPropertiesMap().get(str))
                            );
                }
            }
        }
    }

    public void assertRelevanceOfStockNumberResponse(Response searchApiResponse) {
        String expectedStockNumber = (Page.getProperty("expected.stock.no") + System.getProperty("country"));
        String recordId = searchApiResponse.path("resultsList.records[0].id");
        assertEquals("The record returned did not match the search term", expectedStockNumber, recordId);
    }

    public void assertL3CategoryDetailsDisplayed(Response searchApiResponse, Response assemblerResponse) {
        assertCategoryName(searchApiResponse, assemblerResponse);
        assertSeoUrl(searchApiResponse, assemblerResponse);
        assertBinCount(searchApiResponse, assemblerResponse);
    }

    private void assertBinCount(Response searchApiResponse, Response assemblerResponse) {
        List<Integer> desiredAssemblerList = assemblerResponse.path("headerContent[1].navigation[1].refinements.count");
        convertAssemblerBinCountToString(desiredAssemblerList);
        List<List<String>> apiBinCountList = searchApiResponse.path("refinements[0].value.categories.binCount");
        List<String> desiredApiList = new ArrayList<>();
        for (int i = 0; i < apiBinCountList.size(); i++) {
            List<String> searchApiSeoUrlNode = searchApiResponse.path("refinements[0].value[" + i + "].categories.binCount");
            for (String apiCategory : searchApiSeoUrlNode)
                desiredApiList.add(apiCategory);
        }
        assertDesiredApiAndAssemblerListsEqual(desiredApiList, convertAssemblerBinCountToString(desiredAssemblerList));
    }

    private List<String> convertAssemblerBinCountToString(List<Integer> desiredAssemblerList) {
        List<String> binCountList = new ArrayList<>();
        for (int count : desiredAssemblerList) {
            String binCount = String.valueOf(count);
            binCountList.add(binCount);
        }
        return binCountList;
    }

    private void assertSeoUrl(Response searchApiResponse, Response assemblerResponse) {
        List<String> desiredAssemblerList = assemblerResponse.path("headerContent[1].navigation[1].refinements.properties.SeoURL");
        List<List<String>> apiSeoUrlList = searchApiResponse.path("refinements[0].value.categories.categoryState.seoUrl");
        List<String> desiredApiList = new ArrayList<>();
        for (int i = 0; i < apiSeoUrlList.size(); i++) {
            List<String> searchApiSeoUrlNode = searchApiResponse.path("refinements[0].value[" + i + "].categories.categoryState.seoUrl");
            for (String apiCategory : searchApiSeoUrlNode)
                desiredApiList.add(apiCategory);
        }
        assertDesiredApiAndAssemblerListsEqual(desiredApiList, desiredAssemblerList);

    }

    private void assertCategoryName(Response searchApiResponse, Response assemblerResponse) {
        List<String> desiredAssemblerList = assemblerResponse.path("headerContent[1].navigation[1].refinements.label");
        List<List<String>> searchApiCategoryList = searchApiResponse.path("refinements[0].value.categories.label");
        List<String> desiredApiList = new ArrayList<>();
        for (int i = 0; i < searchApiCategoryList.size(); i++) {
            List<String> searchApiCategoryNode = searchApiResponse.path("refinements[0].value[" + i + "].categories.label");
            for (String apiCategory : searchApiCategoryNode)
                desiredApiList.add(apiCategory);
        }
        assertDesiredApiAndAssemblerListsEqual(desiredApiList, desiredAssemblerList);
    }

    private void assertDesiredApiAndAssemblerListsEqual(List<String> desiredApiList, List<String> desiredAssemblerList) {
        List<String> sortedApiSeoUrl = desiredApiList.stream().sorted().collect(Collectors.toList());
        List<String> sortedAssemblerSeoUrl = desiredAssemblerList.stream().sorted().collect(Collectors.toList());
        assertEquals(
                "The SEO Url lists between SearchAPI and Assembler should have been the same",
                sortedAssemblerSeoUrl, sortedApiSeoUrl
        );
    }

    public void assertRecordCountNotEqual(String searchQuery) {
        Response assemblerResponse = StepsHelper.getAssemblerResponseWithoutRefiniements(searchQuery);
        List<List<String>> assemblerRecords = assemblerResponse.path(assmeblerRecordPath);
        List<String> recordList = returnSearchApiRecords(searchQuery);
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : assemblerRecords)
            for (String id : idList)
                pRecordIds.add(id);
        assertNotEquals(
                "The product record ids between SearchAPI and Assembler should have matched",
                pRecordIds, recordList
        );
    }

    public void assertRecordCountEqual(String searchQuery) {
        Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        List<List<String>> assemblerRecords = assemblerResponse.path(assmeblerRecordPath);
        List<String> recordList = returnSearchApiRecords(searchQuery);
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : assemblerRecords)
            for (String id : idList)
                pRecordIds.add(id);
        assertEquals(
                "The product pack list or record counts between SearchAPI and Assembler should have been " +
                        "the same after applying customer filter to it",
                pRecordIds,
                recordList
        );
    }

    public void assertAdobeTagsPresent(Response searchApiResponse) {
        List<String> entryList = new ArrayList<>();
        Map<String, String> adobeTags = searchApiResponse.path("queryInfo");
        String[] details = UrlBuilder.getSearchResultsTaggingList().split(",");
        Set keys = adobeTags.keySet();
        entryList.addAll(keys);
        for (String adobeTag : details) {
            assertTrue(adobeTag + "tag was not present in details list", entryList.contains(adobeTag));
        }
    }

    public void assertL2CategoryDetailsDisplayed(Response searchApiResponse, Response assemblerResponse) {
        Map<String, List<Map<String, String>>> searchAPIProductCount = terminalNodeHelper.getL3CategoryBinCounts(searchApiResponse);
        Map<String, List<Map<String, String>>> assemblerProductCount = terminalNodeHelper.getAssemblerRefinementMenu(assemblerResponse);
        int searchApICount = 0;
        int assemblerCount = 0;
        for (Map.Entry<String, List<Map<String, String>>> entry : searchAPIProductCount.entrySet())
            for (Map<String, String> l3Cat : entry.getValue())
                searchApICount += Integer.parseInt(l3Cat.get("binCount"));
        for (Map.Entry<String, List<Map<String, String>>> entry : assemblerProductCount.entrySet()) {
            for (Map<String, String> l3Cat : entry.getValue())
                assemblerCount += Integer.parseInt(l3Cat.get("binCount"));
        }
        assertEquals(
                "Level 2 category details between SearchAPI and Assembler should have been the same",
                assemblerCount, searchApICount
        );
    }

    public void assertSearchResultsPageBrandDetails(Response searchApiResponse, Response assemblerResponse) {
        List<List<String>> assemblerBrandDetails = assemblerResponse.path("mainContent[0].contents[0].records.attributes.P_brand");
        assertBrandDetailsEqual(assemblerBrandDetails, searchApiResponse);
    }

    private void assertBrandDetailsEqual(List<List<String>> assemblerBrandDetails, Response searchApiResponse) {
        List<String> brandListFromSearchApi = getPropertyValues("P_brand", searchApiResponse);
        List<String> assemblerBrandList = new ArrayList<>();
        for (List<String> brand : assemblerBrandDetails) {
            for (String brandName : brand) {
                assemblerBrandList.add(brandName);
            }
        }
        assertEquals("The brand list's returned did not match", brandListFromSearchApi, assemblerBrandList);
    }

    public List<String> getPropertyValues(String attribute, Response searchApiResponse) {
        List<String> propertiesList = new ArrayList<>();
        JsonNode response = terminalNodeHelper.parseStream(searchApiResponse.getBody().jsonPath().prettify());
        List<JsonNode> childrenNode = response.findValue("records").findValues("properties");
        for (JsonNode propertyList : childrenNode) {
            for (JsonNode property : propertyList) {
                if (property.findValue("key").asText().equals(attribute)) {
                    propertiesList.add(property.get("value").asText());
                }
            }
        }
        return propertiesList;
    }

    public void assertCategorySeoDetails(Response searchApiResponse, Response assemblerResponse) {
        List<List<String>> sApiCategoryUrls = searchApiResponse.path("refinements[0].value.categories.categoryState.seoUrl");
        List<String> sAPICategoryDetailList = new ArrayList<>();
        List<String> assemblerApiCategoryUrls = assemblerResponse.path("headerContent[1].navigation[1].refinements.properties.SeoURL");
        for (List<String> categoryURL : sApiCategoryUrls) {
            sAPICategoryDetailList.addAll(categoryURL);
        }
        List<String> sortedApiSeoUrl = sAPICategoryDetailList.stream().sorted().collect(Collectors.toList());
        List<String> sortedAssemblerSeoUrl = assemblerApiCategoryUrls.stream().sorted().collect(Collectors.toList());
        assertEquals(
                "The Category SEO details between SearchAPI and Assembler should have been the same",
                sortedAssemblerSeoUrl, sortedApiSeoUrl
        );
    }

    public void assertCommonProductsFromRecordListEqualCombinedAttributeRecordList(List<String> recordList1, List<String> recordList2, List<String> combinedAttributeRecordList) {
        List<String> commonRecordsList = new ArrayList<>();
        for (String record : recordList1) {
            if (recordList2.contains(record)) {
                commonRecordsList.add(record);
            }
        }
        assertEquals("The combined records list was not equal to the records returned by the combined attribute term", commonRecordsList, combinedAttributeRecordList);
    }

    public void assertThatResponsesContainDesiredProduct(List<String> searchApiRecordList, Response assemblerResponse) {
        String desiredProductStockNumber = Page.getProperty("desired.product.stock.no") + "_" + System.getProperty("country");
        List<List<String>> assemblerRecordList = assemblerResponse.path(assmeblerRecordPath);
        assertTrue(
                "search api record list did not contain the desired product",
                searchApiRecordList.contains(desiredProductStockNumber)
        );
        List<String> assemblerRecords = new ArrayList<>();
        for (List<String> recordItem : assemblerRecordList) {
            assemblerRecords.addAll(recordItem);
        }
        assertTrue(
                "assembler record list did not contain desired product",
                assemblerRecords.contains(desiredProductStockNumber)
        );
    }

    public List<String> assertResultsListEqualAndReturnAssemblerRecordList(String searchTerm) {
        String searchQuery = Page.getProperty(searchTerm);
        List<String> searchApiRecordsList = returnSearchApiRecords(searchQuery);
        Response assemblerResponse = StepsHelper.getAssemblerKeywordInterfaceResponse(searchQuery);
        List<List<String>> assemblerRecordList = assemblerResponse.path(assmeblerRecordPath);
        List<String> assemblerRecords = new ArrayList<>();
        for (List<String> record : assemblerRecordList) {
            assemblerRecords.addAll(record);
        }
        assertEquals(
                "Records List were not equal in the Search API and Assembler Responses",
                searchApiRecordsList, assemblerRecords
        );
        return searchApiRecordsList;
    }

    public void assertAttributesContainLocalisedAttributes(Response searchApiResponse) {
        String localisedAttributeTerm = Page.getProperty("localised.attribute.unit.value");
        String productLabel = searchApiResponse.path("resultsList.records[0].label");
        productLabel = productLabel.replaceAll(" ", "");

        assertTrue(
                String.format(
                        "The localised attribute term %s was not found in the product description",
                        localisedAttributeTerm
                ),
                productLabel.contains(localisedAttributeTerm)
        );
    }

    public void assertSortOption(Response searchApiResponse, Response assemblerResponse) {
        List<String> assemblerSortOptions = assemblerResponse.path("mainContent[0].contents[0].sortOptions.label");
        List<String> searchAPISortOptions = searchApiResponse.path("resultsList.sortOptions.label");
        assertEquals(
                "The Search API and Assembler Sort Options did not match", assemblerSortOptions, searchAPISortOptions
        );
    }

    public void assertTopLevelAttributes(Response searchApiResponse) {
        assertNotNull("Breadbox should have been found in the SearchAPI response", searchApiResponse.path("breadbox"));
        assertNotNull("Refinements should have been found in the SearchAPI response", searchApiResponse.path("refinements"));
        assertNotNull("Results list should have been found in the SearchAPI response", searchApiResponse.path("resultsList"));
        assertNotNull("Queryinfo should have been found in the SearchAPI response", searchApiResponse.path("queryInfo"));
    }

    public void assertResultsCount(Response searchApiResponse, Response assemblerResponse) {
        Integer assemblerCount = assemblerResponse.path("mainContent[0].contents[0].totalNumRecs");
        Integer searchApiCount = searchApiResponse.path("resultsList.pagination.count");
        assertEquals(
                "The pagination count and total number of records between SearchAPI and Assembler should have matched",
                assemblerCount, searchApiCount
        );
    }

    public int sapiLastPageCount(Response searchApiResponse) {
        return searchApiResponse.path("resultsList.pagination.lastPage");
    }

    public void assertSapiElementCountGreaterThan(Response searchApiResponse, String xpath, int listNumber) {
        int resultList = searchApiResponse.getBody().prettyPeek().jsonPath().get(xpath);
        assertTrue(
                "The result list should have defaulted to an expected number of items",
                resultList > listNumber
        );
    }

    public void assertSapiElementCountEqual(Response searchApiResponse, String xpath, int listNumber) {
        int resultList = searchApiResponse.getBody().prettyPeek().jsonPath().get(xpath);
        assertEquals(
                "The result list should have defaulted to an expected number of items",
                resultList, listNumber
        );
    }

    public void assertSapiCountFromListLessOrEqual(Response searchApiResponse, String xpath, int listNumber) {
        List<String> responseList = searchApiResponse.getBody().prettyPeek().jsonPath().getList(xpath);
        int resultList = responseList.size();
        assertTrue(
                "The result list should have defaulted to an expected number of items",
                resultList <= listNumber
        );
    }

    public String sapiSortByIndex(Response searchApiResponse, String xpath, int IndexNumber) {
        List<String> SortOptionList = searchApiResponse.getBody().prettyPeek().jsonPath().getList(xpath);
        return SortOptionList.get(IndexNumber);
    }

    public String SapiSortByIndexByConvertingBooleanToString(Response searchApiResponse, String xpath, int IndexNumber) {
        List<String> SortOptionList = searchApiResponse.getBody().prettyPeek().jsonPath().getList(xpath);
        return String.valueOf(SortOptionList.get(IndexNumber));
    }

    public List<String> sapiListElements(Response searchApiResponse, String xpath) {
        return searchApiResponse.getBody().prettyPeek().jsonPath().getList(xpath);
    }

    public List<List<String>> sapiGetListOfElements(Response searchApiResponse, String xpath) {
        return searchApiResponse.getBody().prettyPeek().jsonPath().getList(xpath);
    }

    public String sapiGetElement(Response searchApiResponse, String xpath) {
        return searchApiResponse.path(xpath);
    }

    public void assertSapiRelevancePriceorder(Response searchApiResponse) {
        List<List<String>> recordsLists = sapiGetListOfElements(searchApiResponse, "resultsList.records.properties.value");
        List<String> priceList = recordsLists.stream().map(subList -> subList.get(0)).collect(Collectors.toList());
        assertFalse("price are in order", priceList.stream().sequential().isParallel());
    }

    public void assertSapiAscendingPricesOrderList(Response searchApiResponse) {
        String firstPriceValue = sapiSortByIndex(searchApiResponse, "resultsList.records[1].properties.value", 0);
        String fifthPriceValue = sapiSortByIndex(searchApiResponse, "resultsList.records[5].properties.value", 0);
        double price1 = Double.parseDouble(firstPriceValue);
        double price2 = Double.parseDouble(fifthPriceValue);
        assertTrue("The prices are not in ascending order", price1 < price2);
    }

    public void assertSapiDescendingPricesOrderList(Response searchApiResponse) {
        String firstPriceValue = sapiSortByIndex(searchApiResponse, "resultsList.records[1].properties.value", 0);
        String secondPriceValue = sapiSortByIndex(searchApiResponse, "resultsList.records[3].properties.value", 0);
        double price1 = Double.parseDouble(firstPriceValue);
        double price2 = Double.parseDouble(secondPriceValue);
        assertTrue("The prices are not in ascending order", price1 > price2);
    }

    public void assertPageCount(Response searchApiResponse, int pageNumber) {
        Integer pageNumberSAPI = pageNumber;
        Integer searchApiPageCount = searchApiResponse.path("resultsList.pagination.page");
        Integer searchApiMaxPageCount = searchApiResponse.path("resultsList.pagination.lastPage");
        assertEquals(
                "The max page count (or last page number) for SearchAPI should have matched",
                pageNumberSAPI, searchApiMaxPageCount
        );
        assertEquals(
                "The page count for SearchAPI should have matched", pageNumberSAPI, searchApiPageCount
        );
    }

    public void assertPositionOfBoostedProduct(Response searchApiResponse, String boostedProduct) {
        String firstRecord = searchApiResponse.path("resultsList.records[0].id");
        String boostedRecord = Page.getProperty(boostedProduct) + "_" + System.getProperty("country");
        assertEquals("The first record returned did not match the boosted record in XM", firstRecord, boostedRecord);
    }

    public void assertBoostRuleFired(Response searchApiResponse, String xmRule) {
        String resultsListRuleFired = searchApiResponse.path("queryInfo.rulesFired.ResultsList");
        assertTrue("The XM boost rule was not triggered for this search API response", resultsListRuleFired.contains(xmRule));
    }

    public void assertSearchKeywordMatchesSearchTerm(Response searchApiResponse, String searchTerm) {
        String searchKeyword = searchApiResponse.path("queryInfo.search_keyword_app");
        String autocorrectApplied = searchApiResponse.path("queryInfo.search_autocorrected");

        assertNotEquals(
                "The autocorrect applied flag should have been enabled", "true", autocorrectApplied
        );
        assertTrue(
                "The search keyword applied does not match the input search term",
                searchKeyword
                        .replaceAll(" ", "")
                        .equalsIgnoreCase(
                                searchTerm.replaceAll("%20", "")
                        ));
    }

    public Map<String, String> getProductId(Response searchAPI) {
        JsonNode response = terminalNodeHelper.parseStream(searchAPI.getBody().jsonPath().prettify());
        JsonNode childrenNode = response.findValue("category").get("children");
        Map<String, String> productMap = new HashMap<>();
        String psfId;
        String productId;
        for (JsonNode bookNode : childrenNode) {
            JsonNode levelOneArrNode = bookNode.get("children");
            for (JsonNode l1Node : levelOneArrNode) {
                JsonNode levelTwoArrNode = l1Node.get("children");
                productMap.put("levelOneParent", l1Node.get("id").asText());
                productMap.put("levelOneSeoUrl", l1Node.get("seoUrl").asText());
                productMap.put("levelOneLabel", l1Node.get("label").asText());
                productMap.put("levelOneInternalId", l1Node.get("internalId").asText());
                for (JsonNode l2Node : levelTwoArrNode) {
                    JsonNode levelThreeArrNode = l2Node.get("children");
                    productMap.put("levelTwoParent", l2Node.get("id").asText());
                    productMap.put("levelTwoSeoUrl", l2Node.get("seoUrl").asText());
                    productMap.put("levelTwoLabel", l2Node.get("label").asText());
                    productMap.put("levelTwoInternalId", l2Node.get("internalId").asText());
                    for (JsonNode l3Node : levelThreeArrNode) {
                        psfId = l3Node.get("id").asText();
                        productMap.put("levelThreeId", psfId);
                        productMap.put("levelThreeSeoUrl", l3Node.get("seoUrl").asText());
                        productMap.put("levelThreeLabel", l3Node.get("label").asText());
                        productMap.put("levelThreeInternalId", l3Node.get("internalId").asText());
                        new StepsHelper().getSearchAPIPSFCategory(psfId);
                        Response termNodeResponse = ScenarioContext.getData(psfId);
                        List<TermNodeRecordModel> recordList = terminalNodeHelper.getTermNodeProperties(termNodeResponse);
                        for (TermNodeRecordModel record : recordList) {
                            Map<String, String> propertyMap = record.getPropertiesMap();
                            if (propertyMap.get("P_groupNbr") != null) {
                                productId = propertyMap.get("P_groupNbr");
                                productMap.put("productId", productId);
                                break;
                            }
                        }
                        if (productMap.get("productId") != null) {
                            ScenarioContext.setSearchTermMap(productMap);
                            return productMap;
                        }
                    }
                }
            }
        }
        return productMap;
    }

    public void assertCategoryHierarchyInfo(Response searchAPI) {
        String psfId = searchAPI.path("category.id");
        String levelThreeSeoUrl = searchAPI.path("category.seoUrl");
        String levelThreeLabel = searchAPI.path("category.label");
        String pssId = searchAPI.path("category.parent.id");
        String levelTwoSeoUrl = searchAPI.path("category.parent.seoUrl");
        String levelTwoLabel = searchAPI.path("category.parent.label");
        String psssId = searchAPI.path("category.parent.parent.id");
        String levelOneSeoUrl = searchAPI.path("category.parent.parent.seoUrl");
        String levelOneLabel = searchAPI.path("category.parent.parent.label");
        Map<String, String> productCatMap = ScenarioContext.getSearchTermMap();
        assertEquals(
                "Level 3 id should have matched between the product category and SearchAPI response",
                productCatMap.get("levelThreeId"), psfId
        );
        assertEquals(
                "Level 2 parent should have matched between the product category and SearchAPI response",
                productCatMap.get("levelTwoParent"), pssId
        );
        assertEquals(
                "Level 1 parent should have matched between the product category and SearchAPI response",
                productCatMap.get("levelOneParent"), psssId
        );
        assertEquals(
                "Level 1 SEO Url should have matched between the product category and SearchAPI response",
                productCatMap.get("levelOneSeoUrl"), levelOneSeoUrl
        );
        assertEquals(
                "Level 2 SEO Url should have matched between the product category and SearchAPI response",
                productCatMap.get("levelTwoSeoUrl"), levelTwoSeoUrl
        );
        assertEquals(
                "Level 3 SEO Url should have matched between the product category and SearchAPI response",
                productCatMap.get("levelThreeSeoUrl"), levelThreeSeoUrl
        );
        assertEquals(
                "Level 1 label should have matched between the product category and SearchAPI response",
                productCatMap.get("levelOneLabel"), levelOneLabel
        );
        assertEquals(
                "Level 2 label should have matched between the product category and SearchAPI response",
                productCatMap.get("levelTwoLabel"), levelTwoLabel
        );
        assertEquals(
                "Level 3 label should have matched between the product category and SearchAPI response",
                productCatMap.get("levelThreeLabel"), levelThreeLabel
        );
    }

    public List<Map<String, String>> getCategorySiblings(Response searchAPI) {
        JsonNode response = terminalNodeHelper.parseStream(searchAPI.getBody().jsonPath().prettify());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode childrenNode = response.findValue("category").get("children");
        List<Map<String, String>> siblingsMap = new ArrayList<>();
        String psfId = "";
        String productId = "";
        for (JsonNode bookNode : childrenNode) {
            JsonNode levelOneArrNode = bookNode.get("children");
            for (JsonNode l1Node : levelOneArrNode) {
                JsonNode levelTwoArrNode = l1Node.get("children");
                for (JsonNode l2Node : levelTwoArrNode) {
                    JsonNode levelThreeArrNode = l2Node.get("children");
                    for (JsonNode l3Node : levelThreeArrNode) {
                        if (productId.equals("")) {
                            psfId = l3Node.get("id").asText();
                            new StepsHelper().getSearchAPIPSFCategory(psfId);
                            Response termNodeResponse = ScenarioContext.getData(psfId);
                            List<TermNodeRecordModel> recordList = terminalNodeHelper.getTermNodeProperties(termNodeResponse);
                            for (TermNodeRecordModel record : recordList) {
                                Map<String, String> propertyMap = record.getPropertiesMap();
                                if (propertyMap.get("P_groupNbr") != null) {
                                    productId = propertyMap.get("P_groupNbr");
                                    ScenarioContext.setIds(productId);
                                    Response searchAPIResponse = stepsHelper.getSearchProductResponse(productId);
                                    assertEquals(
                                            "SearchAPI should return a 200 status code",
                                            EXPECTED_OK_STATUS_CODE, searchAPIResponse.getStatusCode()
                                    );
                                    ScenarioContext.setData(productId, searchAPIResponse);
                                    break;
                                }
                            }
                        }
                        if (!"".equals(productId) && !psfId.equals(l3Node.get("id").asText())) {
                            Map<String, String> result = mapper.convertValue(l3Node, Map.class);
                            siblingsMap.add(result);
                        }
                    }
                    return siblingsMap;
                }
            }
        }
        return siblingsMap;
    }

    public void assertSiblingCategoryDetails(Response searchAPI, List<Map<String, String>> siblingsMap) {
        Set<String> actualIdSet = new HashSet<>();
        Set<String> expectedIdSet = new HashSet<>();
        JsonNode response = terminalNodeHelper.parseStream(searchAPI.getBody().jsonPath().prettify());
        JsonNode siblingsNode = response.findValue("category").get("siblings");
        for (JsonNode siblingNode : siblingsNode) {
            boolean siblingFound = false;
            String psfId = siblingNode.get("id").asText();
            assertTrue(
                    String.format(
                            "Sibling category list should NOT have had the specific category (%s) already present", psfId
                    ),
                    actualIdSet.add(psfId)
            );
            for (Map<String, String> sibling : siblingsMap) {
                if (psfId.equals(sibling.get("id"))) {
                    siblingFound = true;
                    assertEquals(
                            "Sibling category id should have matched that of the sibling node",
                            sibling.get("id"), siblingNode.get("id").asText()
                    );
                    assertEquals(
                            "Sibling category label should have matched that of the sibling node",
                            sibling.get("label"), siblingNode.get("label").asText()
                    );
                    assertEquals(
                            "Sibling category SEO Category Name should have matched that of the sibling node",
                            sibling.get("seoCategoryName"), siblingNode.get("seoCategoryName").asText());
                    assertEquals(
                            "Sibling category SEO Url should have matched that of the sibling node",
                            sibling.get("seoUrl"), siblingNode.get("seoUrl").asText()
                    );
                    break;
                }
            }
            assertTrue(psfId + " sibling not found", siblingFound);
        }
        for (Map<String, String> sibling : siblingsMap) {
            expectedIdSet.add(sibling.get("id"));
        }
        assertEquals("There are more or less siblings then expected", expectedIdSet, actualIdSet);
    }

    public void assertSiblingCategoryOrder(Response searchAPI) {
        List<String> actualCategoryList = searchAPI.path("category.siblings.label");
        List<String> expectedCategoryList = searchAPI.path("category.siblings.label");
        if (expectedCategoryList != null) {
            Collections.sort(expectedCategoryList);
        }
        assertNotNull(
                "No siblings are present in the category, check if the requested category has sibling(s) set-up",
                actualCategoryList
        );
        assertEquals("There are more or less siblings then expected", expectedCategoryList, actualCategoryList);
    }

    public void assertMvtConfigID(Response searchApiResponse, int searchConfig) {
        Integer searchConfigNumber = searchConfig;
        Integer configID = Integer.parseInt(searchApiResponse.path("queryInfo.search_mvt_config_id"));
        assertEquals("The Search API Config ID applied did not match that passed in", searchConfigNumber, configID);
    }

    public Map<String, String> getCategoryDetails(Response searchAPI) {
        JsonNode response = terminalNodeHelper.parseStream(searchAPI.getBody().jsonPath().prettify());
        JsonNode childrenNode = response.findValue("category").get("children");
        Map<String, String> categoryMap = new HashMap<>();
        String psfId;
        for (JsonNode bookNode : childrenNode) {
            JsonNode levelOneArrNode = bookNode.get("children");
            for (JsonNode l1Node : levelOneArrNode) {
                if (l1Node.get("binCount").asInt() > 100) {
                    categoryMap.put("levelOneParent", l1Node.get("id").asText());
                    categoryMap.put("levelOneSeoUrl", l1Node.get("seoUrl").asText());
                    categoryMap.put("levelOneLabel", l1Node.get("label").asText());
                    categoryMap.put("levelOneInternalId", l1Node.get("internalId").asText());
                    JsonNode levelTwoArrNode = l1Node.get("children");
                    for (JsonNode l2Node : levelTwoArrNode) {
                        if (l2Node.get("binCount").asInt() > 100) {
                            categoryMap.put("levelTwoParent", l2Node.get("id").asText());
                            categoryMap.put("levelTwoSeoUrl", l2Node.get("seoUrl").asText());
                            categoryMap.put("levelTwoLabel", l2Node.get("label").asText());
                            categoryMap.put("levelTwoInternalId", l2Node.get("internalId").asText());
                            JsonNode levelThreeArrNode = l2Node.get("children");
                            for (JsonNode l3Node : levelThreeArrNode) {
                                if (l3Node.get("binCount").asInt() > 100) {
                                    psfId = l3Node.get("id").asText();
                                    categoryMap.put("levelThreeId", psfId);
                                    categoryMap.put("levelThreeSeoUrl", l3Node.get("seoUrl").asText());
                                    categoryMap.put("levelThreeLabel", l3Node.get("label").asText());
                                    categoryMap.put("levelThreeInternalId", l3Node.get("internalId").asText());
                                    ScenarioContext.setSearchTermMap(categoryMap);
                                    return categoryMap;
                                }
                            }
                        }
                    }
                }
            }
        }
        return categoryMap;
    }

    public void assertCategoryRecords(Response searchAPI, Response assemblerResponse) {
        List<String> assemblerPRecordIds = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        List<String> recordsList = searchAPI.path("resultsList.records.id");
        assertEquals(
                "The product record ids between SearchAPI and Assembler should have matched",
                assemblerPRecordIds, recordsList
        );
    }

    public void assertStockNumberPatternMatched(Response searchApiResponse, String searchQuery) {
        String searchTermApplied = searchApiResponse.path("queryInfo.search_keyword_app");
        String searchTermHyphenAndSpaceRemoved = removeRsPrefix(searchQuery).replace(" ", "").replace("-", "").replaceAll("%20", "");
        if (searchTermHyphenAndSpaceRemoved.length() < 7) {
            String searchTermFormatted = "0" + searchTermHyphenAndSpaceRemoved;
            assertEquals(
                    "The search term applied did not match the formatted search term",
                    searchTermApplied, searchTermFormatted
            );
        } else {
            assertEquals(
                    "The search term applied did not match the formatted search term",
                    searchTermApplied, searchTermHyphenAndSpaceRemoved
            );
        }
    }

    private String removeRsPrefix(String searchQuery) {
        if ((searchQuery.contains("RS")) || (searchQuery.contains("rs"))) {
            String searchTerm = searchQuery.replaceAll("RS", "").replaceAll("rs", "");
            return searchTerm;
        } else {
            String searchTerm = searchQuery;
            return searchTerm;
        }
    }

    public void assertMvtConfigWithNoSetup(Response searchApiResponse) {
        Integer configID = Integer.parseInt(searchApiResponse.path("queryInfo.search_mvt_config_id"));
        assertEquals(
                "The config returned was not the correct default config with no input parameters",
                0, (int) configID
        );
    }

    public void assertDoubleByteStockNoPattern(Response searchApiResponse, String singleByteSearchTerm) {
        String searchTermApplied = searchApiResponse.path("queryInfo.search_keyword_app");
        String singleByteTerm = Page.getProperty(singleByteSearchTerm);
        String searchTermHyphenAndSpaceRemoved = removeRsPrefix(singleByteTerm).replace(" ", "").replace("-", "").replaceAll("%20", "");
        if (searchTermHyphenAndSpaceRemoved.length() < 7) {
            String searchTermFormatted = "0" + searchTermHyphenAndSpaceRemoved;
            assertEquals(
                    "The search term applied did not match the formatted search term",
                    searchTermApplied, searchTermFormatted
            );
        } else {
            assertEquals(
                    "The search term applied did not match the formatted search term",
                    searchTermApplied, searchTermHyphenAndSpaceRemoved
            );
        }
    }

    public void assertAtrributesDisplayed(Response searchApiResponse) {
        String productLabel = searchApiResponse.path("resultsList.records[0].label");
        assertFalse("The product labels list should not have been empty", productLabel.isEmpty());
    }

    public void assertFirstRecordBrand(Response sapiResponse) {
        List<Map<String, String>> FirstRecordMap = sapiResponse.path("resultsList.records[0].properties");
        for (Map<String, String> map : FirstRecordMap) {
            for (Map.Entry<String, String> eachEntry : map.entrySet()) {
                String brand = "";
                if (eachEntry.getKey().equals("P_brand")) {
                    brand = eachEntry.getValue();
                    assertEquals("Brand should have been 'RS Pro', as expected", "RS Pro", brand);
                    break;
                }
            }
            break;
        }
    }

    public void assertSameSpeficicationAttributesAreReturned(List<Map<String, String>> thirdProductSpecAttributesMap, List<Map<String, String>> thirdProductFetchingSpecAttributesMap) {
        Map<String, String> intialMap = returnOrganisedMap(thirdProductSpecAttributesMap);
        Map<String, String> secondaryMap = returnOrganisedMap(thirdProductFetchingSpecAttributesMap);
        for (Map.Entry<String, String> entry : intialMap.entrySet()) {
            String key = entry.getKey();
            assertEquals(
                    String.format(
                            "The specifications of the products returned should have been the same, differs for key '%s'",
                            key
                    ),
                    entry.getValue(), secondaryMap.get(key)
            );
        }
    }

    private Map<String, String> returnOrganisedMap(List<Map<String, String>> thirdProductSpecAttributesMap) {
        Map<String, String> finalMap = new HashMap<>();
        String key = "";
        String value = "";
        for (Map<String, String> map : thirdProductSpecAttributesMap) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("key")) {
                    key = entry.getValue();
                } else {
                    value = entry.getValue();
                }
            }
            finalMap.put(key, value);
        }
        return finalMap;
    }

    public void assertExcludedProductsNotReturnedWithIndividualL2BinCounts(Response searchAPI, Response assembler) {
        Map<String, String> searchAPICategories = terminalNodeHelper.getL2CategoryBinCounts(searchAPI);
        Map<String, List<Map<String, String>>> assemblerCategories = terminalNodeHelper.getAssemblerRefinementMenu(assembler);

        Boolean l2CategoryHasLowerBinCount = doesSapiL2CategoriesHaveLowerBinCountThanAssembler(
                searchAPICategories, assemblerCategories);

        assertTrue(
                "At least one searchAPI L2 category should have less products than assembler",
                l2CategoryHasLowerBinCount
        );
    }

    /**
     * Helper method to loop through all sapi l2 categories and compare against its equivalent assembler category.
     *
     * @param searchAPICategories
     * @param assemblerCategories
     * @return true if at least one l2 sapi category has less than assembler equivalent
     */
    private Boolean doesSapiL2CategoriesHaveLowerBinCountThanAssembler(
            Map<String, String> searchAPICategories, Map<String, List<Map<String, String>>> assemblerCategories) {

        return searchAPICategories.entrySet()
                .stream()
                .anyMatch(sapiCategory -> {

                    String sapiL2CategoryLabel = sapiCategory.getKey();
                    Integer sapiL2CategoryBinCount = Integer.valueOf(sapiCategory.getValue());

                    return doesAssemblerL2CategoryHasLargerBinCountThanSapiCategory(assemblerCategories, sapiL2CategoryLabel, sapiL2CategoryBinCount);
                });
    }

    /**
     * Helper method which loops through assembler categories to find the category matching the label provided.
     * It then returns if the assembler bin count is higher than the bin count provided
     *
     * @param assemblerCategories
     * @param categoryLabel
     * @param categoryBinCount
     * @return assembler l2 category has higher bin count than one provided
     */
    private Boolean doesAssemblerL2CategoryHasLargerBinCountThanSapiCategory(
            Map<String, List<Map<String, String>>> assemblerCategories,
            String categoryLabel, Integer categoryBinCount) {
        Optional<Boolean> equivalentL2CategoryHasLargerBinCountThanSapi = assemblerCategories.entrySet()
                .stream()
                .filter(assemblerL2Category -> assemblerL2Category.getKey().equals(categoryLabel))
                .findFirst()
                .map(equivalentL2Category -> {
                    Integer assemblerL2CategoryCount = getL2AssemblerTotalBinCount(equivalentL2Category);
                    return categoryBinCount < assemblerL2CategoryCount;
                });

        return equivalentL2CategoryHasLargerBinCountThanSapi.isPresent()
                && equivalentL2CategoryHasLargerBinCountThanSapi.get();
    }
}
