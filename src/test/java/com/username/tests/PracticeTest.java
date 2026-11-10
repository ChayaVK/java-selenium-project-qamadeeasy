package com.username.tests;

import com.username.base.BaseTest;
import com.username.pages.PracticePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Alerts, frames, windows, waits, dropdowns, checkboxes, uploads and web tables. */
public class PracticeTest extends BaseTest {

    @Test(groups = "regression", description = "Accept an alert, dismiss a confirm, answer a prompt")
    public void alerts() {
        PracticePage page = new PracticePage().open();
        Assert.assertEquals(page.acceptSimpleAlert(), "Hello from the Practice Shop");
        page.clickConfirmAndDismiss();
        Assert.assertEquals(page.getAlertResult(), "You clicked: Cancel");
        page.answerPrompt("Selenium");
        Assert.assertEquals(page.getAlertResult(), "You entered: Selenium");
    }

    @Test(groups = "regression", description = "Type inside an iframe and come back")
    public void frame() {
        Assert.assertEquals(new PracticePage().open().typeInsideFrame("hello"), "Typed: hello");
    }

    @Test(groups = "regression", description = "Switch to a new tab and back")
    public void newWindow() {
        Assert.assertEquals(new PracticePage().open().readNewWindowHeading(), "New Window");
    }

    @Test(groups = "regression", description = "Wait for text that appears after 2 seconds")
    public void dynamicLoading() {
        Assert.assertEquals(new PracticePage().open().startLoadingAndWait(), "Loading complete");
    }

    @Test(groups = "regression", description = "Dropdown, checkbox and file upload")
    public void formControls() {
        PracticePage page = new PracticePage().open().selectCountry("India");
        Assert.assertEquals(page.getSelectedCountry(), "India");
        Assert.assertTrue(page.acceptTerms(), "Checkbox should be selected");

        Path file = Paths.get("src/test/resources/testdata/upload-sample.txt").toAbsolutePath();
        Assert.assertEquals(page.uploadFile(file.toString()), "Uploaded: upload-sample.txt");
    }

    @Test(groups = "regression", description = "Read values from a web table")
    public void webTable() {
        PracticePage page = new PracticePage().open();
        Assert.assertEquals(page.getTableValue("Carlos", 3), "71000");
        Assert.assertEquals(page.countRowsWhere(2, "QA"), 2);
    }
}
