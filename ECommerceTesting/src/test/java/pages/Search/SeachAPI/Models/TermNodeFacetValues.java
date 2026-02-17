package pages.Search.SeachAPI.Models;

import java.util.List;

public class TermNodeFacetValues {

    private String Key;
    private String facetInternalID;
    private String label;
    private String binCount;
    private String brand;
    private String searchQuery;
    private List<String> descriptors;
    private String rsId;
    private String seoUrl;
    private String internalId;
    private String type;
    private boolean targetStatePresent;

    public String getKey() { return Key; }

    public void setKey(String key) {Key = key; }

    public String getLabel() {return label; }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBinCount() {
        return binCount;
    }

    public void setBinCount(String binCount) {
        this.binCount = binCount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<String> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<String> descriptors) {
        this.descriptors = descriptors;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) { this.rsId = rsId; }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }

    public String getInternalId() { return internalId; }

    public void setInternalId(String internalId) { this.internalId = internalId; }

    public boolean isTargetStatePresent() { return targetStatePresent; }

    public void setTargetStatePresent(boolean targetStatePresent) { this.targetStatePresent = targetStatePresent; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getFacetInternalID() { return facetInternalID; }

    public void setFacetInternalID(String facetInternalID) { this.facetInternalID = facetInternalID; }

}
