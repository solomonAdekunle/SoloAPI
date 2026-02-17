package pages;

import com.endeca.navigation.Navigation;
import com.google.common.collect.Ordering;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import config.Environment;
import config.PropertiesReader;
import config.SearchPropertiesReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.*;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import stepdefs.SharedDriver;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static net.lingala.zip4j.util.InternalZipConstants.CHARSET_UTF8;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public abstract class Page {

    private static final CharSequence URL = null;
    public static SharedDriver webDriver;
    /**
     * Wait 10 seconds
     **/
    protected static WebDriverWait WAIT_10;
    /**
     * Wait 20 seconds
     **/
    protected static WebDriverWait WAIT_20;
    /**
     * Wait 30 seconds
     **/
    protected static WebDriverWait WAIT_30;
    /**
     * Wait 30 seconds
     **/
    protected static WebDriverWait WAIT_180;
    /**
     * Wait 3 seconds
     **/
    static WebDriverWait WAIT_3;
    /**
     * Actions instance for ALL pages.
     */
    static Actions ACTIONS;
    /**
     * JavascriptExecutor instance for ALL pages.
     */
    static JavascriptExecutor JSEXECUTE;
    /**
     * Introducing the fluent WAIT_FLUENT
     */
    static Wait<WebDriver> WAIT_FLUENT;
    /**
     * Properties reader for ALL pages.
     */
    public static PropertiesReader PROPS_READER;

    public static SearchPropertiesReader SEARCH_PROPS_READER;

    public static final By MODAL_FEEDBACK_CLOSE = By.cssSelector("#fsrFocusFirst .fsrButton__inviteClose");
    public static final By MODAL_TERMS_CLOSE = By.cssSelector("#termsConditionsFrameClose");
    public static final By APAC_MODAL_FEEDBACK_CLOSE = By.cssSelector(".acsClassicInvite a#acsFocusFirst");


    protected Page(SharedDriver webDriver) {
        Page.webDriver = webDriver;
        WAIT_3 = new WebDriverWait(webDriver, 3);
        WAIT_10 = (WebDriverWait) new WebDriverWait(webDriver, 10).pollingEvery(2, TimeUnit.SECONDS);
        WAIT_20 = (WebDriverWait) new WebDriverWait(webDriver, 20).pollingEvery(2, TimeUnit.SECONDS);
        WAIT_30 = (WebDriverWait) new WebDriverWait(webDriver, 30).pollingEvery(2, TimeUnit.SECONDS);
        WAIT_180 = (WebDriverWait) new WebDriverWait(webDriver, 180).pollingEvery(2, TimeUnit.SECONDS);
        PROPS_READER = new PropertiesReader();
        SEARCH_PROPS_READER = new SearchPropertiesReader();
        ACTIONS = new Actions(webDriver);
        JSEXECUTE = webDriver;
        WAIT_FLUENT = new FluentWait<WebDriver>(webDriver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(WebDriverException.class);
    }

    // ========================================================//
    // ================= Waits + Conditions ===================//
    // ========================================================//

    /**
     * Sets the Entity Resolver for the DocumentBuilder object parameter to an empty input source.
     * This disables any Doctype validation being ran against any XML parsed using this DocumentBuilder instance.
     *
     * @param db The DocumentBuilder you want to disable Doc Type Declaration Validation on.
     */
    private static void disableDtdValidation(DocumentBuilder db) {
        // disable the dtd validation by setting a custom entity resolver
        db.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) {
                System.out.println("Ignoring " + publicId + ", " + systemId);
                return new InputSource(new StringReader(""));
            }
        });
    }

    /**
     * Takes a url string as a parameter and uses Http connection to validate
     * whether the url is valid by checking whether it has a 404 response code
     * or not. It then returns a boolean value whether this condition is true
     * or false. It will also print out the response code as well as the urlString
     * it is checking for debugging.
     *
     * @param urlString used to make the connection
     * @return a boolean value whether the url in question is valid or not
     */
    public static boolean is404CodeNotReturned(String urlString) {
        boolean isValid = false;
        try {
            java.net.URL u = new java.net.URL(urlString);
            HttpURLConnection h = (HttpURLConnection) u.openConnection();
            h.setRequestMethod("GET");
            h.connect();
            //System.out.println(h.getResponseCode() + " - " + urlString);
            if (h.getResponseCode() != 404) {
                isValid = true;
            }
        } catch (Exception e) {

        }
        return isValid;
    }

    /**
     * Takes a url string as a parameter and uses Http client to get response
     * code. It will return the response code for "Actual url" before redirecting
     *
     * @param urlString used to make the connection
     * @return a response code for the url string passed
     */
    public static int getStatusCodeBeforeRedirect(String urlString) throws IOException {

        HttpUriRequest httpUriRequest = new HttpGet(urlString);
        HttpClient httpClient =
                HttpClientBuilder.create().disableRedirectHandling().build();

        HttpResponse response = httpClient.execute(httpUriRequest);
        System.out.println(response.getStatusLine().getStatusCode());

        return response.getStatusLine().getStatusCode();
    }

    /**
     * Returns the string data of the first node from the Element parameter.
     *
     * @param e The element containing the desired text
     * @return The string data from the Element
     */
    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static String getProperty(String property) {
        String returnString = PROPS_READER.getProperty(property);
        if (returnString != null) return returnString;
        else return SEARCH_PROPS_READER.getProperty(property);
    }

    public void refreshPropsReader() {
        PROPS_READER = new PropertiesReader();

    }

    /**
     * Specific to Quote Manager
     * <p>
     * Waits for 30 seconds (Polling every 2 seconds) for the scrubbing/shade element to either:
     * - No longer be part of the DOM
     * - For the WebElement Reference to become stale
     */
    protected void waitForQMShadeToDissapear() {
        waitForElementToDisappear(webDriver.findElement(By.cssSelector("div#ajaxLMB_shade")), 100);
    }

    /**
     * Manually set the Implicit WAIT_FLUENT time for the current Thread.
     *
     * @param implicitTimeout New timeout in Seconds
     */
    private void setImplicitTimeout(long implicitTimeout) {
        //FIXME
        //webDriver.manage().timeouts().implicitlyWait(implicitTimeout, TimeUnit.SECONDS);
    }

    /**
     * ONLY use this when waits do not work in the scenario.
     * This will hang the run time of the run by the seconds parameter
     *
     * @param seconds The number of seconds you NEED the thread to sleep for
     */
    public void sleep(final int seconds) {

        try {

            Thread.sleep(seconds * 1000);
        } catch (final InterruptedException interruptedException) {

            interruptedException.printStackTrace();
        }
    }

    void waitForNavBar() {
        waitForPageToLoad();
        waitForElementVisible(By.cssSelector("div.shHeader"));
    }

    /**
     * Repeatedly gets the readyState of the current page document, until it returns "complete" indicating successful page load
     * <p>
     * -- Please note this doesn't include javascript actions outside of page load, including filter calculations
     */
    public void waitForPageToLoad() {
        String pageLoadStatus;
        do {

            //get the current document ready state - i.e. is the page loaded
            pageLoadStatus = (String) JSEXECUTE.executeScript("return document.readyState");

            //Once the page loaded status is complete
        } while (!pageLoadStatus.equals("complete") && !pageLoadStatus.equals("interactive"));
    }

    /**
     * Waits for asynchronous ajax calls that are called after the initial page load by the website.
     */
    public void waitForJQuery() {
        waitForPageToLoad();
        sleep(1);
        waitForPageToLoad();
    }

    public void waitForComplexPageLoad() {
        waitForPageToLoad();
        ExpectedCondition<Boolean> jQueryLoad = webDriver -> ((Long) JSEXECUTE.executeScript("return jQuery.active") == 0);
        boolean jqueryReady = (Boolean) JSEXECUTE.executeScript("return jQuery.active==0");
        if (!jqueryReady) {
            System.out.println("JQuery NOT Ready!");
            WAIT_10.until(jQueryLoad);
        } else {
            System.out.println("JQuery is Ready!");
        }
    }


    /**
     * Waits 10 seconds for the element to be visible. Using By variable
     *
     * @param locator By locator for the element you want to WAIT_FLUENT for
     */
    protected WebElement waitForElementVisible(By locator) {
        return WAIT_FLUENT.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ========================================================//
    // ================== INTERACTIONS ========================//
    // ========================================================//

    /**
     * Waits 10 seconds for the element to be visible. Using WebElement reference
     *
     * @param element WebElement reference for the element you want to WAIT_FLUENT for
     */
    WebElement waitForElementVisible(WebElement element) {
        return WAIT_10.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits 10 seconds for the element to be clickable. Using WebElement reference
     *
     * @param locator WebElement reference for the element you want to WAIT_FLUENT for
     */
    protected WebElement waitforElementClickable(By locator) {
        return WAIT_10.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Returns true when element is present
     * Returns false when element is not present
     * <p>
     * temporarily sets the implicit WAIT_FLUENT time to 1 second
     *
     * @param element The By locator for the element
     * @return boolean based on elements presence
     */
    public boolean elementPresent(By element) {
        setImplicitTimeout(1);
        List<WebElement> s = webDriver.findElements(element);
        setImplicitTimeout(Long.parseLong(PROPS_READER.getProperty("driver.implicit.wait")));
        return s.size() != 0;
    }

    /**
     * Waits 10 seconds for the element to be clickable. Using WebElement reference
     *
     * @param element WebElement reference for the element you want to WAIT_FLUENT for
     */
    public WebElement waitforElementClickable(WebElement element) {
        return WAIT_10.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Returns true when element is clickable
     * Returns false when element is not clickable
     * <p>
     *
     * @param locator The By locator for the element
     * @return boolean based on elements clickable state
     */
    protected boolean elementClickable(By locator) {
        try {
            WAIT_3.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    /**
     * Returns true when element is selected
     * Returns false when element is not selected
     * <p>
     *
     * @param locator The By locator for the element
     * @return {@code boolean} based on elements selected state
     */
    protected boolean elementSelected(By locator) {
        try {
            WAIT_3.until(ExpectedConditions.elementToBeSelected(locator));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    /**
     * Returns true when element is selected
     * Returns false when element is not selected
     * <p>
     *
     * @param locator The By locator for the element
     * @return {@code boolean} based on elements selected state
     */
    protected boolean elementEnabled(By locator) {
        try {
            WAIT_FLUENT.until((webDriver1) -> webDriver1.findElement(locator).isEnabled());
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    /**
     * Click without WAIT
     * To be used when click elements not represented as clickable within the DOM
     *
     * @param element The element to click
     */
    public void clickNoWait(WebElement element) {
        element.click();
    }

    /**
     * Click without WAIT
     * To be used when click elements not represented as clickable within the DOM
     *
     * @param locator The element to click
     */
    void clickNoWait(By locator) {
        locator.findElement(webDriver).click();
    }

    /**
     * Click Element using By locator
     *
     * @param locator the locator for the web element to WAIT_FLUENT for
     */
    protected void click(By locator) {
        if (System.getProperty("headless", "false").equals("true")) {
            sleep(1);
            WAIT_FLUENT.until(
                    ExpectedConditions.elementToBeClickable(locator)).click();
        } else {
            waitForPageToLoad();
            WAIT_FLUENT.until(
                    ExpectedConditions.elementToBeClickable(locator)).click();
            sleep(1);
        }
    }

    protected void hover(By locator) {
        ACTIONS = new Actions(webDriver);
        ACTIONS.moveToElement(webDriver.findElement(locator)).build().perform();
    }

    public void hover(WebElement element) {
        ACTIONS = new Actions(webDriver);
        ACTIONS.pause(100);
        ACTIONS.moveToElement(element).build().perform();
    }

    /*Only for use when the default hover does not work as this injects js rather than emulate a user*/
    public void hoverWithJS(String category) {
        String categorypath = category.toLowerCase().replaceAll(" ", "-");

        WebElement element = webDriver.findElement(By.cssSelector("a[href='/web/c/" + categorypath + suffixSwitch()));
        String strJavaScript = "var element = arguments[0];"
                + "var mouseEventObj = document.createEvent('MouseEvents');"
                + "mouseEventObj.initEvent( 'mouseover', true, true );"
                + "element.dispatchEvent(mouseEventObj);";
        ((JavascriptExecutor) webDriver).executeScript(strJavaScript, element);
    }

    /*The new header hrefs do not finish with a forwards slash, while the current header switches do not
    eg: Current header: /web/c/passive-components/, old header: /web/c/passive-components
    This switch should be removed once the approach is standardised.
     */
    private String suffixSwitch() {
        String suffix;
        if (System.getProperty("env").equalsIgnoreCase("discoverydev1")) {
            suffix = "']";
        }
        else {
            suffix = "/']";
        }
        return suffix;
    }

    protected void waitUntilElementIsVisible(By locator) {
        WAIT_FLUENT.until(visibilityOfElementLocated(locator));
    }

    void waitUntilElementIsInvisible(By locator) {
        WAIT_FLUENT.until(invisibilityOfElementLocated(locator));
    }

    /**
     * Enters the {@code String} argument into the {@code WebElement} argument
     *
     * @param element {@code WebElement} to enter text into
     * @param text    text to enter into the element as {@code String}
     */
    protected void enterText(WebElement element, String text) {
        element.sendKeys(text);
    }

    /**
     * Clears and then Enters the {@code String} argument into the element located with the {@code By} argument
     *
     * @param locator {@code By} locator for element to enter text into
     * @param text    text to enter into the element as {@code String}
     */
    protected void clearAndEnterText(By locator, String text) {
        WebElement e = WAIT_3.until(ExpectedConditions.elementToBeClickable(locator));
        e.clear();
        e.sendKeys(text);
    }

    /**
     * Clears and then Enters the {@code String} argument into the {@code WebElement} argument
     *
     * @param element {@code WebElement} to enter text into
     * @param text    text to enter into the element as {@code String}
     */
    public void clearAndEnterText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Click Element using WebElement object reference
     *
     * @param element the web element to WAIT_FLUENT for
     */
    public void click(WebElement element) {
        if (System.getProperty("headless", "false").equals("true")) {
            sleep(1);
        } else {
            waitForPageToLoad();
        }
        WAIT_FLUENT.until(
                ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Double clicks, clears and enters the keys argument into the element
     *
     * @param elem The element you want to click, clear and send keys to
     * @param keys The keys to enter
     */
    protected void doubleClickClearAndSendKeys(WebElement elem, String keys) {
        ACTIONS = new Actions(webDriver);
        ACTIONS.doubleClick(elem);
        clear(elem);
        enterText(elem, keys);
    }

    /**
     * Clicks on a link based on whether the string parameter is contained within it's link text
     *
     * @param text the text expected to be contained within the link
     */
    public void clickOnLinkByPartialText(String text) {
        webDriver.findElement(By.partialLinkText(text)).click();
    }

    /**
     * Double clicks, clears and enters the keys argument into the element
     *
     * @param elem The By locaotr for the element you want to click, clear and send keys to
     * @param keys The keys to enter
     */
    protected void doubleClickClearAndSendKeys(By elem, String keys) {
        doubleClickClearAndSendKeys(webDriver.findElement(elem), keys);
    }

    /**
     * Double clicks the {@code WebElement} argument
     *
     * @param element {@code WebElement} to double click
     */
    protected void doubleClick(WebElement element) {
        ACTIONS = new Actions(webDriver);
        ACTIONS.doubleClick(element).build().perform();
    }

    /**
     * Clicks the {@code WebElement} argument which should be a radio element
     * Use only as a workaround where radio buttons are not clickable due to Selenium issues
     *
     * @param radio {@code WebElement} to double click
     */
    void clickRadio(WebElement radio) {
        radio.sendKeys(Keys.SPACE);
    }

    /**
     * Return a List of WebElements using a locator
     *
     * @param locator The locator for the list of WebElements
     * @return the Web Elements found for this locator
     */
    protected List<WebElement> getElementsList(By locator) {
        return WAIT_FLUENT.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Return a WebElement from a list using index.
     *
     * @param locator The locator for the list of Web Elements
     * @param index   The index of the Web Element in the list
     * @return the Web Element found at indez in Web Element list
     */
    public WebElement getElementFromListByIndexWithWait(By locator, int index) {
        List<WebElement> elements = WAIT_FLUENT.until(ExpectedConditions.visibilityOfAllElementsLocatedBy((locator)));
        return elements.get(index);
    }

    public WebElement getElementFromListByIndex(By locator, int index) {
        List<WebElement> elements = webDriver.findElements(locator);
        return elements.get(index);
    }

    /**
     * Selects a WebElement from a list using index.
     *
     * @param locator The locator for the list of Web Elements
     * @param index   The index of the Web Element in the list
     */
    public void selectFromListByIndex(By locator, int index) {
        List<WebElement> menuOptions = webDriver.findElements(locator);
        click(menuOptions.get(index));
    }

    /**
     * Selects a WebElement from a dropdown list using index.
     *
     * @param locator The locator for the dropdown list of Web Elements
     * @param index   The index of the Web Element in the list
     */
    protected void selectDropdownByIndex(By locator, int index) {
        WAIT_10.until(
                ExpectedConditions.elementToBeClickable(locator));
        Select dropdown = new Select(webDriver.findElement(locator));
        dropdown.selectByIndex(index);
    }

    /**
     * Selects a random option from the dropdown parameter
     *
     * @param select The Select dropdown you are selecting a random option from
     */
    private void selectDropdownByRandomIndex(Select select) {
        Random random = new Random();
        int selectSize = select.getOptions().size();
        int selectIndex = random.nextInt(selectSize);
        select.selectByIndex(selectIndex);
    }

    /**
     * Selects a random option from the dropdown found by the By locator parameter
     *
     * @param locator The locator for the dropdown you are selecting a random option from
     */
    protected void selectDropdownByRandomIndex(By locator) {
        Select select = new Select((WAIT_10).until(ExpectedConditions.visibilityOfElementLocated(locator)));
        selectDropdownByRandomIndex(select);
    }

    /**
     * Selects a random option from the dropdown WebElement parameter
     *
     * @param element The WebElement dropdown you are selecting a random option from
     */
    public void selectDropdownByRandomIndex(WebElement element) {
        Select select = new Select((WAIT_10).until(ExpectedConditions.elementToBeClickable(element)));
        selectDropdownByRandomIndex(select);
    }

    /**
     * Selects an option from the drop down found by the By locator parameter, by visible text.
     *
     * @param locator    The locator for the dropdown you are selecting the option from
     * @param optionText The text you want to select the option by.
     */
    protected String selectDropdownByText(By locator, String optionText) {
        WebElement element = webDriver.findElement(locator);
        selectDropdownByText(element, optionText);
        return element.getText();
    }

    /**
     * Selects an option from the drop down found by the WebElement parameter, by visible text.
     *
     * @param element    The element dropdown you are selecting the option from
     * @param optionText The text you want to select the option by.
     */
    private void selectDropdownByText(WebElement element, String optionText) {
        Select dropDown = new Select((WAIT_10).until(ExpectedConditions.elementToBeClickable(element)));
        dropDown.selectByVisibleText(optionText);
    }

    /**
     * Return the size of a list of elements found
     *
     * @param locator The locator for the list of elements
     * @return The int size of the Elements found
     */
    protected int getElementsListSize(By locator) {
        return (webDriver.findElements(locator)).size();
    }

    /**
     * Selects an option from the drop down found by the By locator parameter, by visible text.
     *
     * @param select     The dropdown you are selecting the option from
     * @param optionText The text you want to select the option by.
     */
    public void selectDropdownByText(Select select, String optionText) {
        select.deselectByVisibleText(optionText);
    }

    /**
     * Selects an option from the drop down found by the WebElement parameter, by the value
     *
     * @param element The element dropdown you are selecting the option from
     * @param value   The value you want to select the option by.
     */
    protected void selectDropdownByValue(WebElement element, String value) {
        Select dropDown = new Select(element);
        dropDown.selectByValue(value);
    }

    protected void randomListValueSelect(By locator) {
        List<WebElement> variable = webDriver.findElements(locator);
        variable.get(new Random().nextInt(variable.size())).click();
    }

    public List<WebElement> getAllDropDownOptions(WebElement element) {
        Select dropDown = new Select(element);
        return dropDown.getOptions();
    }

    protected List<WebElement> getAllDropDownOptions(By locator) {
        Select dropDown = new Select(webDriver.findElement(locator));
        return dropDown.getOptions();
    }

    /**
     * Selects an option from the drop down found by the WebElement parameter, by the value
     *
     * @param locator The element dropdown you are selecting the option from
     * @param value   The value you want to select the option by.
     */
    protected void selectDropdownByValue(By locator, String value) {
        Select dropDown = new Select(webDriver.findElement(locator));
        dropDown.selectByValue(value);
    }

    protected List<String> getAllWebElementsStrings(By locator) {
        waitForPageToLoad();
        return getAllWebElements(locator).stream().map(WebElement::getText).map(this::removeBinCount).collect(Collectors.toList());
    }

    protected List<String> getAllL2CategoryElementTexts(By locator) {
        waitForPageToLoad();
        return getAllWebElements(locator).stream().map(el -> el.getAttribute("textContent")).map(this::removeBinCount).collect(Collectors.toList());
    }


    // ========================================================//
    // ====================== Data ============================//
    // ========================================================//

    /**
     * Clears the passed in element of any contained text.
     *
     * @param element The element you want to clear
     */
    protected void clear(By element) {
        WAIT_10.until(
                ExpectedConditions.presenceOfElementLocated(element)).clear();
    }

    /**
     * Clears the passed in element of any contained text.
     *
     * @param element The element you want to clear
     */
    protected void clear(WebElement element) {
        WAIT_10.until(
                ExpectedConditions.elementToBeClickable(element)).clear();
    }

    /**
     * Enters the text argument into the element located by the locator argument
     *
     * @param locator The element you want to enter text into
     * @param text    The text you want to enter into the element
     */
    protected void enterText(By locator, String text) {
        WAIT_10.until(
                ExpectedConditions.elementToBeClickable(locator)).sendKeys(text);
    }

    protected List<WebElement> getAllWebElements(By locator) {
        return webDriver.findElements(locator);
    }

    /**
     * To be used when entering text into input elements underneath a button element
     * Or if the text box is not 'clickable'
     * (Example being quote upload, text is sent to the input element within the span button)
     * <p>
     * Enters the text argument into the element argument
     *
     * @param locator The element you want to enter text into
     * @param keys    The text you want to enter into the element
     */
    protected void inputSendKeys(By locator, String keys) {
        webDriver.findElement(locator).sendKeys(keys);
    }

    /**
     * Returns the string data of the Element parameter.
     *
     * @param locator The locator for the element containing the desired text
     * @return The string data from the Element
     */
    public String getTextFromElement(By locator) {
        return WAIT_3.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }

    /**
     * Verifies an element has text in it
     *
     * @param webElement locator of element to check
     * @return true if element has text
     */
    Boolean elementHasText(WebElement webElement) {
        return webElement.getText().length() > 0;
    }

    /**
     * Returns the string data of the Element parameter.
     *
     * @param element The element reference for the element containing the desired text
     * @return The string data from the Element
     */
    protected String getTextFromElement(WebElement element) {
        return WAIT_3.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    protected String getAttributeFromElement(By locator, String attribute) {
        return webDriver.findElement(locator).getAttribute(attribute);
    }

    public String getAttributeByInnerHtml(By locator) {
        return webDriver.findElement(locator).getAttribute("innerHTML");
    }

    protected String getAttributeFromElement(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    protected ArrayList<String> getAttributeListFromElementList(By locator, String attribute) {
        ArrayList<String> elemList = new ArrayList<>();
        webDriver.findElements(locator)
                .forEach(element -> elemList.add(element.getAttribute(attribute)));
        return elemList;
    }

    void listContainsStringsThatContainsStringFromList(List<String> originalLis, List<String> searchStrList) {
        searchStrList.forEach(searchedTerm -> {
            long i = originalLis.stream().filter(link -> link.contains(searchedTerm)).count();
            assertTrue("No search terms were found in the list", i > 0);
        });
    }

    boolean listInAlphaNumericOrder(List<String> list) {
        return Ordering.from(String.CASE_INSENSITIVE_ORDER).isOrdered(list);
    }

    boolean isInReverseAlphaNumericOrder(List<String> aList) {
        return Ordering.from(String.CASE_INSENSITIVE_ORDER).reverse().isOrdered(aList);
    }

    boolean textFromElementContainsIgnoreCase(By locator, String string) {

        return getLowercaseTextFromElement(locator).contains(string.toLowerCase());
    }

    protected boolean textFromElementContains(By locator, String substr) {
        return getTextFromElement(locator).contains(substr);
    }

    private String getLowercaseTextFromElement(By locator) {

        return WAIT_3.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim().toLowerCase();
    }

    boolean textContentIsNotBlank(final By element) {

        return StringUtils.isNotBlank(webDriver.findElement(element).getText());
    }

    protected String getTextFromFirstOption(By locator) {
        return new Select(webDriver.findElement(locator)).getFirstSelectedOption().getText().trim();
    }

    public String removeAccentsFromString(String stringToNormalize) {
        return StringUtils.stripAccents(stringToNormalize);
    }

    public String replaceSpacesWith20(String stringToNormalize) {
        return stringToNormalize.replaceAll(" ", "%20");
    }

    public String replaceSpacesWithPlus(String stringToNormalize) {
        return stringToNormalize.replaceAll(" ", "+");
    }

    protected String replaceSpacesWithDashes(String stringToNormalize) {
        return stringToNormalize.replaceAll(" ", "-");
    }

    protected String replaceSpacesWithNothing(String stringToNormalize) {
        return stringToNormalize.replaceAll(" ", "");
    }

    protected String removeTESTFromEndOfString(String stringToNormalize) {
        return stringToNormalize.replaceAll("TEST$", "");
    }

    protected String removeBinCount(String stringToNormalize) {
        return stringToNormalize.replaceAll(" ?\\([0-9]*.\\)", "");
    }

    protected String getTitleFromL2Cat(String stringToNormalize) {
        return stringToNormalize.replaceAll(" ?\\([0-9]*.\\)", "").replaceAll("\t", "").replaceAll("\n", "");
    }


    protected String getBinCountFromL2Cat(String stringToNormalize) {
        String string = stringToNormalize.replaceAll("\t", "").replaceAll("\n", "");
        return string.substring(string.indexOf("(") + 1, string.length() - 1);
    }

    public boolean pageDataBlockValuesChecker(String name, String parameter, String value) {
        String Val = (String) JSEXECUTE.executeScript("return " + name + parameter);
        return value.equals(Val);
    }

    /**
     * System print line
     *
     * @param string The string to output to the console
     */
    void puts(String string) {
        System.out.println(string);
    }

    /**
     * Returns a boolean based on whether the CharSecquence parameter is contained within the page source of the current page
     *
     * @param searchString - The characters expected to be contained within the page souree
     * @return boolean based on presence of CharSequence in page source
     */
    public boolean isInPageSource(CharSequence searchString) {
        sleep(5);
        return webDriver.getPageSource().contains(searchString);
    }

    /**
     * @return the URL of the current page
     */
    public String getUrl() {
        return webDriver.getCurrentUrl();
    }

    /**
     * Returns a boolean based on whether the string parameter is contained within the current url
     *
     * @param expected The String expected to be contained within the URL
     * @return boolean based on presence of string in URL
     */
    public boolean isInUrl(String expected) {
        return webDriver.getCurrentUrl().contains(expected.toLowerCase());
    }

    /**
     * @return The page source of the current page as a String
     */
    public String getPageSource() {
        return webDriver.getPageSource();
    }

    public boolean urlContainsString(String expected) {
        waitForJQuery();
        return webDriver.getCurrentUrl().toLowerCase().contains(expected.toLowerCase());
    }

    public boolean urlDoesNotContain(String term) {
        return !containsIgnoreCase(webDriver.getCurrentUrl().toLowerCase(), term.toLowerCase());
    }

    public boolean containsIgnoreCase(String stringToSearch, String searchString) {
        return StringUtils.containsIgnoreCase(stringToSearch, searchString);
    }

    /**
     * Waits the specified amount of time (Polling every 2 seconds) for the element to either:
     * - No longer be part of the DOM
     * - For current WebElement Reference to become stale
     *
     * @param element    - The original element reference before the element is expected to disappear
     * @param maxSeconds - The max number of seconds to WAIT_FLUENT before returning false
     * @return boolean based on whether the original element has disappeared or changed state
     */
    protected boolean waitForElementToDisappear(WebElement element, int maxSeconds) {
        int timer = 0;

        boolean present = element.isDisplayed();
        while (present && timer <= maxSeconds) {
            try {
                present = element.isDisplayed();
                sleep(1);
                timer++;
            } catch (Exception e) {
                return true;
            }
        }
        return timer < maxSeconds;
    }

    protected boolean elementDisplayed(By locator) {
        return webDriver.findElement(locator).isDisplayed();
    }


    /**
     * Will return true if the element is displayed, or false if not.
     * This will capture the NoSuchElementException and return false to be used when expecting the element
     * to NOT be present (saves throwing the exception higher.
     *
     * @param locator
     * @return
     */
    public boolean elementIsDisplayed(By locator) {
        boolean retVal = true;
        try {
            retVal = webDriver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException nse) {
            retVal = false;
        }
        return retVal;
    }

    public boolean elementIsSelected(final By locator) {

        return webDriver.findElement(locator).isSelected();
    }

    /**
     * @return Boolean based on whether the header element is visible
     */
    public boolean resultsHeaderIsDisplayed() {
        boolean headerVisible;
        headerVisible = (boolean) JSEXECUTE.executeScript("var elemTop = el.getBoundingClientRect().top;\n" +
                "    var elemBottom = el.getBoundingClientRect().bottom;\n" +
                "\n" +
                "    var isVisible = (elemTop >= 0) && (elemBottom <= window.innerHeight);\n" +
                "    return isVisible;");
        return headerVisible;
    }

    /**
     * @return Boolean based on whether the header element is visible when scrolling
     */
    public boolean resultsHeaderHorizontalScroll() {
        boolean headerScrollable;
        headerScrollable = (boolean) JSEXECUTE.executeScript("var elemTop = el.getBoundingClientRect().top;\n" +
                "    var elemBottom = el.getBoundingClientRect().bottom;\n" +
                "\n" +
                "    var isVisible = (elemTop >= 0) && (elemBottom <= window.innerHeight);\n" +
                "    return isVisible;");
        return headerScrollable;
    }

    /**
     * Returns boolean based on if element is displayed or not. Waits 3 seconds
     *
     * @param element Element you expect to be displayed
     * @return boolean based on if element is displayed or not.
     */
    protected boolean elementDisplayed(WebElement element) {
        return WAIT_3.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
    }

    /* Returns boolean base on if element is present from list of web elements */
    public boolean isElementPresent(By locator) {
        setImplicitTimeout(3);
        List<WebElement> elements = getAllWebElements(locator);
        return elements.size() > 0;
    }


    /**
     * Used to retrieve a value from a Key and Value pair store in the SharedData Map in SharedDriver,
     * by using the Key name.
     *
     * @param key The Key name of the data you want to retrieve
     * @return The actual value, saved against the specific Key
     */
    public String getSharedData(String key) {
        return webDriver.getSharedData(key);
    }

    /**
     * Saves a Key and Value pair set to the SharedData Map stored in SharedDriver,
     * which will be available for the duration of the test
     *
     * @param key   The Key name of the data you want to store - you will use this to retrieve the value later on
     * @param value The actual value, saved against the specific Key
     */
    public void saveSharedData(String key, Object value) {
        webDriver.setSharedData(key, value);
    }

    /**
     * @return The size of shared search data
     */
    public Integer getSharedDataSize() {
        return webDriver.getSharedDataSize();
    }

    /**
     * @return All of the values from shared data
     */
    public Collection<Object> getSharedDataValues() {
        return webDriver.getSharedDataValues();
    }

    /**
     * @return All of the keys from shared data
     */
    public Set<String> getSharedDataKeys() {
        return webDriver.getSharedDataKeys();
    }

    /**
     * Saves a Key and Value pair set to the SharedData Map stored in SharedDriver,
     * which will be available for the duration of the test
     *
     * @param key The Key name of the data you want to store - you will use this to retrieve the value later on
     * @param nav The actual Navigation Object, saved against the specific Key
     */
    public void saveSearchSharedData(String key, Navigation nav) {
        webDriver.setSearchSharedData(key, nav);
    }

    /**
     * Used to retrieve a value from a Key and Value pair store in the SharedData Map in SharedDriver,
     * by using the Key name.
     *
     * @param key The Key name of the data you want to retrieve
     * @return The actual value, saved against the specific Key
     */
    public Object getSharedDataAsObject(String key) {
        return webDriver.getSharedDataAsObject(key);
    }

    /**
     * @return The size of shared search data
     */
    public Integer getSearchSharedDataSize() {
        return webDriver.getSearchSharedDataSize();
    }

    /**
     * @return All of the values from search shared data
     */
    protected Collection<Navigation> getSearchSharedDataValues() {
        return webDriver.getSearchSharedDataValues();
    }

    /**
     * Used to retrieve a value from a Key and Value pair store in the SharedData Map in SharedDriver,
     * by using the Key name.
     *
     * @param key The Key name of the data you want to retrieve
     * @return The actual value, saved against the specific Key
     */
    public Navigation getSearchSharedData(String key) {
        return webDriver.getSearchSharedData(key);
    }

    /**
     * @return All of the keys from search shared data
     */
    public Set<String> getSearchSharedDataKeys() {
        return webDriver.getSearchSharedDataKeys();
    }


    // ========================================================//
    // ================== Browser Actions =====================//
    // ========================================================//

    /**
     * Loops through a list of Web elements and checks if the http response code of any of the 'href'
     * attributes is valid or not via 'is404CodeNotReturned' method in Base Page. It will also fail the test
     * if no links are found at all.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    void checkValidLinks(List<WebElement> elements) {
        if (elements.isEmpty()) {
            fail("No links where found");
        } else {
            for (WebElement element : elements) {
                assertTrue(
                    String.format("The link: %s is invalid.", element.getAttribute("href")),
                    is404CodeNotReturned(element.getAttribute("href"))
                );
            }
        }
    }

    /**
     * @return Boolean based on whether a browser alert box has appeared on screen
     */
    public boolean isAlertPresent() {
        try {
            new WebDriverWait(webDriver, 2).ignoring(NoAlertPresentException.class)
                    .until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    /**
     * Verify there is no alert displayed on the UI
     */
    public boolean checkNoAlertPresent() {
        return isAlertPresent();
    }

    /**
     * Scrolls to the height and width int parameters of the current page
     *
     * @param width  - location of desired element on page's X axis as int
     * @param height - location of desired element on page's Y axis as int
     */
    public void scrollTo(int width, int height) {
        webDriver.executeScript("scroll(" + width + "," + height + ")");
    }

    /**
     * Switches to the active Element
     */
    public void switchToActiveElement() {
        webDriver.switchTo().activeElement();
    }

    public WebElement getFocussedElement() {
        return webDriver.switchTo().activeElement();
    }

    void switchToTabByIndex(int tab) {
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tab));
    }

    void closeTabByIndex(int tab) {
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tab)).close();
    }

    /**
     * Switches to an alert if there is one
     */
    public void switchToAlert() {
        webDriver.switchTo().alert();
    }

    public void acceptAlert() {
        webDriver.switchTo().alert().accept();
    }

    /**
     * Switches to the default content for that thread
     */
    public void switchToDefaultContent() {
        webDriver.switchTo().defaultContent();
    }

    /**
     * Navigates back to the previous page, within the browser
     */
    public void clickToGoBack() {
        webDriver.navigate().back();
    }

    /**
     * @return The server current server number
     */
    protected String getServerNumber() {
        String serverNo = webDriver.getPageSource();
        Pattern pattern = Pattern.compile("<!--\\sBuild\\sDate\\s*:\\s\\d*_\\d*.(\\d*)");
        Matcher matcher = pattern.matcher(serverNo);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "unknown";
        }
    }

    public String getPropertyFromPageData(String tagName) {
        String tagNameProperty;
        String scriptData = getPageData();
        Pattern pattern = Pattern.compile(tagName + "\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(scriptData);
        if (matcher.find()) {
            tagNameProperty = matcher.group(1);
        } else {
            tagNameProperty = "Script Tag Was Not Found";
        }
        return tagNameProperty;
    }

    public String getPageData() {
        waitForPageToLoad();
        return trim(webDriver.findElement(By.xpath("//script[contains(text(),'pageData')]"))
                .getAttribute("innerHTML"));
    }

    public String getPageTypeFromPageData() {
        return getPropertyFromPageData("page_type");
    }

    public String getPageHierarchyFromPageData() {
        return getPropertyFromPageData("page_hierarchy");
    }

    public String getSearchKeywordFromPageData() {
        waitForPageToLoad();
        return getPropertyFromPageData("search_keyword");
    }

    public String getPropertyFromPageDataLayer(String tagName, String page) {
        String tagNameProperty, scriptData;
        scriptData = webDriver.findElement(By.xpath("//script[contains(text(),'pageData')]")).getAttribute("innerHTML");
        Pattern pattern = Pattern.compile(tagName + "\":\"*(.*?)\"");
        Matcher matcher = pattern.matcher(scriptData);
        if (matcher.find()) {
            tagNameProperty = matcher.group(1);
        } else {
            tagNameProperty = "Script Tag Was Not Found";
        }
        return tagNameProperty;
    }

    public boolean checkForPropertyFromPageDataLayer(String tagName) {
        String scriptData;
        boolean tagNameFound = false;
        scriptData = webDriver.findElement(By.xpath("//script[contains(text(),'pageData')]")).getAttribute("innerHTML");
        Pattern pattern = Pattern.compile(tagName + "\":\"*(.*?)\"");
        Matcher matcher = pattern.matcher(scriptData);
        if (matcher.find()) {
            tagNameFound = true;
        }
        return tagNameFound;
    }

    public String getDatablockPagePropertyValue(String expectedPageProperty) {
        String actualPageName = (String) webDriver.executeScript("return rs.web.digitalData." + expectedPageProperty);
        return actualPageName.trim();
    }

    public boolean isDataBlockValuePresent(String expectedPageProperty) {
        return null != JSEXECUTE.executeScript("return rs.web.digitalData." + expectedPageProperty);
    }


    // ========================================================//
    // ======================= XML ============================//
    // ========================================================//

    public void pageRefresh() {
        webDriver.navigate().refresh();
    }

    /**
     * Returns the property as a Boolean
     * Anything other than "true" will return false, including null
     * <p>
     * Example: {@code Boolean.parseBoolean("True")} returns {@code true}.<br>
     * Example: {@code Boolean.parseBoolean("yes")} returns {@code false}.
     *
     * @param property desired property to parse as boolean
     * @return boolean value of property parsed as boolean where "true" is {@code true}
     */
    public boolean getPropertyAsBoolean(String property) {
        PROPS_READER = new PropertiesReader();
        return Boolean.parseBoolean(PROPS_READER.getProperty(property));
    }

    /**
     * Gets the value (if there is one) of the node specified in the tagName parameter, form the xmlString parameter
     *
     * @param tagName   The Key name of the value you want to be returned
     * @param xmlString The XML data, as a string. Containing the value you want
     * @return The value of the node - tagName
     * @throws SAXException - Couldn't process XML or xmlString was null
     */
    public boolean valueFromXMLMatchesExpected(String tagName, String xmlString, String expected) throws ParserConfigurationException, IOException, SAXException {
        return getXMLValue(tagName, xmlString).equals(expected);
    }

    public String getXMLValue(String tagName, String xmlString) throws ParserConfigurationException, IOException, SAXException {
        if (xmlString.contains(tagName)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            disableDtdValidation(db);
            InputSource is = new InputSource();
            String xmlFormatted = cleanXML(xmlString);
            is.setCharacterStream(new StringReader(xmlFormatted));
            Document doc = null;
            doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(tagName);
            Element element = (Element) nodes.item(0);
            return getCharacterDataFromElement(element);
        } else return "";
    }

    /**
     * Replaces any invalid characters that need escaping with valid escapes
     *
     * @param xmldirty The xml string that needs escaping
     * @return the original xml containing escaped
     */
    private String cleanXML(String xmldirty) {
        return xmldirty.replaceAll("&", "&amp;");
    }

    /**
     * THIS WAS WRITTEN DURING AN ELS PERIOD AND CAN BE DELETED
     *
     * @param orderNumber
     * @param country
     */
    public void writeOrderNumberToCSV(String orderNumber, String country) {
        webDriver.writeOrderNumberToCSV(orderNumber, country);
    }

    public LogEntries getConsoleOutput() {
        return webDriver.manage().logs().get(LogType.BROWSER);
    }

    List<String> getAttributesFromString(String string) {
        String[] array = string.split(Pattern.quote("||"));
        List<String> returnList = new ArrayList<>();
        for (String str : array) {
            returnList.add(str.split(Pattern.quote("|"))[0]);
        }
        return returnList;
    }

    protected String normalisePrice(String price) {
        String clean1 = price.replaceAll("[^0-9]", "");
        return clean1.replaceAll("[0]*$", "");
    }

    protected void assertEqualsLowerCase(String expected, String actual) {
        assertEquals(
            "The values should have been the same (when compared in lowercase)",
            expected.toLowerCase(),
            actual.toLowerCase()
        );
    }

    public void goToURL(String url) {
        webDriver.navigate().to(url);
    }

    public String getMetaTagAttributeValue(String name, String attribute) {
        return webDriver.findElement(By.xpath("//meta[@name='" + name + "']")).getAttribute(attribute);
    }

    /**
     * Checks that the text on the page is a shade of green
     */
    protected boolean isTextGreen(final String rgba) {

        final String rgbaValues = rgba.replaceAll("[^0-9,]", "");

        final int indexOfFirstComma = rgbaValues.indexOf(",");
        final int indexOfSecondComma = rgbaValues.indexOf(",", indexOfFirstComma + 1);
        final int indexOfThirdComma = rgbaValues.indexOf(",", indexOfSecondComma + 1);

        final int red = Integer.valueOf(rgbaValues.substring(0, indexOfFirstComma));
        final int green = Integer.valueOf(rgbaValues.substring(indexOfFirstComma + 1, indexOfSecondComma));
        final int blue = Integer.valueOf(rgbaValues.substring(indexOfSecondComma + 1, indexOfThirdComma));

        return green > red + 50 && green > blue + 50;
    }

    protected void acceptCertificateForIE11() {

        if (webDriver.isType(InternetExplorerDriver.class)) {

            if (elementClickable(By.id("overridelink"))) {

                webDriver.get("javascript:document.getElementById('overridelink').click();");
            }
        }
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public List<WebElement> getAllElementsInsideFirstTagFound(By selector) {
        List<WebElement> rootWebElement = webDriver.findElements(selector);
        List<WebElement> childs = rootWebElement.get(0).findElements(By.xpath(".//*"));
        return childs;
    }

    public String getParameterValueFromUrl(String url, String pn) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        List<NameValuePair> nameValuePairs = uriBuilder.getQueryParams();
        String paramValue = null;
        for (NameValuePair nameValuePair : nameValuePairs) {
            if (pn.equals(nameValuePair.getName())) {
                paramValue = nameValuePair.getValue();
                break;
            }
        }
        return paramValue;
    }

    public void addCookie(String jSessionIdCookieName, String jSessionIdCookieValue) {
        waitForPageToLoad();
        Cookie newCookie = new Cookie(jSessionIdCookieName, jSessionIdCookieValue);
        webDriver.manage().addCookie(newCookie);
    }

    public String getCookieValue(String cookieName) {
        waitForPageToLoad();
        String cookieValue = webDriver.manage().getCookieNamed(cookieName).getValue();
        return cookieValue;
    }

    public void editCookieValue(String cookieName, String expectedCookieValue, Cookie newCookie) {
        try {
            waitForPageToLoad();
            Cookie cookie = webDriver.manage().getCookieNamed(cookieName);
            if (cookie != null) {
                String cookieValue = cookie.getValue();
                if (!cookieValue.equalsIgnoreCase(expectedCookieValue)) {
                    webDriver.manage().deleteCookie(cookie);
                    webDriver.manage().addCookie(newCookie);
                    webDriver.navigate().refresh();
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public static WebElement getWebElement(By identifier) {
        try {
            WebElement element = webDriver.findElement(identifier);
            return element;

        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
    }

    public static WebElement getWebElements(By identifier, int index) {
        try {
            List<WebElement> webElements = webDriver.findElements(identifier);
            return webElements.get(index);

        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
    }

    public String getColour(String cssValue, String newIconColourType) {
        try {
            String[] hexValue = StringUtils.substringBetween(cssValue, newIconColourType + "(", ")").split(",");

            int hexValue1 = Integer.parseInt(hexValue[0]);
            hexValue[1] = hexValue[1].trim();
            int hexValue2 = Integer.parseInt(hexValue[1]);
            hexValue[2] = hexValue[2].trim();
            int hexValue3 = Integer.parseInt(hexValue[2]);

            if (newIconColourType.equalsIgnoreCase("rgba")) {
                hexValue[3] = hexValue[3].trim();
                int hexValue4 = Integer.parseInt(hexValue[3]);

                String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3, hexValue4);
                return actualColor;
            }

            String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
            return actualColor;
        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
    }

    /**
     * Removes the Live Chat elements that causes tests to fail when Jenkins attempts to click them.
     */

    public void removeLiveChat() {
        if (elementPresent(By.cssSelector(".LPMimage")) || elementPresent(By.cssSelector(".LPMoverlay"))) {
            webDriver.executeScript("$('.LPMimage').remove();");
            webDriver.executeScript("$('.LPMoverlay').remove();");
        }
    }

    public String getTitleFromPageSource() {
        return webDriver.getTitle();
    }

    public boolean doesStockNumberInURLContain(String charactersToCheckPresenceOf) {
        int stockNumberIndex = 6;
        String[] splitByBackslash = getUrl().split("/");
        String stockNumberFromURL = splitByBackslash[stockNumberIndex];
        return stockNumberFromURL.contains(charactersToCheckPresenceOf);
    }

    public void closeModalIfDisplayed() {
        if (elementPresent(MODAL_FEEDBACK_CLOSE)) {
            webDriver.findElement(MODAL_FEEDBACK_CLOSE).click();
        } else if (elementPresent(APAC_MODAL_FEEDBACK_CLOSE)) {
            click(APAC_MODAL_FEEDBACK_CLOSE);
        }
    }

    public void closeTermsModalIfDisplayed() {
        if (elementPresent(MODAL_TERMS_CLOSE)) {
            webDriver.findElement(MODAL_TERMS_CLOSE).click();
        }
    }

    public void hitURLDirectly(String path) {
        refreshPropsReader();
        Environment env = Environment.valueOf(System.getProperty("env"));
        String domain = env.getURL(System.getProperty("country"));
        String url = domain + path;
        webDriver.get(url);
    }

    public boolean textOnPage(String text) {
        return webDriver.getPageSource().contains(text);
    }

    public String getInnerHTMLFromElement(By locator) {
        WebElement scriptElement = webDriver.findElement(locator);
        return (String) webDriver.executeScript("return arguments[0].innerHTML", scriptElement);
    }

    public FileInputStream getFile(String path) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        return new FileInputStream(new File(classLoader.getResource(path).getFile()));
    }

    public long getPageLoadTime() {
        Long loadTime = (Long) ((JavascriptExecutor) webDriver).executeScript(
                "return performance.timing.loadEventEnd - performance.timing.navigationStart;");
        System.out.println(loadTime);
        return loadTime;
    }

    public static String getPageTimeToInteractiveData() {
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String netData = ((JavascriptExecutor) webDriver).executeScript(scriptToExecute).toString();
        System.out.println(netData);
        return netData;
    }

    public static void enterKeySimulation(String url) {
        webDriver.navigate().to(url);
    }

    public void clickCompareCheckbox(int numProducts, By checkMarks, By stockNumber) {
        List<WebElement> allCheckMarks = webDriver.findElements(checkMarks);
        removeLiveChat();
        for (int i = 0; i < numProducts; i++) {
            click(allCheckMarks.get(i));
            waitForJQuery();
        }
        List<WebElement> skuList = getAllWebElements(stockNumber).stream().limit(numProducts).collect(Collectors.toList());
        saveSharedData("comparedProducts", skuList.stream().map(WebElement::getText).collect(Collectors.toList()));
    }

    public WebElement getElementFromList(By bySelectorForListOfWebElements, String expectedText) {
        waitForElementVisible(bySelectorForListOfWebElements);
        List<WebElement> list = getAllWebElements(bySelectorForListOfWebElements);
        return list.stream().filter(e -> e.getText().contains(expectedText)).findFirst().get();
    }

    public boolean isElementInList(By bySelectorForListOfWebElements, String expectedText) {
        List<WebElement> list = new ArrayList<>();
        boolean elementPresent = false;
        try {
            list = getAllWebElements(bySelectorForListOfWebElements);
        } catch (NoSuchElementException e) {
            fail("The element containing the list could not be found");
        }
        for (WebElement listItem : list) {
            if ((listItem.getText()).contains(expectedText)) {
                elementPresent = true;
            }
        }
        return elementPresent;
    }

    public void clickElementFromList(By by, String expectedText) {
        waitforElementClickable(by);
        click(getElementFromList(by, expectedText));
    }

    /* This Method is return Json Object from Json Script */
    public List<JsonObject> getAllJsonLdByLocator(By locator) {
        final JsonParser parser = new JsonParser();
        final List<String> jsonList = getAttributeListFromElementList(locator, "innerHTML");
        return jsonList.stream().map(json -> (JsonObject) parser.parse(json)).collect(Collectors.toList());
    }

    public void hoverElementFromList(By by, String expectedText) {
        waitforElementClickable(by);
        hover(getElementFromList(by, expectedText));
    }

    /* This method return Attributes value*/
    public String getTagAttributeValue(String type, String identifier, String name, String attribute) {
        waitForPageToLoad();
        String text = webDriver.findElement(By.xpath("//" + type + "[@" + identifier + "='" + name + "']")).getAttribute(attribute);
        getPageLoadTime();
        return text;
    }

    public String replaceSpacesWithFullstopsAndGetProperty(String property) {
        return getProperty(property.replaceAll(" ", "."));
    }

    public String removeAllNonNumericCharacters(String string) {
        return string.replaceAll("[^\\d.]", "");
    }

    public int getWebElementsPositionInList(By bySelectorForListOfWebElements, String elementText) {
        int intValue = 0;
        boolean loopAccessed = false;
        waitForElementVisible(bySelectorForListOfWebElements);
        List<WebElement> list = getAllWebElements(bySelectorForListOfWebElements);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().contains(elementText)) {
                intValue = i;
                loopAccessed = true;
                break;
            }
        }
        if (!loopAccessed) {
            Assert.fail("The category \"" + elementText + "\" was not found.");
        }
        return intValue;
    }

    public boolean isJenkinsRun() {
        return (System.getProperty("headless").equalsIgnoreCase("true") && System.getProperty("local").equalsIgnoreCase("false"));
    }

    protected void clickRandomElementInList(By selector) {
        Random rand = new Random();
        waitForElementVisible(selector);
        List<WebElement> list = getAllWebElements(selector);
        click(list.get(rand.nextInt(list.size())));
    }

    public Integer getSearchResultsFromDatablock() {
        long Val = (long) JSEXECUTE.executeScript("return " + "rs.web.digitalData." + "search_results");
        Integer val = (int) (long) Val;
        return val;
    }
    /* This Method get data from property file and convert them to a list */
    public static List<String> getDataFromPropertyFileToAList(String property){
        return Arrays.stream(getProperty(property).split(",")).collect(Collectors.toList());
    }

    /* This Method is used to navigate back to the current page*/
    public void navigateBack() {
        webDriver.navigate().back();
    }

    /* This Method is close unexpected alert*/
 public void closeUnexpectedAlertBox() {
      webDriver.switchTo().alert().dismiss();
 }

    protected void  clickJSByElement(WebElement element) {
        JavascriptExecutor je = (JavascriptExecutor) webDriver;
        je.executeScript("arguments[0].click();", element);
    }


    void sendKeyStrokes(WebElement elem, Keys key, int number) {
        for (int i = 0; i < number; i++) {
            elem.sendKeys(key);
        }
    }
    public static String decodeHexToStr(final String hexEncodedStr) {
        String decodedStr = null;
        if (hexEncodedStr != null) {
            try {
                char[] hexCharArray = hexEncodedStr.toCharArray();
                int hexArrayLength = hexCharArray.length;
                byte[] out = new byte[hexArrayLength >> 1];
                // two characters form the hex value.
                for (int i = 0, j = 0; j < hexArrayLength; i++) {
                    int f = toDigit(hexCharArray[j], j) << 4;
                    j++;
                    f = f | toDigit(hexCharArray[j], j);
                    j++;
                    out[i] = (byte) (f & 0xFF);
                }
                decodedStr = new String(out, CHARSET_UTF8);
            } catch (Exception e) {
                System.out.println(e);
                //.error("Decode Hex To String exception.", e);
            }
        }
        return decodedStr;
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch A character to convert to an integer digit
     * @param index The index of the character in the source
     * @return An integer
     * ##@throws DecoderException Thrown if ch is an illegal hex character
     */
    protected static int toDigit(final char ch, final int index) throws RuntimeException {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }


      public static boolean verifyRelevancyDataParameter(String decodedRelevancyData, String parameterRegEx) {
        if (decodedRelevancyData == null || parameterRegEx == null) {
            return false;
        }
          if (decodedRelevancyData.matches(parameterRegEx)) {
            System.out.println(decodedRelevancyData + " matches " + parameterRegEx);
            return true;
        }
        System.out.println("no matches found for " + parameterRegEx);
        return false;
    }
}