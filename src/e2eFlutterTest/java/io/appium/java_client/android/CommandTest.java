package io.appium.java_client.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.TestUtils;
import io.appium.java_client.flutter.commands.DoubleClickParameter;
import io.appium.java_client.flutter.commands.DragAndDropParameter;
import io.appium.java_client.flutter.commands.LongPressParameter;
import io.appium.java_client.flutter.commands.ScrollParameter;
import io.appium.java_client.flutter.commands.WaitParameter;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandTest extends BaseFlutterTest {

    private static final AppiumBy.FlutterBy MESSAGE_FIELD = AppiumBy.flutterKey("message_field");
    private static final AppiumBy.FlutterBy TOGGLE_BUTTON = AppiumBy.flutterKey("toggle_button");

    @Test
    void testWaitCommand() {
        WebElement loginButton = driver.findElement(BaseFlutterTest.LOGIN_BUTTON);
        loginButton.click();
        openScreen("Lazy Loading");

        WebElement messageField = driver.findElement(MESSAGE_FIELD);
        WebElement toggleButton = driver.findElement(TOGGLE_BUTTON);

        assertEquals(messageField.getText(), "Hello world");
        toggleButton.click();
        assertEquals(messageField.getText(), "Hello world");

        WaitParameter waitParameter = new WaitParameter().setLocator(MESSAGE_FIELD);

        driver.waitForInVisible(waitParameter);
        assertEquals(0, driver.findElements(MESSAGE_FIELD).size());
        toggleButton.click();
        driver.waitForVisible(waitParameter);
        assertEquals(1, driver.findElements(MESSAGE_FIELD).size());
        assertEquals(messageField.getText(), "Hello world");
    }

    @Test
    void testScrollTillVisibleCommand() {
        WebElement loginButton = driver.findElement(BaseFlutterTest.LOGIN_BUTTON);
        loginButton.click();
        openScreen("Vertical Swiping");

        WebElement firstElement = driver.scrollTillVisible(new ScrollParameter(AppiumBy.flutterText("Java")));
        assertTrue(Boolean.parseBoolean(firstElement.getAttribute("displayed")));

        WebElement lastElement = driver.scrollTillVisible(new ScrollParameter(AppiumBy.flutterText("Protractor")));
        assertTrue(Boolean.parseBoolean(lastElement.getAttribute("displayed")));
        assertFalse(Boolean.parseBoolean(firstElement.getAttribute("displayed")));

        firstElement = driver.scrollTillVisible(
                new ScrollParameter(AppiumBy.flutterText("Java"),
                        ScrollParameter.ScrollDirection.UP)
        );
        assertTrue(Boolean.parseBoolean(firstElement.getAttribute("displayed")));
        assertFalse(Boolean.parseBoolean(lastElement.getAttribute("displayed")));
    }

    @Test
    void testDoubleClickCommand() {
        driver.findElement(BaseFlutterTest.LOGIN_BUTTON).click();
        openScreen("Double Tap");

        WebElement doubleTapButton = driver
                .findElement(AppiumBy.flutterKey("double_tap_button"))
                .findElement(AppiumBy.flutterText("Double Tap"));
        assertEquals("Double Tap", doubleTapButton.getText());

        AppiumBy.FlutterBy okButton = AppiumBy.flutterText("Ok");
        AppiumBy.FlutterBy successPopup = AppiumBy.flutterTextContaining("Successful");

        driver.performDoubleClick(new DoubleClickParameter().setElement(doubleTapButton));
        assertEquals(driver.findElement(successPopup).getText(), "Double Tap Successful");
        driver.findElement(okButton).click();

        driver.performDoubleClick(new DoubleClickParameter()
                .setElement(doubleTapButton)
                .setOffset(new Point(10, 2))
        );
        assertEquals(driver.findElement(successPopup).getText(), "Double Tap Successful");
        driver.findElement(okButton).click();
    }

    @Test
    void testLongPressCommand() {
        driver.findElement(BaseFlutterTest.LOGIN_BUTTON).click();
        openScreen("Long Press");

        AppiumBy.FlutterBy successPopup = AppiumBy.flutterText("It was a long press");
        WebElement longPressButton = driver
                .findElement(AppiumBy.flutterKey("long_press_button"));

        driver.performLongPress(new LongPressParameter().setElement(longPressButton));
        assertEquals(driver.findElement(successPopup).getText(), "It was a long press");
        assertTrue(driver.findElement(successPopup).isDisplayed());
    }

    @Test
    void testDragAndDropCommand() {
        driver.findElement(BaseFlutterTest.LOGIN_BUTTON).click();
        openScreen("Drag & Drop");

        driver.performDragAndDrop(new DragAndDropParameter(
                driver.findElement(AppiumBy.flutterKey("drag_me")),
                driver.findElement(AppiumBy.flutterKey("drop_zone"))
        ));
        assertTrue(driver.findElement(AppiumBy.flutterText("The box is dropped")).isDisplayed());
        assertEquals(driver.findElement(AppiumBy.flutterText("The box is dropped")).getText(), "The box is dropped");

    }

    @Test
    void testCameraMocking() throws IOException {
        try {
            System.out.printf("Directory Path: " + System.getProperty("user.dir"));
            driver.findElement(BaseFlutterTest.LOGIN_BUTTON).click();
            openScreen("Image Picker");

            final String successQr = driver.injectMockImage(
                    new File(String.valueOf(TestUtils.resourcePathToAbsolutePath("success_qr.png"))));
            driver.injectMockImage(new File(String.valueOf(TestUtils.resourcePathToAbsolutePath("second_qr.png"))));

            driver.findElement(AppiumBy.flutterKey("capture_image")).click();
            driver.findElement(AppiumBy.flutterText("PICK")).click();
            assertEquals(driver.findElement(AppiumBy.flutterText("SecondInjectedImage")).getText(), "SecondInjectedImage");
            assertTrue(driver.findElement(AppiumBy.flutterText("SecondInjectedImage")).isDisplayed());

            driver.activateInjectedImage(successQr);

            driver.findElement(AppiumBy.flutterKey("capture_image")).click();
            driver.findElement(AppiumBy.flutterText("PICK")).click();
            assertEquals(driver.findElement(AppiumBy.flutterText("Success!")).getText(), "Success!");
            assertTrue(driver.findElement(AppiumBy.flutterText("Success!")).isDisplayed());
        } catch (Exception e) {
            File image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(image.toPath(), new FileOutputStream(System.getProperty("user.dir") + "/logs/screenshot.png"));
            throw e;
        }
    }
}
