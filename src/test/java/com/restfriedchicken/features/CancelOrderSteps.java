package com.restfriedchicken.features;

import com.jayway.restassured.RestAssured;
import com.restfriedchicken.env.EnvConfig;
import com.restfriedchicken.env.EnvVars;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.jayway.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {
        EnvConfig.class,
        EnvVars.class,
        HalConfiguration.class,
        Paths.class}
)
public class CancelOrderSteps {

    @Autowired
    private LinkDiscoverer linkDiscoverer;

    @Autowired
    private EnvVars env;
    @Autowired
    private OrderPath orderPath;

    @Before
    public void setup_rest_assured() {

        RestAssured.baseURI = env.getOrderingServiceBaseUri();
        RestAssured.port = env.getOrderingServicePort();
    }

    @Given("^I placed an order$")
    public void i_placed_an_order() throws Throwable {
        orderPath.placeOrder().assertPlaced();
    }

    @When("^I cancel the order$")
    public void i_cancel_the_order() throws Throwable {
        orderPath.cancelOrder();
    }

    @Then("^it should accept the cancellation$")
    public void it_should_accept_the_cancellation() throws Throwable {
        orderPath.assertCanceled();
    }

    @Then("^it should suggest no more further steps$")
    public void it_should_suggest_no_more_further_steps() throws Throwable {
        orderPath.assertContainsSelfLinkeOnly();
    }
}
