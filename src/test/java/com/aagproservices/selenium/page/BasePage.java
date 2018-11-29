package com.aagproservices.selenium.page;

import com.aagproservices.selenium.searchwith.FileBasedElementLocatorFactory;
import com.aagproservices.selenium.searchwith.SearchWithFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new SearchWithFieldDecorator(new FileBasedElementLocatorFactory(driver)), this);
    }

    public boolean isGooglePage() {
        try {
            URL url = new URL(driver.getCurrentUrl());
            return url.getHost().contains("google");
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
