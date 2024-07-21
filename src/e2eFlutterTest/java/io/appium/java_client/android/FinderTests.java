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

}
