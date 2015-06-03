package com.restfriedchicken.features;

import com.restfriedchicken.env.EnvConfig;
import com.restfriedchicken.env.EnvVars;
import com.restfriedchicken.ordering.command.PlaceOrderCommand;
import com.restfriedchicken.ordering.command.PlaceOrderCommandFixture;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {
        EnvConfig.class,
        EnvVars.class,
        HalConfiguration.class,
        Paths.class}
)
public class PlaceOrderSteps {

    @Autowired
    private OrderPath placeOrderPath;

    @Before
    public void setup_rest_assured() {

    }

    @When("^I place an order with items:$")
    public void i_place_an_order_with(DataTable rawArgs) throws Throwable {

        final PlaceOrderCommand command = new PlaceOrderCommandFixture().
                withItems(rawArgs).build();

        placeOrderPath.placeOrderWith(command);

    }

    @Then("^it should accept the order$")
    public void it_should_accept_the_order() throws Throwable {
        placeOrderPath.assertPlaced();
    }

    @Then("^it should suggest making a payment$")
    public void it_should_suggest_making_a_payment() throws Throwable {
        placeOrderPath.assertSuggestPayment();

    }

    @Then("^it should suggest cancelling the order$")
    public void it_should_suggest_cancelling_the_order() throws Throwable {
        placeOrderPath.assertSuggestCancellation();
    }

}
