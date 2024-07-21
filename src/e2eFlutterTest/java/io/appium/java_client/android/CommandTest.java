package io.appium.java_client.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.flutter.commands.ScrollParameter;
import io.appium.java_client.flutter.commands.WaitParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

public class CommandTest extends BaseFlutterTest {

    AppiumBy.FlutterBy messageFieldLocator = AppiumBy.flutterKey("message_field");
    AppiumBy.FlutterBy toggleButtonLocator = AppiumBy.flutterKey("toggle_button");

    @Test
    public void testWaitCommand() {
        WebElement loginButton = driver.findElement(this.loginButton);
        loginButton.click();
        openScreen("Lazy Loading");

        WebElement messageField = driver.findElement(messageFieldLocator);
        WebElement toggleButton = driver.findElement(toggleButtonLocator);

        Assertions.assertEquals(messageField.getText(), "Hello world");
        toggleButton.click();
        Assertions.assertEquals(messageField.getText(), "Hello world");

        WaitParameter waitParameter = new WaitParameter().setLocator(messageFieldLocator);

        driver.waitForInVisible(waitParameter);
        Assertions.assertEquals(0, driver.findElements(messageFieldLocator).size());
        toggleButton.click();
        driver.waitForVisible(waitParameter);
        Assertions.assertEquals(1, driver.findElements(messageFieldLocator).size());
        Assertions.assertEquals(messageField.getText(), "Hello world");
    }

    @Test
    public void testScrollTillVisibleCommand() {
        WebElement loginButton = driver.findElement(this.loginButton);
        loginButton.click();
        openScreen("Vertical Swiping");

        WebElement firstElement = driver.scrollTillVisible(new ScrollParameter(AppiumBy.flutterText("Java")));
        Assertions.assertTrue(Boolean.parseBoolean(firstElement.getAttribute("displayed")));

        WebElement lastElement = driver.scrollTillVisible(new ScrollParameter(AppiumBy.flutterText("Protractor")));
        Assertions.assertTrue(Boolean.parseBoolean(lastElement.getAttribute("displayed")));
        Assertions.assertFalse(Boolean.parseBoolean(firstElement.getAttribute("displayed")));

        firstElement = driver.scrollTillVisible(
                new ScrollParameter(AppiumBy.flutterText("Java"),
                        ScrollParameter.ScrollDirection.UP)
        );
        Assertions.assertTrue(Boolean.parseBoolean(firstElement.getAttribute("displayed")));
        Assertions.assertFalse(Boolean.parseBoolean(lastElement.getAttribute("displayed")));
    }

}
