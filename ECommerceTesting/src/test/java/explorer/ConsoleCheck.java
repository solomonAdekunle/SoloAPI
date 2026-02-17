package explorer;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.EventFiringWebDriver;


public class ConsoleCheck {

    public static LogEntries getConsoleLog(final EventFiringWebDriver webDriver) {

        return webDriver.manage().logs().get(LogType.BROWSER);
    }
}
