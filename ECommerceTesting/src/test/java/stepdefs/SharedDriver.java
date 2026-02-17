package stepdefs;

import config.*;
import edu.emory.mathcs.backport.java.util.Collections;
import explorer.ConsoleCheck;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class SharedDriver implements SearchContext, TakesScreenshot, JavascriptExecutor, WebDriver {

    private EventFiringWebDriver driver;

    private String server;
    private Map<String, Object> sharedData = Collections.synchronizedMap(new HashMap());
    private Map<String, com.endeca.navigation.Navigation> searchSharedData = Collections.synchronizedMap(new HashMap());

    private HashMap<String, List<String>> errorLogs = new HashMap<>();

    public void setDriver(WebDriver driver) {
        this.driver = new EventFiringWebDriver(driver);
        this.driver.register(new NavigationConsoleListener(this));
    }

    public boolean isType(Class klass) {
        return klass.isAssignableFrom(driver.getWrappedDriver().getClass());
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public String getSharedData(String key) {
        return sharedData.get(key).toString();
    }

    public Object getSharedDataAsObject(String key) {
        return sharedData.get(key);
    }

    public com.endeca.navigation.Navigation getSearchSharedData(String key) {
        return searchSharedData.get(key);
    }

    public void setSharedData(String key, Object value) {
        sharedData.put(key, value);
    }

    public void setSearchSharedData(String key, com.endeca.navigation.Navigation nav) {
        searchSharedData.put(key, nav);
    }

    public Integer getSearchSharedDataSize() {
        return searchSharedData.size();
    }

    public Integer getSharedDataSize() {
        return sharedData.size();
    }

    public Set<String> getSharedDataKeys() {
        return sharedData.keySet();
    }

    public Collection<Object> getSharedDataValues() {
        return sharedData.values();
    }

    public Collection<com.endeca.navigation.Navigation> getSearchSharedDataValues() {
        return searchSharedData.values();
    }

    public Set<String> getSearchSharedDataKeys() {
        return searchSharedData.keySet();
    }

    public void writeOrderNumberToCSV(String orderNumber, String country) {
        OrdersLogger.logAnOrder(orderNumber, country);
    }

    public void clickAndSendKeys(WebElement elem, String keys) {
        elem.clear();
        elem.sendKeys(keys);
    }

    private List<String> getConsoleLogs() {
        final List<String> entries = new ArrayList<>();
        // FIXME
        final List<LogEntry> logEntries = ConsoleCheck.getConsoleLog(driver).getAll();

        for (final LogEntry logEntry : logEntries) {

            final String entry = logEntry.getLevel().getName() + " <--> " + logEntry.getMessage();
            entries.add(entry);
        }

        return entries;
    }

    private String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    @Override
    public void get(String url) {
        driver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();

    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) this.driver).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) this.driver).executeAsyncScript(script, args);
    }

    private class NavigationConsoleListener implements WebDriverEventListener {

        private SharedDriver sharedDriver;

        NavigationConsoleListener(final SharedDriver sharedDriver) {
            this.sharedDriver = sharedDriver;
        }

        @Override
        public void beforeNavigateTo(String s, WebDriver webDriver) {
        }

        public void beforeAlertAccept(WebDriver driver) {

        }

        public void afterAlertAccept(WebDriver driver) {
            checkMixedContent();
        }

        public void afterAlertDismiss(WebDriver driver) {

            checkMixedContent();
        }

        public void beforeAlertDismiss(WebDriver driver) {

        }

        @Override
        public void afterNavigateTo(String s, WebDriver webDriver) {
            checkMixedContent();
        }

        @Override
        public void beforeNavigateBack(WebDriver webDriver) {
        }

        @Override
        public void afterNavigateBack(WebDriver webDriver) {
            checkMixedContent();
        }

        @Override
        public void beforeNavigateForward(WebDriver webDriver) {

        }

        @Override
        public void afterNavigateForward(WebDriver webDriver) {
            checkMixedContent();
        }

        @Override
        public void beforeNavigateRefresh(WebDriver webDriver) {

        }

        @Override
        public void afterNavigateRefresh(WebDriver webDriver) {
            checkMixedContent();
        }

        @Override
        public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {

        }

        @Override
        public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {

        }

        @Override
        public void beforeClickOn(WebElement webElement, WebDriver webDriver) {

        }

        @Override
        public void afterClickOn(WebElement webElement, WebDriver webDriver) {
            checkMixedContent();
        }

        @Override
        public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {

        }

        @Override
        public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {

        }

        @Override
        public void beforeScript(String script, WebDriver driver) {

        }

        @Override
        public void afterScript(String script, WebDriver driver) {

        }

        @Override
        public void onException(Throwable throwable, WebDriver driver) {

        }


        private void checkMixedContent() {
            if (System.getProperty("driver.type", "").equalsIgnoreCase("CHROME")) {
                this.checkChromeMixedContent();
            }
        }

        private void checkChromeMixedContent() {
            List<String> logs = sharedDriver.getConsoleLogs();
            List<String> mixedContentLogs = logs.stream().filter(log -> log.toLowerCase().contains("mixed content"))
                    .collect(Collectors.toList());

            if (mixedContentLogs.size() > 0) {
                sharedDriver.errorLogs.put(sharedDriver.getCurrentURL(), mixedContentLogs);
            }
        }
    }
}