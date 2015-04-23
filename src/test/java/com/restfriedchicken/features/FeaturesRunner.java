package com.restfriedchicken.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:"},
        glue = {"com.restfriedchicken.features"},
        plugin={"pretty", "html:build/cucumber"}
)
public class FeaturesRunner {
}
