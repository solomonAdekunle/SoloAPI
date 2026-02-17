package stepdefs.hooks;

import config.DriverFactory;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stepdefs.SharedDriver;

import java.lang.management.ManagementFactory;

/**
 * Created by e0653032 on 17/04/2019.
 */
public class BrowserHooks {

    private final SharedDriver sharedDriver;

    private static final Logger logger = LoggerFactory.getLogger(BrowserHooks.class);

    public BrowserHooks(SharedDriver sharedDriver) {
        this.sharedDriver = sharedDriver;
    }

    @Before(order=1)
    public synchronized void setUp() {
        logger.info(
            String.format(
                "Started in thread %d, in JVM '%s'.",
                Thread.currentThread().getId(),
                ManagementFactory.getRuntimeMXBean().getName()
            )
        );
        WebDriver webDriver = new DriverFactory().getDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().deleteAllCookies();

        this.sharedDriver.setDriver(webDriver);
    }

    @After(order=1)
    public void closeBrowser(){
        this.sharedDriver.quit();
    }
}
