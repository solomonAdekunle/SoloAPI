package stepdefs.hooks;

import config.*;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.ScenarioImpl;
import edu.emory.mathcs.backport.java.util.Collections;
import errors.ApplicationError;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ApplicationErrorPage;
import stepdefs.SharedDriver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static config.RunCukesTest.outputScreenShotForAllTests;
import static org.junit.Assert.assertTrue;

/**
 * Created by e0653032 on 17/04/2019.
 */
public class TestRailHooks {

    public static TestRailAPI testRailAPI = new TestRailAPI();
    public static String testSuiteID;
    public static String testSection;
    private static JSONArray cases;
    public static String testCaseID;
    public static String testRunID;
    public static Boolean ignoredRetest = false;
    public static Boolean rerunSuccessfulTests = RunCukesTest.rerunSuccessfulTests;
    public static Boolean notInScope = false;
    public static String status;
    public static Config runConfig = new Config(RunCukesTest.testRailOutput, RunCukesTest.testRailProjectID, RunCukesTest.testRailSuiteID, RunCukesTest.testRailRunID, RunCukesTest.mileStoneID, RunCukesTest.runBlockedTests);
    public static JSONArray sections;
    private static final Logger logger = LoggerFactory.getLogger(SharedDriver.class);
    private static final Map<Scenario, ApplicationError> applicationErrors = new ConcurrentHashMap<>();
    private static final String SKIPPING_TEST_ERROR_MESSAGE = "Skipping test based on its testrail status.";
    private String server;
    private Map<String, Object> sharedData = Collections.synchronizedMap(new HashMap());
    private final SharedDriver sharedDriver;
    private HashMap<String, List<String>> errorLogs = new HashMap<>();

    public TestRailHooks(SharedDriver sharedDriver) {
        this.sharedDriver = sharedDriver;
    }

    @Before
    public void apiSetUp(Scenario scenario) throws Exception {
        OrdersLogger.init();

        if (testInScope(scenario)) {
            notInScope = true;
            org.junit.Assume.assumeTrue(false);
        }

        if (runConfig.getTestRailOutput()) {
            //set up the testRailAPI Connection and log in as testRail user
            testRailAPI.setupAPI();

            //Set the value of currentBrowser and currentCountry to the relevant values
            String currentBrowser = getCurrentBrowser();
            String currentCountry = getCurrentCountry(scenario);

            JSONObject plan;

            //Get the testPlan JSONObject based on the ID supplied in RunCukesTest
            if (RunCukesTest.endeca) {
                RunCukesTest.testRailTestPlan = RunCukesTest.testRailTestEndecaPlan;
            }

            plan = testRailAPI.getTestPlan(RunCukesTest.testRailTestPlan);

            //Set the projectID as that of the TestPlan's project ID
            runConfig.setTestRailProjectID(String.valueOf(plan.get("project_id")));

            //Get all of the entries in the returned plan
            JSONArray planEntries = (JSONArray) plan.get("entries");

            //Get all of the configurations available in the project
            JSONArray configs = testRailAPI.getConfigs(plan.get("project_id").toString());

            //Get the Browser and Country config values and create a hashmap of their names and the IDs
            HashMap<String, String> browserConfigOptions = getConfigHashMaps(configs, "Browser");
            HashMap<String, String> countryConfigOptions = getConfigHashMaps(configs, "Country");

            //Set the browser Config Code to the ID of the relevant configuration option
            String currentBrowserConfigCode = browserConfigOptions.get(currentBrowser);
            //if there is no matching config in the group, add it and set the currentBrowserConfig to it
            if (currentBrowserConfigCode == null) {
                currentBrowserConfigCode = addConfigToGroup(configs, "Browser", currentBrowser);
            }
            String currentCountryConfigCode = countryConfigOptions.get(currentCountry);
            if (currentCountryConfigCode == null) {
                currentCountryConfigCode = addConfigToGroup(configs, "Country", currentCountry);
            }

            JSONObject Run = new JSONObject();
            String featureName = getFeatureTagName(scenario);
            JSONArray runs;

            //Get any entries in the testplan with the name matching the feature file of the scenario being run
            List<JSONObject> allMatchingEntries = matchListFromJsonArray(planEntries, "name", featureName);

            //if not null entry exists in plan, and can be set
            if (allMatchingEntries != null) {
                for (JSONObject Entry : allMatchingEntries) {
                    testSuiteID = Entry.get("suite_id").toString();
                    runConfig.setTestRailSuiteID(testSuiteID);
                    runs = (JSONArray) Entry.get("runs");
                    Run = matchStringFromJsonArray(runs, "config_ids", currentBrowserConfigCode, currentCountryConfigCode);
                    if (Run != null) {
                        break;
                    }
                }
            } else {
                //If no matching Run can be found, see if the project has a suite with the same name as the feature file
                JSONArray projectSuites = testRailAPI.getProjectSuites(plan.get("project_id").toString());
                JSONObject suiteNotAdded = matchStringFromJsonArray(projectSuites, "name", featureName);
                //If there is no suite found in the project, suiteNotAdded will be Null
                if (suiteNotAdded != null) {
                    //Add a run of the suite with the matching name, and all the test cases within it
                    JSONObject newEntry = testRailAPI.addTestPlanEntry(RunCukesTest.testRailTestPlan, suiteNotAdded.get("id").toString(), suiteNotAdded.get("name").toString(), browserConfigOptions.get(currentBrowser), countryConfigOptions.get(currentCountry));
                    testSuiteID = newEntry.get("suite_id").toString();
                    runConfig.setTestRailSuiteID(testSuiteID);
                    runs = (JSONArray) newEntry.get("runs");
                    Run = matchStringFromJsonArray(runs, "config_ids", currentBrowserConfigCode, currentCountryConfigCode);
                } else {
                    //Add a suite and then add a run of that suite, by adding each test case run to it
                    JSONObject newSuite = testRailAPI.newTestSuite(featureName, "Selenium Automation pack test suite");
                    JSONObject newEntry2 = testRailAPI.addTestPlanEntry(RunCukesTest.testRailTestPlan, newSuite.get("id").toString(), newSuite.get("name").toString(), browserConfigOptions.get(currentBrowser), countryConfigOptions.get(currentCountry));
                    testSuiteID = newEntry2.get("suite_id").toString();
                    runConfig.setTestRailSuiteID(testSuiteID);
                    runs = (JSONArray) newEntry2.get("runs");
                    Run = matchStringFromJsonArray(runs, "config_ids", currentBrowserConfigCode, currentCountryConfigCode);
                }
            }

            //If a run is found, which matches the name and configuration options specified
            if (Run != null) {
                //Add a new run
                testRunID = Run.get("id").toString();
                runConfig.setTestRailRunID(testRunID);
            } else {
                //Add a new test plan entry and a add a run to that new entry
                JSONObject newEntry4 = testRailAPI.addTestPlanEntry(RunCukesTest.testRailTestPlan, testSuiteID, featureName, browserConfigOptions.get(currentBrowser), countryConfigOptions.get(currentCountry));
                runs = (JSONArray) newEntry4.get("runs");
                Run = matchStringFromJsonArray(runs, "config_ids", currentBrowserConfigCode, currentCountryConfigCode);
                testRunID = Run.get("id").toString();
                runConfig.setTestRailRunID(testRunID);
            }

            testRunID = runConfig.getTestRailRunID();
            cases = testRailAPI.getTestsFromTestRun(testRunID);
            sections = testRailAPI.getSuiteSections(runConfig.getTestRailProjectID(), testSuiteID);

            processSectionName(getFeatureTagName(scenario));
            processTestCase(scenario);
        }
    }

    @After
    public void tearDown(Scenario scenario) throws IOException, APIException {
        // Verify if a fatal error has occurred and log it
        if (scenario.isFailed() && hasFatalErrorOccurred()) {
            applicationErrors.put(scenario, getErrorDetails());
            logFatalErrors();
        }

        if (Boolean.parseBoolean(System.getProperty("checkHttps"))) {
            String violationsMessage = convertHashMapToMessage();
            assertTrue("Unfortunately Mixed Content Errors have been recognized" + '\n' + violationsMessage, errorLogs.isEmpty());
        }

        /**
         * Ouput the result of the test case to each test case
         * 1 = Passed
         * 4 = ReTest
         * Other statuses are not currently required for use
         * Add a comment to each completed test case containing the BDD scenario of the case
         */
        if (runConfig.getTestRailOutput() && !ignoredRetest) {
            Map Result = new HashMap();
            int status = 3;

            switch (scenario.getStatus()) {
                case PASSED:
                    status = 1;
                    break;
                case UNDEFINED:
                    status = 2;
                    break;
                case PENDING:
                    status = 2;
                    break;
                case SKIPPED:
                    status = 4;
                    break;
                case FAILED:
                    status = 5;
                    break;
            }

            if (notInScope) {
                status = 6;
            }

            if (StepsContext.steps != null) {
                Result.put("status_id", status);
                List<String> steps = StepsContext.steps;
                String comment = "BDD";

                for (String step : steps) {
                    comment = comment + "\n" + step;
                }

                comment = comment + "\n";
                String errorMessage = null;

                if (scenario.isFailed() &&
                        ScenarioImpl.class.isAssignableFrom(scenario.getClass())) {
                    ScenarioImpl scenarioImpl = (ScenarioImpl) scenario;

                    errorMessage = getExceptionString(scenarioImpl.getError());
                }

                Result.put("comment", comment + "\n" + errorMessage);
                Result = TestRailHooks.processResultOutput(Result);

                if (!notInScope) {
                    testRailAPI.addResultRun(testCaseID, Result);
                } else {
                    testRailAPI.addResultRun(testRunID, testCaseID, Result);

                }

            }
        }

        /**
         * 	If the scenario has failed OR
         * 	The test is an Endeca API test AND outputScreenShotForAllTests is true
         * 	Embed a screenshot in the test report
         */
        if (scenario.isFailed() || (!TestRailHooks.isEndecaAPITest(scenario) && outputScreenShotForAllTests)) {
            scenario.write("Server number: " + server);
            try {
                for (Object o1 : (List<String>) sharedData.get("consoleListLog")) {
                    scenario.write(o1.toString());
                }
            } catch (NullPointerException ignored) {

            }

            try {
                scenario.embed(this.sharedDriver.getPageSource().getBytes(), "text/html");
                byte[] screenshot = sharedDriver.getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/png");
            } catch (WebDriverException somePlatformsDontSupportScreenshots) {
                System.err.println(somePlatformsDontSupportScreenshots.getMessage());
            }
        }

        if (isAlertPresent()) {
            System.out.print("An Alert Popped Up");
            this.sharedDriver.switchTo().alert();
            this.sharedDriver.switchTo().alert().accept();
            this.sharedDriver.switchTo().defaultContent();
        }

        try {
            String previousCountry = getSharedData("previousCountry");
            if (previousCountry != null) {
                System.setProperty("country", previousCountry);
                setSharedData("previousCountry", null);

            }
        } catch (NullPointerException ignored) {
        }
    }

    private String getExceptionString(Throwable throwable) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        throwable.printStackTrace(printStream);

        printStream.close();

        return outputStream.toString();
    }

    public static void processSectionName(String sectionName) throws Exception {
        long sectionID = getSectionID("Automated Regression");
        if (sectionID <= 0) {
            JSONObject newSection = testRailAPI.newTestSection("Automated Regression", testSuiteID);
            sections.add(newSection);
            testSection = newSection.get("id").toString();
        } else {
            testSection = String.valueOf(sectionID);
        }
    }

    public static void processTestCase(Scenario scenario) throws Exception {
        JSONObject thisCase = getTestCase(scenario.getName(), cases);
        Boolean incomplete = getIncompleteFeatureTagName(scenario);

        if (thisCase == null) {
            JSONArray suiteCases = testRailAPI.getSuiteCases(runConfig.getTestRailProjectID(), testSuiteID);
            thisCase = getTestCase(scenario.getName(), suiteCases);

            if (thisCase == null) {
                JSONObject newTestCase = testRailAPI.newTestCase(testSuiteID, scenario.getName());
                cases.add(newTestCase);
                testCaseID = newTestCase.get("id").toString();
                cases = testRailAPI.getTestsFromTestRun(testRunID);

                thisCase = getTestCase(scenario.getName(), cases);

                testCaseID = thisCase.get("id").toString();

            } else {
                testCaseID = thisCase.get("id").toString();
                ignoredRetest = false;

                String thisCaseResult = thisCase.get("status_id").toString();

                if ((thisCaseResult.equals("1") && !rerunSuccessfulTests)
                        || thisCaseResult.equals("6")
                        || (thisCaseResult.equals("2") && !runConfig.getRunBlockedTests())) {
                    ignoredRetest = true;
                    org.junit.Assume.assumeTrue(SKIPPING_TEST_ERROR_MESSAGE,false);
                }

            }
        } else {
            testCaseID = thisCase.get("id").toString();
            ignoredRetest = false;

            String thisCaseResult = thisCase.get("status_id").toString();

            if ((thisCaseResult.equals("1") && !rerunSuccessfulTests)
                    || thisCaseResult.equals("6")
                    || (thisCaseResult.equals("2") && !runConfig.getRunBlockedTests())) {
                ignoredRetest = true;
                org.junit.Assume.assumeTrue(SKIPPING_TEST_ERROR_MESSAGE, false);
            }

        }
        if (incomplete) {
            org.junit.Assume.assumeTrue(false);
        }
    }

    public static long getSectionID(String sectionName) {
        if (sections == null || sections.isEmpty()) {
            return -1;
        } else {
            for (Object section : sections) {
                JSONObject thisSection = (JSONObject) section;

                if (sectionName.equals(thisSection.get("name").toString())) {
                    return (long) thisSection.get("id");
                }
            }
            return -1;
        }
    }

    private static JSONObject getTestCase(String testCaseName, JSONArray cases) {
        if (cases == null) {
            return null;
        } else {
            if (cases.isEmpty()) {
                return null;
            } else {
                for (Object aCase : cases) {
                    JSONObject thisCase = (JSONObject) aCase;

                    if (testCaseName.equals(thisCase.get("title").toString())) {
                        return thisCase;
                    }
                }
                return null;
            }
        }
    }

    private static Boolean getIncompleteFeatureTagName(Scenario scenario) {
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.matches("@Incomplete")) {
                return true;
            }
        }
        return false;
    }

    public static String getFeatureTagName(Scenario scenario) {
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.matches("@Feature_(.*)")) {
                tag = tag.replace("@Feature_", "");
                return StringUtils.capitalize(tag);
            }
        }
        return "";
    }

    public static Map processResultOutput(Map result) {
        String comment = result.get("comment").toString();
        if (comment.contains("at ✽.")) {
            comment = highlightFailedBDD(comment);
            if (comment.contains("Exception")) {
                comment = highlightException(comment);
            } else {
                comment = highlightAssertion(comment);
            }
            result.replace("comment", comment);
        }
        return result;
    }

    private static String highlightAssertion(String comment) {
        //TODO - FINISH ME
        return comment;
    }

    private static String highlightFailedBDD(String comment) {
        // Split on failed BDD line
        String[] splitOnFailedBDD = comment.split("at ✽\\.");
        // Split on NEW LINE FEED from the 1st index of
        String[] splitOnReturn = splitOnFailedBDD[1].split("\n");
        String bddStatement = "**".concat(splitOnReturn[0]).trim() + "**";
        String[] commentParts = comment.split("\\n\\n");
        return commentParts[0] + "\n>>>Failed at BDD Statement: " + bddStatement + "\n\r\n" + commentParts[1];
    }

    private static String highlightException(String comment) {
        String[] splitOnException = comment.split("Exception");
        Pattern pattern = Pattern.compile("[A-z]+$");
        Matcher m = pattern.matcher(splitOnException[0]);
        String exception = "couldn't find exception - my regex wasn't good enough";
        if (m.find()) exception = "**" + m.group(0);
        String[] commentParts = comment.split("\\n\\n");
        return commentParts[0] + "\n>>>Exception was: " + exception + " Exception!!**";
    }

    private JSONObject matchStringFromJsonArray(JSONArray jsonArray, String searchString, String featureName) {
        for (Object entry : jsonArray) {
            JSONObject Entry = (JSONObject) entry;
            String name = (String) Entry.get(searchString);
            if (name.equals(featureName)) {
                return Entry;
            }

        }
        return null;
    }

    private JSONObject matchStringFromJsonArray(JSONArray jsonArray, String searchString, String configOne, String configTwo) {
        for (Object entry : jsonArray) {
            JSONObject Entry = (JSONObject) entry;
            String config_ids = Entry.get(searchString).toString();
            if (config_ids.contains(configOne) && config_ids.contains(configTwo)) {
                return Entry;
            }

        }
        return null;
    }

    private List<JSONObject> matchListFromJsonArray(JSONArray jsonArray, String searchString, String featureName) {
        List<JSONObject> returnList = new ArrayList<>();
        for (Object entry : jsonArray) {
            JSONObject Entry = (JSONObject) entry;
            String name = (String) Entry.get(searchString);
            if (name.equals(featureName)) {
                returnList.add(Entry);
            }
        }
        if (returnList.size() != 0) return returnList;
        else return null;
    }

    private String getCurrentBrowser() {
        if (Boolean.parseBoolean(System.getProperty("runFromCMD"))) {
            return System.getProperty("browser") + System.getProperty("browserVersion");
        } else if (!RunCukesTest.useBrowserStack) {
            return "localRun";
        } else {
            return RunCukesTest.browserStackBrowser + RunCukesTest.browserStackBrowserVersion;
        }
    }

    private String getCurrentCountry(Scenario scenario) {
        if (StringUtils.containsIgnoreCase(System.getProperty("parallelTags"), "ProcessProve") ||
                scenario.getSourceTagNames().contains("@processprove") ||
                scenario.getSourceTagNames().contains("@endtoendprocessprove")) {
            return "Various";
        } else {
            return System.getProperty("country").toUpperCase();
        }
    }

    private HashMap<String, String> getConfigHashMaps(JSONArray configs, String configName) {
        HashMap<String, String> returnable = new HashMap<>();
        JSONObject browsers = matchStringFromJsonArray(configs, "name", configName);
        JSONArray browserConfigs = (JSONArray) browsers.get("configs");
        for (Object b : browserConfigs) {
            JSONObject c = (JSONObject) b;
            returnable.put(c.get("name").toString(), c.get("id").toString());
        }
        return returnable;
    }

    private String addConfigToGroup(JSONArray configs, String configGroupName, String configName) throws IOException, APIException {
        JSONObject configGroup = matchStringFromJsonArray(configs, "name", configGroupName);
        String configGroupID = String.valueOf(configGroup.get("id"));
        JSONObject config = testRailAPI.addConfigToGroup(configGroupID, configName);
        return String.valueOf(config.get("id"));
    }

    public static boolean isEndecaAPITest(Scenario scenario) {
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.matches("@Endeca_API")) {
                return true;
            }
        }
        return false;
    }

    public static Boolean testInScope(Scenario scenario) {
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.contains("not" + System.getProperty("country"))) {
                return true;
            }
        }
        return false;
    }

    private synchronized String addTestRun(String testRunName, Config runConfig) throws IOException, APIException {
        String runName = testRunName
                + " - "
                + StringUtils.upperCase(System.getProperty("country"))
                + " Market";

        return testRailAPI.
                addTestRunToProject(testSuiteID, runName, runConfig.getTestRailProjectID()).
                get("id").toString();
    }

    private void logFatalErrors() {
        StringBuilder stringBuilder = new StringBuilder();
        applicationErrors.forEach((scenario, error) -> stringBuilder.append(String.format("%s: %s %s\n", scenario.getName(), error.getType(), error.getCode())));
        logger.error(applicationErrors.size() + " Application Errors occurred\n" + stringBuilder.toString());
    }

    private ApplicationError getErrorDetails() {
        ApplicationErrorPage page = new ApplicationErrorPage(this.sharedDriver);
        return new ApplicationError(page.getErrorCode(), ApplicationError.ApplicationErrorType.valueOf(page.getErrorType().toUpperCase()));
    }

    private String convertHashMapToMessage() {
        String errorMessage1 = "";

        for (final String key : errorLogs.keySet()) {

            errorMessage1 = errorMessage1 + " Console Error Logs at --> " + key + '\n';
            String errorMessage2 = "";
            List<String> violationsData = errorLogs.get(key);

            for (final String violation : violationsData) {

                errorMessage2 = errorMessage2 + " " + violation + '\n';
            }
            errorMessage1 = errorMessage1 + errorMessage2;
        }
        return errorMessage1;
    }

    private boolean isAlertPresent() {
        try {
            this.sharedDriver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasFatalErrorOccurred() {
        return new ApplicationErrorPage(this.sharedDriver).isApplicationErrorPageDisplayed();
    }

    public void setSharedData(String key, Object value) {
        sharedData.put(key, value);
    }

    public String getSharedData(String key) {
        return sharedData.get(key).toString();
    }
}