package pages.Search.SeachAPI;

import io.restassured.response.Response;
import org.omg.CORBA.PRIVATE_MEMBER;
import pages.Search.SeachAPI.Models.TermNodeFacetValues;

import java.util.*;

public class ScenarioContext {
    private static String Ids;
    private static List<String> sAPIattributesList;
    private static List<String> searchAPIFacetBinCount;
    private static List<String>  assemblerPRecordIds;
    private static List<String>  searchApiRecordIds;

    private static List<String> propertiesList;

    private static LinkedHashMap<String, List<TermNodeFacetValues>> propertiesMap;
    private static Map<String, List<String>> PropertiesAttibutesMap;
    private static Map<String, String> searchTermMap;
    private static ThreadLocal<Map<String, Response>> context = new ThreadLocal<>();
    private static ThreadLocal<HashMap<String, Object>> sessionData = new ThreadLocal<HashMap<String, Object>>() {
        @Override
        protected HashMap<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    public static LinkedHashMap<String, List<TermNodeFacetValues>> getPropertiesMap() {
        return propertiesMap;
    }

    public static void setPropertiesMap(LinkedHashMap<String, List<TermNodeFacetValues>> propertiesMap) {
        ScenarioContext.propertiesMap = propertiesMap;
    }

    public static void put(Map<String, Response> map) {context.set(map);}

    public static Map<String, Response> get(final String name) {return context.get();}

    public static <T> void setData(String key, Object value) {sessionData.get().put(key, value);}

    public static <T> T getData(final String key) {return (T) sessionData.get().get(key);}

    public static String getIds() {return Ids;}

    public static void setIds(String ids) {Ids = ids;}

    public static Map<String, String> getSearchTermMap() {return searchTermMap;}

    public static void setSearchTermMap(Map<String, String> searchTermMap) {ScenarioContext.searchTermMap = searchTermMap; }

    public static List<String> getSearchApiRecordIds() {return searchApiRecordIds;}

    public static void setSearchApiRecordIds(List<String> searchApiRecordIds) {ScenarioContext.searchApiRecordIds = searchApiRecordIds; }

    public static List<String> getAssemblerPRecordIds() {return assemblerPRecordIds; }

    public static void setAssemblerPRecordIds(List<String> assemblerPRecordIds) {ScenarioContext.assemblerPRecordIds = assemblerPRecordIds;}

    public static List<String> getSearchAPIFacetBinCount() {return searchAPIFacetBinCount;}

    public static void setSearchAPIFacetBinCount(List<String> searchAPIFacetBinCount) {ScenarioContext.searchAPIFacetBinCount = searchAPIFacetBinCount;}

    public static List<String> getsAPIattributesList() {return sAPIattributesList;}

    public static void setsAPIattributesList(List<String> sAPIattributesList) {ScenarioContext.sAPIattributesList = sAPIattributesList;}

    public static Map<String, List<String>> getPropertiesAttibutesMap() {return PropertiesAttibutesMap;}

    public static void setPropertiesAttibutesMap(Map<String, List<String>> specificAttributesMap) {ScenarioContext.PropertiesAttibutesMap = specificAttributesMap;
    }

    public static List<String> getPropertiesList() {return propertiesList;}

    public static void setPropertiesList(List<String> propertiesList) {ScenarioContext.propertiesList = propertiesList;}
}

