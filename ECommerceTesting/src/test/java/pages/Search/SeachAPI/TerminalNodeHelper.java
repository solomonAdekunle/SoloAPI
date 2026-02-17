package pages.Search.SeachAPI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.restassured.response.Response;
import pages.Search.SeachAPI.Models.TermNodeFacetValues;
import pages.Search.SeachAPI.Models.TermNodeRecordModel;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class TerminalNodeHelper {

    private final LinkedHashMap<String, List<TermNodeFacetValues>> refinementsMap = new LinkedHashMap<>();
    private final CommonHelpers helper = new CommonHelpers();
    private final StepsHelper stepsHelper = new StepsHelper();

    private void putOnlyIfKeyExistsInMap(
            String targetKey,
            Map<String, Object> sourceMap,
            String sourceKey,
            Map<String, Object> targetMap
    ) {
        Object sourceValue = sourceMap.get(sourceKey);
        if (sourceValue != null) {
            if (!(sourceValue instanceof String) && !(sourceValue instanceof Integer)) {
                fail(
                        String.format(
                                "Unexpected type: the REST API response contains an unexpected type for '%s', " +
                                        "we got type '%s' instead",
                                sourceKey, sourceValue.getClass().getName()
                        )
                );
            }

            targetMap.put(targetKey, sourceValue);
        }
    }

    public Map<String, Object> getSAPITermNodeCatDetails(Response searchApiResponse) {
        Map<String, Object> searchApiCategoriesList = new HashMap<>();
        Map<String, Object> levelThreeCategories = searchApiResponse.path("category");
        if (levelThreeCategories != null) {
            putOnlyIfKeyExistsInMap("l3label", levelThreeCategories, "label", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l3seoUrl", levelThreeCategories, "seoUrl", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l3binCount", levelThreeCategories, "binCount", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l3internalId", levelThreeCategories, "internalId", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l3seoPageTitle", levelThreeCategories, "seoPageTitle", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l3imgUrl", levelThreeCategories, "imgUrl", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l3seoCategoryName", levelThreeCategories, "seoCategoryName", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l3seoMetaDescription", levelThreeCategories, "seoMetadataDescription", searchApiCategoriesList);
        }

        Map<String, Object> levelTwoCategories = searchApiResponse.path("category.parent");
        if (levelTwoCategories != null) {
            putOnlyIfKeyExistsInMap("l2label", levelTwoCategories, "label", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l2rsId", levelTwoCategories, "id", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l2seoCategoryName", levelTwoCategories, "seoCategoryName", searchApiCategoriesList);
        }

        Map<String, Object> levelOneCategories = searchApiResponse.path("category.parent.parent");
        if (levelOneCategories != null) {
            putOnlyIfKeyExistsInMap("l1label", levelOneCategories, "label", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l1rsId", levelOneCategories, "id", searchApiCategoriesList);
            putOnlyIfKeyExistsInMap("l1seoCategoryName", levelOneCategories, "seoCategoryName", searchApiCategoriesList);
        }
        return searchApiCategoriesList;
    }

    public Map<String, Object> getAssemblerTermNodeCatDetails(Response response) {
        Map<String, Object> assemblerCategoriesList = new HashMap<>();
        assemblerCategoriesList.put("l3label", response.path("secondaryContent[0].refinementCrumbs[0].label"));
        assemblerCategoriesList.put("l3binCount", response.path("secondaryContent[0].refinementCrumbs[0].count"));

        Map<String, Object> levelThreeProperties = response.path("secondaryContent[0].refinementCrumbs[0].properties");
        if (levelThreeProperties != null) {
            putOnlyIfKeyExistsInMap("l3seoPageTitle", levelThreeProperties, "seoPageTitle", assemblerCategoriesList);
            putOnlyIfKeyExistsInMap("l3seoUrl", levelThreeProperties, "seoURL", assemblerCategoriesList);
            putOnlyIfKeyExistsInMap("l3imgUrl", levelThreeProperties, "img", assemblerCategoriesList);
            putOnlyIfKeyExistsInMap("l3seoCategoryName", levelThreeProperties, "seoCategoryName", assemblerCategoriesList);
            putOnlyIfKeyExistsInMap("l3seoMetaDescription", levelThreeProperties, "seoMetadataDescription", assemblerCategoriesList);
        }

        return assemblerCategoriesList;
    }

    public Map<String, String> getAssemblerTermNodetotalNumRecsDetails(Response response) {
        Map<String, String> assemblerCategoriesList = new HashMap<>();
        assemblerCategoriesList.put("l3binCount", response.path("mainContent[0].contents[0].totalNumRecs"));
        return assemblerCategoriesList;
    }

    public Map<String, String> getl3ParentProperties(Response assemblerResponse) {
        Map<String, String> assemblerCategoriesList = new HashMap<>();
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode levelThreeAncestors = response.findValue("secondaryContent").findValue("refinementCrumbs").findValue("ancestors");
        if (levelThreeAncestors != null) {
            List<JsonNode> parents = levelThreeAncestors.findParents("label");
            for (JsonNode parent : parents) {
                String label = parent.get("label").asText();
                JsonNode parentProperties = parent.get("properties");
                String number = parentProperties.get("level").asText();
                int level = Integer.parseInt(number) - 1;
                assemblerCategoriesList.put("l" + level + "label", label);
                assemblerCategoriesList.put("l" + level + "rsId", parentProperties.get("heirarchyId").asText());
                assemblerCategoriesList.put("l" + level + "seoCategoryName", parentProperties.get("seoCategoryName").asText());
            }
        }
        return assemblerCategoriesList;
    }

    public JsonNode parseStream(String sr) {
        JsonNode searchResponse = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            searchResponse = mapper.readTree(sr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return searchResponse;
    }

    public List<TermNodeRecordModel> getTermNodeProperties(Response searchAPIResponse) {
        JsonNode response = parseStream(searchAPIResponse.getBody().jsonPath().prettify());
        JsonNode records = response.findValue("resultsList").findValue("records");
        List<JsonNode> parents = records.findParents("id");
        List<TermNodeRecordModel> recordList = new ArrayList<>();
        for (JsonNode jn : parents) {
            TermNodeRecordModel recordModel = new TermNodeRecordModel();
            recordModel.setId(jn.get("id").asText());
            recordModel.setLabel(jn.get("label").asText());
            List<JsonNode> attr = jn.get("specificationAttributes").findParents("key");
            recordModel.setSpecAttributes(addJsonToMap(attr));
            List<JsonNode> properties = jn.get("properties").findParents("key");
            recordModel.setPropertiesMap(addJsonToMap(properties));
            recordList.add(recordModel);
        }
        return recordList;
    }

    private Map<String, String> addJsonToMap(List<JsonNode> jn) {
        Map<String, String> jsonMap = new HashMap<String, String>();
        for (JsonNode jn1 : jn) {
            jsonMap.put(jn1.get("key").asText(), jn1.get("value").asText());
        }
        return jsonMap;
    }


    public Map<String, Map<String, List<String>>> getAssemblerTermNodeProp(Response assemblerResponse) {
        List<Map<String, List<String>>> attributes = assemblerResponse.path("mainContent[0].contents[0].records.attributes");
        Map<String, Map<String, List<String>>> records = new HashMap<>();
        for (Map<String, List<String>> map : attributes) {
            String id = null;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey().equals("P_recordID"))
                    id = entry.getValue().get(0);
            }
            records.put(id, map);
        }
        return records;
    }

    public List<Map<String, List<String>>> getPreservedOrderAssemblerTermNodeProps(Response assemblerResponse) {
        return assemblerResponse.path("mainContent[0].contents[0].records.attributes");
    }

    public void assertTermNodePageAttributes(Response searchAPIResponse, Response assemblerResponse) {
        List<String> details = Arrays.asList(UrlBuilder.getSpecificationAttributesList().split(","));
        List<TermNodeRecordModel> recordList = getTermNodeProperties(searchAPIResponse);
        Map<String, Map<String, List<String>>> records = getAssemblerTermNodeProp(assemblerResponse);
        assertAssemblerRecordSize(recordList);
        assertSpecificationAttributesDisplayed(recordList, details, records);
    }

    public void assertAssemblerRecordSize(List<TermNodeRecordModel> recordList) {
        assertFalse("returned records list are more than 20", recordList.size() > 20);
    }

    public void assertSpecificationAttributesDisplayed(List<TermNodeRecordModel> recordList, List<String> details, Map<String, Map<String, List<String>>> records) {
        for (TermNodeRecordModel record : recordList) {
            for (String str : details) {
                Map<String, List<String>> assemblerRecord = records.get(record.getId());
                if (str.equals("brandURL") || str.equals("productURL") || str.equals("specificationAttributes")) {
                    Object value = str.equals("specificationAttributes") ? record.getSpecAttributes() : record.getPropertiesMap().get(str);
                    assertNotNull(String.format("%s should have been a valid value", str), value);
                } else {
                    if (!str.equals("P_manufacturerPartNumber") || !assemblerRecord.get("P_commerciallySensitive").contains("Y"))
                        if ((!str.equals("P_imagePrimary") || record.getPropertiesMap().get(str) != null))
                            assertTrue(
                                    String.format(
                                            "The SearchAPI record does NOT contain the value for the expected " +
                                                    "specification (%s) in Assembler", str
                                    ),
                                    assemblerRecord.get(str).contains(record.getPropertiesMap().get(str))
                            );
                }
            }
        }
    }


    public int getAssemblerFacetCount(Response assemblerResponse, List<String> categoryMetaData) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode navigationNode = response.findValue("headerContent").findValue("navigation");
        List<JsonNode> eachNavNode = navigationNode.findParents("dimensionName");
        int totalFacetCount = 0;
        for (JsonNode node : eachNavNode) {
            int totalCount = 0;
            String displayName = node.get("displayName").asText();
            if (categoryMetaData.contains(displayName)) {
                List<JsonNode> refinementList = node.findParents("count");
                for (JsonNode facetNode : refinementList) {
                    int count = Integer.parseInt(facetNode.get("count").asText());
                    totalCount += count;
                }
                totalFacetCount += totalCount;
            }
        }
        return totalFacetCount;
    }

    public List<String> getCategoryProperties(Response searchApiResponse) {
        Map<String, String> properties = searchApiResponse.path("category.properties");
        Map<Integer, String> map = new TreeMap<>();
        List<String> attributes = new ArrayList<>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (isNumber(entry.getKey())) {
                int k = Integer.parseInt(entry.getKey());
                map.put(k, entry.getValue());
            }
        }
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            String[] str = entry.getValue().split(",");
            if (str.length > 1) {
                Collections.addAll(attributes, str);
            } else if (str.length == 1) {
                attributes.add(str[0]);
            }
        }
        return attributes;
    }

    public boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    public Map<String, List<String>> getSearchApiRefinementProperties(Response searchApiResponse) throws Exception {
        JsonNode response = parseStream(searchApiResponse.getBody().jsonPath().prettify());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode records = response.findValue("refinements");
        List<JsonNode> parents = records.findParents("key");
        Map<String, List<String>> facetMap = new HashMap<>();
        for (JsonNode node : parents) {
            String displayName = node.get("key").asText();
            String facetInternalId = null;
            JsonNode facetNode = node.get("value");
            if (node.get("internalId") != null)
                facetInternalId = node.get("internalId").asText();
            List<String> countList = new ArrayList<>();
            List<String> dimensionList = new ArrayList<>();
            List<String> labelDimensionsList = new ArrayList<>();
            List<TermNodeFacetValues> valuesList = new ArrayList<>();
            List<JsonNode> facetList = facetNode.findParents("binCount");
            for (JsonNode nd : facetList) {
                TermNodeFacetValues values = new TermNodeFacetValues();
                values.setInternalId(nd.get("internalId").asText());
                values.setFacetInternalID(facetInternalId);
                ObjectReader reader = mapper.readerFor(new TypeReference<List<String>>() {
                });
                String dimensionId = "";
                if (nd.get("targetState") != null && !nd.get("targetState").asText().equals("null")) {
                    List<String> list = reader.readValue(nd.get("targetState").get("descriptors"));
                    values.setDescriptors(list);
                    dimensionId = nd.get("targetState").get("descriptors").get(0).asText();
                    values.setTargetStatePresent(true);
                } else {
                    values.setTargetStatePresent(false);
                    dimensionId = null;
                }
                values.setKey(displayName);
                countList.add(nd.get("binCount").asText());
                values.setBinCount(nd.get("binCount").asText());
                dimensionList.add(dimensionId);
                labelDimensionsList.add(nd.get("label").asText() + ":" + dimensionId);
                values.setLabel(nd.get("label").asText());
                if (nd.get("type") != null && nd.get("type").asText().equals("implicit"))
                    values.setType(nd.get("type").asText());
                valuesList.add(values);
            }
            facetMap.put(displayName + "binCount", countList);
            facetMap.put(displayName + "dimension", dimensionList);
            facetMap.put(displayName + ":labelDimensions", labelDimensionsList);
            refinementsMap.put(displayName, valuesList);
        }
        return facetMap;
    }

    private Map<String, List<String>> getAssemblerBinCountAndDimensions(Response assemblerResponse) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode records = response.findValue("headerContent").findValue("navigation");
        List<JsonNode> parents = records.findParents("displayName");
        Map<String, List<String>> facetCountMap = new HashMap<>();
        for (JsonNode node : parents) {
            String displayName = node.get("displayName").asText();
            if (!(displayName.equals("I18NsearchByBrandPage") || (displayName.equals("I18NSalesUnitOfMeasure"))
                    || (displayName.equals("I18NsearchBySeries")))) {
                JsonNode refinement = node.get("refinements");
                List<String> countList = new ArrayList<>();
                List<String> dimensionList = new ArrayList<>();
                List<JsonNode> facetList = refinement.findParents("navigationState");
                for (JsonNode facetNode : facetList) {
                    dimensionList.add(getDimensionId(facetNode.get("navigationState").asText()));
                    countList.add(facetNode.get("count").asText());
                }
                facetCountMap.put(displayName + "binCount", countList);
                facetCountMap.put(displayName + "dimension", dimensionList);
            }
        }
        return facetCountMap;
    }

    public boolean assertBinCountAndDimensions(Response searchResponse, Response assemblerResponse) throws Exception {
        List<String> columnHeadersList = CommonHelpers.getRecordIds(searchResponse, "refinements.key");
        Map<String, List<String>> searchAPIProperties = getSearchApiRefinementProperties(searchResponse);
        Map<String, List<String>> assemblerProperties = getAssemblerBinCountAndDimensions(assemblerResponse);
        boolean flag = false;
        if (columnHeadersList.size() == 0)
            flag = true;
        else {
            for (String facetName : columnHeadersList) {
                flag = true;
                String binCountKey = facetName + "binCount";
                String dimensionKey = facetName + "dimension";
                assertEquals(
                        "The bin count between SearchAPI and Assembler should have matched",
                        assemblerProperties.get(binCountKey), searchAPIProperties.get(binCountKey)
                );
                assertEquals(
                        "The dimension(s) between SearchAPI and Assembler should have matched",
                        assemblerProperties.get(dimensionKey), searchAPIProperties.get(dimensionKey)
                );
            }
        }
        return flag;
    }

    private String getDimensionId(String dimensionId) {
        return dimensionId.substring(dimensionId.indexOf("+") + 1);
    }

    public List<String> getFacetBrandValues(Response searchResponse) {
        List<String> keys = searchResponse.path("refinements.key");
        List<String> brandsList = new ArrayList<>();
        if (keys != null && !keys.isEmpty() && "I18NsearchBybrand".equals(keys.get(0)))
            brandsList = searchResponse.path("refinements[0].value.label");
        return brandsList;
    }

    public List<String> getAssemblerBrandList(Response assemblerResponse) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode records = response.findValue("headerContent").findValue("navigation");
        List<JsonNode> parents = records.findParents("displayName");
        List<String> facetBrandList = new ArrayList<>();
        for (JsonNode node : parents) {
            if (node.get("displayName").asText().equals("I18NsearchBybrand")) {
                List<JsonNode> labelNode = node.get("refinements").findParents("label");
                for (JsonNode nd : labelNode) {
                    facetBrandList.add(nd.get("label").asText());
                }
            }
        }
        return facetBrandList;
    }


    private Map<String, List<String>> getAttributesList(Response searchAPIResponse) {
        JsonNode response = parseStream(searchAPIResponse.getBody().jsonPath().prettify());
        JsonNode records = response.findValue("refinements");
        List<JsonNode> parents = records.findParents("key");
        Map<String, List<String>> facetMap = new HashMap<>();
        for (JsonNode node : parents) {
            String displayName = node.get("key").asText();
            JsonNode facetNode = node.get("value");
            List<String> labelList = new ArrayList<>();
            List<JsonNode> facetList = facetNode.findParents("label");
            for (JsonNode nd : facetList) {
                labelList.add(nd.get("label").asText());
            }
            facetMap.put(displayName, labelList);
        }
        return facetMap;
    }

    public boolean assertLabelOrder(Response searchResponse) {
        boolean flag = false;
        Map<String, List<String>> facetMap = getAttributesList(searchResponse);
        if (facetMap.isEmpty())
            flag = true;
        else {
            for (Map.Entry<String, List<String>> entry : facetMap.entrySet()) {
                List<String> labelSortedList = entry.getValue();
                Collections.sort(labelSortedList);
                assertEquals(
                        "Labels (facets / refinements) should have been in the expected sort order",
                        labelSortedList,
                        entry.getValue()
                );
                flag = true;
            }
        }
        return flag;
    }

    public void setAppliedDimensionDetails(Response searchAPI) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        LinkedHashMap<String, List<TermNodeFacetValues>> breadCrumbsMap = new LinkedHashMap<>();
        if (!refinementsMap.isEmpty()) {
            for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
                if (breadCrumbsMap.isEmpty() || breadCrumbsMap.size() < 2) {
                    if (entry.getValue().size() > 1) {
                        List<TermNodeFacetValues> filterList = new ArrayList<>();
                        filterList.add(entry.getValue().get(0));
                        filterList.add(entry.getValue().get(1));
                        breadCrumbsMap.put(entry.getKey(), filterList);
                    }
                }
            }
        }
        ScenarioContext.setPropertiesMap(breadCrumbsMap);
    }

    public void setAppliedDimension(Response searchAPI) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        LinkedHashMap<String, List<TermNodeFacetValues>> breadCrumbsMap = new LinkedHashMap<>();
        if (!refinementsMap.isEmpty()) {
            for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
                if (breadCrumbsMap.isEmpty() && !entry.getValue().isEmpty()) {
                    List<TermNodeFacetValues> filterList = new ArrayList<>();
                    filterList.add(entry.getValue().get(0));
                    breadCrumbsMap.put(entry.getKey(), filterList);
                }
            }
        }
        ScenarioContext.setPropertiesMap(breadCrumbsMap);
    }

    public void assertAppliedDimensions(Response searchAPI) {
        List<Map<String, Object>> breadCrumbs = searchAPI.path("breadbox.breadcrumbs");
        List<String> breadCrumbsKeyList = new ArrayList<>();
        List<String> breadCrumbsLabelList = new ArrayList<>();
        String key = null;
        for (Map<String, Object> appliedDimensions : breadCrumbs) {
            if (!appliedDimensions.get("key").equals("I18NsearchBycategory")) {
                key = (String) appliedDimensions.get("key");
                breadCrumbsKeyList.add(key);
                List<Map<String, String>> values = (List) appliedDimensions.get("value");
                for (Map<String, String> valueMap : values)
                    breadCrumbsLabelList.add(valueMap.get("label"));
            }
        }
        assertBreadCrumbs(breadCrumbsKeyList, breadCrumbsLabelList);
    }

    private void assertBreadCrumbs(List<String> breadCrumbsKeyList, List<String> breadCrumbsLabelList) {
        LinkedHashMap<String, List<TermNodeFacetValues>> dimensions = ScenarioContext.getPropertiesMap();
        List<String> orderedKeyList = new ArrayList<>(dimensions.keySet());
        List<String> orderedLabelList = new ArrayList<>();
        for (String key : orderedKeyList) {
            List<TermNodeFacetValues> facetValuesList = dimensions.get(key);
            List<String> labelList = new ArrayList<>();
            for (TermNodeFacetValues values : facetValuesList) {
                labelList.add(values.getLabel());
            }
            orderedLabelList.addAll(labelList);
        }
        assertEquals(
                "The bread crumbs keys should have been in the expected order",
                orderedKeyList,
                breadCrumbsKeyList
        );
        assertEquals(
                "The bread crumbs labels should have been in the expected order",
                orderedLabelList,
                breadCrumbsLabelList
        );
    }

    public void assertRefinementsForAppliedDimensions(Response searchResponse, Response assemblerResponse) {
        List<String> sAPIRefinementList = CommonHelpers.getRecordIds(searchResponse, "refinements.key");
        Collections.sort(sAPIRefinementList);
        List<String> assemblerRefinementList = CommonHelpers.getRecordIds(assemblerResponse, "headerContent[0].navigation.displayName");
        List<String> list = Arrays.asList(UrlBuilder.getDimensionsBlackList().split(","));
        assemblerRefinementList.removeAll(list);
        Collections.sort(assemblerRefinementList);
        assertEquals(
                "The refinement list between SearchAPI and Assembler should have been the same (in the expected order)",
                assemblerRefinementList,
                sAPIRefinementList
        );
    }

    public List<String> getRemovalDescriptorsList(Response searchAPI, String keyAttribute, String valueAttribute) {
        List<Map<String, Object>> breadCrumbs = searchAPI.path("breadbox.breadcrumbs");
        List<String> descriptorsList = null;
        for (Map<String, Object> appliedDimensions : breadCrumbs) {
            if (appliedDimensions.get("key").equals(keyAttribute)) {
                Map<String, Object> removalState = (Map) appliedDimensions.get("removalState");
                descriptorsList = (List) removalState.get("descriptors");
                List<Map<String, Object>> values = (List) appliedDimensions.get("value");
                if (valueAttribute != null && !valueAttribute.isEmpty())
                    for (Map<String, Object> valueMap : values) {
                        if (valueMap.get("label").equals(valueAttribute)) {
                            Map<String, Object> valueRemovalState = (Map) valueMap.get("removalState");
                            descriptorsList = (List) valueRemovalState.get("descriptors");
                        }
                    }
            }
        }
        return descriptorsList;
    }

    public void assertUpdateRecordCount(Response searchAPI, Response assemblerAPI) {
        String searchTotalCount = helper.getIds(searchAPI, "resultsList.pagination.count");
        String assemblerTotalRecs = helper.getIds(assemblerAPI, "mainContent[0].contents[0].totalNumRecs");
        assertEquals(
                "The pagination count between SearchAPI and Assembler should have been the same",
                assemblerTotalRecs,
                searchTotalCount
        );
    }

    public String getAssemblerDescriptorsString() {
        LinkedHashMap<String, List<TermNodeFacetValues>> refinementsMap = ScenarioContext.getPropertiesMap();
        String descriptors = "";
        List<List<TermNodeFacetValues>> descriptorList = new ArrayList<>(refinementsMap.values());
        for (List<TermNodeFacetValues> value : descriptorList) {
            for (TermNodeFacetValues values : value) {
                descriptors = descriptors.concat("+").concat(values.getDescriptors().get(0));
            }
        }
        return descriptors;
    }

    public String getSAPIDescriptorsString() {
        LinkedHashMap<String, List<TermNodeFacetValues>> refinementsMap = ScenarioContext.getPropertiesMap();
        String descriptors = "";
        List<List<TermNodeFacetValues>> descriptorList = new ArrayList<>(refinementsMap.values());
        for (List<TermNodeFacetValues> value : descriptorList) {
            for (TermNodeFacetValues values : value) {
                descriptors = descriptors.concat(values.getDescriptors().get(0)).concat(",");
            }
        }
        return descriptors;
    }

    public void assertInternalIdNotNull(Response searchAPI) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        boolean internalIdISNull = false;
        List<List<TermNodeFacetValues>> facetValues = new ArrayList<>(refinementsMap.values());
        for (List<TermNodeFacetValues> facet : facetValues) {
            for (TermNodeFacetValues values : facet) {
                internalIdISNull = values.getInternalId() == null || values.getInternalId().isEmpty();
            }
        }
        assertFalse("Internal id should NOT have been null", internalIdISNull);
    }

    public boolean assertTargetStateBlock(Response searchAPI, boolean expected) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        boolean targetStateChecked = false;
        List<List<TermNodeFacetValues>> facetValues = new ArrayList<>(refinementsMap.values());
        for (List<TermNodeFacetValues> facet : facetValues) {
            for (TermNodeFacetValues values : facet) {
                assertEquals("Target state block should have matched", expected, values.isTargetStatePresent());
                targetStateChecked = true;
            }
        }
        return targetStateChecked;
    }

    public String getRefinementsBinCount(String key, String label) {
        List<TermNodeFacetValues> facetValues = refinementsMap.get(key);
        for (TermNodeFacetValues facet : facetValues) {
            if (facet.getLabel().equals(label)) {
                return facet.getBinCount();
            }
        }
        return null;
    }

    public void assertCategoryDetails(Response SearchAPI, Response assemblerResponse) {
        Map<String, Object> searchResponseMap = getSAPITermNodeCatDetails(SearchAPI);
        Map<String, Object> assemblerTermNodeMap = getAssemblerTermNodeCatDetails(assemblerResponse);
        Map<String, String> termNodeParentMap = getl3ParentProperties(assemblerResponse);
        assertEquals(
                "Level 3 labels between SearchAPI and Assembler should have matched",
                assemblerTermNodeMap.get("l3label"),
                searchResponseMap.get("l3label")
        );
        assertEquals(
                "Level 3 SEO Urls between SearchAPI and Assembler should have matched",
                assemblerTermNodeMap.get("l3seoUrl"),
                searchResponseMap.get("l3seoUrl")
        );
        assertEquals(
                "Level 3 SEO Page Titles between SearchAPI and Assembler should have matched",
                assemblerTermNodeMap.get("l3seoPageTitle"),
                searchResponseMap.get("l3seoPageTitle")
        );
        if (searchResponseMap.get("l3imgUrl") != "" && searchResponseMap.get("l3imgUrl") != null)
            assertEquals(
                    "Level 3 Image Urls between SearchAPI and Assembler should have matched",
                    assemblerTermNodeMap.get("l3imgUrl"),
                    searchResponseMap.get("l3imgUrl")
            );
        assertEquals(
                "Level 3 SEO Category Names between SearchAPI and Assembler should have matched",
                assemblerTermNodeMap.get("l3seoCategoryName"),
                searchResponseMap.get("l3seoCategoryName")
        );
        assertEquals(
                "Level 3 Meta descriptions between SearchAPI and Assembler should have matched",
                assemblerTermNodeMap.get("l3seoMetaDescription"),
                searchResponseMap.get("l3seoMetaDescription")
        );
        assertEquals(
                "Level 2 SEO Category Names between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l2seoCategoryName"),
                searchResponseMap.get("l2seoCategoryName")
        );
        assertEquals(
                "Level 2 labels between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l2label"),
                searchResponseMap.get("l2label")
        );
        assertEquals(
                "Level 2 RS Ids between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l2rsId"),
                searchResponseMap.get("l2rsId")
        );
        assertEquals(
                "Level 1 labels between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l1label"),
                searchResponseMap.get("l1label")
        );
        assertEquals(
                "Level 1 RS ids between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l1rsId"),
                searchResponseMap.get("l1rsId")
        );
        assertEquals(
                "Level 1 SEO Category Names between SearchAPI and Assembler should have matched",
                termNodeParentMap.get("l1seoCategoryName"),
                searchResponseMap.get("l1seoCategoryName")
        );
    }

    public void assertCommerciallySensitiveProducts(Response searchAPIResponse, Response assemblerResponse) {
        final String mpnNumber = "P_manufacturerPartNumber";
        final String commerciallySensitive = "P_commerciallySensitive";
        List<TermNodeRecordModel> recordList = getTermNodeProperties(searchAPIResponse);
        Map<String, Map<String, List<String>>> records = getAssemblerTermNodeProp(assemblerResponse);
        for (TermNodeRecordModel record : recordList) {
            Map<String, List<String>> assemblerRecord = records.get(record.getId());
            if (assemblerRecord.get(commerciallySensitive).contains("N")) {
                assertTrue(
                        "The MPN between SearchAPI and Assembler should have matched",
                        assemblerRecord.get(mpnNumber).contains(record.getPropertiesMap().get(mpnNumber))
                );
            } else if (assemblerRecord.get(commerciallySensitive).contains("Y")) {
                assertNull(
                        "The MPN returned by SearchAPI should have been a valid value",
                        record.getPropertiesMap().get(mpnNumber)
                );
            }
        }
    }

    public void setL3CategoryId(List<Map<String, String>> responseList, int count) {
        for (Map<String, String> jsonResponse : responseList) {
            String binCount = String.valueOf(jsonResponse.get("binCount"));
            if (Integer.parseInt(binCount) >= count) {
                new StepsHelper().getSearchAPIPSFCategory(jsonResponse.get("id"));
                break;
            }
        }
    }

    public Map<String, String> getSearchTerms(Response response) {
        Map<String, String> searchTermMap = new HashMap<>();
        List<TermNodeRecordModel> recordList = getTermNodeProperties(response);
        for (TermNodeRecordModel record : recordList) {
            if (searchTermMap.size() < 4) {
                Map<String, String> propertyMap = record.getPropertiesMap();
                if (propertyMap.get("P_longDescription") != null && propertyMap.get("P_manufacturerPartNumber") != null &&
                        propertyMap.get("P_groupNbr") != null) {
                    String[] description = propertyMap.get("P_longDescription").split(" ");
                    searchTermMap.put("singleSearchTerm", description[1]);
                    if (description.length > 2) {
                        String multiSearchTerm = description[0] + "%20" + description[1];
                        searchTermMap.put("multiSearchTerm", multiSearchTerm);
                    }
                    String mpnValue = propertyMap.get("P_manufacturerPartNumber");
                    if (mpnValue.split(" ").length == 1)
                        searchTermMap.put("mpnValue", mpnValue);
                    searchTermMap.put("stockNumber", propertyMap.get("P_groupNbr"));
                }
            }
        }
        ScenarioContext.setSearchTermMap(searchTermMap);
        return searchTermMap;
    }

    public Map<String, List<Map<String, String>>> getAssemblerRefinementMenu(Response assemblerResponse) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode headerContent = response.findValue("headerContent");
        JsonNode navigation = headerContent.findParent("navigation");
        List<JsonNode> facets = navigation.findParents("displayName");
        Map<String, List<Map<String, String>>> categoryMap = new HashMap<>();
        for (JsonNode facet : facets) {
            if ("I18NsearchBytechnology".equals(facet.get("displayName").asText())) {
                JsonNode refinements = facet.get("refinements");
                List<JsonNode> refList = refinements.findParents("label");
                for (JsonNode category : refList) {
                    String section = category.get("properties").get("section").asText();
                    String seoUrl = category.get("properties").get("SeoURL").asText();
                    String count = category.get("count").asText();
                    String label = category.get("label").asText();
                    List<Map<String, String>> l3List;
                    Map<String, String> level3Cat;
                    if (categoryMap.get(section) == null) {
                        l3List = new ArrayList<>();
                        level3Cat = new HashMap<>();
                    } else {
                        l3List = categoryMap.get(section);
                        level3Cat = new HashMap<>();
                    }
                    level3Cat.put("label", label);
                    level3Cat.put("binCount", count);
                    level3Cat.put("seoUrl", seoUrl);
                    l3List.add(level3Cat);
                    categoryMap.put(section, l3List);
                }
            }
        }
        return categoryMap;
    }

    public List<JsonNode> getSAPICategoryBinCounts(Response searchAPIResponse) {
        JsonNode response = parseStream(searchAPIResponse.getBody().jsonPath().prettify());
        List<JsonNode> categoryNodes = null;
        JsonNode navigation = response.findValue("refinements");
        List<JsonNode> facets = navigation.findParents("key");
        for (JsonNode facet : facets) {
            if ("I18NsearchBytechnology".equals(facet.get("key").asText())) {
                categoryNodes = facet.findParents("categories");
            }
        }
        return categoryNodes;
    }

    public Map<String, String> getL2CategoryBinCounts(Response searchAPIResponse) {
        Map<String, String> categoryCountMap = new HashMap<>();
        List<JsonNode> categoryNodes = getSAPICategoryBinCounts(searchAPIResponse);
        for (JsonNode l2Category : categoryNodes)
            categoryCountMap.put(l2Category.get("label").asText(), l2Category.get("binCount").asText());
        return categoryCountMap;
    }

    public Map<String, List<Map<String, String>>> getL3CategoryBinCounts(Response searchAPIResponse) {
        Map<String, List<Map<String, String>>> categoryCountMap = new HashMap<>();
        List<JsonNode> categoryNodes = getSAPICategoryBinCounts(searchAPIResponse);
        for (JsonNode l2Category : categoryNodes) {
            String l2CatName = l2Category.get("label").asText();
            JsonNode categories = l2Category.get("categories");
            List<JsonNode> l3CatList = categories.findParents("categoryState");
            List<Map<String, String>> l3List = new ArrayList<>();
            for (JsonNode l3Category : l3CatList) {
                Map<String, String> l3CatMap = new HashMap<>();
                l3CatMap.put("label", l3Category.get("label").asText());
                l3CatMap.put("binCount", l3Category.get("binCount").asText());
                l3CatMap.put("seoUrl", l3Category.get("categoryState").get("seoUrl").asText());
                l3List.add(l3CatMap);
            }
            categoryCountMap.put(l2CatName, l3List);
        }
        return categoryCountMap;
    }

    protected int getTotalBinCount(Map<String, String> categoryMap) {
        int count = 0;
        for (String binCount : categoryMap.values())
            count += Integer.parseInt(binCount);
        return count;
    }

    public void assertExcludedProductsNotReturnedL3BinCount(Response searchAPI, Response assembler) {
        Map<String, List<Map<String, String>>> searchAPIProductCount = getL3CategoryBinCounts(searchAPI);
        Map<String, List<Map<String, String>>> assemblerProductCount = getAssemblerRefinementMenu(assembler);
        int searchApICount = 0;
        int assemblerCount = 0;
        for (Map.Entry<String, List<Map<String, String>>> entry : searchAPIProductCount.entrySet())
            for (Map<String, String> l3Cat : entry.getValue())
                searchApICount += Integer.parseInt(l3Cat.get("binCount"));
        for (Map.Entry<String, List<Map<String, String>>> entry : assemblerProductCount.entrySet()) {
            for (Map<String, String> l3Cat : entry.getValue())
                assemblerCount += Integer.parseInt(l3Cat.get("binCount"));
        }
        assertNotEquals(
                "Search API and Assembler should not have the same number of products due to customer restrictions",
                assemblerCount,
                searchApICount
        );
        assertTrue(
                "Search API should return less products than assembler due to customer restrictions",
                searchApICount < assemblerCount
        );
    }

    public void assertExcludedProductsNotReturnedWithIndividualL3BinCount(Response searchAPI, Response assembler) {
        Map<String, List<Map<String, String>>> searchAPICategories = getL3CategoryBinCounts(searchAPI);
        Map<String, List<Map<String, String>>> assemblerAPICategories = getAssemblerRefinementMenu(assembler);

        Boolean sapiCategoryHasLessProducts = doesSapiResponseHaveLowerL3CategoryBinCountsThanAssembler(searchAPICategories, assemblerAPICategories);

        assertTrue(
                "At least one searchAPI L3 category should have less products than assembler",
                sapiCategoryHasLessProducts
        );
    }

    /**
     * Helper method to get all L3 category lables and bincounts and check if
     * equivilant assembler category has a larger bincount
     *
     * @param searchAPICategories
     * @param assemblerAPICategories
     * @return
     */
    private Boolean doesSapiResponseHaveLowerL3CategoryBinCountsThanAssembler(
            Map<String, List<Map<String, String>>> searchAPICategories,
            Map<String, List<Map<String, String>>> assemblerAPICategories) {

        return searchAPICategories
                .entrySet()
                .stream()
                .anyMatch(l2Category -> {
                    return l2Category
                            .getValue()
                            .stream()
                            .anyMatch(l3Category ->
                                    equivalentAssemblerL3CatagoryHasGreaterBinCount(
                                            assemblerAPICategories, l3Category.get("label"), Integer.valueOf(l3Category.get("binCount"))
                                    )
                            );
                });
    }

    /**
     * Helper method to return true if assembler list has a category
     * matching the label provided with a larger bin count than the bincount provided
     *
     * @param assemblerAPICategories
     * @param categoryLabel
     * @param categoryBinCount
     * @return
     */
    private boolean equivalentAssemblerL3CatagoryHasGreaterBinCount(Map<String, List<Map<String, String>>> assemblerAPICategories, String categoryLabel, Integer categoryBinCount) {

        return assemblerAPICategories
                .entrySet()
                .stream()
                .anyMatch(l2AssemblerCategory ->
                        l2AssemblerCategory
                                .getValue()
                                .stream()
                                .anyMatch(l3Category ->
                                        l3Category.get("label").equals(categoryLabel)
                                                && categoryBinCount < Integer.valueOf(l3Category.get("binCount"))
                                )
                );
    }

    public Map<String, String> setL3CategorySearchTerms(List<Map<String, String>> responseList) {
        String searchTerm;
        Map<String, String> searchTermMap = new HashMap<>();
        for (Map<String, String> jsonResponse : responseList) {
            searchTerm = jsonResponse.get("label");
            String[] description = searchTerm.split(" ");
            if (description.length > 1 && !searchTerm.contains("&")) {
                searchTerm = searchTerm.replaceAll(" ", "%20");
                new StepsHelper().getSAPIResponseWithSearchQueryParam(searchTerm);
                Response response = ScenarioContext.getData(searchTerm);
                JsonNode node = parseStream(response.getBody().jsonPath().prettify());
                if (node.findValue("category") != null && response.path("category") != null) {
                    List<TermNodeRecordModel> recordList = getTermNodeProperties(response);
                    for (TermNodeRecordModel record : recordList) {
                        if (searchTermMap.size() < 3) {
                            Map<String, String> propertyMap = record.getPropertiesMap();
                            if (propertyMap.get("P_manufacturerPartNumber") != null && propertyMap.get("P_groupNbr") != null) {
                                if (description.length > 1)
                                    searchTermMap.put("searchTerm", searchTerm);
                                String mpnValue = propertyMap.get("P_manufacturerPartNumber");
                                if (mpnValue.split(" ").length == 1 && !isNumber(mpnValue)) {
                                    new StepsHelper().getSAPIResponseWithSearchQueryParam(searchTerm);
                                    Response mpnResponse = ScenarioContext.getData(searchTerm);
                                    if (mpnResponse.path("category") != null)
                                        searchTermMap.put("mpnValue", mpnValue);
                                }
                                searchTermMap.put("stockNumber", propertyMap.get("P_groupNbr"));
                            }
                        }
                    }
                    if (searchTermMap.size() == 3) {
                        ScenarioContext.setSearchTermMap(searchTermMap);
                        break;
                    }
                }
            }
        }
        return searchTermMap;
    }

    public List<String> getPageNumberBoostedProducts(Response searchAPI, String term, String psfId) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        String boostedBrand = UrlBuilder.getBoostedBrandPSFWithSearchTerm();
        String BoostedBrandBinCount = getRefinementsBinCount("I18NsearchBybrand", boostedBrand);
        Double boostedBrandStoppingPage = Double.parseDouble(BoostedBrandBinCount) / 20;
        int pageNumber = 0;
        Integer recorsIndexOnStoppingPage = Integer.parseInt(BoostedBrandBinCount) % 20;
        if (recorsIndexOnStoppingPage > 0)
            pageNumber = boostedBrandStoppingPage.intValue() + 1;
        else
            pageNumber = boostedBrandStoppingPage.intValue();
        stepsHelper.getSAPIEndOfBoostedResponse(pageNumber, psfId, term);
        Response lastBrandPageResponse = ScenarioContext.getData(term);
        List<TermNodeRecordModel> records = getTermNodeProperties(lastBrandPageResponse);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : records) {
            if (!model.getPropertiesMap().get("P_brand").equals(boostedBrand)) {
                recordIdList.add(model.getPropertiesMap().get("P_recordID"));

            }
        }
        return recordIdList;
    }

    public void assertExcludedProductsNotReturnedL2BinCount(Response searchAPI, Response assembler) {
        Map<String, String> searchAPIProductCount = getL2CategoryBinCounts(searchAPI);
        Map<String, List<Map<String, String>>> assemblerProductCount = getAssemblerRefinementMenu(assembler);
        int assemblerCount = 0;
        int searchApICount = getTotalBinCount(searchAPIProductCount);
        for (Map.Entry<String, List<Map<String, String>>> entry : assemblerProductCount.entrySet()) {
            for (Map<String, String> l2Cat : entry.getValue())
                assemblerCount += Integer.parseInt(l2Cat.get("binCount"));
        }
        assertNotEquals(
                "Search API and Assembler should not have the same number of products due to customer restrictions",
                assemblerCount,
                searchApICount
        );
        assertTrue(
                "Search API should return less products than assembler due to customer restrictions",
                searchApICount < assemblerCount
        );
    }

    public Map<String, Integer> getSAPIBrandBinCounts(Response searchAPIResponse) {
        JsonNode response = parseStream(searchAPIResponse.getBody().jsonPath().prettify());
        Map<String, Integer> brandCountMap = new HashMap<>();
        JsonNode navigation = response.findValue("refinements");
        List<JsonNode> facets = navigation.findParents("key");
        for (JsonNode facet : facets) {
            if ("I18NsearchBybrand".equals(facet.get("key").asText())) {
                List<JsonNode> brandNodes = facet.findParents("internalId");
                for (JsonNode brands : brandNodes) {
                    JsonNode label = brands.get("label");
                    JsonNode binCount = brands.get("binCount");
                    brandCountMap.put(
                            label == null ? "<Node 'label/binCount' NOT found>" : label.asText(),
                            binCount == null ? 0 : binCount.asInt()
                    );
                }
            }
        }
        return brandCountMap;
    }

    public Map<String, Integer> getAssemblerBrandBincounts(Response assemblerResponse) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode headerContent = response.findValue("headerContent");
        JsonNode navigation = headerContent.findParent("navigation");
        List<JsonNode> facets = navigation.findParents("displayName");
        Map<String, Integer> brandMap = new HashMap<>();
        for (JsonNode facet : facets) {
            if ("I18NsearchBybrand".equals(facet.get("displayName").asText())) {
                JsonNode refinements = facet.get("refinements");
                List<JsonNode> refList = refinements.findParents("label");
                for (JsonNode brand : refList) {
                    Integer count = brand.get("count").asInt();
                    String label = brand.get("label").asText();
                    brandMap.put(label, count);
                }
            }
        }
        return brandMap;
    }

    public void setLargestL3CategoryIdWithSearchTerm(List<Map<String, String>> responseList, int count, String searchTerm, String customerFilter) {
        String unescapedSearchTerm = searchTerm.replaceAll("%20", " ");
        String id = null;
        int max = 0;
        for (Map<String, String> jsonResponse : responseList) {
            if (jsonResponse.get("label").toLowerCase().contains(unescapedSearchTerm.toLowerCase())) {
                int temp = Integer.parseInt(String.valueOf(jsonResponse.get("binCount")));
                if (temp > max) {
                    max = temp;
                    id = jsonResponse.get("id");
                }
            }
        }
        assertNotNull("Level 3 category Id should have been a valid value", id);
        new StepsHelper().getTerminalNodeWithSearchTermResponseWithRestriction(searchTerm, id, customerFilter);
    }

    public void assertSearchRelevancyRanking(Response searchAPI, Response assemblerResponse) {
        List<TermNodeRecordModel> recordModels = getTermNodeProperties(searchAPI);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : recordModels)
            recordIdList.add(model.getPropertiesMap().get("P_recordID"));
        List<List<String>> list = assemblerResponse.getBody().jsonPath().getList("mainContent[0].contents[0].records.attributes.P_recordID");
        List<String> pRecordIds = new ArrayList<>();
        for (List<String> idList : list) {
            for (String id : idList) {
                pRecordIds.add(id);
            }
        }
        assertEquals(
                "The search relevancy ranking between SearchAPI and Assembler should have matched",
                pRecordIds,
                recordIdList
        );
    }

    public void assertRefinementsBinCount(Response searchAPI, Response assemblerResponse, List<String> metaData) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        int searchAPIRefinementCount = 0;
        List<List<TermNodeFacetValues>> facetValues = new ArrayList<>(refinementsMap.values());
        for (List<TermNodeFacetValues> facetList : facetValues) {
            if (metaData.contains(facetList.get(0).getKey()))
                for (TermNodeFacetValues facet : facetList) {
                    if (metaData.contains(facet.getKey())) {
                        searchAPIRefinementCount += Integer.valueOf(facet.getBinCount());
                    }
                }
        }
        int assemblerRefinementCount = getAssemblerFacetCount(assemblerResponse, metaData);
        assertEquals(
                "Refinement counts between SearchAPI and Assembler should have matched",
                assemblerRefinementCount,
                searchAPIRefinementCount
        );
    }

    public Map<String, String> setSearchTermMap(List<Map<String, String>> responseList, int count) throws Exception {
        Map<String, String> searchTermMap = new HashMap<>();
        for (Map<String, String> jsonResponse : responseList) {
            String binCount = String.valueOf(jsonResponse.get("binCount"));
            String searchTerm = jsonResponse.get("label").split(" ")[0];
            if (Integer.parseInt(binCount) >= count) {
                new StepsHelper().getSearchAPIPSFCategory(jsonResponse.get("id"));
                getSearchApiRefinementProperties(ScenarioContext.getData(ScenarioContext.getIds()));
                for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
                    if ("I18NsearchBybrand".equals(entry.getKey())) {
                        List<TermNodeFacetValues> refinements = entry.getValue();
                        for (TermNodeFacetValues facetValues : refinements) {
                            if (Integer.parseInt(facetValues.getBinCount()) > 20) {
                                searchTermMap.put("brandName", facetValues.getLabel());
                                searchTermMap.put("brandInternalId", facetValues.getInternalId());
                                searchTermMap.put("searchTerm", searchTerm);
                                break;
                            }
                        }
                    }
                }
                if (searchTermMap.size() == 3) {
                    stepsHelper.getSAPIResponseWithSearchQueryAndDimensionId(searchTermMap.get("searchTerm"), searchTermMap.get("brandInternalId"));
                    Response searchAPI = ScenarioContext.getData(ScenarioContext.getIds());
                    Object obj = searchAPI.path("category");
                    if (obj == null || obj.equals("")) {
                        ScenarioContext.setSearchTermMap(searchTermMap);
                        break;
                    }
                }
            }
        }
        return searchTermMap;
    }

    public Response getAssemblerResponseWithSearchTermAndDimensionId() {
        Map<String, String> searchTermMap = ScenarioContext.getSearchTermMap();
        String searchStr = searchTermMap.get("searchTerm");
        String dimensionId = searchTermMap.get("brandInternalId");
        Response assemblerResponse = stepsHelper.getAssemblerResponseForTermNodeSearch(searchStr, dimensionId, "0", "I18NSearchGeneric", "20");
        return assemblerResponse;
    }

    public void assertCategoryDetailsForL2Category(Response searchAPI, Response assembler) {
        Map<String, String> searchAPIProductCount = getL2CategoryBinCounts(searchAPI);
        Map<String, List<Map<String, String>>> assemblerProductCount = getAssemblerRefinementMenu(assembler);
        assertEquals(
                "The product count between SearchAPI and Assembler should have matched",
                assemblerProductCount.size(),
                searchAPIProductCount.size()
        );
        for (Map.Entry<String, List<Map<String, String>>> entry : assemblerProductCount.entrySet()) {
            assertNotNull(
                    String.format("The key '%s' should have had a valid value", entry.getKey()),
                    searchAPIProductCount.get(entry.getKey())
            );
            int assemblerCount = 0;
            for (Map<String, String> l3Cat : entry.getValue())
                assemblerCount += Integer.parseInt(l3Cat.get("binCount"));
            assertEquals(
                    "The Level 3 bin count between SearchAPI and Assembler should have matched",
                    assemblerCount,
                    Integer.parseInt(searchAPIProductCount.get(entry.getKey()))
            );
        }
    }

    public void assertCategoryDetailsForL3Category(Response searchAPI, Response assembler) {
        Map<String, List<Map<String, String>>> searchAPIProductCount = getL3CategoryBinCounts(searchAPI);
        Map<String, List<Map<String, String>>> assemblerProductCount = getAssemblerRefinementMenu(assembler);
        assertEquals(
                "The Level 3 product count between SearchAPI and Assembler should have matched",
                assemblerProductCount.size(),
                searchAPIProductCount.size()
        );
        for (Map.Entry<String, List<Map<String, String>>> entry : assemblerProductCount.entrySet()) {
            List<Map<String, String>> l2catList = searchAPIProductCount.get(entry.getKey());
            assertEquals(
                    "The Level 2 categories count between SearchAPI and Assembler should have matched",
                    entry.getValue().size(),
                    l2catList.size()
            );
            int assemblerCount = 0;
            int searchAPICount = 0;
            List<String> l3CatSeoUrl = new ArrayList<>();
            List<String> l3CatName = new ArrayList<>();
            for (Map<String, String> l3Cat : entry.getValue()) {
                l3CatSeoUrl.add(l3Cat.get("seoUrl"));
                l3CatName.add(l3Cat.get("label"));
                assemblerCount += Integer.parseInt(l3Cat.get("binCount"));
            }
            for (Map<String, String> l3Cat : l2catList) {
                assertTrue(l3Cat.get("seoUrl") + " is missing in the assembler", l3CatSeoUrl.contains(l3Cat.get("seoUrl")));
                assertTrue(l3Cat.get("label") + " is missing in the assembler", l3CatName.contains(l3Cat.get("label")));
                searchAPICount += Integer.parseInt(l3Cat.get("binCount"));
            }
            assertEquals(
                    "The Level 3 bin count between SearchAPI and Assembler should have matched",
                    assemblerCount,
                    searchAPICount
            );
        }
    }

    public int getCategoryCount(String section, Response searchAPI) {
        if ("search_super_sections".equals(section)) {
            Map<String, String> searchAPIProductCount = getL2CategoryBinCounts(searchAPI);
            return searchAPIProductCount.size();
        } else {
            Map<String, List<Map<String, String>>> searchAPIProductCount = getL3CategoryBinCounts(searchAPI);
            int l3CatCount = 0;
            for (Map.Entry<String, List<Map<String, String>>> entry : searchAPIProductCount.entrySet()) {
                l3CatCount += entry.getValue().size();
            }
            return l3CatCount;
        }
    }

    public void assertBrandNameForReturnedProducts(Response searchAPI) {
        List<TermNodeRecordModel> recordList = getTermNodeProperties(searchAPI);
        String brandSelected = ScenarioContext.getSearchTermMap().get("brandName");
        for (TermNodeRecordModel record : recordList) {
            String brandFound = record.getPropertiesMap().get("P_brand");
            assertEquals(
                    "The selected brand should have been found",
                    brandSelected,
                    brandFound
            );
        }
    }

    public void assertProductListDoesNotIncludeRestrictedBrandNames(Response searchAPI, String restrictedBrand) {
        List<TermNodeRecordModel> recordList = getTermNodeProperties(searchAPI);
        recordList.forEach( record -> {
            String brandFound = record.getPropertiesMap().get("P_brand");
            assertNotEquals(
                    String.format("%s should not be included within Search API response.", restrictedBrand),
                    restrictedBrand, brandFound);
        });
    }

    public void selectIsNewDimensionDetails(Response searchApiResponse) {
        LinkedHashMap<String, List<TermNodeFacetValues>> refinementsMap = ScenarioContext.getPropertiesMap();
        List<String> nonSpecificationAttrIsNew = Arrays.asList(UrlBuilder.getNonSpecificationAttrIsNew().split(","));
        Map<String, List<String>> specificAttributesMap = new HashMap<>();
        for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
            List<String> internalIdList = new ArrayList<>();
            if (nonSpecificationAttrIsNew.contains(entry.getKey())) {
                if ("I18NIsNew".equals(entry.getKey())) {
                    for (TermNodeFacetValues values : entry.getValue()) {
                        if (values.getLabel().equals("Y"))
                            internalIdList.add(values.getInternalId());
                    }
                } else {
                    for (TermNodeFacetValues values : entry.getValue()) {
                        internalIdList.add(values.getInternalId());
                    }
                }
                specificAttributesMap.put(entry.getKey(), internalIdList);
            }
        }
        ScenarioContext.setPropertiesAttibutesMap(specificAttributesMap);
    }

    public String getSAPIIntenalIdsString() {
        Map<String, List<String>> refinementsMap = ScenarioContext.getPropertiesAttibutesMap();
        String internalIds = "";
        List<String> internalIdList = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : refinementsMap.entrySet()) {
            internalIdList.add(String.valueOf(entry.getValue()));
        }
        internalIds = String.join(",", internalIdList);
        return internalIds.replaceAll("\\]", "").replaceAll("\\[", "").replaceAll(" ", "");


    }

    public String getAssemblerInternalIdsString() {
        Map<String, List<String>> refinementsMap = ScenarioContext.getPropertiesAttibutesMap();
        String internalIds = "";
        List<String> internalIdList = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : refinementsMap.entrySet()) {
            internalIdList.add(String.valueOf(entry.getValue()));
        }
        internalIds = String.join("+", internalIdList);
        return internalIds.replaceAll("\\]", "").replaceAll("\\[", "").replaceAll(" ", "").replace(",", "+").replace(",", "+");
    }

    public List<String> getAssemblerAnalyticsList(Response assemblerResponse) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode records = response.findValue("secondaryContent").findValue("refinementCrumbs");
        List<JsonNode> parents = records.findParents("analyticsId");
        List<String> analyticsIdsList = new ArrayList<>();
        for (JsonNode node : parents) {
            if (!analyticsIdsList.contains(node.get("analyticsId").asText()))
                analyticsIdsList.add(node.get("analyticsId").asText());
        }

        return analyticsIdsList;
    }

    public void setSpecificAppliedDimensionDetails(Response searchAPI) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        LinkedHashMap<String, List<TermNodeFacetValues>> breadCrumbsMap = new LinkedHashMap<>();
        if (!refinementsMap.isEmpty()) {
            for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
                if (!(entry.getKey().equals("I18NsearchBybrand"))) {
                    if (breadCrumbsMap.isEmpty() || breadCrumbsMap.size() < 2) {
                        if (entry.getValue().size() > 1) {
                            List<TermNodeFacetValues> filterList = new ArrayList<>();
                            filterList.add(entry.getValue().get(0));
                            filterList.add(entry.getValue().get(1));
                            breadCrumbsMap.put(entry.getKey(), filterList);
                        }
                    }
                }
            }
        }
        ScenarioContext.setPropertiesMap(breadCrumbsMap);
    }

    public List<String> getAssemblerNavigationAssemblerList(String assemblerUrl, Response assemblerResponse) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode records = response.findValue("refinementCrumbs");
        List<String> finalNavList = new ArrayList<>();
        List<List<String>> listOfLists = new ArrayList<>();
        Collection<String> splittedAssemblerCollection = Arrays.asList(assemblerUrl.split("\\+"));
        for (JsonNode node : records) {
            if (!node.get("displayName").asText().equals("I18NsearchBycategory")) {

                listOfLists.add(Arrays.asList(node.get("removeAction").get("navigationState").asText().split("N=")[1].split("\\+")));

            }
        }
        for (Collection<String> splitArrayCollection : listOfLists) {
            List<String> assemblerUrlList = new ArrayList<String>(splittedAssemblerCollection);
            assemblerUrlList.removeAll(splitArrayCollection);

            String finalValue = assemblerUrlList.get(0);
            finalNavList.add(finalValue);

        }
        return finalNavList;
    }

    public void selectLeadTimeDimensionDetails(Response searchApiResponse) {
        LinkedHashMap<String, List<TermNodeFacetValues>> refinementsMap = ScenarioContext.getPropertiesMap();
        List<String> nonSpecificationAttrLeadTime = Arrays.asList(UrlBuilder.getNonSpecificationAttrLeadTime().split(","));
        Map<String, List<String>> specificAttributesMap = new HashMap<>();
        for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
            List<String> internalIdList = new ArrayList<>();
            if (nonSpecificationAttrLeadTime.contains(entry.getKey())) {
                if ("I18NsearchByStockPolicy".equals(entry.getKey())) {
                    for (TermNodeFacetValues values : entry.getValue()) {
                        internalIdList.add(values.getInternalId());
                    }
                } else {
                    for (TermNodeFacetValues values : entry.getValue()) {
                        internalIdList.add(values.getInternalId());
                    }
                }
                specificAttributesMap.put(entry.getKey(), internalIdList);
            }
        }
        ScenarioContext.setPropertiesAttibutesMap(specificAttributesMap);
    }

    public void selectAppliedDimensionDetails(Response searchApiResponse) throws Exception {
        getSearchApiRefinementProperties(searchApiResponse);
        LinkedHashMap<String, List<TermNodeFacetValues>> internalIdsMap = new LinkedHashMap<>();
        if (!refinementsMap.isEmpty()) {
            for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {

                List<TermNodeFacetValues> filterList = new ArrayList<>();
                if (!(entry.getValue().size() < 2)) {
                    filterList.add(entry.getValue().get(0));
                    filterList.add(entry.getValue().get(1));
                    internalIdsMap.put(entry.getKey(), filterList);
                }
            }
        }
        ScenarioContext.setPropertiesMap(internalIdsMap);
    }

    public void assertAttributeIdMaps(Map<String, List<String>> searchAPIProperties, Map<String, List<String>> navigationStateMap) {
        for (Map.Entry<String, List<String>> entry : searchAPIProperties.entrySet()) {
            String key = entry.getKey();
            assertEquals(
                    String.format(
                            "The values for the key '%s' between SearchAPI and Assembler should have been the same", key
                    ),
                    navigationStateMap.get(key),
                    searchAPIProperties.get(key)
            );
        }
    }

    public Map<String, List<String>> getAssemblerAttributeIdsFromSpecificDimensions(Response assemblerResponse) {
        JsonNode response = parseStream(assemblerResponse.getBody().jsonPath().prettify());
        JsonNode records = response.findValue("headerContent").findValue("navigation");
        List<JsonNode> parents = records.findParents("displayName");
        Map<String, List<String>> facetmap = new HashMap<>();

        for (JsonNode node : parents) {
            List<String> navigationStateList = new ArrayList<>();
            String displayName = node.get("displayName").asText();
            if ((displayName.equals("I18NsearchBybrand") || (displayName.equals("I18NIsNew"))
                    || (displayName.equals("I18NsearchByStockPolicy")))) {
                JsonNode refinement = node.get("refinements");

                List<JsonNode> facetList = refinement.findParents("navigationState");
                for (JsonNode facetNode : facetList) {
                    if (!(displayName.equals("I18NIsNew") && facetNode.get("label").asText().equals("N"))) {
                        String nav = facetNode.get("navigationState").asText();
                        String splitedNav = nav.split("N=")[1];
                        String replacedNav = splitedNav.replace('+', ',');
                        String id = replacedNav.split(",")[1];

                        navigationStateList.add(id);
                    }
                }
                facetmap.put(displayName, navigationStateList);
            }
        }

        return facetmap;
    }

    public Map<String, List<String>> getDimensionIds(Response searchApi) throws Exception {
        getSearchApiRefinementProperties(searchApi);
        List<String> whiteList = Arrays.asList(UrlBuilder.getDimensionsWhiteList().split(","));
        Map<String, List<String>> nonSpecificationAttributessMap = new HashMap<>();
        for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
            List<String> internalIdList = new ArrayList<>();
            if (whiteList.contains(entry.getKey())) {
                if ("I18NIsNew".equals(entry.getKey())) {
                    for (TermNodeFacetValues values : entry.getValue()) {
                        if (values.getLabel().equals("Y"))
                            internalIdList.add(values.getInternalId());
                    }
                } else {
                    for (TermNodeFacetValues values : entry.getValue()) {
                        internalIdList.add(values.getInternalId());
                    }
                }
                nonSpecificationAttributessMap.put(entry.getKey(), internalIdList);
            }
        }
        return nonSpecificationAttributessMap;
    }

    public void assertRefinementBinCount(Response response) {
        LinkedHashMap<String, List<TermNodeFacetValues>> breadCrumbsMap = ScenarioContext.getPropertiesMap();
        JsonNode jsonResponse = parseStream(response.getBody().jsonPath().prettify());
        JsonNode records = jsonResponse.findValue("breadbox").findValue("breadcrumbs");
        List<JsonNode> parents = records.findParents("key");
        for (JsonNode node : parents) {
            String displayName = node.get("key").asText();
            if (!"I18NsearchBycategory".equals(displayName)) {
                JsonNode facetNode = node.get("value");
                List<JsonNode> values = facetNode.findParents("label");
                List<TermNodeFacetValues> facetValue = breadCrumbsMap.get(displayName);
                for (JsonNode valueNode : values) {
                    for (TermNodeFacetValues refinement : facetValue) {
                        if (valueNode.get("label").asText().equals(refinement.getLabel())) {
                            assertEquals("The bin counts should have matched", valueNode.get("binCount").asText(), refinement.getBinCount());
                            assertEquals(
                                    "The bin count and pagination count should have matched",
                                    response.path("resultsList.pagination.count"),
                                    Integer.valueOf(valueNode.get("binCount").asText())
                            );
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean hasImplicitRefinement(List<Map<String, String>> responseList, int count) throws Exception {
        Map<String, String> appliedDimensionsMap = new HashMap<>();
        for (Map<String, String> jsonResponse : responseList) {
            String binCount = String.valueOf(jsonResponse.get("binCount"));
            if (Integer.parseInt(binCount) <= count) {
                new StepsHelper().getSearchAPIPSFCategory(jsonResponse.get("id"));
                getSearchApiRefinementProperties(ScenarioContext.getData(ScenarioContext.getIds()));
                for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
                    for (TermNodeFacetValues facetValues : entry.getValue()) {
                        String internalId = facetValues.getInternalId();
                        Response response = new StepsHelper().getSearchAPIResImplicitRefinements(jsonResponse.get("id"), internalId);
                        boolean isTypeImplicit = hasImplicitRefinement(response);
                        if (isTypeImplicit)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasImplicitRefinement(Response searchAPI) throws Exception {
        getSearchApiRefinementProperties(searchAPI);
        for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
            for (TermNodeFacetValues facetValues : entry.getValue()) {
                if (facetValues.getType() != null && facetValues.getType().equals("implicit")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void validateImplicitRefinementBinCounts(Response searchAPI) throws Exception {
        int count = searchAPI.path("queryInfo.results_count");
        boolean flag = false;
        getSearchApiRefinementProperties(searchAPI);
        for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
            for (TermNodeFacetValues facetValues : entry.getValue()) {
                flag = true;
                if (facetValues.getType() != null && facetValues.getType().equals("implicit")) {
                    assertEquals(
                            "The results count and the bin count should have been the same",
                            Integer.parseInt(facetValues.getBinCount()), count
                    );
                }
            }
        }
        assertTrue("There should have been one or more refinement bin counts", flag);
    }

    public Response isInternalIdPresentInTopLevelDimension(List<Map<String, String>> responseList, int count) throws Exception {
        for (Map<String, String> jsonResponse : responseList) {
            String binCount = String.valueOf(jsonResponse.get("binCount"));
            if (Integer.parseInt(binCount) >= count) {
                new StepsHelper().getSearchAPIPSFCategory(jsonResponse.get("id"));
                getSearchApiRefinementProperties(ScenarioContext.getData(ScenarioContext.getIds()));
                for (Map.Entry<String, List<TermNodeFacetValues>> entry : refinementsMap.entrySet()) {
                    for (TermNodeFacetValues facetValues : entry.getValue()) {
                        String internalId = facetValues.getInternalId();
                        String facetInternalId = facetValues.getFacetInternalID();
                        assertNotNull(
                                "The internal facet id should have been a valid value", facetInternalId
                        );
                        if (!internalId.isEmpty())
                            return new StepsHelper().getSearchAPIResImplicitRefinements(jsonResponse.get("id"), internalId);
                    }
                }
            }
        }
        return null;
    }

    public void assertTopLevelInternalIdInBreadbox(Response searchAPI) {
        List<Map<String, Object>> dimensionList = searchAPI.path("breadbox.breadcrumbs");
        assertNotNull("The dimension list should have been a valid value", dimensionList);
        for (Map<String, Object> dimensionMap : dimensionList) {
            String internalId = (String) dimensionMap.get("internalId");
            assertNotNull("The internal id should have been a valid value", internalId);
        }
    }
}
