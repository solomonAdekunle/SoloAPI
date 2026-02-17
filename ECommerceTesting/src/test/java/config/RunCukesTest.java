package config;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumber",
                "json:target/cucumber.json",
                "cucumber.StepReporter"
        },
        features = "src/test/resources/cucumber/",
        glue = {"stepdefs"},
        tags = {"@dont"})

public class RunCukesTest {
    // TestRail Config
    public static boolean testRailOutput = Boolean.parseBoolean(System.getProperty("testrail.enabled","false"));

    public static boolean rerunSuccessfulTests = testRailOutput && Boolean.parseBoolean(System.getProperty("testrail.rerunSuccessful","false"));

    //Update testRailTestPlan for execution of Automated Regression only

    public static String testRailTestPlan = System.getProperty("testrail.planid", "136008");

    public static boolean endeca = Boolean.parseBoolean(System.getProperty("endeca", "false"));
    //Update testRailTestEndecaPlan for execution of Endeca Automated Regression only
    public static String testRailTestEndecaPlan = System.getProperty("testrail.planid", "127238");


    // BrowserStack Config
    public static boolean useBrowserStack = Boolean.parseBoolean(System.getProperty("browserstack.enabled", "false"));
    public static String browserStackBrowser = System.getProperty("browserstack.browser.name", "Chrome");
    public static String browserStackBrowserVersion = System.getProperty("browserstack.browser.version", "72.0");
    public static String browserStackOS = System.getProperty("browserstack.os.name", "Windows");
    public static String browserStackOSVersion = System.getProperty("browserstack.os.version", "10");
    public static String browserStackLocal = System.getProperty("browserstack.local", "false");
    public static String browserStackLocalIdentifier = System.getProperty("browserstack.localIdentifier", "true");
    public static String browserStackDebug = System.getProperty("browserstack.debug", "true");

    /**
     * Only works when using test rail API
     * True = run blocked tests
     * False = do not run blocked tests
     */
    public static boolean runBlockedTests = false;
    /**
     * Set to true if you need all none Endeca API tests to embed a screenshot of the 'final state' of the test
     * and output to the Cucumber report.
     */
    public static boolean outputScreenShotForAllTests = false;

    //Dont need to touch these values if using a testPlan
    public static String testRailSuiteID = "";
    public static String testRailRunID = "";
    public static String mileStoneID = "";
    public static String testRailProjectID = "89";
}
