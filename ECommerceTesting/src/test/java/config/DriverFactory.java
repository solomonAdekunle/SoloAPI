package config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Sort out various names for the driver system property.
 * Sometimes it's derived from RunCukes, other times from props files, other times from a system property. Needs consistency
 */
public class DriverFactory {

    public static RemoteWebDriver driver;
    private PropertiesReader properties;

    public DriverFactory() {
        properties = new PropertiesReader();
    }

    /**
     * Get a Web Driver instance
     *
     * @return a local or remote webdriver instance based on system config
     */
    public WebDriver getDriver() {
        WebDriver driver = null;

        try {
            // Attempt to init driver. Exit if not available as no point continuing
            if (RunCukesTest.useBrowserStack || Boolean.parseBoolean(System.getProperty("runFromCMD"))) {
                driver = getRemoteDriver();
            } else {
                driver = getLocalDriver();
            }
        } catch (WebDriverException e) {
            System.err.println("Issue initialising Remote web driver: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL provided for remote driver: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (driver == null) {
            throw new RuntimeException("An unknown issue occurred creating a web driver instance. If running locally, rerun in debug with breakpoints in the getDriver method");
        }

        driver.manage().timeouts().implicitlyWait(Long.parseLong(properties.getProperty("driver.implicit.wait")), TimeUnit.SECONDS);

        System.out.println("Returning Driver: " + driver);
        return driver;
    }

    /**
     * Launch a webdriver instance on Browserstack
     *
     * @return remote browser instance
     * @throws MalformedURLException if an invalid URL is provided to the browserstack wd hub
     * @throws WebDriverException    if there was some issue initialising Browserstack connection
     */
    private WebDriver getRemoteDriver() throws MalformedURLException, WebDriverException {
        String USERNAME = properties.getProperty("browserstack.username");
        String AUTOMATE_KEY = properties.getProperty("browserstack.automate_key");

        String url = "https://hub-cloud.browserstack.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("acceptInsecureCerts", true);

        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", AUTOMATE_KEY);

        caps.setCapability("name", String.format("Local run by %s on %s", System.getProperty("user.name"), System.getProperty("os.name")));
        caps.setCapability("project", String.format("Ecommerce Testing on %s with coutry %s", System.getProperty("env"), System.getProperty("country")));

        if (System.getenv("BUILD_TAG") != null) {
            caps.setCapability("build", System.getenv("BUILD_TAG"));
        }

        if (Boolean.parseBoolean(System.getProperty("runFromCMD"))) {
            caps.setCapability("browser", System.getProperty("browser"));
            caps.setCapability("browser_version", System.getProperty("browserVersion"));
            caps.setCapability("os", System.getProperty("os"));
            caps.setCapability("os_version", System.getProperty("osVersion"));
            caps.setCapability("browserstack.debug", "true");
        } else if (System.getProperty("browserstack.local").equals("true") && System.getProperty("browserstack.localIdentifier") != null) {
            caps.setCapability("browser", RunCukesTest.browserStackBrowser);
            caps.setCapability("browser_version", RunCukesTest.browserStackBrowserVersion);
            caps.setCapability("os", RunCukesTest.browserStackOS);
            caps.setCapability("os_version", RunCukesTest.browserStackOSVersion);
            caps.setCapability("acceptSslCerts", "true");
            caps.setCapability("browserstack.local", "true");
            caps.setCapability("browserstack.localIdentifier", RunCukesTest.browserStackLocalIdentifier);
            caps.setCapability("browserstack.debug", RunCukesTest.browserStackDebug);
            caps.setCapability("resolution", "1920x1080");
            caps.setCapability("project", "DevCI Test - Monolith");
            System.getProperties().put("https.proxyHost", "10.251.26.93");
            System.getProperties().put("https.proxyPort", "3128");
        } else {
            caps.setCapability("browser", RunCukesTest.browserStackBrowser);
            caps.setCapability("browser_version", RunCukesTest.browserStackBrowserVersion);
            caps.setCapability("os", RunCukesTest.browserStackOS);
            caps.setCapability("os_version", RunCukesTest.browserStackOSVersion);
            caps.setCapability("browserstack.local", RunCukesTest.browserStackLocal);
            caps.setCapability("acceptSslCerts", "true");
            caps.setCapability("resolution", "1920x1080");
        }

        return new RemoteWebDriver(new URL(url), caps);
    }

    private String disableCrossOriginResourceSharing() {
        System.out.println("Cross Origin Resource Sharing has been disabled within DriverFactory for " + System.getProperty("country"));
        return "disable-web-security";
    }

    /**
     * Launch a local webdriver instance
     *
     * @return local webdriver instance
     * @throws IllegalArgumentException unrecognised webdriver specified
     */
    private WebDriver getLocalDriver() throws IllegalArgumentException {
        WebDriver webDriver;
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean local = Boolean.parseBoolean(System.getProperty("local", "false"));
        boolean legacy = Boolean.parseBoolean(System.getProperty("legacy", "false"));
        String requiredDriver = System.getProperty("driver.type", "chrome").toUpperCase();
        boolean useWebDriverManager = Boolean.parseBoolean(System.getProperty("useWebDriverManager", "false"));
        String chromeUserAgentVersion = System.getProperty("chromeUserAgent", "UNKNOWN");

        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("start-maximized", "disable-extensions", "disable-plugins", "no-sandbox", "dns-prefetch-disable", "no-proxy-server");
        chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
        corsSwitch(chromeOptions);

        if (System.getProperty("localChromeOnJenkins", "false").equalsIgnoreCase("true")) {
            chromeOptions.addArguments("headless", "disable-gpu");
            chromeOptions.addArguments("windows-size=1920x1080");
            return new ChromeDriver(chromeOptions);
        }

        String team = System.getProperty("team", "others").toUpperCase();
        if ("CATALYST".equals(team)) {
            ChromeOptions options = new ChromeOptions();
            if (Boolean.parseBoolean(System.getProperty("linux"))) {
                System.setProperty("webdriver.chrome.driver", "/home/jenkins/drivers/chromedriver");
                options.addArguments("headless");

                System.out.println("OBTAINED LOCAL DRIVER: ");
                return new ChromeDriver(options);
            }
        }
        switch (requiredDriver) {
            case "FIREFOX":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("headless");
                }
                webDriver = new FirefoxDriver();
                break;
            case "CHROME":
                // When running on boxes like Jenkins with no network, we need to retrieve the
                // webdriver binaries from Nexus
                System.setProperty("driver.type", "CHROME");

                // Only use driver managers if the system property for the location of the driver is not set
                if (System.getProperty("webdriver.chrome.driver") == null) {
                    if (headless) {
                        if (local || useWebDriverManager) {
                            WebDriverManager.chromedriver().setup();
                        } else {
                            if (!legacy) {
                                System.setProperty("webdriver.chrome.driver", "chromedriver");
                            } else {
                                System.setProperty("webdriver.chrome.driver", "chromedriver241");
                            }
                        }
                    } else {
                        WebDriverManager.chromedriver().setup();
                    }
                }

                if (headless) {
                    chromeOptions.addArguments("headless", "disable-gpu");
                    chromeOptions.addArguments("windows-size=1920x1080");
                    String userAgentArgument =
                            String.format("--user-agent=Mozilla/5.0 (Linux; SAMSUNG SM-T550 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/3.3 Chrome/%s Safari/537.36",
                                    // Upgrading WebDriverManager from 3.8.0 to 5.5.2 means the name of the method getDownloadedVersion() would need changing to a new name
                                    WebDriverManager.chromedriver().getDownloadedDriverVersion() != null ? WebDriverManager.chromedriver().getDownloadedDriverVersion() : chromeUserAgentVersion);
                    chromeOptions.addArguments(userAgentArgument);
                }

                webDriver = new ChromeDriver(chromeOptions);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            case "EDGE":
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                webDriver = new EdgeDriver(options);
                break;
            case "IE11": //TODO - unable to test it
                //TODO - only try it, when you can change security settings on this #£%$*&^@

                // Upgrading WebDriverManager from 3.8.0 to 5.5.2 means the name of the method version() would need changing to a new name
                WebDriverManager.iedriver().arch32().driverVersion("3.8.0").setup();
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.introduceFlakinessByIgnoringSecurityDomains();
                ieOptions.destructivelyEnsureCleanSession();
                webDriver = new InternetExplorerDriver(ieOptions);
                break;
            default:
                throw new IllegalArgumentException("Unknown driver type");
        }

        return webDriver;
    }

    private void corsSwitch(ChromeOptions chromeOptions) {
        if (System.getProperty("env").equalsIgnoreCase("discoverydev1")) {
            chromeOptions.addArguments(disableCrossOriginResourceSharing());
        }
    }

}
