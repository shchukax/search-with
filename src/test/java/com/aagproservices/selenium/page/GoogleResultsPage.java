package com.aagproservices.selenium.page;

import com.aagproservices.selenium.searchwith.FileBasedElementLocatorFactory;
import com.aagproservices.selenium.searchwith.SearchWith;
import com.aagproservices.selenium.searchwith.SearchWithFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class GoogleResultsPage extends BasePage {
    private final static String GOOGLE_RESULTS_PAGE = "GoogleResultsPage";

    @FindBy(name="q")
    private WebElement searchField;

    @SearchWith(page=GOOGLE_RESULTS_PAGE, name="searchButton", locatorsFile="{locators.file}")
    private WebElement searchButton;

    public GoogleResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            URL currentUrl = new URL(driver.getCurrentUrl());
            return currentUrl.getFile().startsWith("/search?");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public String getSearchTermInField() {
        return searchField.getAttribute("value");
    }

    public void search(String term) {
        searchField.clear();
        searchField.sendKeys(term);
        searchButton.click();
    }
}
