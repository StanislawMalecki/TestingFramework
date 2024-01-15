package org.testing.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
public class TestRunner 
{
    private static final Logger logger = LogManager.getLogger(TestRunner.class);
    public static void main(String[] args)
    {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        String packageName = System.getenv("packageName");
        String testName = System.getenv("testName");

        if (packageName != null && !packageName.isEmpty() && testName != null && !testName.isEmpty())
        {
            logger.info("Obie zmienne środowiskowe packageName i testName istnieją.");
        } else
        {
            logger.info("Nie wszystkie wymagane zmienne środowiskowe istnieją.");
        }

        if (packageName != null && !packageName.isEmpty())
        {
            logger.info("Zmienna środowiskowa packageName istnieje: " + packageName);
        } else
        {
            logger.info("Zmienna środowiskowa packageName nie istnieje lub jest pusta.");
        }

        if (testName != null && !testName.isEmpty())
        {
            logger.info("Zmienna środowiskowa testName istnieje: " + testName);
        } else
        {
            logger.info("Zmienna środowiskowa testName nie istnieje lub jest pusta.");
        }

        String domain = "org.testing.%s".formatted(packageName).replaceAll("\"", "");

        LauncherDiscoveryRequest discoveryRequest = LauncherDiscoveryRequestBuilder
                .request()
                .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                .configurationParameter("junit.jupiter.execution.parallel.config.dynamic.factor", "10")
                .selectors(DiscoverySelectors.selectPackage(domain))
                .build();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(discoveryRequest);

        TestExecutionSummary summary = listener.getSummary();
        logger.info("Wyniki testów: %nSucceeded tests: %d%nFailed tests: %d%n".formatted(summary.getTestsSucceededCount(), summary.getTestsFailedCount()));

        // Możesz także przetworzyć wyniki w bardziej zaawansowany sposób, jeśli to konieczne
        // np. sprawdzając czy są błędy w wynikach
        if (summary.getFailures().isEmpty()) {
            logger.info("Wszystkie testy zakończone sukcesem.");
        } else {
            logger.info("Znaleziono błędy w testach.");
        }
    }
}
