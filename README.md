# Selenium Java Test Automation Framework

A complete, ready-to-run UI test automation framework built with **Java 17, Selenium 4, TestNG, Cucumber and Maven**.
It follows the Page Object Model and includes data-driven tests (Excel, CSV and JSON), a config file with environments,
reusable utilities, Extent and Cucumber HTML reports, logging, retries, parallel and cross-browser runs, Selenium Grid
with Docker, and CI pipelines for Jenkins and GitHub Actions.

It comes with its own demo web application, the **Practice Shop**, which starts automatically when you run the tests.
You need no internet connection and no test website to try it. When you are ready, point it at your own application.

All 35 tests (29 TestNG tests and 6 Cucumber scenarios) pass on a fresh setup.

---

## Contents

1. [What is inside](#1-what-is-inside)
2. [Before you start](#2-before-you-start)
3. [Open and run the project in IntelliJ IDEA](#3-open-and-run-the-project-in-intellij-idea)
4. [Run from the command line](#4-run-from-the-command-line)
5. [Project structure](#5-project-structure)
6. [How the framework works](#6-how-the-framework-works)
7. [Configuration and environments](#7-configuration-and-environments)
8. [Reports, screenshots and logs](#8-reports-screenshots-and-logs)
9. [Make it your own framework](#9-make-it-your-own-framework)
10. [Add a new page and a new test](#10-add-a-new-page-and-a-new-test)
11. [Data-driven tests: Excel, CSV and JSON](#11-data-driven-tests-excel-csv-and-json)
12. [Cucumber BDD](#12-cucumber-bdd)
13. [Parallel and cross-browser runs](#13-parallel-and-cross-browser-runs)
14. [Selenium Grid with Docker](#14-selenium-grid-with-docker)
15. [Run in GitHub Actions](#15-run-in-github-actions)
16. [Run in Jenkins](#16-run-in-jenkins)
17. [Troubleshooting](#17-troubleshooting)
18. [Explain this framework in an interview](#18-explain-this-framework-in-an-interview)

---

## 1. What is inside

| Component | Where | What it does |
|---|---|---|
| **Maven** | `pom.xml` | Downloads every library and runs the tests with `mvn test`. All versions are in one place. |
| **Page Object Model** | `src/main/java/.../pages` | One class per page. Locators stay private; tests call methods such as `loginAs()` and `addToCart()`. |
| **BasePage** | `pages/BasePage.java` | Common actions (click, type, read text, select) with waits and logging, shared by every page. |
| **TestNG** | `src/test/java/.../tests`, `suites/` | Annotations, assertions, groups (`smoke`, `regression`), data providers, suite files, parallel runs. |
| **Cucumber BDD** | `src/test/resources/features`, `.../cucumber` | Feature files in plain English (Gherkin), step definitions, hooks, tags, Scenario Outline, data tables. |
| **Data-driven tests** | `src/test/resources/testdata`, `dataproviders/` | Excel (Apache POI), CSV and JSON (Jackson) data, each row running the test once. |
| **config.properties** | `src/main/resources` | Browser, URL, waits, Grid, retries. Override any key with `-Dkey=value`. |
| **Environments** | `src/main/resources/env` | `qa.properties`, `staging.properties`: choose with `-Denv=qa`. |
| **DriverFactory + ThreadLocal** | `driver/` | Chrome, Firefox or Edge, headless or not, local or on Selenium Grid. One browser per thread, so parallel runs are safe. |
| **Utilities** | `utils/` | `WaitUtils`, `BrowserUtils` (alerts, frames, windows), `JavaScriptUtils`, `ScreenshotUtils`, `ExcelUtils`, `CsvUtils`, `JsonUtils`, `RandomDataUtils`. |
| **Listeners** | `listeners/` | Extent report entries, screenshot on failure, automatic retry of failed tests. |
| **Extent Reports** | `reports/ExtentReport.html` | HTML report with steps, categories and embedded screenshots, for TestNG and Cucumber. |
| **Cucumber reports** | `reports/cucumber/` | Cucumber's own HTML and JSON reports, and a list of failed scenarios to run again. |
| **Logging (Log4j 2)** | `log4j2.xml`, `logs/framework.log` | Every step written to the console and to a log file. |
| **Selenium Grid** | `docker-compose.yml` | Hub with Chrome, Firefox and Edge nodes; run with `-Dremote=true`. |
| **Jenkins** | `Jenkinsfile` | Pipeline with parameters (suite, browser, headless, environment, tags), nightly trigger, reports. |
| **GitHub Actions** | `.github/workflows/ui-tests.yml` | Runs on every push and pull request, uploads the reports. |
| **Practice Shop** | `src/test/resources/demo-app` | Demo app: login, products, sorting, cart, checkout, plus a practice page with alerts, frames, windows, waits, dropdowns, uploads and a web table. |

---

## 2. Before you start

Install these once. Everything else (Selenium, TestNG, Cucumber, browser drivers) is downloaded automatically.

| Tool | Why | How to check |
|---|---|---|
| **JDK 17 or newer** | Compiles and runs Java. Download from adoptium.net (Temurin 17 or 21, choose the installer for your system). | `java -version` |
| **IntelliJ IDEA Community** (free) | The editor. It already contains Maven. Download from jetbrains.com/idea. | Opens the project |
| **Google Chrome** | The default browser for the tests. Firefox or Edge also work. | Installed in the usual place |
| **Maven** (optional) | Only to run `mvn` commands in a terminal. Download from maven.apache.org, unzip, and add its `bin` folder to `PATH`. | `mvn -version` |
| **IntelliJ plugins** (recommended) | **Cucumber for Java** and **Gherkin**: color and "go to step" in feature files. Settings > Plugins > Marketplace. | |

> **Browser drivers:** you do not download ChromeDriver. Selenium Manager, built into Selenium 4, finds your browser
> version and downloads the matching driver on the first run (this needs internet once).

---

## 3. Open and run the project in IntelliJ IDEA

1. Unzip the project to a folder without spaces in the path if you can, for example `C:\Projects\SeleniumJavaFramework`.
2. Open IntelliJ IDEA and click **Open**. Select the project folder (the one that contains `pom.xml`) and click **OK**.
   Choose **Trust Project** when asked.
3. Wait for IntelliJ to import the Maven project. The first time, it downloads the libraries; the progress bar at the
   bottom shows it. When it finishes, the `src` folders turn blue and green.
4. Set the JDK: **File > Project Structure > Project > SDK**: choose 17 or newer (or **Download JDK**). Click **OK**.
5. Run the TestNG suite: in the Project panel open `suites`, right-click **testng.xml** > **Run**.
   Chrome opens and closes for each test. At the end the Run panel shows **Tests passed: 29**.
6. Run the Cucumber scenarios: right-click `suites/cucumber.xml` > **Run** (6 scenarios), or open
   `src/test/java/com/username/cucumber/runners/CucumberRunnerTest.java` and click the green arrow.
7. Open the report: in the Project panel, right-click `reports/ExtentReport.html` > **Open In** > **Browser**.
   (If the `reports` folder is not shown, right-click the project > **Reload from Disk**.)

**Run with Maven inside IntelliJ** (same as the command line, no Maven install needed): open the **Maven** tool window
(right side) > **Lifecycle** > double-click **test**. To pass options, click the **Execute Maven Goal** icon (the "m" button)
and type, for example, `mvn clean test -DsuiteXmlFile=suites/smoke.xml`.

> **Tip:** to see the Practice Shop yourself, run `src/test/java/com/username/demoapp/DemoAppServer.java`
> (green arrow next to `main`) and open http://localhost:8080. Log in with `standard_user` / `Test@123`.

---

## 4. Run from the command line

Open a terminal in the project folder (in IntelliJ: **View > Tool Windows > Terminal**).

| Goal | Command |
|---|---|
| Regression suite (all TestNG tests) | `mvn clean test` |
| Smoke tests only | `mvn clean test -DsuiteXmlFile=suites/smoke.xml` |
| Parallel run (3 at a time) | `mvn clean test -DsuiteXmlFile=suites/parallel.xml` |
| Cucumber scenarios | `mvn clean test -DsuiteXmlFile=suites/cucumber.xml` |
| Cucumber, only `@smoke` scenarios | `mvn clean test -DsuiteXmlFile=suites/cucumber.xml -Dcucumber.filter.tags="@smoke"` |
| Cucumber scenarios in parallel | `mvn clean test -DsuiteXmlFile=suites/cucumber-parallel.xml` |
| Run again only the failed scenarios | `mvn test -DsuiteXmlFile=suites/cucumber-rerun.xml` |
| Everything (TestNG + Cucumber) | `mvn clean test -DsuiteXmlFile=suites/all.xml` |
| Same tests on Chrome and Edge | `mvn clean test -DsuiteXmlFile=suites/cross-browser.xml` |
| One test class | `mvn clean test -Dtest=LoginTest` |
| One test method | `mvn clean test -Dtest="LoginTest#validUserCanLogIn"` |
| Another browser | `mvn clean test -Dbrowser=firefox` (or `edge`) |
| No browser window | `mvn clean test -Dheadless=true` |
| Another environment | `mvn clean test -Denv=qa` |
| On Selenium Grid | `mvn clean test -Dremote=true -Dgrid.url=http://localhost:4444` |

Options combine: `mvn clean test -DsuiteXmlFile=suites/smoke.xml -Dbrowser=edge -Dheadless=true -Denv=qa`.

**Windows PowerShell** (the default terminal in IntelliJ on Windows) splits options that contain dots. Put each
`-D` option in double quotes: `mvn clean test "-DsuiteXmlFile=suites/smoke.xml" "-Dbrowser=edge"`. Command Prompt
(`cmd`) and macOS/Linux terminals need no quotes, except around values with spaces, `#`, `@` or `|`.

---

## 5. Project structure

```
SeleniumJavaFramework
├── pom.xml                         Maven: libraries, versions, plugins
├── suites/                         TestNG suite files (testng, smoke, parallel, cucumber...)
├── Jenkinsfile                     Jenkins pipeline
├── .github/workflows/ui-tests.yml  GitHub Actions workflow
├── docker-compose.yml              Selenium Grid (hub + Chrome, Firefox, Edge)
├── src/main/java/com/username      THE FRAMEWORK (reusable code)
│   ├── config/ConfigReader         reads config.properties, env files, -D options
│   ├── constants/                  folder and file names
│   ├── driver/                     DriverFactory (creates browsers), DriverManager (ThreadLocal)
│   ├── pages/                      BasePage + one page object per page
│   ├── reports/                    ExtentManager, ExtentTestManager
│   ├── models/                     Customer (JSON test data as a Java object)
│   └── utils/                      waits, alerts/frames/windows, JS, screenshots, Excel, CSV, JSON, random data
├── src/main/resources
│   ├── config.properties           default settings
│   ├── env/qa.properties           settings per environment
│   └── log4j2.xml                  logging setup
├── src/test/java/com/username      THE TESTS
│   ├── base/BaseTest               browser before each test, quit after
│   ├── tests/                      TestNG tests (login, data-driven, products, checkout, practice)
│   ├── dataproviders/              Excel, CSV and JSON data providers
│   ├── listeners/                  report + screenshot listener, retry analyzer and transformer
│   ├── cucumber/                   runners, step definitions, hooks, scenario context
│   └── demoapp/DemoAppServer       starts the Practice Shop
└── src/test/resources
    ├── features/                   Cucumber feature files
    ├── testdata/                   LoginData.xlsx, products.csv, customers.json
    └── demo-app/                   the Practice Shop web pages
```

Created when you run the tests (and removed by `mvn clean`): `reports/`, `logs/`, `target/`.

---

## 6. How the framework works

One test, from start to finish:

1. **Maven** (`mvn test`) starts TestNG with the suite file given in `-DsuiteXmlFile` (default `suites/testng.xml`).
2. **TestListener** runs before the suite: it starts the Practice Shop (only when `base.url` is localhost) and prepares the Extent report.
3. **RetryTransformer** (registered in the suite file) gives every `@Test` the **RetryAnalyzer**.
4. **BaseTest** `@BeforeMethod` calls **DriverFactory**, which reads **ConfigReader** (browser, headless, remote) and stores
   the new browser in **DriverManager**, one per thread.
5. The **test** calls **page objects**. Each page action goes through **BasePage**, which waits with **WaitUtils**, acts,
   and logs the step to the log file and the report.
6. The test **asserts** with TestNG `Assert`.
7. **TestListener** marks the test passed or failed. On failure it takes a **screenshot** (embedded in the report and saved
   in `reports/screenshots`). If retries are left, the test runs again.
8. **BaseTest** `@AfterMethod` quits the browser. After the suite, the report is written to `reports/ExtentReport.html`.

Cucumber follows the same path, with **Hooks** in place of BaseTest and TestListener, and **step definitions** calling
the same page objects.

**Design rules used everywhere**

- Tests never contain locators, URLs, passwords or `Thread.sleep`. Locators live in page objects, settings in
  `config.properties`, waits in `WaitUtils`.
- No implicit wait: only explicit waits, so each wait says what it waits for.
- Page methods return the next page (`loginAs()` returns `ProductsPage`), so a test reads like the user's journey.
- Every test starts with a fresh browser, so tests never depend on each other and can run in any order or in parallel.

---

## 7. Configuration and environments

All settings are in `src/main/resources/config.properties`:

| Key | Default | Meaning |
|---|---|---|
| `browser` | `chrome` | `chrome`, `firefox` or `edge` |
| `headless` | `false` | `true` runs without a window (used on CI) |
| `window.width`, `window.height` | `1366`, `768` | Browser window size |
| `base.url` | `http://localhost:8080` | Address of the application under test |
| `demo.app.autostart`, `demo.app.port` | `true`, `8080` | Start the Practice Shop when `base.url` is localhost |
| `explicit.wait` | `10` | Seconds to wait for an element |
| `page.load.timeout` | `30` | Seconds to wait for a page to load |
| `remote`, `grid.url` | `false`, `http://localhost:4444` | Run on a Selenium Grid |
| `retry.count` | `1` | Times to run a failed TestNG test again (0 = never) |
| `screenshot.on.pass` | `false` | Also attach a screenshot when a test passes |
| `report.title`, `report.name` | | Titles in the Extent report |
| `testdata.excel`, `testdata.csv`, `testdata.json` | | Test data files in `src/test/resources` |

**Three layers, the last one wins:**

1. `config.properties`: the defaults.
2. `env/<name>.properties` when you run with `-Denv=<name>`. It holds only the keys that differ, for example the
   staging URL. Add your own: create `src/main/resources/env/uat.properties` and run with `-Denv=uat`.
3. `-Dkey=value` on the command line, for example `-Dbrowser=edge -Dexplicit.wait=20`.

**Passwords:** do not commit real passwords. Pass them at run time (`-Dapp.password=...`, read with
`ConfigReader.get("app.password")`) or from your CI tool's secrets (see sections 15 and 16).

---

## 8. Reports, screenshots and logs

| Output | Where | Open it |
|---|---|---|
| Extent report | `reports/ExtentReport.html` | In a browser. Dashboard, every test with its steps, categories (groups and tags), screenshot on failure. |
| Failure screenshots | `reports/screenshots/*.png` | Named after the test and the time. |
| Cucumber HTML report | `reports/cucumber/cucumber-report.html` | In a browser. Every scenario and step, screenshot on failure. |
| Cucumber JSON | `reports/cucumber/cucumber-report.json` | For CI plugins and report tools. |
| Failed scenarios | `reports/cucumber/failed-scenarios.txt` | Used by `suites/cucumber-rerun.xml`. |
| Log file | `logs/framework.log` | Every step, with time and thread. |
| TestNG / Surefire results | `target/surefire-reports/` | XML results that Jenkins and other CI tools read. |

> The Extent report loads its styles from the internet. Offline it still shows all results, without colors.

---

## 9. Make it your own framework

### 9.1 Rename the package `com.username`

1. In the Project panel, open `src/main/java`, right-click the package **com.username** > **Refactor** > **Rename**.
2. If IntelliJ asks, choose **Rename package**. Type your name, for example `com.priyaqa`, tick **Search in comments
   and strings** and **Search for text occurrences**, and click **Refactor**, then **Do Refactor**.
3. Do the same for `src/test/java` if it was not renamed with it.
4. Check that the suite files in `suites/` and the `glue` lines in the two Cucumber runners now use the new name
   (search with **Ctrl+Shift+F** for `com.username`; nothing should be left).
5. In `pom.xml`, change `<groupId>com.username</groupId>` to your name, and the `<artifactId>` if you like.
6. Run `suites/testng.xml` to check everything still passes.

### 9.2 Point it at your own application

1. In `config.properties` set `base.url` to your application, for example `https://test.mycompany.com`, and
   `demo.app.autostart=false`.
2. Write page objects for your pages (section 10). Keep `BasePage`, the utilities, listeners, reports and CI files as they are.
3. When your own tests are in place, delete the Practice Shop parts you no longer need: the `demo-app` folder,
   `DemoAppServer.java` (and its call in `TestListener` and `Hooks`), the example pages and tests.

---

## 10. Add a new page and a new test

**Example:** a test that checks "Continue shopping" on the cart page keeps the items. It uses `ProductsPage`, `CartPage`
and a new method on `CartPage`.

**Step 1: add the action to the page object** (`CartPage.java`), next to the other methods:

```java
public int getItemCount() {
    return getItemNames().size();
}
```

**Step 2: create the test class** `src/test/java/com/username/tests/CartTest.java`:

```java
package com.username.tests;

import com.username.base.BaseTest;
import com.username.pages.CartPage;
import com.username.pages.LoginPage;
import com.username.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(groups = "regression", description = "Continue shopping keeps the cart items")
    public void continueShoppingKeepsItems() {
        CartPage cart = new LoginPage().open()
                .loginAs("standard_user", "Test@123")
                .addToCart("Wireless Mouse")
                .openCart();
        Assert.assertEquals(cart.getItemCount(), 1);

        ProductsPage products = cart.continueShopping();
        Assert.assertEquals(products.getCartCount(), 1, "Cart badge after coming back");
    }
}
```

**Step 3: add it to a suite.** In `suites/testng.xml` (and `parallel.xml`) add
`<class name="com.username.tests.CartTest"/>` inside `<classes>`. Right-click `testng.xml` > **Run**.

**A new page object** follows the same pattern as the existing ones:

```java
public class ProfilePage extends BasePage {

    private final By nameField = By.id("name");      // 1. private locators
    private final By saveButton = By.id("save");

    public ProfilePage updateName(String name) {      // 2. public actions
        type(nameField, name);
        click(saveButton);
        return this;                                  // 3. return the page you end up on
    }
}
```

**Locator tips:** prefer `id`, then `name`, then a short CSS selector such as `[data-test='error']`. Use XPath only when
you must find an element by its text or its parent.

---

## 11. Data-driven tests: Excel, CSV and JSON

All data providers are in `dataproviders/TestDataProviders.java`. Each row of the file runs the test once, and each
run appears separately in the report.

| File | Read with | Used by | To add data |
|---|---|---|---|
| `testdata/LoginData.xlsx` (sheet `Login`) | `ExcelUtils` (Apache POI) | `LoginDataDrivenTest` | Add a row in Excel. Row 1 holds the column names. |
| `testdata/products.csv` | `CsvUtils` | `ProductsTest.productPriceAndAddToCart` | Add a line `name,price`. |
| `testdata/customers.json` | `JsonUtils` (Jackson) into `Customer` objects | `CheckoutTest.placeOrder` | Add an object with the same keys. |

**Use a data provider in a test:**

```java
@Test(dataProvider = "loginData", dataProviderClass = TestDataProviders.class)
public void loginWithExcelData(String username, String password, String expectedResult, String expectedMessage) {
    ...
}
```

**Add a new Excel sheet:** create the sheet (for example `Search`) with column names in row 1, then add a provider:

```java
@DataProvider(name = "searchData")
public static Object[][] searchData() {
    List<Map<String, String>> rows = ExcelUtils.readSheet("testdata/LoginData.xlsx", "Search");
    Object[][] data = new Object[rows.size()][];
    for (int i = 0; i < rows.size(); i++) {
        data[i] = new Object[]{rows.get(i).get("keyword"), rows.get(i).get("expectedCount")};
    }
    return data;
}
```

> Close the Excel file before running the tests. On Windows, an open file can block reading.

---

## 12. Cucumber BDD

| Part | File | Role |
|---|---|---|
| Feature files | `src/test/resources/features/*.feature` | Scenarios in Gherkin: Feature, Background, Scenario, Scenario Outline with Examples, data tables, tags |
| Step definitions | `cucumber/stepdefinitions/` | Java methods for each step; they call page objects |
| Hooks | `cucumber/hooks/Hooks.java` | Browser per scenario, screenshot on failure, report |
| Scenario context | `cucumber/context/ScenarioContext.java` | Shares pages between step classes (PicoContainer creates one per scenario) |
| Runner | `cucumber/runners/CucumberRunnerTest.java` | Runs the features with TestNG; plugins for HTML, JSON and rerun reports |

**Add a scenario:**

1. Write it in a feature file:

   ```gherkin
   @regression
   Scenario: Add one product to the cart
     When the user adds these products to the cart:
       | Desk Lamp |
     Then the cart badge shows 1
   ```

2. Run `suites/cucumber.xml`. Steps that already exist are reused. For a new step, Cucumber prints the method to add
   (in IntelliJ with the Cucumber plugin: Alt+Enter on the step > **Create step definition**).
3. Put the new method in a step class. It calls page objects and asserts, like a TestNG test.

**Tags:** run a subset with `-Dcucumber.filter.tags="@smoke"`, `"@login and not @regression"` or `"@smoke or @checkout"`.

---

## 13. Parallel and cross-browser runs

- **TestNG parallel:** `suites/parallel.xml` runs 3 test methods at a time (`parallel="methods" thread-count="3"`).
  Each thread gets its own browser from `DriverManager` (ThreadLocal), so tests never share a browser.
- **Cucumber parallel:** `suites/cucumber-parallel.xml` runs 3 scenarios at a time (`data-provider-thread-count="3"`).
- **Cross-browser:** `suites/cross-browser.xml` runs the smoke tests on Chrome, then on Edge. Each `<test>` passes a
  `browser` parameter that `BaseTest` reads. Change the values to `firefox` if you use Firefox.
- Start with `thread-count` equal to your CPU cores minus one. More threads than the machine can handle make tests slower, not faster.

---

## 14. Selenium Grid with Docker

Selenium Grid runs browsers on other machines or in containers. `docker-compose.yml` starts a hub with Chrome, Firefox
and Edge nodes.

1. Install **Docker Desktop** (docker.com) and start it.
2. In the project folder: `docker compose up -d`. Open http://localhost:4444 to see the Grid and its nodes.
3. Run the tests on the Grid:
   `mvn clean test -Dremote=true -Dheadless=true -Dbase.url=http://host.docker.internal:8080`
   (the browsers run inside containers, so they reach the Practice Shop on your computer through `host.docker.internal`).
   For your own application, use its normal address in `base.url`.
4. Stop the Grid: `docker compose down`.

A Grid you already have works the same way: `-Dremote=true -Dgrid.url=http://your-grid:4444`.

---

## 15. Run in GitHub Actions

The workflow `.github/workflows/ui-tests.yml` runs all tests (TestNG and Cucumber) in headless Chrome on every push and
pull request to `main`, and uploads the reports.

1. Create an empty repository on github.com (no README, no .gitignore).
2. In the project folder:

   ```
   git init -b main
   git add .
   git commit -m "Selenium Java framework"
   git remote add origin https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
   git push -u origin main
   ```

3. On GitHub, open the **Actions** tab. The run **UI tests** starts by itself. A green tick means every test passed.
4. Open the run and download **test-reports** (under **Artifacts**). Unzip it and open `reports/ExtentReport.html`.
5. To run a chosen suite by hand: **Actions > UI tests > Run workflow**, pick the suite, click **Run workflow**.

Secrets (passwords) go in **Settings > Secrets and variables > Actions** and are passed to Maven as
`-Dapp.password=${{ secrets.APP_PASSWORD }}`.

---

## 16. Run in Jenkins

1. In Jenkins: **Manage Jenkins > Tools**. Add a JDK named **JDK17** and a Maven installation named **Maven3**
   (tick "Install automatically" if you like).
2. **Manage Jenkins > Plugins**: install **HTML Publisher** (Pipeline, Git, JUnit and Timestamper come with the
   suggested plugins).
3. **New Item** > name > **Pipeline** > OK. Under **Pipeline**, choose **Pipeline script from SCM**, **Git**, your
   repository URL, branch `*/main`, script path `Jenkinsfile`. Save.
4. Click **Build Now** once (Jenkins reads the parameters on the first run), then **Build with Parameters**: choose the
   suite, browser, headless, environment and Cucumber tags.
5. After the run: **Extent Report** link on the job page, **Test Result** trend, and all reports under **Build Artifacts**.
   The pipeline also runs every night around 2 AM (remove the `triggers` block if you do not want that).

> If the Extent report looks plain in Jenkins, Jenkins' security settings block its styles. Download it from Build
> Artifacts and open it locally, or ask your Jenkins admin about the Content Security Policy for HTML Publisher.

---

## 17. Troubleshooting

| Problem | Fix |
|---|---|
| `java: release version 17 not supported` | IntelliJ uses an older JDK: File > Project Structure > SDK = 17 or newer. For Maven in a terminal, set `JAVA_HOME` to JDK 17. |
| Red code everywhere after opening | The Maven import has not finished or failed: Maven tool window > **Reload All Maven Projects**. |
| `SessionNotCreatedException` / driver errors | Update Chrome, then run again: Selenium Manager downloads the matching driver. Behind a company proxy, ask for access to the Selenium Manager download sites or set `webdriver.chrome.driver` to a driver you downloaded. |
| `Port 8080 is in use` in the log | Another program uses 8080. Set `demo.app.port=8090` and `base.url=http://localhost:8090`. |
| `Config file not found: .../env/xyz.properties` | The name after `-Denv=` must match a file in `src/main/resources/env`. |
| Tests fail only in parallel | A test shares something static. Keep state in local variables or ThreadLocal, like `DriverManager`. |
| Cucumber step shows as undefined | Check the `glue` packages in the runner, and that the step text matches exactly. |
| `No tests to run` with `-Dtest=...` | Check the class name and the quotes: `-Dtest="LoginTest#validUserCanLogIn"`. |
| Report has no colors | It loads styles from the internet; open it on a computer with internet access. |

---

## 18. Explain this framework in an interview

> "It is a hybrid framework in Java with Selenium 4, TestNG and Cucumber, built with Maven. Pages follow the Page Object
> Model: locators are private and every action goes through a BasePage that waits explicitly and logs. A DriverFactory
> creates Chrome, Firefox or Edge, locally or on Selenium Grid, and keeps one driver per thread in a ThreadLocal, so
> tests run in parallel safely. Settings come from config.properties, with environment files and command-line
> overrides. Test data is in Excel, CSV and JSON, fed through TestNG data providers. Listeners add Extent report
> entries, screenshots on failure and retries. Business scenarios are also written in Gherkin with Cucumber, sharing
> the same page objects. It runs from Maven, in Jenkins with parameters and a nightly schedule, and in GitHub Actions
> on every push, with the reports kept as artifacts."
