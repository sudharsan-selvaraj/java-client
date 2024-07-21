package io.appium.java_client.android;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.flutter.android.FlutterAndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.InvalidArgumentException;

import java.net.MalformedURLException;
import java.util.Optional;

public class BaseFlutterTest {

    protected static final boolean IS_ANDROID = Optional
            .ofNullable(System.getProperty("platform"))
            .orElse("android")
            .equalsIgnoreCase("android");
    public static final String APP_ID = IS_ANDROID
            ? "com.example.appium_testing_app" : "com.example.appiumTestingApp";
    protected static final int PORT = 4723;

    private static AppiumDriverLocalService service;
    protected static AndroidDriver driver;

    /**
     * initialization.
     */
    @BeforeAll
    public static void beforeClass() {
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(PORT)
                .build();
        service.start();
    }

    @BeforeEach
    public void startSession() throws MalformedURLException {
        if (IS_ANDROID) {
            // TODO: update it with FlutterDriverOptions once implemented
            UiAutomator2Options options = new UiAutomator2Options()
                    .setAutomationName(AutomationName.FLUTTER_INTEGRATION)
                    .setApp(System.getenv("FLUTTER_APP"))
                    .eventTimings();
            driver = new FlutterAndroidDriver(service.getUrl(), options);
        } else {
            throw new InvalidArgumentException(
                    "Currently flutter driver implementation only supports android platform");
        }
    }

    @AfterEach
    public void stopSession() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    public static void afterClass() {
        if (service.isRunning()) {
            service.stop();
        }
    }
}
