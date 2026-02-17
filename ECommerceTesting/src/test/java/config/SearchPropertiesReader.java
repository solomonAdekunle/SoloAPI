package config;

import java.util.Properties;


public class SearchPropertiesReader extends PropertiesReader {

    private Properties countryProperties;

    public SearchPropertiesReader() {
        String country = System.getProperty("country");
        Country countryCode;
        if (country == null) {
            System.setProperty("country", defaultCountry);
            countryCode = Country.uk;
        } else {

            switch (country) {
                case "uk":
                    countryCode = Country.uk;
                    break;
                case "f1":
                case "fr":
                    countryCode = Country.f1;
                    break;
                case "de":
                    countryCode = Country.de;
                    break;
                case "sg":
                    countryCode = Country.sg;
                    break;
                case "jp":
                    countryCode = Country.jp;
                    break;
                case "cn":
                case "china":
                    countryCode = Country.cn;
                    break;
                case "at":
                    countryCode = Country.at;
                    break;
                case "befr":
                case "be01":
                    countryCode = Country.be01;
                    break;
                case "benl":
                case "be02":
                    countryCode = Country.be02;
                    break;
                case "au":
                    countryCode = Country.au;
                    break;
                case "hken":
                case "hk01":
                    countryCode = Country.hk01;
                    break;
                case "hkcn":
                case "hk02":
                    countryCode = Country.hk02;
                    break;
                case "my":
                    countryCode = Country.my;
                    break;
                case "nz":
                    countryCode = Country.nz;
                    break;
                case "ph":
                    countryCode = Country.ph;
                    break;
                case "za":
                    countryCode = Country.za;
                    break;
                case "kr":
                    countryCode = Country.kr;
                    break;
                case "twen":
                case "tw01":
                    countryCode = Country.tw01;
                    break;
                case "twcn":
                case "tw02":
                    countryCode = Country.tw02;
                    break;
                case "th":
                    countryCode = Country.th;
                    break;
                case "cz":
                    countryCode = Country.cz;
                    break;
                case "dk":
                    countryCode = Country.dk;
                    break;
                case "nl":
                    countryCode = Country.nl;
                    break;
                case "hu":
                    countryCode = Country.hu;
                    break;
                case "it":
                    countryCode = Country.it;
                    break;
                case "ie":
                    countryCode = Country.ie;
                    break;
                case "no":
                    countryCode = Country.no;
                    break;
                case "pl":
                    countryCode = Country.pl;
                    break;
                case "pt":
                    countryCode = Country.pt;
                    break;
                case "es":
                    countryCode = Country.es;
                    break;
                case "se":
                    countryCode = Country.se;
                    break;
                case "dech":
                    countryCode = Country.dech;
                    break;
                case "ch":
                    countryCode = Country.ch;
                    break;
                default:
                    countryCode = Country.uk;
                    System.out.println("Country '" + country + "' not recognised, using 'uk' properties file.");
            }
        }

        init(countryCode);
    }

    private void init(Country country) {
        Properties generalProperties = new Properties();
        String generalPropertiesFileName = "search_data/general.properties";
        initProperties(generalProperties, generalPropertiesFileName);

        countryProperties = new Properties(generalProperties);
        String countryPropertiesFileName = "search_data/" + getPropertiesFileNameFor(country);
        initProperties(countryProperties, countryPropertiesFileName);
    }

    public String getProperty(String property) {
        return countryProperties.getProperty(property);
    }

}
