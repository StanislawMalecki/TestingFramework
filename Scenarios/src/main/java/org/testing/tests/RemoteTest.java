package org.testing.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testing.WDAbstractUtilitiesForTest;

import java.net.URI;

@Execution(ExecutionMode.CONCURRENT)
public class RemoteTest extends WDAbstractUtilitiesForTest {
    public void init() {

    }

    @Test
    public void caller() {
        String seleniumGridUrl = "http://localhost:4444/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");

        try {
            WebDriver driver = new RemoteWebDriver(URI.create(seleniumGridUrl).toURL(), capabilities);

            driver.get("https://www.example.com");
            System.out.println("Title: " + driver.getTitle());
            Thread.sleep(9);
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanUp() {

    }
}