package config;

public class Config {

    private static String runMode = System.getProperty("runFromCMD");

    private boolean testRailOutput;
    private String testRailProjectID;
    private String testRailSuiteID;
    private String testRailRunID;
    private String milestoneID;
    private boolean runBlockedTests;

    public Config(boolean output, String projectID, String suiteID, String runID, String milestoneID, boolean runBlockedTests) {
        this.testRailOutput = output;
        this.testRailProjectID = projectID;
        this.testRailSuiteID = suiteID;
        this.testRailRunID = runID;
        this.milestoneID = milestoneID;
        this.runBlockedTests = runBlockedTests;
    }

    public boolean getTestRailOutput() {
        return testRailOutput;
    }

    public void setTestRailOutput(boolean output) {
        this.testRailOutput = output;
    }

    public String getTestRailProjectID() {
        return testRailProjectID;
    }

    public void setTestRailProjectID(String projetID) {
        this.testRailProjectID = projetID;
    }

    public String getTestRailSuiteID() {
        return testRailSuiteID;
    }

    public void setTestRailSuiteID(String suiteID) {
        this.testRailSuiteID = suiteID;
    }

    public String getTestRailRunID() {
        return testRailRunID;
    }

    public void setTestRailRunID(String runID) {
        this.testRailRunID = runID;
    }

    public boolean getRunBlockedTests() {
        return runBlockedTests;
    }

    public String getMilestoneID() {
        return milestoneID;
    }

    public void setMilestoneID(String mileStoneID) {
        this.milestoneID = mileStoneID;
    }


    /**
     * TestRail Specific configuration components
     * testRailOutput - true will output details to new test suite and run in TestRail
     * testRailProjectID - ID of project to output to in Testrail n.b. Automation project is 50, Test is 1
     * createNewTestSuite - when set to true, a new test suite will be created and all test cases added to it
     * testRailSuiteID - if createNewTestSuite is false, creates a run of this test suite and adds cases to it
     * testRailReTest - if true testRailRunId must be populated.  The test results will update the existing test run specified
     * testRailRunID - Run to be updated by retesting results
     */

}
