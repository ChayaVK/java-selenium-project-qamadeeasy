package com.username.cucumber.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runs the feature files with TestNG.
 * Choose scenarios by tag:  mvn test -DsuiteXmlFile=suites/cucumber.xml -Dcucumber.filter.tags="@smoke"
 * Parallel scenarios: the thread count is data-provider-thread-count in the suite XML.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.username.cucumber.stepdefinitions", "com.username.cucumber.hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber/cucumber-report.html",
                "json:reports/cucumber/cucumber-report.json",
                "rerun:reports/cucumber/failed-scenarios.txt"
        },
        monochrome = true
)
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
