package pages.Search.SeachAPI.Models;

public class SearchAPIModel {

    private String internalId;

    private String seoCategoryName;

    private String imgUrl;

    private String binCount;

    private String seoURL;

    public SearchAPIModel() {

    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getSeoCategoryName() {
        return seoCategoryName;
    }

    public void setSeoCategoryName(String seoCategoryName) {
        this.seoCategoryName = seoCategoryName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBinCount() {
        return binCount;
    }

    public void setBinCount(String binCount) {
        this.binCount = binCount;
    }

    public String getSeoURL() {
        return seoURL;
    }

    public void setSeoURL(String seoURL) {
        this.seoURL = seoURL;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null) {
            return false;
        } else if (object instanceof SearchAPIModel) {
            SearchAPIModel searchAPIModel = (SearchAPIModel) object;
            if (((searchAPIModel.getInternalId() == null && internalId == null) || searchAPIModel.getInternalId().equals(internalId))
                    && ((searchAPIModel.getSeoCategoryName() == null && seoCategoryName == null) || searchAPIModel.getSeoCategoryName().equals(seoCategoryName))
                    && ((searchAPIModel.getBinCount() == null && binCount == null) || searchAPIModel.getBinCount().equals(binCount))
                    && (((searchAPIModel.getImgUrl() == null || searchAPIModel.getImgUrl().equals("")) && imgUrl == null) || searchAPIModel.getImgUrl().equals(imgUrl))
                    && ((searchAPIModel.getSeoURL() == null && seoURL == null) || searchAPIModel.getSeoURL().equals(seoURL))) {
                return true;
            }
        }
        return false;
    }
}
