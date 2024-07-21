package io.appium.java_client.android;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;


public class FinderTests extends BaseFlutterTest {


    @Test
    public void testFlutterByKey() {
        WebElement userNameField = driver.findElement(AppiumBy.flutterKey("username_text_field"));
        Assertions.assertEquals("admin", userNameField.getText());
        userNameField.clear();
        driver.findElement(AppiumBy.flutterKey("username_text_field")).sendKeys("admin123");
        Assertions.assertEquals("admin123", userNameField.getText());
    }

    @Test
    public void testFlutterByType() {
        WebElement loginButton = driver.findElement(AppiumBy.flutterType("ElevatedButton"));
        Assertions.assertEquals(loginButton.findElement(AppiumBy.flutterType("Text")).getText(), "Login");
    }

    @Test
    public void testFlutterText() {
        WebElement loginButton = driver.findElement(AppiumBy.flutterText("Login"));
        Assertions.assertEquals(loginButton.getText(), "Login");
        loginButton.click();

        Assertions.assertEquals(1, driver.findElements(AppiumBy.flutterText("Slider")).size());
    }

    @Test
    public void testFlutterTextContaining() {
        WebElement loginButton = driver.findElement(this.loginButton);
        loginButton.click();
        Assertions.assertEquals(driver.findElement(AppiumBy.flutterTextContaining("Vertical")).getText(),
                "Vertical Swiping");
    }

    @Test
    public void testFlutterSemanticsLabel() {
        WebElement loginButton = driver.findElement(this.loginButton);
        loginButton.click();
        openScreen("Lazy Loading");

        WebElement messageField = driver.findElement(AppiumBy.flutterSemanticsLabel("message_field"));
        Assertions.assertEquals(messageField.getText(),
                "Hello world");
    }
}
