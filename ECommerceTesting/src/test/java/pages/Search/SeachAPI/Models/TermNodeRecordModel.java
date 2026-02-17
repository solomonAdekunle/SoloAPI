package pages.Search.SeachAPI.Models;

import java.util.Map;

public class TermNodeRecordModel {

    private String id;
    private String label;
    private Map<String, String> specAttributes;
    private Map<String, String> propertiesMap;

    public Map<String, String> getSpecAttributes() {
        return specAttributes;
    }

    public void setSpecAttributes(Map<String, String> specAttributes) {
        this.specAttributes = specAttributes;
    }

    public Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(Map<String, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null) {
            return false;
        } else if (object instanceof TermNodeRecordModel) {
            TermNodeRecordModel termNodeRecordModel = (TermNodeRecordModel) object;
            if (((termNodeRecordModel.getId() == null && id == null) || termNodeRecordModel.getId().equals(id))
                    && ((termNodeRecordModel.getLabel() == null && label == null) || termNodeRecordModel.getLabel().equals(label))) {
                return true;
            }
        }
        return false;
    }
}
