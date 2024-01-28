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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRunner
{
    private static final Logger logger = LogManager.getLogger(TestRunner.class);
    private static final SummaryGeneratingListener listener = new SummaryGeneratingListener();
    public static void main(String[] args) throws IOException
    {
        String testName = System.getenv("testName");
        String packageName = System.getenv("packageName");
        List<TestExecutionSummary.Failure> getFailures2 = null;

        if (packageName != null && !packageName.isEmpty() && testName != null && !testName.isEmpty())
        {
            runPackageTests(packageName);

            TestExecutionSummary summary2 = listener.getSummary();
            long succeededTests = summary2.getTestsSucceededCount();
            long failedTests = summary2.getTestsFailedCount();
            getFailures2 = summary2.getFailures();

            runSelectedTests(testName);

            logger.info("Test results: %nSucceeded tests: %d%nFailed tests: %d%n".formatted(succeededTests, failedTests));
        }
        else if (packageName != null && !packageName.isEmpty())
        {
            runPackageTests(packageName);
        }
        else if (testName != null && !testName.isEmpty())
        {
            runSelectedTests(testName);
        }
        else
        {
            logger.debug("No tests were passed as environmental variable.");
        }

        TestExecutionSummary summary = listener.getSummary();

        logger.info("Test results: %nSucceeded tests: %d%nFailed tests: %d%n".formatted(summary.getTestsSucceededCount(), summary.getTestsFailedCount()));

        printStacks(summary.getFailures());

        if(getFailures2 != null)
        {
            printStacks(getFailures2);
        }
    }

    private static void runPackageTests(String packageName)
    {
        List<String> list = processTestPackageVariable(packageName);
        Launcher launcher = LauncherFactory.create();
        LauncherDiscoveryRequest discoveryRequest;

        if(list == null)
        {
            packageName = "org.testing.%s".formatted(packageName).replaceAll("\"", "");

            discoveryRequest = LauncherDiscoveryRequestBuilder
                    .request()
                    .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                    .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                    .selectors(DiscoverySelectors.selectPackage(packageName))
                    .build();
        }
        else
        {

            discoveryRequest = LauncherDiscoveryRequestBuilder
                    .request()
                    .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                    .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                    .selectors(list.stream()
                            .map(DiscoverySelectors::selectPackage)
                            .toArray(DiscoverySelector[]::new))
                    .build();
        }

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(discoveryRequest);
    }

    private static void runSelectedTests(String testName) throws IOException {
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

    private static List<String> processTestPackageVariable(String testPackage)
    {
        if(testPackage.contains(","))
        {
            return Arrays.stream(testPackage.split(","))
                    .map(element -> "org.testing." + element)
                    .collect(Collectors.toList());
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
        if(aa.isEmpty())
        {
            throw new InvalidParameterException("There are no objects with this name:%s".formatted(testName));
        }

        return preparePath(aa.get(0).toString());
    }

    private static void printStacks(List<TestExecutionSummary.Failure> failures)
    {
        for (TestExecutionSummary.Failure failure : failures)
        {
            for (StackTraceElement elm : failure.getException().getStackTrace())
            {
                System.err.println(elm);
            }
        }
    }
}
