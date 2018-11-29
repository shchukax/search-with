package com.aagproservices.selenium.page;

import com.aagproservices.selenium.searchwith.SearchWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GoogleHomepage extends BasePage {
    private final static String GOOGLE_HOME_PAGE = "GoogleHomepage";

    @FindBy(name="q")
    private WebElement searchField;

    @SearchWith(page=GOOGLE_HOME_PAGE, name="searchButton", locatorsFile="{locators.file}")
    private WebElement searchButton;

    @SearchWith(page=GOOGLE_HOME_PAGE, name="luckyButton", locatorsFile="{locators.file}")
    private WebElement luckyButton;

    public GoogleHomepage(WebDriver driver) {
        super(driver);
    }

    public void search(String term) {
        searchField.clear();
        searchField.sendKeys(term);
        searchButton.click();
    }

    public void feelingLucky(String term) {
        searchField.clear();
        searchField.sendKeys(term);
        luckyButton.click();
    }
}
