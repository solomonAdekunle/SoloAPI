package config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesReader {

    protected final String defaultCountry = "uk";
    private Properties marketConfigproperties;


    public PropertiesReader() {
        this(System.getProperty("country"));
    }

    public PropertiesReader(String country) {
        Country countryCode;

        if (country == null) {
            System.setProperty("country", defaultCountry);
            country = defaultCountry;
        }

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
            case "china":
            case "cn":
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

        init(countryCode);
    }

    public PropertiesReader(Country country) {
        init(country);
    }

    public String getProperty(String property) {
        return marketConfigproperties.getProperty(property);
    }

    protected void initProperties(Properties properties, String propertiesFileName) {
       InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        if (inputStream != null) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
               BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                while (true) {
                    int character = bufferedReader.read();
                    if (character < 0)
                        break;

                    stringBuilder.append((char) character);
                }

                String inputString = PropsUtils.escapeString(stringBuilder.toString());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes("ISO-8859-1"));

                properties.load(byteArrayInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Properties file not found '" + propertiesFileName + "'");
        }
    }

    protected String getPropertiesFileNameFor(Country country) {
        String fileName;

        switch (country) {
            case uk:
                fileName = "uk.properties";
                break;
            case f1:
            case fr:
                fileName = "f1.properties";
                break;
            case de:
                fileName = "de.properties";
                break;
            case sg:
                fileName = "sg.properties";
                break;
            case jp:
                fileName = "jp.properties";
                break;
            case cn:
            case china:
                fileName = "cn.properties";
                break;
            case at:
                fileName = "at.properties";
                break;
            case befr:
            case be01:
                fileName = "be01.properties";
                break;
            case benl:
            case be02:
                fileName = "be02.properties";
                break;
            case my:
                fileName = "my.properties";
                break;
            case au:
                fileName = "au.properties";
                break;
            case hken:
            case hk01:
                fileName = "hk01.properties";
                break;
            case hkcn:
            case hk02:
                fileName = "hk02.properties";
                break;
            case cz:
                fileName = "cz.properties";
                break;
            case dk:
                fileName = "dk.properties";
                break;
            case nl:
                fileName = "nl.properties";
                break;
            case hu:
                fileName = "hu.properties";
                break;
            case it:
                fileName = "it.properties";
                break;
            case ie:
                fileName = "ie.properties";
                break;
            case no:
                fileName = "no.properties";
                break;
            case ph:
                fileName = "ph.properties";
                break;
            case pl:
                fileName = "pl.properties";
                break;
            case pt:
                fileName = "pt.properties";
                break;
            case es:
                fileName = "es.properties";
                break;
            case se:
                fileName = "se.properties";
                break;
            case nz:
                fileName = "nz.properties";
                break;
            case dech:
                fileName = "dech.properties";
                break;
            case kr:
                fileName = "kr.properties";
                break;
            case twen:
            case tw01:
                fileName = "tw01.properties";
                break;
            case twcn:
            case tw02:
                fileName = "tw02.properties";
                break;
            case th:
                fileName = "th.properties";
                break;
            case za:
                fileName = "za.properties";
                break;
            case ch:
                fileName = "ch.properties";
                break;
            default:
                fileName = "uk.properties";
        }

        return fileName;
    }

    private void init(Country country) {
        Properties generalProperties = new Properties();

        String environmentName = Environment.getCurrent().name().equalsIgnoreCase("local") ? "dev" : Environment.getCurrent().name();

        String generalPropertiesFileName = "data/" + environmentName + "/" + "general.properties";
        initProperties(generalProperties, generalPropertiesFileName);

        Properties countryProperties = new Properties(generalProperties);
        String countryPropertiesFileName = "data/" + environmentName + "/" + getPropertiesFileNameFor(country);
        initProperties(countryProperties, countryPropertiesFileName);

        Properties generalMarketConfig = new Properties(countryProperties);
        String generalConfigPropertiesFileName = "data/MarketConfig/generalMarketConfig.properties";
        initProperties(generalMarketConfig, generalConfigPropertiesFileName);

        marketConfigproperties = new Properties(generalMarketConfig);
        String configPropertiesFileName = "data/MarketConfig/" + getMarketConfigFileNameFor(country);
        initProperties(marketConfigproperties, configPropertiesFileName);

        Properties searchProperties = new Properties(generalProperties);
        String searchPropertiesFileName = "data/" + environmentName + "/" + getPropertiesFileNameFor(country);
        initProperties(searchProperties, searchPropertiesFileName);

        marketConfigproperties.putAll(System.getProperties());
    }

    private String getMarketConfigFileNameFor(Country country) {
        String fileName;

        switch (country) {
            case uk:
                fileName = "ukMarketConfig.properties";
                break;
            case fr:
            case f1:
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
            case ch:
                fileName = "chMarketConfig.properties";
                break;
            default:
                fileName = "ukMarketConfig.properties";
        }

        return fileName;
    }

    protected enum Country {uk, f1, fr, de, sg, jp, cn, china, at, be01, befr, be02, benl, au, hk01, hken, hk02, hkcn, my, nz, ph, za, kr, tw01, twen, tw02, twcn, th, cz, dk, nl, hu, it, ie, no, pl, pt, es, se, dech, ch}
}
