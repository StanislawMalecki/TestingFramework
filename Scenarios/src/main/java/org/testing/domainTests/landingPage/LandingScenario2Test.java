package org.testing.domainTests.landingPage;

import org.apache.logging.log4j.core.util.Assert;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testing.WDAbstractUtilitiesForTest;
@Execution(ExecutionMode.CONCURRENT)
public class LandingScenario2Test extends WDAbstractUtilitiesForTest
{
    public void init() {

    }
    @Test
    public void caller() {
        WebDriver driver = new ChromeDriver();

        driver.get("https://selenium.dev");

        driver.quit();
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.fail("massageeee");
        softAssertions.assertAll();
    }

    public void cleanUp() {

    }
}
