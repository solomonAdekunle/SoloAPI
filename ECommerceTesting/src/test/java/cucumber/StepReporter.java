package cucumber;

import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestSourceRead;
import cucumber.api.event.TestStepStarted;
import cucumber.api.formatter.Formatter;
import stepdefs.hooks.StepsContext;

/**
 * Created by e0653032 on 30/04/2019.
 */
public class StepReporter implements Formatter {

    private final TestSourcesModel testSources = new TestSourcesModel();
    private String currentFeatureFile;

    public StepReporter() {
    }

    public StepReporter(String path) {
        // Ignoring the path which is passed in by cucumber-jvm-parallel-plugin
    }

    void testCaseStarted(TestCaseStarted event) {
        StepsContext.steps.clear();
        currentFeatureFile = event.testCase.getUri();
    }


    void processStep(TestStepStarted event) {
        if (!event.testStep.isHook()) {
            String stepText = getKeywordFromSource(event.testStep.getStepLine()) + event.testStep.getStepText();
            StepsContext.steps.add(stepText);
        }
    }

    private String getKeywordFromSource(int stepLine) {
        return testSources.getKeywordFromSource(currentFeatureFile, stepLine);
    }


    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this::processStep);
        publisher.registerHandlerFor(cucumber.api.event.TestCaseStarted.class, this::testCaseStarted);
    }
}
