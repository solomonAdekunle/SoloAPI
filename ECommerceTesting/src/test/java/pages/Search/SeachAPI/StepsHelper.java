package pages.Search.SeachAPI;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import pages.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class StepsHelper {

    private static final int EXPECTED_OK_STATUS_CODE = 200;

    public static Response getAssemblerKeywordInterfaceResponse(String searchQuery) {
        String QueryParam = UrlBuilder.getAssemblerKeywordInterfaceParam() + UrlBuilder.getOffsetParam() + 0 + UrlBuilder.getNrrpParameter()
                + 20 + UrlBuilder.getNtxParam() + "mode%20matchallpartial"
                + UrlBuilder.getNtkParam() + "I18NSearchGeneric" + UrlBuilder.getSpellParam() + 1 + UrlBuilder.getAssemblerSearchTermParam() +
                searchQuery + UrlBuilder.getInternalIdParam() + UrlBuilder.getLocaleDimensionParam() + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(QueryParam);
        String psfBaseUri = UrlBuilder.getAssemblerSearchBaseURI().toString();
        return getResponse(queryParams, psfBaseUri);
    }

    private static String defaultUserSegment() {
        return UrlBuilder.getUserSegmentParam();
    }

    private static String getCustomerFilterParams() {
        return UrlBuilder.getSearchTermCustomerFilter();

    }

    public static Response getAssemblerSearchGenericCascadeInterfaceResponse(String searchQuery) {
        String QueryParam = UrlBuilder.getAssemblerCascadeInterfaceParam() + UrlBuilder.getOffsetParam() + 0 +
                UrlBuilder.getNrrpParameter() + 20 + UrlBuilder.getNtxParam() + "mode%20matchallpartial"
                + UrlBuilder.getNtkParam() + "I18NSearchGeneric" + UrlBuilder.getSpellParam() + 1 +
                UrlBuilder.getAssemblerSearchTermParam() + searchQuery + UrlBuilder.getInternalIdParam() +
                UrlBuilder.getLocaleDimensionParam() + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(QueryParam);
        String psfBaseUri = UrlBuilder.getAssemblerSearchBaseURI().toString();
        return getResponse(queryParams, psfBaseUri);
    }

    public static Response getAssemblerResponseWithoutRefiniements(String searchQuery) {
        String QueryParam = UrlBuilder.getAssemblerCascadeInterfaceParamWithoutRecordFilter() + UrlBuilder.getOffsetParam()
                + 0 + UrlBuilder.getNrrpParameter() + 20 + UrlBuilder.getDiscontinuedAndPackTypeRecordFields() + UrlBuilder.getNtxParam() + "mode%20matchallpartial"
                + UrlBuilder.getNtkParam() + "I18NSearchGeneric" + UrlBuilder.getSpellParam() + 1 + UrlBuilder.getAssemblerSearchTermParam() +
                searchQuery + UrlBuilder.getInternalIdParam() + UrlBuilder.getLocaleDimensionParam() + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(QueryParam);
        String psfBaseUri = UrlBuilder.getAssemblerSearchBaseURI().toString();
        return getResponse(queryParams, psfBaseUri);
    }

    public static Response getAssemblerCatchAllDefaultInterfaceResponse(String searchQuery) {
        String QueryParam = UrlBuilder.getAssemblerCatchAllInterfaceParam() + UrlBuilder.getOffsetParam() + 0 + UrlBuilder.getNrrpParameter()
                + 20 + UrlBuilder.getNtkParam() + "I18NSearchGeneric" + UrlBuilder.getSpellParam() + 1 + UrlBuilder.getAssemblerSearchTermParam()
                + searchQuery + UrlBuilder.getInternalIdParam() + UrlBuilder.getLocaleDimensionParam() + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(QueryParam);
        String psfBaseUri = UrlBuilder.getAssemblerSearchBaseURI().toString();
        return getResponse(queryParams, psfBaseUri);
    }

    public static Response getAssemblerSearchGenericInterfaceResponse(String searchQuery) {
        String queryParam = UrlBuilder.getAssemblerGenericInterfaceParam() + UrlBuilder.getOffsetParam() + 0 + UrlBuilder.getNrrpParameter()
                + 20 + UrlBuilder.getNtxParam() + "mode%20matchall" + UrlBuilder.getNtkParam() + "I18NRSStockNumber" +
                UrlBuilder.getSpellParam() + 0 + UrlBuilder.getAssemblerSearchTermParam() + searchQuery + UrlBuilder.getInternalIdParam()
                + UrlBuilder.getLocaleDimensionParam() + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(queryParam);
        String psfBaseUri = UrlBuilder.getAssemblerSearchBaseURI().toString();
        return getResponse(queryParams, psfBaseUri);
    }

    private static Response getSearchResponse(String categoryName) {
        String queryParam = UrlBuilder.getRequestQueryParam(categoryName) + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(queryParam);
        return getResponse(paramsMap, UrlBuilder.getSearchBaseURI().toString());
    }

    private static Response getl3CatagorySearchResponse(String psfID) {
        String queryParam = UrlBuilder.getl3CategorySearchParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(queryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFAndPSSSHierarchyBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUri);
    }

    private static Map<String, String> searchQueryParamsToMap(String componentQueryParams) {
        Map<String, String> queryparamMap = new HashMap<>();
        for (String param : componentQueryParams.split(";")) {
            String[] queryParams = param.split("=");
            if (queryParams.length > 1) {
                queryparamMap.put(queryParams[0], queryParams[1]);
            }
        }
        return queryparamMap
                .entrySet()
                .stream().filter(
                        e -> !e.getValue().equalsIgnoreCase("null")
                ).collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );
    }

    private static Map<String, String> searchQueryParamsToMapWithSpecialCharacters(String componentQueryParams) {
        Map<String, String> queryparamMap = new HashMap<>();
        for (String param : componentQueryParams.split(" ")) {
            String[] queryParams = param.split("=");
            if (queryParams.length > 1) {
                queryparamMap.put(queryParams[0], queryParams[1]);
            }
        }
        return queryparamMap.entrySet().stream().filter(e -> !e.getValue().equalsIgnoreCase("null")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Response getAssemblerResponse() {
        String queryParam = UrlBuilder.getRequestAssemblerQueryParam();
        Map<String, String> assemblerQueryParam = searchQueryParamsToMap(queryParam);
        return getResponse(assemblerQueryParam, UrlBuilder.getAssemblerBaseURI().toString());
    }

    public static Response getPSFAncestorsHierarchyAssemblerResponse(String psfID) {
        String queryParam = UrlBuilder.getAssemblerSearchTermParam().substring(1) + psfID + defaultUserSegment();
        Map<String, String> assemblerQueryParam = searchQueryParamsToMap(queryParam);
        return getResponse(assemblerQueryParam, UrlBuilder.getAssemblerBaseURI().toString());
    }

    public static Response getAsseblerRelevanceResponse(String internalId) {
        String QueryParam = UrlBuilder.getL3Param() + internalId + UrlBuilder.getAssemblerRelevanceParam() + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(QueryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }

    public static Response getSeoUrlAssemblerL3Response(String internalId) {
        String QueryParam = UrlBuilder.getL3Param() + internalId + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(QueryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }


    /*
     * The below method builds the assembler url
     */

    public static Response getAssemblerLimitResponse(String internalId, String searchQuery) {
        String QueryParam = UrlBuilder.getL3Param() + internalId + UrlBuilder.getNtkParam() + "I18NSearchGeneric" +
                UrlBuilder.getAssemblerSearchTermParam() + searchQuery + UrlBuilder.getNtxParam() + "mode%20matchallpartial" + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(QueryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }
    /*
     * The below method builds the assembler url for ancestor category details
     */

    public static Response getPSSSAncestorsHierarchyAssemblerResponse(String psssID) {
        String queryParam = UrlBuilder.getAssemblerSearchTermParam().substring(1) + psssID + UrlBuilder.getRequestPSSSAssemblerQueryParam() + defaultUserSegment();
        Map<String, String> assemblerQueryParam = searchQueryParamsToMap(queryParam);
        return getResponse(assemblerQueryParam, UrlBuilder.getAssemblerBaseURI().toString());
    }

    private static Response getPSFAncestorsHierarchy(String psfID) {
        String queryParam = UrlBuilder.getRequestPSFQueryParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(queryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFAndPSSSHierarchyBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUri);
    }

    private static Response getPSSSChildHierarchy(String psssID) {
        String queryParam = UrlBuilder.getRequestPSSSQueryParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(queryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFAndPSSSHierarchyBaseURI().toString()) + psssID;
        return getResponse(paramsMap, psfBaseUri);
    }

    private static Response getSearchAPIResponseUsingSeoUrl(String seoUrl) {
        String queryParam = UrlBuilder.getPSFQueryParamSeoUrl() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(queryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFAndPSSSHierarchyBaseURI().toString()) + seoUrl;
        return getResponse(paramsMap, psfBaseUri);
    }
    /*
     * The below method builds the assembler url for child category details
     */

    private static Response getSearchTermNodeResponse(String psfID) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUri);
    }

    /*
     * The below method gets the ancestor category details for search api
     */

    public static Response getAssemblerPopularityResponse(String internalid) {
        String queryParam = UrlBuilder.getL3Param() + internalid + UrlBuilder.getAssemblerPopularityParam();
        Map<String, String> queryParams = searchQueryParamsToMap(queryParam);
        return given().baseUri(UrlBuilder.getLevel3BaseUrl()).params(queryParams).urlEncodingEnabled(true).header("Accept", "application/json").log().all().when().get();
    }

    public static Response getAssemblerTermNodeResponse(String internalid) {
        String queryParam = UrlBuilder.getL3Param() + internalid + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(queryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }

    public static Response getAssemblerTermNodeResponsePSFWithSearchTerm(String internalid, String term) {
        String queryParam = UrlBuilder.getL3Param() + internalid + UrlBuilder.getRedirectParams() + UrlBuilder.getClientChannelParams() +
                UrlBuilder.getAssemblerSearchTermParam() + term + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(queryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }

    public static Response getAssemblerHighToLowResponse(String internalId) {
        String highToLowQueryParam = UrlBuilder.getL3Param() + internalId + UrlBuilder.getAssemblerHighToLowParam() + "%7C1" + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(highToLowQueryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }

    public static Response getAssemblerLowToHighResponse(String internalId) {
        String lowToHighQueryParam = UrlBuilder.getL3Param() + internalId + UrlBuilder.getAssemblerLowToHighParam() + "%7C0" + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(lowToHighQueryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }

    public static void getSearchApiResponse(String psfId) {
        Response searchApiResponse = getL3searchApiResponse(psfId);
        ScenarioContext.setData(psfId, searchApiResponse);
        ScenarioContext.setIds(psfId);
    }

    private static Response getL3searchApiResponse(String psfId) {
        String localeId = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment();
        Map<String, String> queryaParam = searchQueryParamsToMap(localeId);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(queryaParam, baseUrl);
    }

    public static Response getAssemblerCapacityResponse(String internalId, String order) {
        String capacityQueryParam = UrlBuilder.getL3Param() + internalId + UrlBuilder.getAssemblerCapacityParam() +
                UrlBuilder.getCapacityOrder(order) + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(capacityQueryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }

    public static Response getSearchInvalidTermNodeResponse(String psfId) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + "Dddds" + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUri);
    }

    public static Response getSeoUrlApi(String seoUrl, String targetState) {
        String seoUrlLocale = UrlBuilder.getSeoLocaleParam() + targetState + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(seoUrlLocale);
        String seoUrlBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + seoUrl;
        return getResponse(paramsMap, seoUrlBaseUrl);
    }

    public static Response getInternalIdApi(String internald, String targetState) {
        String internalIdlLocaleParam = UrlBuilder.getInternalIdLocaleParam() + targetState + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(internalIdlLocaleParam);
        String internalIdBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + internald;
        return getResponse(paramsMap, internalIdBaseUrl);
    }

    public static Response getAsseblerPaginationResponse(String internald) {
        String assemblerParams = UrlBuilder.getAssemblerFilterParam() + internald + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerParameterisedResponse(int pageNumber, int limit, String internald) {
        String paginationParams = UrlBuilder.getAssemblerFilterNrppParam() + limit + UrlBuilder.getNrppParam() + pageNumber +
                UrlBuilder.getInternalIdParam() + internald + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramMap, baseUrl);
    }

    public static Response getAsseblerCategoryResponse(String internald) {
        String assemblerParams = UrlBuilder.getL3Param() + internald + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getSearchTermSearchApiResponse(String searchQuery) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    private static Response getSearchWithCustomerFilterApiResponse(String searchQuery, String customerFilterId) {
        String localeQueryParam = UrlBuilder.getlocaleParamsWithClientIdTest() + searchQuery + UrlBuilder.getCustomerFilter() + customerFilterId;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public static Response getSearchWithoutCustomerFilterApiResponse(String searchQuery) {
        String localeQueryParam = UrlBuilder.getlocaleParamsWithClientIdTest() + searchQuery;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);

    }

    private static Response getSearchTermSearchApiResponseWithSpecialCharacters(String searchTerm) {
        String localeQueryParam = UrlBuilder.getSearchQueryParamWithSpecialCharacters() + searchTerm + " " + defaultUserSegment().substring(1);
        Map<String, String> paramsMap = searchQueryParamsToMapWithSpecialCharacters(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return given().baseUri(psfBaseUri).params(paramsMap).urlEncodingEnabled(true).header("Accept", "application/json").log().all().when().get();
    }

    private static Response getAssemblerStratifyOff(String internalId) {
        String params = UrlBuilder.getL3Param() + internalId + UrlBuilder.getStratifyParam() + "0" + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getAssemblerStratifyOffPSFWithSearchTerm(String internalId, String term) {
        String params = UrlBuilder.getL3Param() + internalId + UrlBuilder.getStratifyParam() + "0" + UrlBuilder.getRedirectParams() +
                UrlBuilder.getClientChannelParams() + UrlBuilder.getAssemblerSearchTermParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getLastPageNumberSAPIResponse(String psfId, int pageNumber) {
        String paginationParams = UrlBuilder.getPaginationPageParam() + pageNumber + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    private static Response getLastPageAssemblerStratifyOff(String internalId, int offsetParam) {
        String params = UrlBuilder.getAssemblerFilterNrppParam() + "20" + UrlBuilder.getNrrpParam() + offsetParam + UrlBuilder.getInternalIdParam()
                + internalId + UrlBuilder.getStratifyParam() + "0" + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getAssemblerLastTermNodeResponse(String internalId, int offsetParam) {
        String params = UrlBuilder.getAssemblerFilterNrppParam() + "20" + UrlBuilder.getOffsetParam() + offsetParam +
                UrlBuilder.getInternalIdParam() + internalId + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getAssemblerLastTermNodeResponseForPSFWithSearchTerm(String term, String internalId, int offsetParam) {
        String params = UrlBuilder.getAssemblerFilterNrppParam() + "20" + UrlBuilder.getOffsetParam() + offsetParam +
                UrlBuilder.getInternalIdParam() + internalId + UrlBuilder.getRedirectParams() + UrlBuilder.getClientChannelParams() +
                UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerResponseWitBoostedRecords(String internalId, int listSize) {
        String params = UrlBuilder.getBoostAssemblerRelRankingParam() + internalId + UrlBuilder.getNrrpParameter() + listSize + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerResponsePSFWithSearchTermWithBoostedRecords(String term, String internalId, int listSize) {
        String params = UrlBuilder.getBoostAssemblerRelRankingParameter() + internalId + UrlBuilder.getNrrpParameter() + listSize +
                UrlBuilder.getRedirectParams() + UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerResponseWithBuriedRecords(String internalId, int listSize, int No) {
        String params = UrlBuilder.getBuryAssemblerRelRankingParam() + internalId + UrlBuilder.getNrrpParameter() + listSize +
                UrlBuilder.getOffsetParam() + No + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getTermNodeWithCustomerFilterResponse(String searchQuery, String cust_FilterId, String psfId) {
        String QueryParams = UrlBuilder.getlocaleParamsWithClientIdTest() + searchQuery + UrlBuilder.getCustomerFilter() + cust_FilterId;
        System.out.println(QueryParams);
        Map<String, String> paramsMap = searchQueryParamsToMap(QueryParams);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        System.out.println(psfBaseUri);
        return getResponse(paramsMap, psfBaseUri);

    }

    public static Response getTerminalNodeDesktopWithoutCustomerFilterResponse(String searchQuery, String psfId) {
        String QueryParams = UrlBuilder.getlocaleParamsWithClientIdTest() + searchQuery;
        Map<String, String> paramsMap = searchQueryParamsToMap(QueryParams);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUri);
    }

    public static Response getAssemblerPopularityWithSearchTermResponse(String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" +
                UrlBuilder.getNtkParam() + interfaceName + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getDecodedAssemblerPopularityParam() + "%7C1" +
                UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerLowToHighWithSearchTermResponse(String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" +
                UrlBuilder.getNtkParam() + interfaceName.trim() + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getAssemblerLowToHighParam()
                + "%7C0" + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerHighToLowWithSearchTermResponse(String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" + UrlBuilder.getRedirectParams() +
                UrlBuilder.getNtkParam() + interfaceName + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getAssemblerHighToLowParam() + "%7C1" + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerRelevanceWithSearchTermResponse(String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" +
                UrlBuilder.getNtkParam() + interfaceName + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerTerminalNodeRelevanceResponse(String internald, String term) {
        String assemblerParams = UrlBuilder.getAssemblerNtkStockInterfaceParam() + UrlBuilder.getAssemblerLocaleId() + internald +
                UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerTerminalNodeRelevanceWithInterfaceResponse(String internald, String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" +
                UrlBuilder.getNtkParam() + interfaceName + UrlBuilder.getAssemblerLocaleId() + internald + UrlBuilder.getAssemblerSeachQueryParam() +
                term + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerTerminalNodePopularityWithInterfaceResponse(String internalId, String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" + UrlBuilder.getNtkParam() + interfaceName +
                UrlBuilder.getAssemblerLocaleId() + internalId + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getPartialAssemblerPopularityParam() + "%7C1" + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerTerminalNodeDescendingOrderWithInterfaceResponse(String internalId, String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" + UrlBuilder.getNtkParam() + interfaceName +
                UrlBuilder.getAssemblerLocaleId() + internalId + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getAssemblerHighToLowParam() + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerTerminalNodeAscendingOrderWithInterfaceResponse(String internalId, String term, String interfaceName) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" + UrlBuilder.getNtkParam() + interfaceName +
                UrlBuilder.getAssemblerLocaleId() + internalId + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getAssemblerLowToHighParam() + "%7C0" + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerTermNodeProductOrderResponse(String internalId, String term, String order) {
        String assemblerParams = UrlBuilder.getAssemblerNtkParamSAPI() + UrlBuilder.getAssemblerLocaleId() + internalId + UrlBuilder.getRedirectParams() +
                UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getAssemblerCapacityParam() + UrlBuilder.getCapacityOrder(order) + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getResponse(Map<String, String> paramsMap, String baseUrl) {
        return given().baseUri(baseUrl).params(paramsMap).urlEncodingEnabled(false).header("Accept", "application/json").log().all().when().get();
    }

    private static Response getLastPageNumberSAPIResponseWithSearchTerm(String psfId, String term, int lastPaginationPage) {
        String paginationParams = UrlBuilder.getPaginationPageParam() + lastPaginationPage + UrlBuilder.getClientChannelParams() + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    private static Response getLastPageAssemblerStratifyOffForPSFWithSearchTerm(String term, String internalId, int offsetParam) {
        String params = UrlBuilder.getAssemblerFilterNrppParam() + "20" + UrlBuilder.getOffsetParam() + offsetParam +
                UrlBuilder.getInternalIdParam() + internalId + UrlBuilder.getRedirectParams() + UrlBuilder.getClientChannelParams() +
                UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getStratifyParam() + "0" + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getLevel3BaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getEndOfBoostResponse(int panasonicStoppingPage, String psfId, String term) {
        String paginationPageParam = UrlBuilder.getPaginationPageParam() + panasonicStoppingPage +
                UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationPageParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public static Response getAssemblerResponseWithBuriedRecordsPSFWithSearchTerm(String term, String internalId, int listSize, Integer no) {
        String params = UrlBuilder.getBuriedtAssemblerRelRankingParameter() + internalId
                + UrlBuilder.getNrrpParameter() + listSize
                + UrlBuilder.getRedirectParams()
                + UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();

        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getAssemblerStratifyOffWithSearchTerm(String term) {

        String params = UrlBuilder.getassemblerQueryConstantParam() + UrlBuilder.getStratifyParam() + "0" + UrlBuilder.getRedirectParams() +
                UrlBuilder.getClientChannelParams() + UrlBuilder.getAssemblerSearchTermParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getAssemblerTermNodeResponseWithSearchTerm(String term) {
        String queryParam = UrlBuilder.getassemblerQueryConstantParam() + UrlBuilder.getRedirectParams() +
                UrlBuilder.getClientChannelParams() + UrlBuilder.getAssemblerSearchTermParam() + term + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(queryParam);
        return getResponse(queryParams, UrlBuilder.getAssemblerSearchBaseUrl());
    }

    private static Response getAssemblerLastResponseWithSearchTerm(String term, Integer offsetParam) {
        String params = UrlBuilder.getAssemblerFilterNrppParam() + "20" + UrlBuilder.getOffsetParam() +
                offsetParam + UrlBuilder.getRedirectParams() + UrlBuilder.getClientChannelParams() +
                UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getLastPageAssemblerStratifyOffWithSearchTerm(int offsetParam, String term) {
        String params = UrlBuilder.getAssemblerFilterNrppParam() + "20" + UrlBuilder.getStratifyParam() + "0" +
                UrlBuilder.getOffsetParam() + offsetParam + UrlBuilder.getRedirectParams() + UrlBuilder.getClientChannelParams() +
                UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getLastPageNumSAPIResponseWithSearchTerm(String term, int lastPaginationPage) {
        String paginationParams = UrlBuilder.getPaginationPageParam() + lastPaginationPage + UrlBuilder.getClientChannelParams() +
                UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramMap, baseUrl);
    }

    public static Response getAssemblerResponseWithSearchTermWithBoostedRecords(String term, int listSize) {
        String params = UrlBuilder.getRelevanceRankingBoostSearchResultsParam() + UrlBuilder.getNrrpParameter() +
                listSize + UrlBuilder.getRedirectParams() + UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public static Response getAssemblerResponseWithBuriedRecordsWithSearchTerm(String term, int listSize, Integer no) {
        String params = UrlBuilder.getRelevanceRankingBurySearchResultsParam() + UrlBuilder.getNrrpParameter() +
                listSize + UrlBuilder.getRedirectParams() + UrlBuilder.getOffsetParam() + no +
                UrlBuilder.getAssemblerSeachQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    private static Response getSearchAPIResponseImplicitRef(String psfID, String appliedDimension) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment() +
                UrlBuilder.getImplicitRefinements() + UrlBuilder.getAppliedDimensionsParam() + appliedDimension;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUri);
    }

    private static Response getSearchByMPNWithCustomerFilterApiResponse(String searchQuery, String cust_FilterId) {
        String localeQueryParam = UrlBuilder.getlocaleParamsWithClientIdTest() + searchQuery + UrlBuilder.getCustomerFilter() + cust_FilterId + UrlBuilder.getMpnParams();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    private static Response setResponseForPSFIDWithProductsFieldParams(String psf_id, String productFieldParam) {
        String QueryParams = UrlBuilder.getSAPIDesktopLocalChannel() + UrlBuilder.getProductFieldsParam() + productFieldParam;
        Map<String, String> paramsMap = searchQueryParamsToMap(QueryParams);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psf_id;
        return getResponse(paramsMap, psfBaseUri);

    }

    public static Response getSearchByMPNWithOutCustomerFilterApiResponse(String searchQuery) {
        String localeQueryParam = UrlBuilder.getlocaleParamsWithClientIdTest() + searchQuery + UrlBuilder.getMpnParams();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    private static String addQuestionMarkIfItIsMissing(String baseUri) {
        if (!baseUri.endsWith("?")) {
            if (baseUri.endsWith("/")) {
                baseUri = StringUtils.chop(baseUri);
            }
            return baseUri + "?";
        }
        return baseUri;
    }

    private static String addSlashIfItIsMissing(String baseUri) {
        return !baseUri.endsWith("/")
                ? baseUri + "/"
                : baseUri;
    }

    /*
     * @param categoryName Level zero, one, two, three is passed from feature
     * The below method builds the search api url
     */
    public void getSearchHierarchyResults(String categoryName) {
        Response response = getSearchResponse(categoryName);
        ScenarioContext.setData(categoryName, response);
        ScenarioContext.setIds(categoryName);
    }

    public void getl3CategorySearchResponse(String psfID) {
        Response response = getl3CatagorySearchResponse(psfID);
        ScenarioContext.setData(psfID, response);
    }

    public void getSearchAPIPSFRecordIds(String psfID) {
        Response searchApiResponse = getSearchRecordIdsResponse(psfID);
        ScenarioContext.setData(psfID, searchApiResponse);
    }

    private Response getSearchRecordIdsResponse(String psfID) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSearchAPIPSFAncestorsHierarchy(String psfID) {
        Response response = getPSFAncestorsHierarchy(psfID);
        ScenarioContext.setData(psfID, response);
    }

    public void getSearchAPIPSFCategory(String psfID) {
        Response response = getSearchTermNodeResponse(psfID);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(psfID, response);
        ScenarioContext.setIds(psfID);
    }

    /*
     * The below method gets the child category details for search api
     */
    public void getSearchAPIPSSSChildHierarchy(String psssID) {
        Response response = getPSSSChildHierarchy(psssID);
        ScenarioContext.setData(psssID, response);
    }

    public void getSearchAPIPSFUsingSeoURL(String psfID) {
        Response responseWithPSF = ScenarioContext.getData(psfID);
        Response responseWithSeo = getSearchAPIResponseUsingSeoUrl(responseWithPSF.path("category.seoUrl"));
        ScenarioContext.setData(psfID, responseWithSeo);
    }

    public void getSAPIPopularityResponse(String psfid) {
        Response searchApiPopularityResponse = getSearchApiPopularityResponse(psfid);
        ScenarioContext.setData(psfid, searchApiPopularityResponse);
    }

    private Response getSearchApiPopularityResponse(String psfId) {
        String popularityParam = UrlBuilder.getPopularityParam() + "%7C1" + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(popularityParam);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public void getSearchApiHIghToLowResponse(String psfId) {
        Response searchApiHighToLowResponse = getSearchApiHighToLowResponse(psfId);
        ScenarioContext.setData(psfId, searchApiHighToLowResponse);
    }

    private Response getSearchApiHighToLowResponse(String psfId) {
        String highToLowQueryParam = UrlBuilder.getHighToLowParam() + "%7C1" + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(highToLowQueryParam);
        String psfbaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(queryParams, psfbaseUrl);
    }

    public void getSAPILowToHighResponse(String psfId) {
        Response searchApiLowToHighResponse = getSearchApiLowToHighResponse(psfId);
        ScenarioContext.setData(psfId, searchApiLowToHighResponse);
    }

    public void getSAPICapacityResponse(String psfId, String order) {
        Response searchApiLowToHighResponse = getSearchApiCapacityResponse(psfId, order);
        ScenarioContext.setData(psfId, searchApiLowToHighResponse);
    }

    private Response getSearchApiLowToHighResponse(String psfId) {
        String lowToHighQueryParam = UrlBuilder.getLowToHighParam() + "%7C0" + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(lowToHighQueryParam);
        String psfbaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(queryParams, psfbaseUrl);
    }

    private Response getSearchApiCapacityResponse(String psfId, String order) {
        String lowToHighQueryParam = UrlBuilder.getCapacityParam() + UrlBuilder.getCapacityOrder(order) + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(lowToHighQueryParam);
        String psfbaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(queryParams, psfbaseUrl);
    }

    public void getSearchApiFacets(String psfId) {
        Response response = getSearchApiFacetsResponse(psfId);
        ScenarioContext.setData(psfId, response);
        ScenarioContext.setIds(psfId);
    }

    public Response getSearchApiFacetsResponse(String psfId) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment();
        Map<String, String> queryParam = searchQueryParamsToMap(localeQueryParam);
        String baseurl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(queryParam, baseurl);
    }

    public void getSAPIInvalidReponse(String psfId) {
        Response response = getSearchInvalidTermNodeResponse(psfId);
        ScenarioContext.setData(psfId, response);
        ScenarioContext.setIds(psfId);
    }

    public void getSAPIInvalidSortResposne(String psfId) {
        Response searchApiResponse = getSearchInvalidSortResponse(psfId);
        ScenarioContext.setData(psfId, searchApiResponse);
    }

    private Response getSearchInvalidSortResponse(String psfID) {
        String popularityParam = UrlBuilder.getInvalidPopularityParam() + "%7C1" + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(popularityParam);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public void getSearchAPIPSFInvalidLimitCategory(String psfId) {
        Response response = getSearchTermNodeInvalidLimitResponse(psfId);
        ScenarioContext.setData(psfId, response);
        ScenarioContext.setIds(psfId);
    }

    private Response getSearchTermNodeInvalidLimitResponse(String psfId) {
        String localeLimitQueryParam = UrlBuilder.getInvalidLimitParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeLimitQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getInvalidSeoUrlResponse(String seoUrl) {
        Response response = getAPIInvalidSeoUrlResponse(seoUrl);
        ScenarioContext.setData(seoUrl, response);
        ScenarioContext.setIds(seoUrl);
    }

    private Response getAPIInvalidSeoUrlResponse(String seoUrl) {
        String seoUrlParam = UrlBuilder.getSeoLocaleParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(seoUrlParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + seoUrl;
        return getResponse(paramsMap, baseUrl);
    }

    public void getInvalidInternalIdResponse(String internalId) {
        Response response = getInvalidInternalIdSAPIResponse(internalId);
        ScenarioContext.setData(internalId, response);
        ScenarioContext.setIds(internalId);
    }

    private Response getInvalidInternalIdSAPIResponse(String internalId) {
        String invalidInternalIdParam = UrlBuilder.getInvalidInternalIdLocaleParam() + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(invalidInternalIdParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + internalId;
        return getResponse(paramMap, baseUrl);
    }

    public void getSAPIResponseForAttrFilters(String psfID, String descriptors) {
        Response response = getAttrFilterOnL3Response(psfID, descriptors);
        ScenarioContext.setData(psfID, response);
        ScenarioContext.setIds(psfID);
    }

    private Response getAttrFilterOnL3Response(String psfID, String descriptors) {
        String localeQueryParam = UrlBuilder.getAttributeFilterQueryParam() + descriptors + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUri);
    }

    public Response getAssemblerResponseForAttrFilters(String internalId, String dimensionId) {
        Response assemblerResponse = getAssemblerTermNodeResponse(internalId + dimensionId);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        return assemblerResponse;
    }

    public void getSAPIAboveLimitResponse(int limit, String psfId) {
        Response aboveLimitResponse = getAboveLimitPsfResponse(limit, psfId);
        ScenarioContext.setData(psfId, aboveLimitResponse);
        ScenarioContext.setIds(psfId);
    }

    private Response getAboveLimitPsfResponse(int limit, String psfId) {
        String aboveLimitParam = UrlBuilder.getLimitParam() + limit + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(aboveLimitParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void getValidLimitPSFResponse(String limit, String psfId) {
        Response validLimitResp = getAValidLimitPSFResponse(limit, psfId);
        ScenarioContext.setData(psfId, validLimitResp);
    }

    public Response getAValidLimitPSFResponse(String limit, String psfId) {
        String belowLimitParam = UrlBuilder.getLimitParam() + limit + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(belowLimitParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void getPaginationPageResponse(int pagPage, String psfId) {
        Response paginationResponse = getPSFPaginationResponse(pagPage, psfId);
        ScenarioContext.setData(psfId, paginationResponse);
        ScenarioContext.setIds(psfId);
    }

    private Response getPSFPaginationResponse(int pagPage, String psfId) {
        String paginationPageParam = UrlBuilder.getPaginationPageParam() + pagPage + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationPageParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void getParamPaginationPSFResponse(int pageNumber, int limit) {
        Response paginationResponse = getParamPSFPaginationResponse(pageNumber, limit);
        ScenarioContext.setData(ScenarioContext.getIds(), paginationResponse);
    }

    private Response getParamPSFPaginationResponse(int pageNumber, int limit) {
        String paginationParams = UrlBuilder.getPaginationLimitParams() + limit + UrlBuilder.getPaginationPageParams() + pageNumber + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + ScenarioContext.getIds();
        return getResponse(paramMap, baseUrl);
    }

    public void getInvalidPaginationPageResponse(String invalidPage, String psfId) {
        Response paginationResponse = getInvalidPagPSFPaginationResponse(invalidPage, psfId);
        ScenarioContext.setData(psfId, paginationResponse);
        ScenarioContext.setIds(psfId);
    }

    private Response getInvalidPagPSFPaginationResponse(String invalidPage, String psfId) {
        String paginationParams = UrlBuilder.getPaginationPageParam() + invalidPage + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    private Response getParamPSFPaginationResponse(int invalidPage) {
        String paginationParams = UrlBuilder.getPaginationPageParam() + invalidPage + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + ScenarioContext.getIds();
        return getResponse(paramMap, baseUrl);
    }

    public void getInvalidLimitSAPIResponse(String invalidLimit, String psfId) {
        Response paginationResponse = getInvalidLimitResponse(invalidLimit);
        ScenarioContext.setData(psfId, paginationResponse);
    }

    private Response getInvalidLimitResponse(String invalidlimit) {
        String paginationParams = UrlBuilder.getLimitParam() + invalidlimit + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString());
        return getResponse(paramMap, baseUrl);
    }

    public void getOutOfBoundsPageSAPIResponse(int pageNumber, int limit) {
        Response paginationResponse = getOutOfBoundsResponse(pageNumber, limit);
        ScenarioContext.setData(ScenarioContext.getIds(), paginationResponse);
    }

    private Response getOutOfBoundsResponse(int pageNumber, int limit) {
        String paginationParams = UrlBuilder.getLimitParam() + limit + UrlBuilder.getPaginationPageParams() + pageNumber + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + ScenarioContext.getIds();
        return getResponse(paramMap, baseUrl);
    }

    public void getPageNumberResponse(int pageNumber) {
        Response paginationResponse = getPageNumberSAPIResponse(pageNumber);
        ScenarioContext.setData(ScenarioContext.getIds(), paginationResponse);
    }

    private Response getPageNumberSAPIResponse(int pageNumber) {
        String paginationParams = UrlBuilder.getPaginationPageParam() + pageNumber + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationParams);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + ScenarioContext.getIds();
        return getResponse(paramMap, baseUrl);
    }

    public void getCategorySearchResponse(String categories) {
        Response categoryResponse = getCategorySAPIResponse(categories);
        ScenarioContext.setData(categories, categoryResponse);
    }

    private Response getCategorySAPIResponse(String categories) {
        String localeParam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(localeParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFAndPSSSHierarchyBaseURI().toString()) + categories;
        return getResponse(paramMap, baseUrl);
    }

    public void setSearchAPINaturalSearchRequest(String searchQuery) {
        Response response = getSearchTermSearchApiResponse(searchQuery);
        ScenarioContext.setData(searchQuery, response);
    }

    public void setSearchAPINaturalSearchRequestWithSpecialCharacters(String searchQuery) {
        Response response = getSearchTermSearchApiResponseWithSpecialCharacters(searchQuery);
        ScenarioContext.setData(searchQuery, response);
    }

    public void setSearchAPINaturalSearchWithCustomerFilterRequest(String searchQuery, String customerFilterId) {
        Response response = getSearchWithCustomerFilterApiResponse(searchQuery, customerFilterId);
        ScenarioContext.setData(customerFilterId, response);
    }

    public void setSearchAPIByMPNWithCustomerFilterRequest(String searchQuery, String cust_FilterId) {
        Response response = getSearchByMPNWithCustomerFilterApiResponse(searchQuery, cust_FilterId);
        ScenarioContext.setData(cust_FilterId, response);
    }

    public void getResultsListResponse(String psfId) {
        Response Response = getSAPIResultsListResponse(psfId);
        ScenarioContext.setData(psfId, Response);
    }

    private Response getSAPIResultsListResponse(String psfId) {
        String localeparam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(localeparam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void assertBoostedProducts(String psfId) {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        List<String> recordsList = searchApiResponse.jsonPath().prettyPeek().getList("resultsList.records.id");

        String internalId = new CommonHelpers().getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTermNodeResponse(internalId);
        List<String> assemblerPRecordIdsWithStratifyOn = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "Stratify on: the boosted products between SearchAPI and Assembler should have matched (by ids)",
                assemblerPRecordIdsWithStratifyOn,
                recordsList
        );

        Response secondAssemblerResponse = StepsHelper.getAssemblerStratifyOff(internalId);
        List<String> assemblerPRecordIdsWithStratifyOff = CommonHelpers.getPRecordIds(secondAssemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertNotEquals(
                "Stratify off: the boosted products between SearchAPI and Assembler should have NOT matched (by ids)",
                assemblerPRecordIdsWithStratifyOff,
                recordsList
        );
    }

    public void assertBoostedProductsForPSFWithSearchTerm() {
        Response searchApiResponse = ScenarioContext.getData(UrlBuilder.getBoostedTerm());
        String term = UrlBuilder.getBoostedTerm();
        List<String> recordsList = searchApiResponse.jsonPath().prettyPeek().getList("resultsList.records.id");

        String internalId = new CommonHelpers().getIds(searchApiResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTermNodeResponsePSFWithSearchTerm(internalId, term);
        List<String> assemblerPRecordIdsWithStratifyOn = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "Stratify on: the boosted products for specific category with search term between " +
                        "SearchAPI and Assembler should have matched (by ids)",
                assemblerPRecordIdsWithStratifyOn,
                recordsList
        );

        Response secondAssemblerResponse = StepsHelper.getAssemblerStratifyOffPSFWithSearchTerm(internalId, term);
        List<String> assemblerPRecordIdsWithStratifyOff = CommonHelpers.getPRecordIds(secondAssemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertNotEquals(
                "Stratify off: the boosted products for specific category with search term between " +
                        "SearchAPI and Assembler should have NOT matched (by ids)",
                assemblerPRecordIdsWithStratifyOff,
                recordsList
        );
    }

    public void assertBuriedProducts(String psfId) {
        Response searchApiResponse = ScenarioContext.getData(psfId);
        int lastPaginationPage = searchApiResponse.path("resultsList.pagination.lastPage");
        Response lastPageResponse = StepsHelper.getLastPageNumberSAPIResponse(psfId, lastPaginationPage);
        List<String> lastPageRecords = lastPageResponse.jsonPath().prettyPeek().getList("resultsList.records.id");

        Integer offsetParam = (lastPaginationPage - 1) * 20;

        String internalId = new CommonHelpers().getIds(searchApiResponse, "category.internalId");
        Response assemblerLatPageResponse = StepsHelper.getAssemblerLastTermNodeResponse(internalId, offsetParam);
        List<String> assemblerPRecordIdsWithStratifyOn = CommonHelpers.getPRecordIds(assemblerLatPageResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "Stratify on: the buried products for specific category between " +
                        "SearchAPI and Assembler should have matched (by ids)",
                assemblerPRecordIdsWithStratifyOn,
                lastPageRecords
        );

        Response secondAssemblerResponse = StepsHelper.getLastPageAssemblerStratifyOff(internalId, offsetParam);
        List<String> assemblerPRecordIdsWithStratifyOff = CommonHelpers.getPRecordIds(secondAssemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertNotEquals(
                "Stratify off: the buried products for specific category between " +
                        "SearchAPI and Assembler should have NOT matched (by ids)",
                assemblerPRecordIdsWithStratifyOff,
                lastPageRecords
        );
    }

    public void getSAPIEndOfBoostResponse(int infineonStoppingPage, String psfId) {
        Response endOfBoostSAPI = getEndOfBoostSAPIResponse(infineonStoppingPage, psfId);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                endOfBoostSAPI.getStatusCode()
        );
        ScenarioContext.setData(psfId, endOfBoostSAPI);
    }

    private Response getEndOfBoostSAPIResponse(int infineonStoppingPage, String psfId) {
        String paginationPageParam = UrlBuilder.getPaginationPageParam() + infineonStoppingPage + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationPageParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void getSAPIResponseWithSearchQueryParam(String searchQuery) {
        String searchTerm = searchQuery + UrlBuilder.getRedirectParams();
        Response response = getResponseWithSearchQueryAsParam(searchTerm);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(searchQuery, response);
        ScenarioContext.setIds(searchQuery);
    }

    public void getSAPIResponseWithKeywordRedirect(String searchQuery) {
        Response response = getResponseWithSearchQueryAsParam(searchQuery);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(searchQuery, response);
        ScenarioContext.setIds(searchQuery);
    }

    public Response getResponseWithSearchQueryAsParam(String searchQuery) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getTermNodeWithSearchTermResponse(String term, String psfId) {
        Response response = getTermNodeSearchTermSAPIResponse(term, psfId);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(term, response);
        ScenarioContext.setIds(term);
    }

    public void setTermNodeWithCustomerFilterResponse(String cust_FilterId, String searchQuery, String psfId) {
        Response response = getTermNodeWithCustomerFilterResponse(cust_FilterId, searchQuery, psfId);
        ScenarioContext.setData(psfId, response);
        ScenarioContext.setIds(psfId);

    }

    private Response getTermNodeSearchTermSAPIResponse(String term, String psfId) {
        String QueryParams = UrlBuilder.getSearchQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(QueryParams);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSearchTermResponse(String term) {
        Response response = getSearchTermSAPIResponse(term);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(term, response);
        ScenarioContext.setIds(term);
    }

    private Response getSearchTermSAPIResponse(String term) {
        String QueryParams = UrlBuilder.getSearchQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(QueryParams);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, addQuestionMarkIfItIsMissing(psfBaseUri));
    }

    public void getDescendingResponseWithSearchTerm(String term) {
        Response searchApiHighToLowResponse = getSearchApiHighToLowResponseWithSearchTerm(term);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                searchApiHighToLowResponse.getStatusCode()
        );
        ScenarioContext.setData(term, searchApiHighToLowResponse);
        ScenarioContext.setIds(term);
    }

    private Response getSearchApiHighToLowResponseWithSearchTerm(String term) {
        String highToLowQueryParam = UrlBuilder.getSearchQueryParam() + term + UrlBuilder.getAscendingDescendingParam() + "%7C1" + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(highToLowQueryParam);
        String psfbaseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(queryParams, psfbaseUrl);
    }

    public void getAscendingResponseWithSearchTerm(String term) {
        Response searchApiLowToHighResponse = getSAPISearchApiLowToHighResponseWithSearchTerm(term);
        ScenarioContext.setData(term, searchApiLowToHighResponse);
        ScenarioContext.setIds(term);
    }

    private Response getSAPISearchApiLowToHighResponseWithSearchTerm(String term) {
        String highToLowQueryParam = UrlBuilder.getLowToHighParam() + "%7C0" + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(highToLowQueryParam);
        String psfbaseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(queryParams, psfbaseUrl);
    }

    public void getSAPIPopularityResponseWithSearchTerm(String term) {
        Response searchApiPopularityResponse = getSAPIPopularityhResponseWithSearchTerm(term);
        ScenarioContext.setData(term, searchApiPopularityResponse);
        ScenarioContext.setIds(term);
    }

    private Response getSAPIPopularityhResponseWithSearchTerm(String term) {
        String popularityParam = UrlBuilder.getPopularityParam() + "%7C1" + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(popularityParam);
        String psfBaseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUrl);
    }

    public void getTerminalNodeWithSearchTermResponse(String term, String psfId) {
        Response searchApiTermNodeWithSearchTermResponse = getSAPIResponseWithSearchTerm(term, psfId);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                searchApiTermNodeWithSearchTermResponse.getStatusCode()
        );
        ScenarioContext.setData(term, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(term);
    }

    public void getTerminalNodeWithSearchTermResponseWithRestriction(String term, String psfId, String customerFilter) {
        Response searchApiTermNodeWithSearchTermResponse = getSAPIResponseWithSearchTermAndRestriction(term, psfId, customerFilter);
        assertNotNull(
                "Search API response must not be null",
                searchApiTermNodeWithSearchTermResponse);
        assertEquals(
                "Search API should return a successful response",
                EXPECTED_OK_STATUS_CODE,
                searchApiTermNodeWithSearchTermResponse.getStatusCode());
        ScenarioContext.setData(term, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(term);
    }

    public void getTerminalNodeWithSearchTermAndLimitResponse(String searchQuery, String psfId, int limit) {
        Response searchApiTermNodeWithSearchTermResponse = getSAPIResponseWithSearchTermAndLimit(searchQuery, psfId, limit);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                searchApiTermNodeWithSearchTermResponse.getStatusCode()
        );
        ScenarioContext.setData(searchQuery, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(searchQuery);
    }

    private Response getSAPIResponseWithSearchTerm(String term, String psfId) {
        String params = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getSearchTermQueryParam() + term + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    private Response getSAPIResponseWithSearchTermAndRestriction(String term, String psfId, String customerFilter) {
        String params = UrlBuilder.getRequestTermNodeQueryParam()
                + UrlBuilder.getSearchTermQueryParam() + term
                + UrlBuilder.getRedirectParams() + defaultUserSegment()
                + UrlBuilder.getCustomerFilter() + customerFilter;
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    private Response getSAPIResponseWithSearchTermAndLimit(String searchQuery, String psfId, int limit) {
        String params = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getSearchTermQueryParam() + searchQuery + UrlBuilder.getRedirectParams() +
                UrlBuilder.getProductLimitParam() + limit + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public void getTerminalNodeByPopularityWithSearchTermResponse(String term, String psfId) {
        Response searchApiTermNodeWithSearchTermResponse = getSAPIResponseByPopularityWithSearchTerm(term, psfId);
        ScenarioContext.setData(term, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(term);
    }

    private Response getSAPIResponseByPopularityWithSearchTerm(String term, String psfId) {
        String popularityParam = UrlBuilder.getPopularityParam() + "%7C1" + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(popularityParam);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public void getTerminalNodeByDescendingOrderWithSearchTermResponse(String term, String psfId) {
        Response searchApiTermNodeWithSearchTermResponse = getTermNodeResponseByDescendingOrderWithSearchTerm(term, psfId);
        ScenarioContext.setData(term, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(term);
    }

    private Response getTermNodeResponseByDescendingOrderWithSearchTerm(String term, String psfId) {
        String params = UrlBuilder.getHighToLowParam() + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public void getTermNodeForProductOrderResponse(String term, String order, String psfId) {
        String orderParam = term + ";" + UrlBuilder.getSortParam() + UrlBuilder.getCapacityOrder(order);
        Response searchApiTermNodeWithSearchTermResponse = getSAPIResponseWithSearchTerm(orderParam, psfId);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                searchApiTermNodeWithSearchTermResponse.getStatusCode()
        );
        ScenarioContext.setData(psfId, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(psfId);
    }

    public Response getAssemblerResponseForTermNodeSearch(String term, String internalId, String offSet, String interfaceName, String nrpp) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + offSet + UrlBuilder.getNrrpParameter() + nrpp + UrlBuilder.getNtkParam() + interfaceName +
                UrlBuilder.getAssemblerLocaleId() + internalId + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public void getTerminalNodeByAscendingOrderWithSearchTermResponse(String term, String psfId) {
        Response searchApiTermNodeWithSearchTermResponse = getTermNodeResponseByAscendingOrderWithSearchTerm(term, psfId);
        ScenarioContext.setData(term, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(term);
    }

    private Response getTermNodeResponseByAscendingOrderWithSearchTerm(String term, String psfId) {
        String params = UrlBuilder.getLowToHighParam() + "%7C0" + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public Response getSAPIResponseWithTrackingParameters(String channelId, String clientId, String psfId) {
        String semiColon = ";";
        String params = UrlBuilder.getClientIdParam() + clientId + semiColon + UrlBuilder.getChannelIdParam() + channelId + semiColon + UrlBuilder.getLocaleIdParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public Response getAssemblerResponseForSearchResults(String term, String offSet, String interfaceName, String nrpp) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + offSet + UrlBuilder.getNrrpParameter() + nrpp + UrlBuilder.getNtkParam() + interfaceName +
                UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public void getBoostedResultsListResponsePSFWithTerm() {
        String psfId = UrlBuilder.getBoostedPsfId();
        String term = UrlBuilder.getBoostedTerm();
        Response Response = getBoostedSAPIResultsListResponsePSFWithSearchTerm(term, psfId);
        ScenarioContext.setData(term, Response);
    }

    private Response getBoostedSAPIResultsListResponsePSFWithSearchTerm(String term, String psfId) {
        String localeparam = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(localeparam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void getBuriedResultsListResponsePSFWithTerm() {
        String psfId = UrlBuilder.getBuriedPsfId();
        String term = UrlBuilder.getBuriedTerm();
        Response Response = getSAPIBuriedResultsListResponsePSFWithSearchTerm(term, psfId);
        ScenarioContext.setData(term, Response);
    }

    private Response getSAPIBuriedResultsListResponsePSFWithSearchTerm(String term, String psfId) {
        String localeparam = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(localeparam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void assertBuriedProductsForPSFWithSearchTerm() {
        String psfId = UrlBuilder.getBuriedPsfId();
        String term = UrlBuilder.getBuriedTerm();
        Response searchApiResponse = ScenarioContext.getData(UrlBuilder.getBuriedTerm());

        Integer lastPaginationPage = searchApiResponse.path("resultsList.pagination.lastPage");
        Response lastPageResponse = StepsHelper.getLastPageNumberSAPIResponseWithSearchTerm(psfId, term, lastPaginationPage);
        List<String> lastPageRecords = lastPageResponse.jsonPath().prettyPeek().getList("resultsList.records.id");

        Integer offsetParam = (lastPaginationPage - 1) * 20;

        String internalId = new CommonHelpers().getIds(searchApiResponse, "category.internalId");
        Response assemblerLatPageResponse = StepsHelper.getAssemblerLastTermNodeResponseForPSFWithSearchTerm(term, internalId, offsetParam);
        List<String> assemblerPRecordIdsWithStratifyOn = CommonHelpers.getPRecordIds(assemblerLatPageResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "Stratify on: the buried products for specific category with search term between SearchAPI and Assembler " +
                        "should have matched (by ids)",
                assemblerPRecordIdsWithStratifyOn,
                lastPageRecords
        );

        Response secondAssemblerResponse = StepsHelper.getLastPageAssemblerStratifyOffForPSFWithSearchTerm(term, internalId, offsetParam);
        List<String> assemblerPRecordIdsWithStratifyOff = CommonHelpers.getPRecordIds(secondAssemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertNotEquals(
                "Stratify off: the buried products for specific category with search term between SearchAPI and Assembler " +
                        "should have NOT matched (by ids)",
                assemblerPRecordIdsWithStratifyOff,
                lastPageRecords
        );
    }

    public void getSAPIEndOfBoostedResponse(int panasonicStoppingPage, String psfId, String term) {
        Response EndOfBoostSAPI = getEndOfBoostResponse(panasonicStoppingPage, psfId, term);
        ScenarioContext.setData(term, EndOfBoostSAPI);
    }

    public void getSAPIStartOfBuryResponse(int buryStartingPage, String psfId) {
        Response EndOfBoostSAPI = getStartOfSAPIResponse(buryStartingPage, psfId);
        ScenarioContext.setData(psfId, EndOfBoostSAPI);
    }

    private Response getStartOfSAPIResponse(int buryStartingPage, String psfId) {
        String paginationPageParam = UrlBuilder.getPaginationPageParam() + buryStartingPage + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationPageParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void getSAPIStartOfBuriedResponse(String term, Integer buryStartingPage, String psfId) {
        Response EndOfBoostSAPI = getStartOfSAPIBuriedResponse(term, buryStartingPage, psfId);
        ScenarioContext.setData(psfId, EndOfBoostSAPI);
    }

    private Response getStartOfSAPIBuriedResponse(String term, int buryStartingPage, String psfId) {
        String paginationPageParam = UrlBuilder.getPaginationPageParam() + buryStartingPage +
                UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationPageParam);
        String baseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramMap, baseUrl);
    }

    public void getBoostedResultsListResponseWithTerm() {
        String term = UrlBuilder.getBoostedSearchResultsTerm();
        Response Response = getBoostedSAPIResultsListResponseWithSearchTerm(term);
        ScenarioContext.setData(term, Response);
    }

    private Response getBoostedSAPIResultsListResponseWithSearchTerm(String term) {
        String localeparam = UrlBuilder.getRequestTermNodeQueryParam() +
                UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(localeparam);
        String baseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramMap, baseUrl);
    }

    public void assertBoostedProductsWithSearchTerm() {
        Response searchApiResponse = ScenarioContext.getData(UrlBuilder.getBoostedSearchResultsTerm());
        String term = UrlBuilder.getBoostedSearchResultsTerm();
        List<String> recordsList = searchApiResponse.jsonPath().prettyPeek().getList("resultsList.records.id");

        Response assemblerResponse = StepsHelper.getAssemblerTermNodeResponseWithSearchTerm(term);
        List<String> assemblerPRecordIdsWithStratifyOn = CommonHelpers.getPRecordIds(assemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "Stratify on: the boosted products for specific category with search term between SearchAPI and Assembler " +
                        "should have matched (by ids)",
                assemblerPRecordIdsWithStratifyOn,
                recordsList
        );

        Response secondAssemblerResponse = StepsHelper.getAssemblerStratifyOffWithSearchTerm(term);
        List<String> assemblerPRecordIdsWithStratifyOff = CommonHelpers.getPRecordIds(secondAssemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertNotEquals(
                "Stratify off: the boosted products for specific category with search term between SearchAPI and Assembler " +
                        "should have NOT matched (by ids)",
                assemblerPRecordIdsWithStratifyOff,
                recordsList
        );
    }

    public void getBuriedResultsListResponseWithTerm() {
        String term = UrlBuilder.getBuriedSearchResultsTerm();
        Response response = getBuriedSAPIResultsListResponseWithSearchTerm(term);
        ScenarioContext.setData(term, response);
    }

    private Response getBuriedSAPIResultsListResponseWithSearchTerm(String term) {
        String localeparam = UrlBuilder.getRequestTermNodeQueryParam() +
                UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(localeparam);
        String baseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramMap, baseUrl);
    }

    public void assertBuriedProductsWithSearchTerm() {
        Response searchApiResponse = ScenarioContext.getData(UrlBuilder.getBuriedSearchResultsTerm());
        String term = UrlBuilder.getBuriedSearchResultsTerm();
        int lastPaginationPage = searchApiResponse.path("resultsList.pagination.lastPage");
        Response lastPageResponse = StepsHelper.getLastPageNumSAPIResponseWithSearchTerm(term, lastPaginationPage);
        List<String> lastPageRecords = lastPageResponse.jsonPath().prettyPeek().getList("resultsList.records.id");

        int offsetParam = (lastPaginationPage - 1) * 20;

        Response assemblerLatPageResponse = StepsHelper.getAssemblerLastResponseWithSearchTerm(term, offsetParam);
        List<String> assemblerPRecordIdsWithStratifyOn = CommonHelpers.getPRecordIds(assemblerLatPageResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertEquals(
                "Stratify on: the buried products for search term between SearchAPI and Assembler " +
                        "should have matched (by ids)",
                assemblerPRecordIdsWithStratifyOn,
                lastPageRecords
        );

        Response secondAssemblerResponse = StepsHelper.getLastPageAssemblerStratifyOffWithSearchTerm(offsetParam, term);
        List<String> assemblerPRecordIdsWithStratifyOff = CommonHelpers.getPRecordIds(secondAssemblerResponse, "mainContent[0].contents[0].records.attributes.P_recordID");
        assertNotEquals(
                "Stratify on: the buried products for search term between SearchAPI and Assembler " +
                        "should have matched (by ids)",
                assemblerPRecordIdsWithStratifyOff,
                lastPageRecords
        );
    }

    public void getSAPIEndOfBoostedSearchResultsResponse(int pageNumber, String term) {
        Response EndOfBoostSAPI = getEndOfBoostResponseSAPI(pageNumber, term);
        ScenarioContext.setData(term, EndOfBoostSAPI);
    }

    private Response getEndOfBoostResponseSAPI(int pageNumber, String term) {
        String paginationPageParam = UrlBuilder.getPaginationPageParam() + pageNumber +
                UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationPageParam);
        String baseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramMap, baseUrl);
    }

    public Response getAssemblerResponseForExclusionSearch(String term, String offSet, String interfaceName, String nrpp) {
        String assemblerParams =
                UrlBuilder.getNrFilterParam() +
                        UrlBuilder.getOffsetParam() + offSet +
                        UrlBuilder.getNrrpParameter() + nrpp +
                        UrlBuilder.getNtkParam() + interfaceName +
                        UrlBuilder.getAssemblerSeachQueryParam() + term +
                        UrlBuilder.getRedirectParams() +
                        UrlBuilder.getExclusionsParams() +
                        defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public void getStartOfBuryResponseSAPI(String term, Integer buryStartingPage) {
        Response EndOfBoostSAPI = getStartOfBuriedSAPIResponse(term, buryStartingPage);
        ScenarioContext.setData(term, EndOfBoostSAPI);
    }

    private Response getStartOfBuriedSAPIResponse(String term, Integer buryStartingPage) {
        String paginationPageParam = UrlBuilder.getPaginationPageParam() + buryStartingPage +
                UrlBuilder.getSearchTermQueryParam() + term + defaultUserSegment();
        Map<String, String> paramMap = searchQueryParamsToMap(paginationPageParam);
        String baseUrl = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramMap, baseUrl);
    }

    public Response getAssemblerResponseForExcludedProductsSearch(String term, String internalId, String offSet, String interfaceName, String nrpp) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + offSet + UrlBuilder.getNrrpParameter() +
                nrpp + UrlBuilder.getNtkParam() + interfaceName + UrlBuilder.getAssemblerLocaleId() + internalId +
                UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getRedirectParams() + UrlBuilder.getExclusionsParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public void setSearchAPIRequestWithUserSegment(String searchQuery, String userSegment) {
        Response response = getSearchApiResponseWithUserSegment(searchQuery, userSegment);
        ScenarioContext.setData(searchQuery, response);
    }

    private Response getSearchApiResponseWithUserSegment(String searchQuery, String userSegment) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + UrlBuilder.getUserSegmentParamNotDefault() + userSegment;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public void setSearchAPIRequestWithLimit(String searchQuery, int limit) {
        Response response = getSearchApiResponseWithLimit(searchQuery, limit);
        ScenarioContext.setData(searchQuery, response);
    }

    private Response getSearchApiResponseWithLimit(String searchQuery, int limit) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + UrlBuilder.getProductLimitParam() + limit + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSearchAPIWithPageLimit(String psfParam, int pageLimit) {
        Response response = getSearchTermNodeResponseWithPageLimit(psfParam, pageLimit);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(psfParam, response);
        ScenarioContext.setIds(psfParam);
    }

    private Response getSearchTermNodeResponseWithPageLimit(String psfID, int pageLimit) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getProductLimitParam() + pageLimit + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void setSearchAPIMpnOnlyNaturalSearchRequest(String searchType, String searchQuery) {
        Response response = getMpnOnlySearchTermSearchApiResponse(searchType, searchQuery);
        ScenarioContext.setData(searchQuery, response);
    }

    private Response getMpnOnlySearchTermSearchApiResponse(String searchType, String searchQuery) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + UrlBuilder.getSearchTypeParam() + searchType + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSAPIResponseWithSearchQueryAndDimensionId(String searchQuery, String dimensionId) {
        String searchTerm = searchQuery + UrlBuilder.getAppliedDimensionsParam() + dimensionId + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Response response = getResponseWithSearchQueryAsParam(searchTerm);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(searchQuery, response);
        ScenarioContext.setIds(searchQuery);
    }

    public void getSearchAPIForIncorrectCategory(String psfID) {
        Response response = getSearchTermNodeResponse(psfID);
        ScenarioContext.setData(psfID, response);
        ScenarioContext.setIds(psfID);
    }

    public void getSearchAPIWithUserSegment(String psfParam, String userSegment) {
        Response response = getSearchTermNodeResponseWithUserSegment(psfParam, userSegment);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(psfParam, response);
        ScenarioContext.setIds(psfParam);
    }

    private Response getSearchTermNodeResponseWithUserSegment(String psfParam, String userSegment) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getUserSegmentParamNotDefault() + userSegment;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfParam;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getTerminalNodeWithSearchTermAndUserSegmentResponse(String searchQuery, String psfID, String userSegment) {
        Response searchApiTermNodeWithSearchTermResponse = getSAPIResponseWithSearchTermAndUserSegment(searchQuery, psfID, userSegment);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                searchApiTermNodeWithSearchTermResponse.getStatusCode()
        );
        ScenarioContext.setData(searchQuery, searchApiTermNodeWithSearchTermResponse);
        ScenarioContext.setIds(searchQuery);
    }

    private Response getSAPIResponseWithSearchTermAndUserSegment(String searchQuery, String psfID, String userSegment) {
        String params = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getSearchTermQueryParam() + searchQuery + UrlBuilder.getRedirectParams() + UrlBuilder.getUserSegmentParamNotDefault() + userSegment;
        Map<String, String> paramsMap = searchQueryParamsToMap(params);
        String psfBaseUrl = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfID;
        return getResponse(paramsMap, psfBaseUrl);
    }

    public Response getSearchProductResponse(String productID) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + UrlBuilder.getSiblingsParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getProductURI()) + productID;
        return getResponse(paramsMap, psfBaseUri);
    }

    public Response getProductAlternativesResponse(String productID, int recordLimit) {
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment() + UrlBuilder.getRecordLimitParamater() + recordLimit;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getProductAlternativesURI()) + productID;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSearchAPIPSFTerminalFor(String type) {
        String psfID = "";
        if (type.equals("IsNew")) {
            psfID = UrlBuilder.getPSFidsForINew();
        } else {
            psfID = UrlBuilder.getPSFidsForLeadTime();
        }
        Response response = getSearchTermNodeResponse(psfID);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(psfID, response);
        ScenarioContext.setIds(psfID);
    }

    public void getSAPIResponseForSelectedAttrFilters(String psfId, String internalIds) {
        Response response = getSelectedAttrFilterOnL3Response(psfId, internalIds);
        ScenarioContext.setData(psfId, response);
        ScenarioContext.setIds(psfId);
    }

    private Response getSelectedAttrFilterOnL3Response(String psfId, String internalIds) {
        String localeQueryParam = UrlBuilder.getAttributeFilterQueryParam() + internalIds + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + psfId;
        return getResponse(paramsMap, psfBaseUri);
    }

    public Response getAssemblerResponseForAttributeFilters(String internalId, String internalIds) {
        Response assemblerResponse = getAssemblerTNodeResponse(internalId, internalIds);
        assertEquals(
                "Assembler request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        return assemblerResponse;
    }

    private Response getAssemblerTNodeResponse(String internalId, String internalIds) {
        String queryParam = UrlBuilder.getL3Param() + internalId + '+' + internalIds + defaultUserSegment();
        Map<String, String> queryParams = searchQueryParamsToMap(queryParam);
        return getResponse(queryParams, UrlBuilder.getLevel3BaseUrl());
    }

    public void setSearchAPINaturalSearchRequestWithSearchConfig(String searchQuery, int searchConfig) {
        Response response = getSearchTermSearchApiResponseWithConfig(searchQuery, searchConfig);
        ScenarioContext.setData(searchQuery, response);
    }

    private Response getSearchTermSearchApiResponseWithConfig(String searchQuery, int searchConfig) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + UrlBuilder.getSearchConfigParam() + searchConfig;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public Response getAssemblerResponseForCategorySearch(String term, String interfaceName, String internalId) {
        String assemblerParams = UrlBuilder.getNrFilterParam() + UrlBuilder.getOffsetParam() + "0" + UrlBuilder.getNrrpParameter() + "20" +
                UrlBuilder.getNtkParam() + interfaceName + UrlBuilder.getAssemblerSeachQueryParam() + term + UrlBuilder.getRedirectParams() + UrlBuilder.getAssemblerLocaleId() + internalId;
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public Response getSearchAPIResImplicitRefinements(String psfID, String appliedDimension) {
        Response response = getSearchAPIResponseImplicitRef(psfID, appliedDimension);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(psfID, response);
        ScenarioContext.setIds(psfID);
        return response;
    }

    public void getSearchAPIRequestWithReservedCharacters(String term) {
        Response response = getSAPIWithReservedCharacters(term);
        ScenarioContext.setData(term, response);
    }


    private Response getSAPIWithReservedCharacters(String term) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + term + UrlBuilder.getMpnParams();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return given().baseUri(psfBaseUri).params(paramsMap).urlEncodingEnabled(true).header("Accept", "application/json").log().all().when().get();
    }

    private Response sendPostRequest(String data, String baseURL) {
         return given().body(data).header("Content-Type", "application/json").log().all().when().post(baseURL);
    }


    public Response getPostSearchApiFacetResponse(String data){
        String searchapiURL = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + "facet/";
        Response response = sendPostRequest(data,searchapiURL);
        assertEquals(
                "SearchAPI request should have returned the expected status code",
                EXPECTED_OK_STATUS_CODE,
                response.getStatusCode()
        );
        ScenarioContext.setData(data,response);
         ScenarioContext.setIds(data);
        return response;
    }


    public Response getPostSearchApiResponse(String data){
        String searchapiURL = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
         Response response = sendPostRequest(data,searchapiURL);
         assertEquals(
                 "SearchAPI request should have returned the expected status code",
                 EXPECTED_OK_STATUS_CODE,
                 response.getStatusCode()
         );
         ScenarioContext.setData(data,response);
         ScenarioContext.setIds(data);
         return response;
     }


    public Response getCustomerFilterResponse(String categoryName) {
        String queryParam = UrlBuilder.getRequestQueryParam(categoryName) + defaultUserSegment() +
                UrlBuilder.getCustomerFilter() + UrlBuilder.getCustomerFilterId();
        Map<String, String> paramsMap = searchQueryParamsToMap(queryParam);
        return getResponse(paramsMap, UrlBuilder.getSearchBaseURI().toString());
    }

    public Response getFilterProductionPackedAssemblerResponse(String term, String offSet, String interfaceName, String nrpp) {
        String assemblerParams = UrlBuilder.getNrParam() + UrlBuilder.getOffsetParam() + offSet +
                UrlBuilder.getNrrpParameter() + nrpp + UrlBuilder.getNtkParam() + interfaceName + UrlBuilder.getAssemblerSeachQueryParam() +
                term + UrlBuilder.getRedirectParams() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(assemblerParams);
        String baseUrl = UrlBuilder.getAssemblerSearchBaseUrl();
        return getResponse(paramsMap, baseUrl);
    }

    public void getL3CategoryWithSeoUrlResponse(String seoUrl) {
        Response response = getSAPISeoUrlResponse(seoUrl);
        ScenarioContext.setData(seoUrl, response);
    }

    private Response getSAPISeoUrlResponse(String seoUrl) {
        String seoURL = Page.getProperty(seoUrl);
        String localeQueryParam = UrlBuilder.getRequestTermNodeQueryParam() + defaultUserSegment();
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getSearchAPIPSFSortBaseURI().toString()) + seoURL;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSAPIResponseWithProductFieldsParam(String searchQuery) {
        Response response = getSAPIWithProductFieldsParameter(searchQuery);
        ScenarioContext.setData(searchQuery, response);
    }

    private Response getSAPIWithProductFieldsParameter(String searchQuery) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + defaultUserSegment() + UrlBuilder.getProductFieldsParam() + "*";
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSAPIResponseWithAllTheProperties(String resultsString, String searchQuery) {
        Response response = getSAPIResponseWithAllProperties(resultsString, searchQuery);
        ScenarioContext.setData(resultsString, response);
    }

    private Response getSAPIResponseWithAllProperties(String resultsString, String searchQuery) {
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + defaultUserSegment() + UrlBuilder.getProductFieldsParam() + resultsString;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return given().baseUri(psfBaseUri).params(paramsMap).urlEncodingEnabled(true).header("Accept", "application/json").log().all().when().get();
    }

    public void getDeclarativeFetchingSAPIResponse(String searchQuery) {
        Response response = getSAPIDeclFetchingResponse(searchQuery);
        ScenarioContext.setData(searchQuery, response);
    }

    private Response getSAPIDeclFetchingResponse(String searchQuery) {
        String fetchingParams = UrlBuilder.getDeclarativeFetchingParams();
        String localeQueryParam = UrlBuilder.getSearchQueryParam() + searchQuery + defaultUserSegment() + UrlBuilder.getProductFieldsParam() + fetchingParams;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return given().baseUri(psfBaseUri).params(paramsMap).urlEncodingEnabled(true).header("Accept", "application/json").log().all().when().get();
    }

    public void getSAPIPSFCategory(String psfID) {
        Response response = getSearchTermNodeResponse(psfID);
        ScenarioContext.setData(psfID, response);
        ScenarioContext.setIds(psfID);

    }

    public Response getResponseWithSearchQueryWithProductsFieldAsParam(String searchQuery, String productField) {
        String localeQueryParam = UrlBuilder.getlocaleParamsWithClientIdTest() + searchQuery + UrlBuilder.getProductFieldsParam() + productField;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = UrlBuilder.getSearchAPIPSFSortBaseURI().toString();
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getSAPISearchQueryProductsFieldResponse(String searchQuery, String productField) {
        Response response = getResponseWithSearchQueryWithProductsFieldAsParam(searchQuery, productField);
        ScenarioContext.setData(searchQuery, response);
    }

    public Response setResponseWithProductIdWithProductsFieldWithMultipleParam(String productID, String productFieldParam, String productFieldParam1) {
        String localeQueryParam = UrlBuilder.getSAPIDesktopLocalChannel() + UrlBuilder.getProductFieldsParam() + productFieldParam + "," + productFieldParam1;
        Map<String, String> paramsMap = searchQueryParamsToMap(localeQueryParam);
        String psfBaseUri = addSlashIfItIsMissing(UrlBuilder.getProductURI()) + productID;
        return getResponse(paramsMap, psfBaseUri);
    }

    public void getResponseWithProductIdWithProductsFieldWithMultipleParam(String productID, String productFieldParam, String productFieldParam1) {
        Response response = setResponseWithProductIdWithProductsFieldWithMultipleParam(productID, productFieldParam, productFieldParam1);
        ScenarioContext.setData(productID, response);
    }

    public void getResponseForPSF_IDWithProductsFieldParams(String psf_id, String productFieldParam) {
        Response response = setResponseForPSFIDWithProductsFieldParams(psf_id, productFieldParam);
        ScenarioContext.setData(psf_id, response);
    }

}