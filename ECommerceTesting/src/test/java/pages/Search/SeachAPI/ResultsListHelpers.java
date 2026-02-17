package pages.Search.SeachAPI;

import io.restassured.response.Response;
import pages.Search.SeachAPI.Models.TermNodeRecordModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C0952563 on 01/05/2019.
 */
public class ResultsListHelpers {

    StepsHelper stepsHelper;
    TerminalNodeHelper termNodeHelper;

    public List<String> getPageNumberBoostedProducts(String boostedBrand,String term,String psfId){

        String BoostedBrandBinCount = termNodeHelper.getRefinementsBinCount("I18NsearchBybrand", boostedBrand);
        Double boostedBrandStoppingPage = Double.parseDouble(BoostedBrandBinCount) / 20;
        int pageNumber = 0;
        Integer recorsIndexOnStoppingPage = Integer.parseInt(BoostedBrandBinCount) % 20;
        if (recorsIndexOnStoppingPage > 0)
            pageNumber = boostedBrandStoppingPage.intValue() + 1;
        else
            pageNumber = boostedBrandStoppingPage.intValue();
        stepsHelper.getSAPIEndOfBoostedResponse(pageNumber,psfId, term);
        Response lastBrandPageResponse = ScenarioContext.getData(term);
        List<TermNodeRecordModel> records = termNodeHelper.getTermNodeProperties(lastBrandPageResponse);
        List<String> recordIdList = new ArrayList<>();
        for (TermNodeRecordModel model : records) {
            if (!model.getPropertiesMap().get("P_brand").equals(boostedBrand)) {
                recordIdList.add(model.getPropertiesMap().get("P_recordID"));

            }
        }
        int listSize = recordIdList.size();
        return recordIdList;
    }
}
