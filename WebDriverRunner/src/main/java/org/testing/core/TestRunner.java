package org.testing.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.platform.commons.util.StringUtils;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRunner
{
    private static final Logger logger = LogManager.getLogger(TestRunner.class);

    public static void main(String[] args) throws IOException
    {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        String testName = System.getenv("testName");
        String packageName = System.getenv("packageName");
        String domain = "org.testing.%s".formatted(packageName).replaceAll("\"", "");

        if (packageName != null && !packageName.isEmpty() && testName != null && !testName.isEmpty())
        {
            runPackageTests(domain, listener);
            runSelectedTests(testName, listener);
        }
        else if (packageName != null && !packageName.isEmpty())
        {
            runPackageTests(domain, listener);
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
                .selectors(DiscoverySelectors.selectPackage(packageName))
                .build();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(discoveryRequest);
    }

    private static void runSelectedTests(String testName, SummaryGeneratingListener listener) throws IOException {
        List<String> list = processTestVariable(testName);
        Launcher launcher = LauncherFactory.create();
        LauncherDiscoveryRequest discoveryRequest;

        if(list == null)
        {
            String path = getPath(testName);
            discoveryRequest = LauncherDiscoveryRequestBuilder
                    .request()
                    .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                    .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                    .selectors(DiscoverySelectors.selectClass(path))
                    .build();
        }
        else
        {
            discoveryRequest = LauncherDiscoveryRequestBuilder
                    .request()
                    .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                    .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                    .selectors(list.stream()
                            .map(test -> {
                                try {
                                    return getPath(test);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
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

    public static List<Path> listFilesUsingFileWalk(String dir, String testName, int depth) throws IOException
    {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth))
        {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.getFileName().toString().equals(testName+".java"))
                    .collect(Collectors.toList());
        }
    }
    public static String preparePath(String input)
    {
        int index = input.indexOf("org\\testing");
        String cropped = input.substring(index);
        cropped = cropped.replace(".java", "");
        return cropped.replaceAll("\\\\", ".");
    }

    public static String getPath(String testName) throws IOException
    {
        List<Path> aa = listFilesUsingFileWalk(System.getProperty("user.dir"), testName, 10);

        if(aa.size() > 1)
        {
            throw new InvalidParameterException("There is %d objects with name %s. Listing: ".formatted(aa.size(), testName)+ aa);
        }

        return preparePath(aa.get(0).toString());
    }
}
