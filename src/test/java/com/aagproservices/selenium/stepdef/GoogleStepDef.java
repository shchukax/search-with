package com.aagproservices.selenium.stepdef;

import com.aagproservices.selenium.page.BasePage;
import com.aagproservices.selenium.page.GoogleHomepage;
import com.aagproservices.selenium.page.GoogleResultsPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class GoogleStepDef {
    private final static String HUB_URL = "http://selenium.vm:4444/wd/hub";
    private static WebDriver driver;

    private WebDriver getDriver() throws MalformedURLException {
        if (driver == null) {
            driver = new RemoteWebDriver(new URL(HUB_URL), new ChromeOptions());
        }
        return driver;
    }

    @Given("^I navigate to google home page$")
    public void navigateToGoogleHomePage() throws MalformedURLException {
        getDriver().navigate().to("https://www.google.com");
    }

    @When("I search for term {string}")
    public void searchForTerm(String term) throws MalformedURLException {
        GoogleHomepage googleHomepage = new GoogleHomepage(getDriver());
        googleHomepage.search(term);
    }

    @Then("I should see search results")
    public void shouldSeeSearchResults() throws MalformedURLException {
        GoogleResultsPage googleResultsPage = new GoogleResultsPage(getDriver());
        assertThat("Google search results not displayed", googleResultsPage.isDisplayed(), is(true) );
    }

    @And("Results page search field should contain {string}")
    public void resultsPageSearchFieldShouldContain(String term) throws MalformedURLException {
        GoogleResultsPage googleResultsPage = new GoogleResultsPage(getDriver());
        assertThat(
                "Incorrect search term displayed on results page",
                googleResultsPage.getSearchTermInField(),
                is(equalTo(term))
        );
    }

    @When("I use feel lucky feature with term {string}")
    public void useFeelLuckyFeatureWithTerm(String term) throws MalformedURLException {
        GoogleHomepage googleHomepage = new GoogleHomepage(getDriver());
        googleHomepage.feelingLucky(term);
    }

    @Then("I should see a non-google page")
    public void iShouldSeeANonGooglePage() throws MalformedURLException {
        BasePage page = new BasePage(getDriver());
        assertThat("Should not be on Google page", page.isGooglePage(), is(false));
    }
}
