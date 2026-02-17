package config;

import pages.Page;

/**
 * Environment
 * Provides functionality for accessing and determining the currently configured environment
 */
public enum Environment {

    //Production environment
    prod("", null, null, false),
    //Static 1 environment (Pre-Production/Prep)
    st1("st1-", null, null, false),
    //Static 2 environment
    st2("ngsp-st2-", null, null, false),
    //Release Candidate environment
    rc(null, "http://eclazjappr01e1.ebs.ecomp.com/web/?locale=%s", "http://eclazjappr01a1.ebs.ecomp.com/web/?locale=%s", true),
    discoverydev(null, "http://10.251.26.110/web/?storeOffline=N&locale=%s&searchConfig=1", "http://10.251.26.106/web/?storeOffline=N&locale=%s&searchConfig=1", true),
    discoverydev1(null, "http://10.251.26.110/hftest/?locale=%s", "http://10.251.26.110/hftest/?locale=%s", true),
    //Dev environment, accessible to all teams but primarily used by Red Squadron (SAP integrated dev environment)
    dev(null, "http://dev-%s.ebs.ecomp.com/web/", "http://dev-%s.ebs.ecomp.com/web/", true),
    //Local using desktop-in-docker
    local(null, "https://localhost:8443/web/?locale=%s", "https://localhost:8443/web/?locale=%s", true);

    private static Environment current; // Cached lookup value
    private String urlPrefix, emeaURL, apacURL, jopURL, shapeURL, qmURL;
    private boolean azureEnv;

    Environment(String urlPrefix, String emeaURL, String apacURL, boolean azureEnv) {
        this.urlPrefix = urlPrefix;
        this.azureEnv = azureEnv;
        this.emeaURL = emeaURL;
        this.apacURL = apacURL;
    }

    /**
     * Sets the currently configured environment. Intended for use with setting the env from system
     * props
     *
     * @param env environment to set
     * @throws NullPointerException     where specified string is null
     * @throws IllegalArgumentException where specified environment string is not a recognised enum
     *                                  value
     */
    private static void setCurrent(String env)
            throws NullPointerException, IllegalArgumentException {
        try {
            setCurrent(Environment.valueOf(env));
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("A valid \"env\" must be specified. Value provided is: " + env);
            throw e;
        }
    }

    /**
     * Gets the currently configured environment, will set the current environment if not already
     * set
     *
     * @return the environment based on the env system property
     */
    public static Environment getCurrent() {
        // Cache the value to prevent unnecessary lookups
        if (current == null) {
            setCurrent(System.getProperty("env", "st1"));
        }

        return current;
    }

    /**
     * Sets the currently configured environment
     *
     * @param env environment to set
     */
    public static void setCurrent(Environment env) {
        System.setProperty("env", env.toString());
        current = env;
    }

    /**
     * Determines if the current environment is production
     *
     * @return true if currently in production
     */
    public static boolean isProduction() {
        return Environment.getCurrent().equals(Environment.prod);
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public String getEmeaURL() {
        return emeaURL;
    }

    public String getApacURL() {
        return apacURL;
    }

    public String getHomepageURL() {
        String baseUrl = ".rs-online.com/web/";
        String homepageURL;
        String searchConfig = getSearchConfig();

        if (searchConfig.equals("cascade")) {
            homepageURL = baseUrl + "?searchConfig=0";
        }
        else if (searchConfig.equals("simplified")){
            homepageURL = baseUrl + "?searchConfig=1";
        } else {
            homepageURL = baseUrl;
        }

        return homepageURL;
    }



    public String getHomepageWithNonWebURL() {
        String homepageURL = ".rs-online.com/";
        return homepageURL;
    }
    public String getHomepageWithInvalidPathURL() {
        String homepageURL = ".rs-online.com/xxxx/";
        return homepageURL;
    }
    public String getHomepageWithInvalidCategoryPath() {
        String homepageURL = ".rs-online.com/web/xxxx";
        return homepageURL;
    }

    private String getSearchConfig() {
        String searchConfig = System.getProperty("searchConfig");
        return searchConfig == null ? "": searchConfig;
    }


    public String getWrappersearchURL(){
        String wrappersearchURL= "http://10.251.26.146:8085/Wrapper-Search";
        return wrappersearchURL;
    }


    /**
     * Get the url for the environment
     *
     * @param country country to visit
     * @return the url
     */
    public String getURL(String country) {
        if (!azureEnv) {
            if (System.getProperty("env").equals("st1") && System.getProperty("country").equals("cn")) {
                return "https://st1rsonline.cn/web/";
            } else if(System.getProperty("env").equals("prod") && System.getProperty("country").equals("cn")){
                return "https://rsonline.cn/web/";
            } else {
                return "https://" + this.getUrlPrefix() + country + getHomepageURL();
            }
        } else {
            String regionUrl = Page.PROPS_READER.getProperty("dev.ci.region").equals("emea")
                    ? this.getEmeaURL()
                    : this.getApacURL();
            return String.format(regionUrl, country);
        }
    }

    public String getURLWithNoWeb(String country) {
        if (!azureEnv) {
            if (System.getProperty("env").equals("st1") && System.getProperty("country").equals("cn")) {
                return "https://st1rsonline.cn/";
            } else {
                return "https://" + this.getUrlPrefix() + country + getHomepageWithNonWebURL();
            }
        } else {
            String regionUrl = Page.PROPS_READER.getProperty("dev.ci.region").equals("emea")
                    ? this.getEmeaURL()
                    : this.getApacURL();
            return String.format(regionUrl, country);
        }
    }

    public String getURLWithInvalidText(String country) {
        if (!azureEnv) {
            if (System.getProperty("env").equals("st1") && System.getProperty("country").equals("cn")) {
                return "https://st1rsonline.cn/xxxx/";
            } else {
                return "https://" + this.getUrlPrefix() + country + getHomepageWithInvalidPathURL();
            }
        } else {
            String regionUrl = Page.PROPS_READER.getProperty("dev.ci.region").equals("emea")
                    ? this.getEmeaURL()
                    : this.getApacURL();
            return String.format(regionUrl, country);
        }
    }


    public String getURLWithInvalidCategoryPath(String country) {
        if (!azureEnv) {
            if (System.getProperty("env").equals("st1") && System.getProperty("country").equals("cn")) {
                return "https://st1rsonline.cn/web/xxxx";
            } else {
                return "https://" + this.getUrlPrefix() + country + getHomepageWithInvalidCategoryPath();
            }
        } else {
            String regionUrl = Page.PROPS_READER.getProperty("dev.ci.region").equals("emea")
                    ? this.getEmeaURL()
                    : this.getApacURL();
            return String.format(regionUrl, country);
        }
    }
}



