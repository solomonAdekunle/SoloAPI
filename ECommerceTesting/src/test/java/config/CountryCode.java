package config;


import stepdefs.SharedDriver;

import java.util.HashMap;

public class CountryCode{

    private String countryCode;

    public CountryCode(String site){
        this.countryCode = setCountryCode(site);
    }

    public String getCountryCode(String site){
        return this.countryCode;
    }


    public String setCountryCode(String site) {
        HashMap<String, String> countryList = new HashMap<>();
        countryList.put("Australian", "au");
        countryList.put("Austrian", "at");
        countryList.put("Czech", "cz");
        countryList.put("Danish", "dk");
        countryList.put("Dutch", "nl");
        countryList.put("German", "de");
        countryList.put("Hungarian", "hu");
        countryList.put("Italian", "it");
        countryList.put("Irish", "ie");
        countryList.put("Japanese", "jp");
        countryList.put("Malaysian", "my");
        countryList.put("New Zealand", "nz");
        countryList.put("Norwegian", "no");
        countryList.put("Phillipines", "ph");
        countryList.put("Polish", "pl");
        countryList.put("Portuguese", "pt");
        countryList.put("Singapore", "sg");
        countryList.put("South African", "za");
        countryList.put("South Korean", "kr");
        countryList.put("Spanish", "es");
        countryList.put("Swedish", "se");
        countryList.put("Thai", "th");
        countryList.put("UK", "uk");

        if (System.getProperty("env").contains("st2")) {
            countryList.put("Belgian - Flemish", "benl");
            countryList.put("Belgian - French", "befr");
            countryList.put("Chinese", "china");
            countryList.put("French", "f1");
            countryList.put("Hong Kong English", "hk01");
            countryList.put("Hong Kong Chinese", "hk02");
            countryList.put("Swiss", "dech");
            countryList.put("Taiwanese English", "tw01");
            countryList.put("Taiwanese Chinese", "tw02");
        } else if (System.getProperty("env").contains("st1")) {
            countryList.put("Belgian - Flemish", "be02");
            countryList.put("Belgian - French", "be01");
            countryList.put("Chinese", "cn");
            countryList.put("French", "f1");
            countryList.put("Hong Kong English", "hk01");
            countryList.put("Hong Kong Chinese", "hk02");
            countryList.put("Swiss", "ch");
            countryList.put("Taiwanese English", "tw01");
            countryList.put("Taiwanese Chinese", "tw02");
        } else {
            countryList.put("Belgian - Flemish", "benl");
            countryList.put("Belgian - French", "befr");
            countryList.put("Chinese", "china");
            countryList.put("French", "fr");
            countryList.put("Hong Kong English", "hken");
            countryList.put("Hong Kong Chinese", "hkcn");
            countryList.put("Swiss", "ch");
            countryList.put("Taiwanese English", "twen");
            countryList.put("Taiwanese Chinese", "twcn");

        }
        return countryList.get(site);
    }
}
