package org.testing.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testing.WDAbstractUtilitiesForTest;

@Execution(ExecutionMode.CONCURRENT)
public class HelloSeleniumTest extends WDAbstractUtilitiesForTest
{
    public void init() {

    }
    @Test
    public void caller() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://selenium.dev");

        driver.quit();
    }

    public void cleanUp() {

    }
}
