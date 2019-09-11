package com.aagproservices.selenium.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features",
        glue="com.aagproservices.selenium.stepdef"
)
public class TestRunner {
}
