package config;

import cucumber.api.Scenario;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberScenario;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static stepdefs.hooks.TestRailHooks.*;

public class TestRailAPI {
    
    private APIClient client;
    public static Config runConfig = new Config(RunCukesTest.testRailOutput, RunCukesTest.testRailProjectID, RunCukesTest.testRailSuiteID, RunCukesTest.testRailRunID, RunCukesTest.mileStoneID, RunCukesTest.runBlockedTests);

    /**
     * Sets up the API Client, and passes user credentials of automated user
     */
    public void setupAPI() {

        client = new APIClient("http://testrail.internal.ecomp.com/testrail/");
        client.setUser("G.AgileTestAutomation@rs-components.com");
        client.setPassword("Welcome1");
    }

    /**
     * Creates a new test suite with the following parameters
     *
     * @param name        sets the name of the new newly created test suite
     * @param description sets the description of the newly created test Suite
     * @return returns the response JSON object of the TestSuite
     * @throws IOException
     * @throws APIException
     */
    public JSONObject newTestSuite(String name, String description) throws IOException, APIException {
        Map<String, String> Suite = new HashMap<>();
        Suite.put("name", name);
        Suite.put("description", description);
        return (JSONObject) client.sendPost("add_suite/" + runConfig.getTestRailProjectID() + "/", Suite);
    }

    /**
     * Creates a new test section with the following parameters
     *
     * @param name    Name of the test section - set as the top tag in the feature file
     * @param suiteID ID of the suite the section is to be added to
     * @return returns the reponse JSON object of the TestSection
     * @throws Exception
     */
    public JSONObject newTestSection(String name, String suiteID) throws Exception {
        Map<String, String> Section = new HashMap<>();
        Section.put("suite_id", suiteID);
        Section.put("name", name);
        
        return (JSONObject) client.sendPost("add_section/" + runConfig.getTestRailProjectID() + "/", Section);
    }

    /**
     * Creates a new test case with the following parameters
     *
     * @param SuiteID ID of the test Suite for the case to added to
     * @param title   Title of the test case to be added
     * @return returns the response JSON object of the new Test Case
     * @throws Exception
     */
    public JSONObject newTestCase(String SuiteID, String title) throws Exception {
        Map<String, String> Case = new HashMap<>();
        Case.put("suite_id", SuiteID);
        Case.put("title", title);
        return (JSONObject) client.sendPost("add_case/" + testSection + "/", Case);
    }

    /**
     * Gets all test cases from a test suite
     *
     * @param projectID ID of the project containing the test suite
     * @param suiteID   ID of the suite within the project
     * @return returns a JSON Array containing each Test Case
     * @throws IOException
     * @throws APIException
     */
    public JSONArray getSuiteCases(String projectID, String suiteID) throws IOException, APIException {
        return (JSONArray) client.sendGet("get_cases/" + projectID + "&suite_id=" + suiteID);
    }

    public JSONArray getTestsFromTestRun(String testRunID) throws IOException, APIException {
        return (JSONArray) client.sendGet("get_tests/" + testRunID);
    }

    public JSONObject getConfigFromTestRun(String testRunID) throws IOException, APIException {
        return (JSONObject) client.sendGet("get_run/" + testRunID);
    }

    public JSONArray getSuiteSections(String projectID, String suiteID) throws IOException, APIException {
        return (JSONArray) client.sendGet("get_sections/" + projectID + "&suite_id=" + suiteID);
    }

    public JSONArray getPlan(String projectID, String suiteID) throws IOException, APIException {
        return (JSONArray) client.sendGet("get_plan/4766");
    }

    public JSONArray getConfigs(String projectID) throws IOException, APIException {
        return (JSONArray) client.sendGet("get_configs/" + projectID);
    }

    public JSONArray returnRunResults(String runID) throws IOException, APIException {
        return (JSONArray) client.sendGet("get_results_for_run/" + runID);
    }

    public JSONArray returnstatuses() throws IOException, APIException {
        return (JSONArray) client.sendGet("/get_statuses");
    }

    /**
     * Adds a new testRunID to a testSuiteID
     *
     * @param suiteID   ID of the test Suite to add the test run to
     * @param runName   Name of the testRunID to be added
     * @param ProjectID ID of the project to add the testRunID to
     * @return returns the JSON object of the new testRunID
     * @throws IOException
     * @throws APIException
     */
    public JSONObject addTestRunToProject(String suiteID, String runName, String ProjectID) throws IOException, APIException {
        Map<String, String> Run = new HashMap<>();
        Run.put("suite_id", suiteID);
        Run.put("name", runName);
        if (!RunCukesTest.mileStoneID.equals("")) {
            Run.put("milestone_id", RunCukesTest.mileStoneID);
        }
        Run.put("description", "Environment : " + System.getProperty("env")
                + " \nMarket : " + System.getProperty("country"));

        return (JSONObject) client.sendPost("add_run/" + ProjectID + "/", Run);
    }

    /**
     * Reflection used to allow access to private fields in Cucumber.runtime.model
     * In this case to access the step information from each scenario
     * returns the CucumberScenario object with the required fields accessible
     */
    public CucumberScenario reflectCucumberScenarioField(Scenario scenario) throws Exception {
        Field f = scenario.getClass().getDeclaredField("reporter");
        f.setAccessible(true);
        JUnitReporter reporter = (JUnitReporter) f.get(scenario);

        Field executionRunnerField = reporter.getClass().getDeclaredField("executionUnitRunner");
        executionRunnerField.setAccessible(true);
//        ExecutionUnitRunner executionUnitRunner = (ExecutionUnitRunner) executionRunnerField.get(reporter);

//        Field cucumberScenarioField = executionUnitRunner.getClass().getDeclaredField("cucumberScenario");
//        cucumberScenarioField.setAccessible(true);
//        return (CucumberScenario) cucumberScenarioField.get(executionUnitRunner);

        // TODO: FIXME
        return null;
    }

    public String getSuiteName(String testSuiteID) throws IOException, APIException {
        JSONObject suite = (JSONObject) client.sendGet("get_suite/" + testSuiteID);
        return suite.get("name").toString();
    }

    public void addResultRun(String testCaseID, Object result) throws IOException, APIException {
        client.sendPost("add_result/" + testCaseID + "/", result);
    }

    public void addResultRun(String testRunID, String testCaseID, Object result) throws IOException, APIException {
        client.sendPost("add_result_for_case/" + testRunID + "/" + testCaseID + "/", result);
    }

    public JSONObject addTestPlan(String projectID, String name, String milestoneID) throws IOException, APIException {
        Map<String, Object> entry = new HashMap<>();
        entry.put("milestone_id", milestoneID);
        entry.put("name", name);
        return (JSONObject) client.sendPost("add_plan/" + projectID, entry);
    }

    public JSONObject getTestPlan() throws IOException, APIException {
        JSONObject plan = (JSONObject) client.sendGet("get_plan/" + "2977");
        return plan;
    }

    public JSONObject getTestPlan(String planID) throws IOException, APIException {
        return (JSONObject) client.sendGet("get_plan/" + planID);
    }

    public JSONArray getProjectSuites(String projectID) throws IOException, APIException {
        return (JSONArray) client.sendGet("get_suites/" + projectID);
    }

    public JSONObject addConfigToGroup(String groupID, String configName) throws IOException, APIException {
        Map<String, Object> entry = new HashMap<>();
        entry.put("name", configName);
        return (JSONObject) client.sendPost("add_config/" + groupID, entry);
    }

    public JSONObject addTestPlanEntry(String planID, String suiteID, String runName, String browserConfigID, String countryConfigID) throws IOException, APIException {
        Map<String, Object> entry = new HashMap<>();
        entry.put("suite_id", suiteID);
        entry.put("name", runName);

        entry.put("config_ids", new ArrayList<String>(
                Arrays.asList(browserConfigID, countryConfigID)));
        ArrayList<JSONObject> runs = new ArrayList<>();
        JSONObject runOne = new JSONObject();
        runOne.put("include_all", true);
        runOne.put("config_ids", new ArrayList<String>(
                Arrays.asList(browserConfigID, countryConfigID)));
        runs.add(runOne);
        entry.put("runs", runs);
        entry.put("description", "Environment : " + System.getProperty("env")
                + " \nMarket : " + System.getProperty("country"));

        return (JSONObject) client.sendPost("add_plan_entry/" + planID + "/", entry);
    }


}
