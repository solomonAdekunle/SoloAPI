package pages.Search.SeachAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Ordering;
import io.restassured.response.Response;
import pages.Search.SeachAPI.Models.SearchAPIModel;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommonHelpers {

    private static final int EXPECTED_OK_STATUS_CODE = 200;

    public static List<String> getRecordIds(Response searchApiResponse, String path) {
        List<String> recordIds = searchApiResponse.path(path);
        return recordIds;
    }

    public static List<String> getPRecordIds(Response assemblerResponse, String path) {
        List<Object> pRecordIds = assemblerResponse.path(path);
        List<String> recordIds = new ArrayList<>();
        for (Object obj : pRecordIds) {
            List<String> list = (ArrayList) obj;
            recordIds.add(list.get(0));
        }
        return recordIds;
    }

    public static int getFacetBinCounts(Response searchApiResponse) {
        List<List<String>> binCounts = searchApiResponse.path("refinements.value.binCount");
        int sum = 0;
        for (List<String> eachList : binCounts) {
            for (String eachCount : eachList) {
                sum += (Integer.parseInt(eachCount));
            }
        }
        return sum;
    }

    public static List<String> getSpecAttributesList(Response searchApiResponse) {
        List<String> specAttributesList = searchApiResponse.path("refinements.key");
        return specAttributesList;
    }

    /**
     *
     * This method verifies the SortOrder/Alphanumeric order for all the categories in search api
     */
    public void assertLevelZeroCategorySortingOrder(Response response) {
        List<String> levelOneCategoryNamesList = response.getBody().jsonPath().getList("category.children.label");
        List<String> booksList = Arrays.asList(UrlBuilder.getBooksList().split(";"));
        assertEquals("Level Zero sort order is NOT as expected.", booksList, levelOneCategoryNamesList);
    }

    public void assertLevelOneCategorySortingOrder(Response response) {
        List<List<String>> categoryElements = response.getBody().jsonPath().getList("category.children.children.label");
        for (List<String> levelOneElementsList : categoryElements) {
            assertTrue(
                    "Level 1 categories should have been sorted in natural (ascending) order",
                    Ordering.natural().isOrdered(levelOneElementsList)
            );
        }
    }

    public void assertLevelTwoCategorySortingOrder(Response response) {
        List<List<List<String>>> categoryElements = response.getBody().jsonPath().getList("category.children.children.children.label");
        for (List<List<String>> levelOneElementsList : categoryElements) {
            for (List<String> levelTwoElementsList : levelOneElementsList) {
                assertTrue(
                        "Level 2 categories should have been sorted in natural (ascending) order",
                        Ordering.natural().isOrdered(levelTwoElementsList)
                );
            }
        }
    }

    public void assertLevelThreeCategorySortingOrder(Response response) {
        List<List<List<List<String>>>> categoryElements = response.getBody().jsonPath().getList("category.children.children.children.children.label");
        for (List<List<List<String>>> levelOneElementsList : categoryElements) {
            for (List<List<String>> levelTwoElementsList : levelOneElementsList) {
                for (List<String> levelThreeElementsList : levelTwoElementsList) {
                    assertTrue(
                            "Level 3 categories should have been sorted in natural (ascending) order",
                            Ordering.natural().isOrdered(levelThreeElementsList)
                    );
                }
            }
        }
    }

    /**
     *
     * This method compares all the WholeCategoryHierarchy in search api with assembler api response
     */
    public void compareLevelZeroSearchApiWithAssemblerResponse(Response searchApiResponse) {
        List<Map<String, String>> levelZeroSearchApiList = getLevelZeroSearchApiResponse(searchApiResponse, "category.children");
        assertEquals(
                "The number of categories elements (category.children) should have been as expected",
                4, levelZeroSearchApiList.size()
        );
    }

    private List<Map<String, String>> getLevelZeroSearchApiResponse(Response searchApiResponse, String categoryPath) {
        List<Map<String, String>> searchApiLevelZeroRespnseList = searchApiResponse.getBody().jsonPath().getList(categoryPath);
        List<Map<String, String>> searchApiLevelZeroResponse = new ArrayList<>();
        for (Map<String, String> searchApiMap : searchApiLevelZeroRespnseList) {
            searchApiLevelZeroResponse.add(searchApiMap);
        }
        return searchApiLevelZeroResponse;
    }

    public void compareLevelOneSearchApiWithAssemblerResponse(Response searchApiResponse, Response assemblerResponse) {
        List<Map<String, String>> levelOneSearchApiList = getLevelOneSearchApiResponse(searchApiResponse, "category.children.children");
        List<Map<String, String>> levelOneAssemblerList = getAssemblerResponseByLevel(assemblerResponse, "2");
        assertEquals(
                "Number of level 1 category items between SearchAPI and Assembler should have been the same",
                levelOneAssemblerList.size(),
                levelOneSearchApiList.size()
        );
    }

    public void compareLevelTwoSearchApiWithAssemblerResponse(Response searchApiResponse, Response assemblerResponse) {
        List<Map<String, String>> levelTwoSearchApiList = getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children");
        List<Map<String, String>> levelTwoAssemblerList = getAssemblerResponseByLevel(assemblerResponse, "3");
        assertEquals(
                "Number of level 2 category items between SearchAPI and Assembler should have been the same",
                levelTwoAssemblerList.size(),
                levelTwoSearchApiList.size()
        );
    }

    public void compareLevelThreeSearchApiWithAssemblerResponse(Response searchApiResponse, Response assemblerResponse) {
        List<Map<String, String>> levelThreeSearchApiList = getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children");
        List<Map<String, String>> levelThreeAssemblerList = getAssemblerResponseByLevel(assemblerResponse, "4");
        assertEquals(
                "Number of level 3 category items between SearchAPI and Assembler should have been the same",
                levelThreeAssemblerList.size(),
                levelThreeSearchApiList.size()
        );
    }

    /**
     *
     * This method compares all the RSID's in search api with assembler api response
     */
    public void compareLevelOneSearchApiIdWithAssemblerHirerarchyId(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiIdList = getSearchAPIIdList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerHierarchyIdList = getAssemblerHierarchyIdList(getAssemblerResponseByLevel(assemblerResponse, "2"));
        assertEquals(
                "The Level 1 category Ids between SearchAPI and Assembler should have matched",
                assemblerHierarchyIdList,
                searchApiIdList
        );
    }

    public void compareLevelTwoSearchApiIdWithAssemblerHirerarchyId(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiIdList = getSearchAPIIdList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        List<String> assemblerHierarchyIdList = getAssemblerHierarchyIdList(getAssemblerResponseByLevel(assemblerResponse, "3"));
        assertEquals(
                "The Level 2 category Ids between SearchAPI and Assembler should have matched",
                assemblerHierarchyIdList,
                searchApiIdList
        );
    }

    public void compareLevelThreeSearchApiIdWithAssemblerHirerarchyId(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiIdList = getSearchAPIIdList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        List<String> assemblerHierarchyIdList = getAssemblerHierarchyIdList(getAssemblerResponseByLevel(assemblerResponse, "4"));
        assertEquals(
                "The Level 3 category Ids between SearchAPI and Assembler should have matched",
                assemblerHierarchyIdList,
                searchApiIdList
        );
    }

    private List<String> getSearchAPIIdList(List<Map<String, String>> responseList) {
        List<String> rsIdList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            rsIdList.add(jsonResponse.get("id"));
        }
        Collections.sort(rsIdList);
        return rsIdList;
    }

    private List<String> getAssemblerHierarchyIdList(List<Map<String, String>> responseList) {
        List<String> hierarchyIdList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            hierarchyIdList.add(jsonResponse.get("heirarchyId"));
        }
        Collections.sort(hierarchyIdList);
        return hierarchyIdList;
    }

    /**
     *
     * This method compares all the category names in search api with assembler api response
     */
    public void compareLevelOneSearchApiCategoryWithAssemblerCategoryName(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiCategoryNameList = getCategoryNameList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerCategoryNameList = getLevelOneAssemblerResponseList(assemblerResponse, "label");
        assertEquals(
                "The Level 1 category names between SearchAPI and Assembler should have matched",
                assemblerCategoryNameList,
                searchApiCategoryNameList
        );
    }

    public void compareLevelTwoSearchApiCategoryWithAssemblerCategoryName(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiCategoryNameList = getCategoryNameList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        List<String> assemblerCategoryNameList = getLevelTwoAssemblerResponseList(assemblerResponse, "label");
        assertEquals(
                "The Level 2 category names between SearchAPI and Assembler should have matched",
                assemblerCategoryNameList,
                searchApiCategoryNameList
        );
    }

    public void compareLevelThreeSearchApiCategoryWithAssemblerCategoryName(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiCategoryNameList = getCategoryNameList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        List<String> assemblerCategoryNameList = getLevelThreeAssemblerResponseList(assemblerResponse, "label");
        assertEquals(
                "The Level 3 category names between SearchAPI and Assembler should have matched",
                assemblerCategoryNameList,
                searchApiCategoryNameList
        );
    }

    private List<String> getCategoryNameList(List<Map<String, String>> responseList) {
        List<String> categoryNameList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            categoryNameList.add(jsonResponse.get("label"));
        }
        Collections.sort(categoryNameList);
        return categoryNameList;
    }

    public List<String> getLevelOneAssemblerResponseList(Response assemblerResponse, String key) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<String> levelOneAsseblerResponseProductList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneAsseblerResponseProductList.add(String.valueOf(assemblerResponseLevelOneEntry.getValue().get(key).asText()));

            }
        }
        Collections.sort(levelOneAsseblerResponseProductList);
        return levelOneAsseblerResponseProductList;
    }

    private List<String> getLevelTwoAssemblerResponseList(Response assemblerResponse, String key) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<JsonNode> levelOneList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneList.add(assemblerResponseLevelOneEntry.getValue());
            }
        }
        List<String> levelTwoAsseblerResponseProductList = new ArrayList<>();
        for (JsonNode levelTwoAssemblerResponse : levelOneList) {
            Iterator<Map.Entry<String, JsonNode>> levelTwoAssemblerIterator = levelTwoAssemblerResponse.fields();
            while (levelTwoAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelTwoEntry = levelTwoAssemblerIterator.next();
                if (assemblerLevelTwoEntry.getKey().startsWith("PSS")) {
                    levelTwoAsseblerResponseProductList.add(String.valueOf(assemblerLevelTwoEntry.getValue().get("label").asText()));
                }
            }
        }

        Collections.sort(levelTwoAsseblerResponseProductList);
        return levelTwoAsseblerResponseProductList;
    }

    private List<String> getLevelThreeAssemblerResponseList(Response assemblerResponse, String key) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<JsonNode> levelOneList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneList.add(assemblerResponseLevelOneEntry.getValue());
            }
        }
        List<JsonNode> levelTwoAsseblerResponseList = new ArrayList<>();
        for (JsonNode levelTwoAssemblerResponse : levelOneList) {
            Iterator<Map.Entry<String, JsonNode>> levelTwoAssemblerIterator = levelTwoAssemblerResponse.fields();
            while (levelTwoAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelTwoEntry = levelTwoAssemblerIterator.next();
                if (assemblerLevelTwoEntry.getKey().startsWith("PSS")) {
                    levelTwoAsseblerResponseList.add(assemblerLevelTwoEntry.getValue());
                }
            }
        }

        List<String> levelThreeAssemblerResponseProductList = new ArrayList<>();
        for (JsonNode levelThreeAssemblerResponse : levelTwoAsseblerResponseList) {
            Iterator<Map.Entry<String, JsonNode>> levelThreeAssemblerIterator = levelThreeAssemblerResponse.fields();
            while (levelThreeAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelThreeEntry = levelThreeAssemblerIterator.next();
                if (assemblerLevelThreeEntry.getKey().startsWith("PSF")) {
                    levelThreeAssemblerResponseProductList.add(String.valueOf(assemblerLevelThreeEntry.getValue().get(key).asText()));
                }
            }
        }
        Collections.sort(levelThreeAssemblerResponseProductList);
        return levelThreeAssemblerResponseProductList;
    }


    /**
     *
     * This method compares all the seoCategoryName in search api with assembler api response
     */
    public void compareLevelTwoSearchApiSeoCategoryWithAssemblerSeoCategoryName(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiCategoryNameList = getSeoCategoryNameList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        List<String> assemblerCategoryNameList = getSeoCategoryNameList(getAssemblerResponseByLevel(assemblerResponse, "3"));
        assertEquals(
                "The Level 2 SEO category names between SearchAPI and Assembler should have matched",
                assemblerCategoryNameList,
                searchApiCategoryNameList
        );
    }

    public void compareLevelThreeSearchApiSeoCategoryWithAssemblerSeoCategoryName(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiCategoryNameList = getSeoCategoryNameList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        List<String> assemblerCategoryNameList = getSeoCategoryNameList(getAssemblerResponseByLevel(assemblerResponse, "4"));
        assertEquals(
                "The Level 3 category names between SearchAPI and Assembler should have matched",
                assemblerCategoryNameList,
                searchApiCategoryNameList
        );
    }

    public void compareLevelOneSearchApiSeoCategoryWithAssemblerSeoCategoryName(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiCategoryNameList = getSeoCategoryNameList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerCategoryNameList = getSeoCategoryNameList(getAssemblerResponseByLevel(assemblerResponse, "2"));
        assertEquals(
                "The Level 1 SEO category names between SearchAPI and Assembler should have matched",
                assemblerCategoryNameList,
                searchApiCategoryNameList
        );
    }

    private List<String> getSeoCategoryNameList(List<Map<String, String>> responseList) {
        List<String> seoCategoryNameList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            seoCategoryNameList.add(jsonResponse.get("seoCategoryName"));
        }
        Collections.sort(seoCategoryNameList);
        return seoCategoryNameList;
    }

    /**
     *
     *  This method compares all the TotalProductCount in search api for each category with assembler api response
     */
    public void compareLevelOneSearchApisTotalCountWithAssemblerTotalCount(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiBinCountList = getSearchApiBinCountList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerResponseBinCount = getLevelOneAssemblerResponseProductCountList(assemblerResponse);
        assertEquals(
                "The Level 1 category bin counts between SearchAPI and Assembler should have matched",
                assemblerResponseBinCount,
                searchApiBinCountList
        );
    }

    public void compareLevelTwoSearchApisTotalCountWithAssemblerTotalCount(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiBinCountList = getSearchApiBinCountList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        List<String> assemblerResponseBinCount = getLevelTwoAssemblerResponseProductCountList(assemblerResponse);
        assertEquals(
                "The Level 2 category bin counts between SearchAPI and Assembler should have matched",
                assemblerResponseBinCount,
                searchApiBinCountList
        );
    }

    public void compareLevelThreeSearchApisTotalCountWithAssemblerTotalCount(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiBinCountList = getSearchApiBinCountList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        List<String> assemblerResponseBinCount = getLevelThreeAssemblerResponseProductCountList(assemblerResponse);
        assertEquals(
                "The Level 3 category bin counts between SearchAPI and Assembler should have matched",
                assemblerResponseBinCount,
                searchApiBinCountList
        );
    }

    private List<String> getLevelOneAssemblerResponseProductCountList(Response assemblerResponse) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<String> levelOneAsseblerResponseProductList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneAsseblerResponseProductList.add(String.valueOf(assemblerResponseLevelOneEntry.getValue().get("count")));
            }
        }
        Collections.sort(levelOneAsseblerResponseProductList);
        return levelOneAsseblerResponseProductList;
    }

    private List<String> getLevelTwoAssemblerResponseProductCountList(Response assemblerResponse) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<JsonNode> levelOneList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneList.add(assemblerResponseLevelOneEntry.getValue());
            }
        }
        List<String> levelTwoAssemblerResponseProductList = new ArrayList<>();
        for (JsonNode levelTwoAssemblerResponse : levelOneList) {
            Iterator<Map.Entry<String, JsonNode>> levelTwoAssemblerIterator = levelTwoAssemblerResponse.fields();
            while (levelTwoAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelTwoEntry = levelTwoAssemblerIterator.next();
                if (assemblerLevelTwoEntry.getKey().startsWith("PSS")) {
                    levelTwoAssemblerResponseProductList.add(String.valueOf(assemblerLevelTwoEntry.getValue().get("count")));
                }
            }
        }

        Collections.sort(levelTwoAssemblerResponseProductList);
        return levelTwoAssemblerResponseProductList;
    }

    private List<String> getLevelThreeAssemblerResponseProductCountList(Response assemblerResponse) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<JsonNode> levelOneList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneList.add(assemblerResponseLevelOneEntry.getValue());
            }
        }
        List<JsonNode> levelTwoAsseblerResponseList = new ArrayList<>();
        for (JsonNode levelTwoAssemblerResponse : levelOneList) {
            Iterator<Map.Entry<String, JsonNode>> levelTwoAssemblerIterator = levelTwoAssemblerResponse.fields();
            while (levelTwoAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelTwoEntry = levelTwoAssemblerIterator.next();
                if (assemblerLevelTwoEntry.getKey().startsWith("PSS")) {
                    levelTwoAsseblerResponseList.add(assemblerLevelTwoEntry.getValue());
                }
            }
        }

        List<String> levelThreeAssemblerResponseProductList = new ArrayList<>();
        for (JsonNode levelThreeAssemblerResponse : levelTwoAsseblerResponseList) {
            Iterator<Map.Entry<String, JsonNode>> levelThreeAssemblerIterator = levelThreeAssemblerResponse.fields();
            while (levelThreeAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelThreeEntry = levelThreeAssemblerIterator.next();
                if (assemblerLevelThreeEntry.getKey().startsWith("PSF")) {
                    levelThreeAssemblerResponseProductList.add(String.valueOf(assemblerLevelThreeEntry.getValue().get("count")));
                }
            }
        }
        Collections.sort(levelThreeAssemblerResponseProductList);
        return levelThreeAssemblerResponseProductList;
    }

    private List<String> getSearchApiBinCountList(List<Map<String, String>> responseList) {
        List<String> searchApiBinCountList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            if (null != jsonResponse.get("binCount")) {
                searchApiBinCountList.add(String.valueOf(jsonResponse.get("binCount")));
            }
        }
        Collections.sort(searchApiBinCountList);
        return searchApiBinCountList;
    }

    /**
     *
     * This method compares BinCount in search api with assembler api response
     * i.e. excluding discontinued and production packed products
     */
    public Long getSearchApiBinCount(List<Map<String, String>> responseList) {
        Long searchApiBinCount = 0L;
        for (Map<String, String> jsonResponse : responseList) {
            if (null != jsonResponse.get("binCount")) {
                String integer = String.valueOf(jsonResponse.get("binCount"));
                searchApiBinCount = searchApiBinCount + Integer.parseInt(integer);
            }
        }
        return searchApiBinCount;
    }

    /**
     *
     * This method compares DimesionidList in search api with assembler api response
     */
    public void compareLevelOneSearchApiInternalIdWithAssemblerDimensionId(Response searchApiResponse, Response assemblerResponse) {
        List<JsonNode> assemblerResponseList = getLevelOneAssemblerResponseList(assemblerResponse);
        List<String> assemblerDimensionIdList = getDimensionId(assemblerResponseList);
        List<String> searchApiInternalIdList = getSearchApiInternalIdList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        assertEquals(
                "The Level 1 internal Ids between SearchAPI and Assembler should have matched",
                assemblerDimensionIdList,
                searchApiInternalIdList
        );
    }

    public void compareLevelTwoSearchApiInternalIdWithAssemblerDimensionId(Response searchApiResponse, Response assemblerResponse) {
        List<JsonNode> assemblerResponseList = getLevelTwoAssemblerResponseList(assemblerResponse);
        List<String> assemblerDimensionIdList = getDimensionId(assemblerResponseList);
        List<String> searchApiInternalIdList = getSearchApiInternalIdList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        assertEquals(
                "The Level 2 internal Ids between SearchAPI and Assembler should have matched",
                assemblerDimensionIdList,
                searchApiInternalIdList
        );
    }

    public void compareLevelThreeSearchApiInternalIdWithAssemblerDimensionId(Response searchApiResponse, Response assemblerResponse) {
        List<JsonNode> assemblerResponseList = getLevelThreeAssemblerResponseList(assemblerResponse);
        List<String> assemblerDimensionIdList = getDimensionId(assemblerResponseList);
        List<String> searchApiInternalIdList = getSearchApiInternalIdList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        assertEquals(
                "The Level 3 internal Ids between SearchAPI and Assembler should have matched",
                assemblerDimensionIdList,
                searchApiInternalIdList
        );
    }

    private List<JsonNode> getLevelOneAssemblerResponseList(Response assemblerResponse) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<JsonNode> assemblerResponseList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseEntry = assemblerResponseIterator.next();
            if (assemblerResponseEntry.getKey().startsWith("PSSS")) {
                assemblerResponseList.add(assemblerResponseEntry.getValue());
            }
        }
        return assemblerResponseList;
    }

    private List<JsonNode> getLevelTwoAssemblerResponseList(Response assemblerResponse) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<JsonNode> levelOneList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneList.add(assemblerResponseLevelOneEntry.getValue());
            }
        }
        List<JsonNode> levelTwoAssemblerResponseList = new ArrayList<>();
        for (JsonNode levelTwoAssemblerResponse : levelOneList) {
            Iterator<Map.Entry<String, JsonNode>> levelTwoAssemblerIterator = levelTwoAssemblerResponse.fields();
            while (levelTwoAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelTwoEntry = levelTwoAssemblerIterator.next();
                if (assemblerLevelTwoEntry.getKey().startsWith("PSS")) {
                    levelTwoAssemblerResponseList.add(assemblerLevelTwoEntry.getValue());
                }
            }
        }
        return levelTwoAssemblerResponseList;
    }

    private List<JsonNode> getLevelThreeAssemblerResponseList(Response assemblerResponse) {
        AssemblerResponseTree searchResponse = null;
        try {
            searchResponse = new AssemblerResponseTree(assemblerResponse.getBody().jsonPath().prettify());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> assemblerResponseIterator = searchResponse.getDimensionTree().fields();
        List<JsonNode> levelOneList = new ArrayList<>();
        while (assemblerResponseIterator.hasNext()) {
            Map.Entry<String, JsonNode> assemblerResponseLevelOneEntry = assemblerResponseIterator.next();
            if (assemblerResponseLevelOneEntry.getKey().startsWith("PSSS")) {
                levelOneList.add(assemblerResponseLevelOneEntry.getValue());
            }
        }
        List<JsonNode> levelTwoAssemblerResponseList = new ArrayList<>();
        for (JsonNode levelTwoAssemblerResponse : levelOneList) {
            Iterator<Map.Entry<String, JsonNode>> levelTwoAssemblerIterator = levelTwoAssemblerResponse.fields();
            while (levelTwoAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelTwoEntry = levelTwoAssemblerIterator.next();
                if (assemblerLevelTwoEntry.getKey().startsWith("PSS")) {
                    levelTwoAssemblerResponseList.add(assemblerLevelTwoEntry.getValue());
                }
            }
        }

        List<JsonNode> levelThreeAssemblerResponseList = new ArrayList<>();
        for (JsonNode levelThreeAssemblerResponse : levelTwoAssemblerResponseList) {
            Iterator<Map.Entry<String, JsonNode>> levelThreeAssemblerIterator = levelThreeAssemblerResponse.fields();
            while (levelThreeAssemblerIterator.hasNext()) {
                Map.Entry<String, JsonNode> assemblerLevelThreeEntry = levelThreeAssemblerIterator.next();
                if (assemblerLevelThreeEntry.getKey().startsWith("PSF")) {
                    levelThreeAssemblerResponseList.add(assemblerLevelThreeEntry.getValue());
                }
            }
        }

        return levelThreeAssemblerResponseList;
    }

    private List<String> getDimensionId(List<JsonNode> assemblerResponseList) {
        List<String> dimensionIdList = new ArrayList<>();
        for (JsonNode assemblerJsonNode : assemblerResponseList) {
            String navigationState = assemblerJsonNode.get("navigationState").textValue();
            String[] navigationArray = navigationState.split("=");
            dimensionIdList.add(navigationArray[1]);
        }
        Collections.sort(dimensionIdList);
        return dimensionIdList;
    }

    private List<String> getSearchApiInternalIdList(List<Map<String, String>> searchApiList) {
        List<String> internalIdList = new ArrayList<>();
        for (Map<String, String> searchApiRecord : searchApiList) {
            internalIdList.add(searchApiRecord.get("internalId"));
        }
        Collections.sort(internalIdList);
        return internalIdList;
    }

    /**
     *
     * This method compares SeoPageTitleList in search api with assembler api response
     */
    public void compareLevelOneSearchApiSeoPageTitleWithAssemblerSeoPageTitle(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiCategoryNameList = getSeoPageTitleList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerCategoryNameList = getSeoPageTitleList(getAssemblerResponseByLevel(assemblerResponse, "2"));
        assertEquals(
                "The Level 1 SEO Page titles between SearchAPI and Assembler should have matched",
                assemblerCategoryNameList,
                searchApiCategoryNameList
        );
    }

    public void compareLevelTwoSearchApiSeoPageTitleWithAssemblerSeoPageTitleList(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiPageTitleNameList = getSeoPageTitleList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        List<String> assemblerPageTitleList = getSeoPageTitleList(getAssemblerResponseByLevel(assemblerResponse, "3"));
        assertEquals(
                "The Level 2 SEO Page titles between SearchAPI and Assembler should have matched",
                assemblerPageTitleList,
                searchApiPageTitleNameList
        );
    }

    public void compareLevelThreeSearchApiSeoPageTitleWithAssemblerSeoPageTitleList(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiPageTitleNameList = getSeoPageTitleList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        List<String> assemblerPageTitleList = getSeoPageTitleList(getAssemblerResponseByLevel(assemblerResponse, "4"));
        assertEquals(
                "The Level 3 SEO Page titles between SearchAPI and Assembler should have matched",
                assemblerPageTitleList,
                searchApiPageTitleNameList
        );
    }

    private List<String> getSeoPageTitleList(List<Map<String, String>> responseList) {
        List<String> seoPageTitleList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            String seoPageTitle = jsonResponse.get("seoPageTitle");
            if (null != seoPageTitle && !seoPageTitle.equals("")) {
                seoPageTitleList.add(seoPageTitle);
            }

        }
        Collections.sort(seoPageTitleList);
        return seoPageTitleList;
    }

    /**
     *
     * This method compares seoURL in search api with assembler api response
     */
    public void compareLevelOneSearchApiSeoUrlWithAssemblerSeoUrl(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiSeoUrlList = getSearchApiSeoUrlList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerSeoUrlList = getAssemblerSeoUrlList(getAssemblerResponseByLevel(assemblerResponse, "2"));
        assertEquals(
                "The Level 1 SEO Urls between SearchAPI and Assembler should have matched",
                assemblerSeoUrlList,
                searchApiSeoUrlList
        );
    }

    public void compareLevelTwoSearchApiSeoUrlWithAssemblerSeoUrl(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiSeoUrlList = getSearchApiSeoUrlList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        List<String> assemblerSeoUrlList = getAssemblerSeoUrlList(getAssemblerResponseByLevel(assemblerResponse, "3"));
        assertEquals(
                "The Level 2 SEO Urls between SearchAPI and Assembler should have matched",
                assemblerSeoUrlList,
                searchApiSeoUrlList
        );
    }

    public void compareLevelThreeSearchApiSeoUrlWithAssemblerSeoUrl(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiSeoUrlList = getSearchApiSeoUrlList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        List<String> assemblerSeoUrlList = getAssemblerSeoUrlList(getAssemblerResponseByLevel(assemblerResponse, "4"));
        assertEquals(
                "The Level 3 SEO Urls between SearchAPI and Assembler should have matched",
                assemblerSeoUrlList,
                searchApiSeoUrlList
        );
    }

    private List<String> getSearchApiSeoUrlList(List<Map<String, String>> responseList) {
        List<String> seoUrlList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            String seoUrl = jsonResponse.get("seoUrl");
            if (null != seoUrl && !seoUrl.equals("")) {
                seoUrlList.add(seoUrl);
            }

        }
        Collections.sort(seoUrlList);
        return seoUrlList;
    }

    private List<String> getAssemblerSeoUrlList(List<Map<String, String>> responseList) {
        List<String> seoUrlList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            String seoUrl = jsonResponse.get("seoURL");
            if (null != seoUrl && !seoUrl.equals("")) {
                seoUrlList.add(seoUrl);
            }

        }
        Collections.sort(seoUrlList);
        return seoUrlList;
    }

    /**
     *
     * This method compares SeoMetaDescription in search api with assembler api response
     */
    public void compareLevelOneSearchApiMetaDescriptionWithAssemblerMetaDescription(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiMetaDescriptionList = getMetaDescriptionList(getLevelOneSearchApiResponse(searchApiResponse, "category.children.children"));
        List<String> assemblerMetaDescriptionList = getMetaDescriptionList(getAssemblerResponseByLevel(assemblerResponse, "2"));
        assertEquals(
                "The Level 1 Meta description(s) between SearchAPI and Assembler should have matched",
                assemblerMetaDescriptionList,
                searchApiMetaDescriptionList
        );
    }

    public void compareLevelTwoSearchApiMetaDescriptionWithAssemblerMetaDescription(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiMetaDescriptionList = getMetaDescriptionList(getLevelTwoSearchApiResponse(searchApiResponse, "category.children.children.children"));
        List<String> assemblerMetaDescriptionList = getMetaDescriptionList(getAssemblerResponseByLevel(assemblerResponse, "3"));
        assertEquals(
                "The Level 2 Meta description(s) between SearchAPI and Assembler should have matched",
                assemblerMetaDescriptionList,
                searchApiMetaDescriptionList
        );
    }

    public void compareLevelThreeSearchApiMetaDescriptionWithAssemblerMetaDescription(Response searchApiResponse, Response assemblerResponse) {
        List<String> searchApiMetaDescriptionList = getMetaDescriptionList(getLevelThreeSearchApiResponse(searchApiResponse, "category.children.children.children.children"));
        List<String> assemblerMetaDescriptionList = getMetaDescriptionList(getAssemblerResponseByLevel(assemblerResponse, "4"));
        assertEquals(
                "The Level 3 Meta description(s) between SearchAPI and Assembler should have matched",
                assemblerMetaDescriptionList,
                searchApiMetaDescriptionList
        );
    }

    private List<Map<String, String>> getAssemblerResponseByLevel(Response assemblerResponse, String categoryLevel) {
        List<Map<String, String>> assemblerPropertiesList = assemblerResponse.getBody().jsonPath().getList("mainContent[0].dimensionSearchGroups[0].dimensionSearchValues.properties");
        List<Map<String, String>> assemblerResponseByLevelList = new ArrayList<>();
        for (Map<String, String> assemblerPropertiesMap : assemblerPropertiesList) {
            if (null != assemblerPropertiesMap.get("level") && (assemblerPropertiesMap.get("level").equalsIgnoreCase(categoryLevel))) {
                assemblerResponseByLevelList.add(assemblerPropertiesMap);
            }
        }
        return assemblerResponseByLevelList;
    }

    public List<Map<String, String>> getLevelOneSearchApiResponse(Response searchApiResponse, String path) {
        List<List<Map<String, String>>> searchApiResponseByLevelList = searchApiResponse.path(path);
        List<Map<String, String>> searchApiResponseByLevel = new ArrayList<>();
        for (List<Map<String, String>> searchApiList : searchApiResponseByLevelList) {
            searchApiResponseByLevel.addAll(searchApiList);
        }
        return searchApiResponseByLevel;
    }

    public List<Map<String, String>> getLevelTwoSearchApiResponse(Response searchApiResponse, String path) {
        List<List<List<Map<String, String>>>> searchApiLevelTwoRespnseList = searchApiResponse.path(path);
        List<Map<String, String>> searchApiLevelTwoResponse = new ArrayList<>();
        for (List<List<Map<String, String>>> levelOneList : searchApiLevelTwoRespnseList) {
            for (List<Map<String, String>> levelTwoList : levelOneList) {
                searchApiLevelTwoResponse.addAll(levelTwoList);
            }
        }
        return searchApiLevelTwoResponse;
    }

    public List<Map<String, String>> getLevelThreeSearchApiResponse(Response searchApiResponse, String path) {
        List<List<List<List<Map<String, String>>>>> searchApiLevelThreeResponseList = searchApiResponse.path(path);
        List<Map<String, String>> searchApiLevelThreeResponse = new ArrayList<>();
        for (List<List<List<Map<String, String>>>> levelOneList : searchApiLevelThreeResponseList) {
            for (List<List<Map<String, String>>> levelTwoList : levelOneList) {
                for (List<Map<String, String>> levelThreeList : levelTwoList) {
                    searchApiLevelThreeResponse.addAll(levelThreeList);
                }
            }
        }
        return searchApiLevelThreeResponse;
    }

    private List<String> getMetaDescriptionList(List<Map<String, String>> responseList) {
        List<String> metaDescriptionList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            String metaDescription = jsonResponse.get("seoMetadataDescription");
            if (null != metaDescription && !metaDescription.equals("")) {
                metaDescriptionList.add(metaDescription);
            }
        }
        Collections.sort(metaDescriptionList);
        return metaDescriptionList;
    }

    /**
     *
     * This method gets AncestorCategoryDetails from assembler api response
     */
    public Map<String, String> getAssemblerAncestorDetails(Response assemblerResponse) {
        Map<String, String> assemblerAncestorDetails = new HashMap<>();
        List<Map<String, Object>> assemblerAncestorResponseList = assemblerResponse.path("mainContent[0].dimensionSearchGroups[0].dimensionSearchValues");
        for (Map<String, Object> assemblerAncestorResponse : assemblerAncestorResponseList) {

            String navigationStateObject = (String) assemblerAncestorResponse.get("navigationState");
            Map<String, String> levelThreeProperties = (HashMap) assemblerAncestorResponse.get("properties");
            assemblerAncestorDetails.put("levelThreeNavigationState", navigationStateObject.split("=")[1]);
            assemblerAncestorDetails.put("levelThreeSeoCategoryName", levelThreeProperties.get("seoCategoryName"));
            assemblerAncestorDetails.put("levelThreeHeirarchyId", levelThreeProperties.get("heirarchyId"));
            assemblerAncestorDetails.put("levelThreeSeoURL", levelThreeProperties.get("seoURL"));

            List<Map<String, Object>> ancestorDetails = (List) assemblerAncestorResponse.get("ancestors");
            Map<String, Object> levelTwoAncestorDetails = ancestorDetails.get(1);
            String levelTwonavigationStateObject = (String) levelTwoAncestorDetails.get("navigationState");
            Map<String, String> levelTwoProperties = (HashMap) levelTwoAncestorDetails.get("properties");
            assemblerAncestorDetails.put("levelTwoNavigationState", levelTwonavigationStateObject.split("=")[1]);
            assemblerAncestorDetails.put("levelTwoSeoCategoryName", levelTwoProperties.get("seoCategoryName"));
            assemblerAncestorDetails.put("levelTwoHeirarchyId", levelTwoProperties.get("heirarchyId"));
            assemblerAncestorDetails.put("levelTwoSeoURL", levelTwoProperties.get("seoURL"));

            Map<String, Object> levelOneAncestorDetails = ancestorDetails.get(0);
            String levelOnenavigationStateObject = (String) levelOneAncestorDetails.get("navigationState");
            Map<String, String> levelOneProperties = (HashMap) levelOneAncestorDetails.get("properties");
            assemblerAncestorDetails.put("levelOneNavigationState", levelOnenavigationStateObject.split("=")[1]);
            assemblerAncestorDetails.put("levelOneSeoCategoryName", levelOneProperties.get("seoCategoryName"));
            assemblerAncestorDetails.put("levelOneHeirarchyId", levelOneProperties.get("heirarchyId"));
            assemblerAncestorDetails.put("levelOneSeoURL", levelOneProperties.get("seoURL"));

        }
        return assemblerAncestorDetails;

    }

    /**
     *
     * This method gets psss child category details from assembler
     */
    public Map<String, List<SearchAPIModel>> getAssemblerCategories(Response assemblerResponse) {
        List<Map<String, Object>> assemblerList = assemblerResponse.path("mainContent[0].dimensionSearchGroups[0].dimensionSearchValues");
        Map<String, List<SearchAPIModel>> assemblerCategoriesList = new HashMap<>();
        List<SearchAPIModel> levelTwoAssemblerCategoriesList = new ArrayList<>();
        List<SearchAPIModel> levelThreeAssemblerCategoriesList = new ArrayList<>();

        for (Map<String, Object> stringObjectMap : assemblerList) {
            Map<String, String> properties = (Map<String, String>) stringObjectMap.get("properties");
            SearchAPIModel assemblerModel = new SearchAPIModel();

            String navigationState = (String) stringObjectMap.get("navigationState");
            String[] navigationArray = navigationState.split("=");
            assemblerModel.setInternalId(navigationArray[1]);
            assemblerModel.setBinCount(String.valueOf(stringObjectMap.get("count")));
            assemblerModel.setSeoCategoryName(properties.get("seoCategoryName"));
            assemblerModel.setImgUrl(properties.get("img"));
            assemblerModel.setSeoURL(properties.get("seoURL"));

            if (null != properties && properties.get("level").equalsIgnoreCase("3")) {
                levelTwoAssemblerCategoriesList.add(assemblerModel);
            } else if (null != properties && properties.get("level").equalsIgnoreCase("4")) {
                levelThreeAssemblerCategoriesList.add(assemblerModel);
            }
        }

        levelTwoAssemblerCategoriesList.sort(Comparator.comparing(SearchAPIModel::getInternalId).thenComparing(SearchAPIModel::getInternalId));
        levelThreeAssemblerCategoriesList.sort(Comparator.comparing(SearchAPIModel::getInternalId).thenComparing(SearchAPIModel::getInternalId));

        assemblerCategoriesList.put("levelTwoList", levelTwoAssemblerCategoriesList);
        assemblerCategoriesList.put("levelThreeList", levelThreeAssemblerCategoriesList);

        return assemblerCategoriesList;
    }

    /**
     *
     * This method gets psss child category details from search api
     */
    public Map<String, List<SearchAPIModel>> getSearchApiCategories(Response searchApiResponse) {
        Map<String, List<SearchAPIModel>> searchApiCategoriesList = new HashMap<>();
        List<Map<String, String>> levelTwoCategories = searchApiResponse.path("category.children");
        List<List<Map<String, String>>> levelThreeCategories = searchApiResponse.path("category.children.children");

        List<SearchAPIModel> levelTwoSearchApiCategoriesList = new ArrayList<>();
        for (Map<String, String> levelTwoCategoryMap : levelTwoCategories) {
            SearchAPIModel searchAPIModel = new SearchAPIModel();
            searchAPIModel.setInternalId(levelTwoCategoryMap.get("internalId"));
            searchAPIModel.setSeoCategoryName(levelTwoCategoryMap.get("seoCategoryName"));
            searchAPIModel.setBinCount(String.valueOf(levelTwoCategoryMap.get("binCount")));
            searchAPIModel.setImgUrl(levelTwoCategoryMap.get("imgUrl"));
            searchAPIModel.setSeoURL(levelTwoCategoryMap.get("seoUrl"));
            levelTwoSearchApiCategoriesList.add(searchAPIModel);
        }

        List<SearchAPIModel> levelThreeSearchApiCategoriesList = new ArrayList<>();
        for (List<Map<String, String>> levelThreeList : levelThreeCategories) {
            for (Map<String, String> levelThreeCategoryMap : levelThreeList) {
                SearchAPIModel searchAPIModel = new SearchAPIModel();
                searchAPIModel.setInternalId(levelThreeCategoryMap.get("internalId"));
                searchAPIModel.setSeoCategoryName(levelThreeCategoryMap.get("seoCategoryName"));
                searchAPIModel.setBinCount(String.valueOf(levelThreeCategoryMap.get("binCount")));
                searchAPIModel.setImgUrl(levelThreeCategoryMap.get("imgUrl"));
                searchAPIModel.setSeoURL(levelThreeCategoryMap.get("seoUrl"));
                levelThreeSearchApiCategoriesList.add(searchAPIModel);
            }
        }
        levelTwoSearchApiCategoriesList.sort(Comparator.comparing(SearchAPIModel::getInternalId).thenComparing(SearchAPIModel::getInternalId));
        levelThreeSearchApiCategoriesList.sort(Comparator.comparing(SearchAPIModel::getInternalId).thenComparing(SearchAPIModel::getInternalId));
        searchApiCategoriesList.put("levelTwoSearchApiList", levelTwoSearchApiCategoriesList);
        searchApiCategoriesList.put("levelThreeSearchApiList", levelThreeSearchApiCategoriesList);
        return searchApiCategoriesList;
    }

    public List<String> getSortByOptionsFromL3Category(Response searchApiResponse) {
        List<String> SortByOptions = searchApiResponse.path("resultsList.sortOptions.label");
        return SortByOptions;
    }

    public String getIds(Response searchApiResponse, String path) {
        Object iDS = searchApiResponse.path(path);
        return iDS == null ? "" : iDS.toString();
    }

    public Response getAssemblerResponse(Response SearchAPIResponse) {
        String internalId = getIds(SearchAPIResponse, "category.internalId");
        Response assemblerResponse = StepsHelper.getAssemblerTermNodeResponse(internalId);
        assertEquals(
                "The Assembler REST call was NOT successful, came back with an unexpected status code.",
                EXPECTED_OK_STATUS_CODE,
                assemblerResponse.getStatusCode()
        );
        return assemblerResponse;
    }

    public String getSeoUrl(Response sapiResponse) {
        String seoUrl = sapiResponse.path("category.seoUrl");
        return seoUrl;
    }

    public List<String> getLevelThreeIdsList(List<Map<String, String>> responseList, int minCount, int maxCount) {
        List<String> psfIdList = new ArrayList<>();
        for (Map<String, String> jsonResponse : responseList) {
            String binCount = String.valueOf(jsonResponse.get("binCount"));
            if (Integer.parseInt(binCount) >= minCount && Integer.parseInt(binCount) <= maxCount) {
                psfIdList.add(jsonResponse.get("id"));
            }
        }
        return psfIdList;
    }

    public int getTotalBinCount(List<Map<String, String>> categoryList) {
        int sum = 0;
        for (Map<String, String> eachcategory : categoryList) {
            String binCount = String.valueOf(eachcategory.get("binCount"));
            sum += (Integer.parseInt(binCount));
        }
        return sum;
    }

    public List<Map<String, String>> getSearchTermSearchApiResponse(Response searchApiResponse, String path) {
        List<List<Map<String, String>>> searchApiResponseByLevelList = searchApiResponse.path(path);
        List<Map<String, String>> searchApiResponseByLevel = new ArrayList<>();
        for (List<Map<String, String>> searchApiList : searchApiResponseByLevelList) {
            searchApiResponseByLevel.addAll(searchApiList);
        }
        return searchApiResponseByLevel;
    }
}