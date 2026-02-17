package pages.Search.SeachAPI;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ComparatorUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AssemblerResponseTree {

    JsonNode dimensionTree = null;

    public JsonNode getDimensionTree() {
        return dimensionTree;
    }

    @SuppressWarnings("unchecked")
    private Comparator<JsonNode> nodeSortComparator = ComparatorUtils
            .nullLowComparator(new Comparator<JsonNode>() {
                @Override
                public int compare(JsonNode o1, JsonNode o2) {
                    String b1 = null;
                    String b2 = null;
                    if (o1.get("book")!= null){
                        b1 = o1.get("book").asText();
                    }
                    if (o2.get("book")!= null){
                        b2 = o2.get("book").asText();
                    }
                    return ComparatorUtils.nullLowComparator(ComparatorUtils.naturalComparator()).compare(b1,
                            b2);
                }
            });

    public AssemblerResponseTree(String assemblerResponse) throws JsonProcessingException, IOException {
        JsonNode searchResponse = parseStream(assemblerResponse);
        JsonNode dimensionSearchValues = searchResponse.findValue("dimensionSearchGroups").findValue("dimensionSearchValues");
        populateDimensionTree(dimensionSearchValues);
    }

    private void populateDimensionTree(JsonNode dimensionSearchValues) {

        ObjectNode root = (ObjectNode) dimensionSearchValues.get(0);
        dimensionTree = root;
        List<JsonNode> dimensionsList = dimensionSearchValues.findParents("parentHierarchyId");
        List<JsonNode> parents = dimensionSearchValues.findParents("navigationState");
        Map<String, JsonNode> parentChildMap = new HashMap<String, JsonNode>();
        int count = 1;
        for (JsonNode jn : dimensionsList) {
            while (!parents.get(count).get("properties").has("heirarchyId")) {
                System.out.print("No properties found for item with label: " + parents.get(count).get("label"));
                count++;
            }
            parentChildMap.put(jn.get("heirarchyId").asText(), parents.get(count));
            count++;
        }
        dimensionsList.sort(nodeSortComparator);
        for (JsonNode jn : dimensionsList) {
            String hierarchyId = jn.get("heirarchyId").asText();
            if (jn.get("parentHierarchyId").asText().equals(root.get("label").asText())) {
                JsonNode parentNode = parentChildMap.get(hierarchyId);
                ObjectNode jn1 = (ObjectNode) jn;
                jn1.set("label", parentNode.get("label"));
                jn1.set("count", parentNode.get("count"));
                jn1.set("navigationState", parentNode.get("navigationState"));
                root.set(hierarchyId, jn1);

            }
            addChildren((ObjectNode) jn, hierarchyId, dimensionsList, parentChildMap);
        }
    }

    private void addChildren(ObjectNode jn, String parentHierarchyId, List<JsonNode> dimensionsList, Map<String, JsonNode> parentChildMap) {
        for (JsonNode child : dimensionsList) {
            if (child.get("parentHierarchyId").asText().equals(parentHierarchyId)) {
                String hierarchyId = child.get("heirarchyId").asText();
                JsonNode parentNode = parentChildMap.get(hierarchyId);
                ObjectNode child1 = (ObjectNode) child;
                child1.set("label", parentNode.get("label"));
                child1.set("count", parentNode.get("count"));
                child1.set("navigationState", parentNode.get("navigationState"));
                jn.set(hierarchyId, child1);

            }
        }

    }

    private static JsonNode parseStream(String sr) throws JsonProcessingException, IOException {
        JsonNode searchResponse;
        ObjectMapper mapper = new ObjectMapper();
        searchResponse = mapper.readTree(sr);
        return searchResponse;
    }
}
