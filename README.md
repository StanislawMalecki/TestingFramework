# TestingFramework
This is a project I've made to present my skills as a QA engineer. 
# Tech stack
- Java 21
- Selenium 4.16
- JUnit 5
- log4j 2.22
# How to run
- Run WebDriverRunner/src/main/java/org/testing/core/TestRunner.java

- In environment variables give package name of a group of tests that you want to run e.g. *packageName=domainTests.landingPage*
- Run multiple test packages, separate them by coma *packageName=domainTests.landingPage,domainTests.workflowPage*

- You can also run individual test via the same mechanic e.g. *testName=LandingScenario1Test*
- Run multiple test, separate them by coma *testName=LandingScenario1Test,LandingScenario2Test*
- You can also run both, separate variables with semicolon *packageName=domainTests.landingPage;testName=CanYouLogInTest,WorkflowScenario1Test*
# Test structure 
This section should be represented in place like Xray repository.

- org.testing
	- domainTests
		- landingPage
			- LandingScenario1Test.java
			- LandingScenario2Test.java
		- workflowPage
			- WorkflowScenario1Test.java
			- WorkflowScenario2Test.java
	- smokeTests
		- CanYouLogInTest.java
		- IsSiteWorkingTest.java
	- WDAbstractUtilitiesForTest.java

