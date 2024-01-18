package org.testing.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.testing.Utils.MyFilenameFilter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TestRunner
{
    private static final Logger logger = LogManager.getLogger(TestRunner.class);
    private static String domain;
    public static void main(String[] args) throws IOException {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        String packageName = System.getenv("packageName");
        String testName = System.getenv("testName");
        domain = "org.testing.%s".formatted(packageName).replaceAll("\"", "");

        if (packageName != null && !packageName.isEmpty() && testName != null && !testName.isEmpty())
        {
            runPackageTests(packageName, listener);
            runSelectedTests(testName, listener);
        }
        else if (packageName != null && !packageName.isEmpty())
        {
            runPackageTests(packageName, listener);
        }
        else if (testName != null && !testName.isEmpty())
        {
            runSelectedTests(testName, listener);
        }
        else
        {
            logger.debug("No tests were passed as environmental variable.");
        }

        TestExecutionSummary summary = listener.getSummary();
        logger.info("Test results: %nSucceeded tests: %d%nFailed tests: %d%n".formatted(summary.getTestsSucceededCount(), summary.getTestsFailedCount()));

        if (summary.getFailures().isEmpty())
        {
            logger.info("Wszystkie testy zakończone sukcesem.");
        }
        else
        {
            logger.info("Znaleziono błędy w testach.");
        }
    }

    private static void runPackageTests(String packageName, SummaryGeneratingListener listener)
    {
        Launcher launcher = LauncherFactory.create();


        LauncherDiscoveryRequest discoveryRequest = LauncherDiscoveryRequestBuilder
                .request()
                .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                .selectors(DiscoverySelectors.selectPackage(domain))
                .build();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(discoveryRequest);
    }

    private static void runSelectedTests(String testName, SummaryGeneratingListener listener)
    {
        List<String> list = processTestVariable(testName);
        Launcher launcher = LauncherFactory.create();
        LauncherDiscoveryRequest discoveryRequest;

        if(list == null)
        {
            String aa = MyFilenameFilter.getFilename(testName + ".java");
            discoveryRequest = LauncherDiscoveryRequestBuilder
                    .request()
                    .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                    .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                    .selectors(DiscoverySelectors.selectClass("org.testing.domainTests.landingPage." + testName))
                    .build();
        }
        else
        {
            discoveryRequest = LauncherDiscoveryRequestBuilder
                    .request()
                    .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                    .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                    .selectors(list.stream()
                            .map(DiscoverySelectors::selectClass)
                            .toArray(DiscoverySelector[]::new))
                    .build();
        }

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(discoveryRequest);
    }

    private static List<String> processTestVariable(String testName)
    {
        if(testName.contains(","))
        {
            return Arrays.asList(testName.split(","));
        }

        return null;
    }

}
