package org.testing.RunnersForPackages;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.PackageNameFilter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class TestRunner {
    public static void main(String[] args)
    {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        String packageName = System.getenv("packageName");
        String testName = System.getenv("testName");

        if (packageName != null && !packageName.isEmpty() && testName != null && !testName.isEmpty())
        {
            System.out.println("Obie zmienne środowiskowe packageName i testName istnieją.");
        } else
        {
            System.out.println("Nie wszystkie wymagane zmienne środowiskowe istnieją.");

            return;
        }

        if (packageName != null && !packageName.isEmpty())
        {
            System.out.println("Zmienna środowiskowa packageName istnieje: " + packageName);
        } else
        {
            System.out.println("Zmienna środowiskowa packageName nie istnieje lub jest pusta.");
        }

        if (testName != null && !testName.isEmpty())
        {
            System.out.println("Zmienna środowiskowa testName istnieje: " + testName);
        } else
        {
            System.out.println("Zmienna środowiskowa testName nie istnieje lub jest pusta.");
        }

        LauncherDiscoveryRequest discoveryRequest = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(DiscoverySelectors.selectPackage("org.testing.%s".formatted(packageName)))
                .build();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(discoveryRequest);

        TestExecutionSummary summary = listener.getSummary();
        System.out.printf("Wyniki testów: %n Succeeded tests: %d%n", summary.getTestsSucceededCount());
        System.out.printf("Failed tests: %d%n", summary.getTestsFailedCount());

        // Możesz także przetworzyć wyniki w bardziej zaawansowany sposób, jeśli to konieczne
        // np. sprawdzając czy są błędy w wynikach
        if (summary.getFailures().isEmpty()) {
            System.out.println("Wszystkie testy zakończone sukcesem.");
        } else {
            System.out.println("Znaleziono błędy w testach.");
        }
    }
}
