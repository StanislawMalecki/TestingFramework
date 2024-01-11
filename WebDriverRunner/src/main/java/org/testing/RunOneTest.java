package org.testing;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.testing.tests.HelloSeleniumTest;

public class RunOneTest {
    public static void main(String[] args)
    {
        HelloSeleniumTest testInstance = new HelloSeleniumTest();

        // Utwórz obiekt JUnitCore
        JUnitCore junit = new JUnitCore();

        // Uruchom testy na instancji klasy testowej
        Result result = junit.run(testInstance.getClass());
        int passedCount = result.getRunCount() - result.getFailureCount();
        System.out.println("Liczba testów: " + result.getRunCount());
        System.out.println("Liczba zaliczonych: " + passedCount);
        System.out.println("Liczba niepowodzeń: " + result.getFailureCount());
    }
}