package config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class MarketConfigReader {

    private Properties countryProperties;

    public MarketConfigReader() {
        this(System.getProperty("country"));
    }

    public MarketConfigReader(String country) {
        Country countryCode;

        if (country == null) {
            countryCode = Country.uk;
        } else {

            switch (country) {
                case "uk":
                    countryCode = Country.uk;
                    break;
                case "fr":
                case "f1":
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
                case "hk01":
                case "hken":
                    countryCode = Country.hk01;
                    break;
                case "hkcn":
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
                case "tw01":
                case "twen":
                    countryCode = Country.tw01;
                    break;
                case "tw02":
                case "twcn":
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
                    countryCode = Country.dech;
                    break;
                default:
                    countryCode = Country.uk;
                    System.out.println("Country '" + country + "' not recognised, using 'uk' properties file.");
            }
        }

        init(countryCode);
    }

    public MarketConfigReader(Country country) {
        init(country);
    }

    private static char hexDigit(char ch, int offset) {
        int value = (ch >> offset) & 0xF;

        if (value <= 9)
            return (char) ('0' + value);

        return (char) ('A' + value - 10);
    }

    private static String escapeString(String str) {
        StringBuilder result = new StringBuilder();

        int length = str.length();
        for (int i = 0; i < length; i++) {
            char character = str.charAt(i);
            if (character <= 0x007e) {
                result.append(character);
                continue;
            }

            result.append('\\');
            result.append('u');
            result.append(hexDigit(character, 12));
            result.append(hexDigit(character, 8));
            result.append(hexDigit(character, 4));
            result.append(hexDigit(character, 0));
        }
        return result.toString();
    }

    private void init(Country country) {
        Properties generalProperties = new Properties();
        String generalPropertiesFileName = "data/MarketConfig/generalMarketConfig.properties";
        initProperties(generalProperties, generalPropertiesFileName);

        countryProperties = new Properties(generalProperties);
        String countryPropertiesFileName = "data/MarketConfig/" + getPropertiesFileNameFor(country);
        initProperties(countryProperties, countryPropertiesFileName);
    }

    private void initProperties(Properties properties, String propertiesFileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        if (inputStream != null) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                while (true) {
                    int character = inputStreamReader.read();
                    if (character < 0)
                        break;

                    stringBuilder.append((char) character);
                }

                String inputString = escapeString(stringBuilder.toString());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes("ISO-8859-1"));

                properties.load(byteArrayInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Properties file not found '" + propertiesFileName + "'");
        }
    }

    public Boolean getProperty(String property) {
        return Boolean.parseBoolean(countryProperties.getProperty(property));
    }

    private String getPropertiesFileNameFor(Country country) {
        String fileName;

        switch (country) {
            case uk:
                fileName = "ukMarketConfig.properties";
                break;
            case f1:
            case fr:
                fileName = "f1MarketConfig.properties";
                break;
            case de:
                fileName = "deMarketConfig.properties";
                break;
            case sg:
                fileName = "sgMarketConfig.properties";
                break;
            case jp:
                fileName = "jpMarketConfig.properties";
                break;
            case cn:
            case china:
                fileName = "cnMarketConfig.properties";
                break;
            case at:
                fileName = "atMarketConfig.properties";
                break;
            case befr:
            case be01:
                fileName = "be01MarketConfig.properties";
                break;
            case benl:
            case be02:
                fileName = "be02MarketConfig.properties";
                break;
            case my:
                fileName = "myMarketConfig.properties";
                break;
            case au:
                fileName = "auMarketConfig.properties";
                break;
            case hken:
            case hk01:
                fileName = "hk01MarketConfig.properties";
                break;
            case hkcn:
            case hk02:
                fileName = "hk02MarketConfig.properties";
                break;
            case cz:
                fileName = "czMarketConfig.properties";
                break;
            case dk:
                fileName = "dkMarketConfig.properties";
                break;
            case nl:
                fileName = "nlMarketConfig.properties";
                break;
            case hu:
                fileName = "huMarketConfig.properties";
                break;
            case it:
                fileName = "itMarketConfig.properties";
                break;
            case ie:
                fileName = "ieMarketConfig.properties";
                break;
            case no:
                fileName = "noMarketConfig.properties";
                break;
            case ph:
                fileName = "phMarketConfig.properties";
                break;
            case pl:
                fileName = "plMarketConfig.properties";
                break;
            case pt:
                fileName = "ptMarketConfig.properties";
                break;
            case es:
                fileName = "esMarketConfig.properties";
                break;
            case se:
                fileName = "seMarketConfig.properties";
                break;
            case nz:
                fileName = "nzMarketConfig.properties";
                break;
            case dech:
                fileName = "dechMarketConfig.properties";
                break;
            case kr:
                fileName = "krMarketConfig.properties";
                break;
            case twen:
            case tw01:
                fileName = "tw01MarketConfig.properties";
                break;
            case twcn:
            case tw02:
                fileName = "tw02MarketConfig.properties";
                break;
            case th:
                fileName = "thMarketConfig.properties";
                break;
            case za:
                fileName = "zaMarketConfig.properties";
                break;
            default:
                fileName = "ukMarketConfig.properties";
        }

        return fileName;
    }

    private enum Country {uk, f1, fr, de, sg, jp, cn, china, at, be01, befr, be02, benl, au, hk01, hken, hk02, hkcn, my, nz, ph, za, kr, tw01, twen, tw02, twcn, th, cz, dk, nl, hu, it, ie, no, pl, pt, es, se, dech}
}


