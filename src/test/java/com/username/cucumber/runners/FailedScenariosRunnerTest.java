package com.username.cucumber.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runs again only the scenarios that failed in the last Cucumber run
 * (listed in reports/cucumber/failed-scenarios.txt by the "rerun" plugin).
 * Use: mvn test -DsuiteXmlFile=suites/cucumber-rerun.xml
 */
@CucumberOptions(
        features = "@reports/cucumber/failed-scenarios.txt",
        glue = {"com.username.cucumber.stepdefinitions", "com.username.cucumber.hooks"},
        plugin = {"pretty", "html:reports/cucumber/rerun-report.html"},
        monochrome = true
)
public class FailedScenariosRunnerTest extends AbstractTestNGCucumberTests {
}
