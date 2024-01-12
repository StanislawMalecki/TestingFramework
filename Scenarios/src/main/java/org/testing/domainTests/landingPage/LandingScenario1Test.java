package org.testing.domainTests.landingPage;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testing.WDAbstractUtilitiesForTest;

public class LandingScenario1Test extends WDAbstractUtilitiesForTest
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
