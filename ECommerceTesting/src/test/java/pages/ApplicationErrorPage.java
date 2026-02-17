package pages;

import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stepdefs.SharedDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationErrorPage Page Object representing an application error page Typically you will see
 * this when an SVC or WEC error has occurred
 *
 * @author C0953512 Nick Lee
 */
public class ApplicationErrorPage extends Page {

    private final Logger logger = LoggerFactory.getLogger(ApplicationErrorPage.class);

    /**
     * Instantiate new ApplicationErrorPage instance
     *
     * @param webDriver webdriver instance
     */
    public ApplicationErrorPage(SharedDriver webDriver) {
        super(webDriver);
    }

    /**
     * Checks if a fatal error is displayed. Generally we should be using this to check if we should
     * be trying to get the error type etc prior to utilising the other methods offered by this
     * class
     *
     * @return true if an SVC or WEC error is displayed
     */
    public boolean isApplicationErrorPageDisplayed() {
        return null != getErrorCode();
    }

    /**
     * Obtains the error type from the error data in the page head
     *
     * @return the error type
     */
    public String getErrorType() {
        return getErrorData().get("error_type");
    }

    /**
     * Obtains the error code from the error data in the page head
     *
     * @return the error code
     */
    public String getErrorCode() {
        return getErrorData().get("error_code");
    }

    /**
     * Obtains the error code from the data layer
     *
     * @return the error data. Will return an empty Map if not data was available on the page
     */
    private Map<String, String> getErrorData() {
        try {
            return (Map<String, String>) webDriver.executeScript("return rs.web.digitalData");
        } catch (WebDriverException e) {
            // We should only end up here if the digital data block was not available on the page
            logger.warn("The digital data block was not available on the page");
            return new HashMap<>();
        }
    }
}
