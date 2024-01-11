package org.testing.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testing.WDAbstractUtilitiesForTest;

public class HelloSeleniumTest extends WDAbstractUtilitiesForTest
{
    @Override
    public void init() {

    }
    @Override
    public void caller() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://selenium.dev");

        driver.quit();
    }

    @Override
    public void cleanUp() {

    }
}
