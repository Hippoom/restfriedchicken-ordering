package com.restfriedchicken.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import com.restfriedchicken.env.EnvConfig;
import com.restfriedchicken.env.EnvVars;
import com.restfriedchicken.ordering.command.PlaceOrderCommand;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {
        EnvConfig.class,
        EnvVars.class,
        PlaceOrderSteps.HalConfiguration.class}
)
public class PlaceOrderSteps {

    @Autowired
    private LinkDiscoverer linkDiscoverer;

    @Autowired
    private EnvVars env;

    private ValidatableResponse response;
    private String trackingId;

    @Before
    public void setup_rest_assured() {

        RestAssured.baseURI = env.getOrderingServiceBaseUri();
        RestAssured.port = env.getOrderingServicePort();
    }

    @When("^I place an order with items:$")
    public void i_place_an_order_with(DataTable rawArgs) throws Throwable {

        trackingId = UUID.randomUUID().toString();

        List<PlaceOrderCommand.Item> items = rawArgs.asList(PlaceOrderCommand.Item.class);

        final PlaceOrderCommand command = new PlaceOrderCommand(
                trackingId, items);

        response = given().contentType(JSON).content(command).
                when().
                post("/orders").
                then().log().everything();

    }

    @Then("^it should accept the order$")
    public void it_should_accept_the_order() throws Throwable {
        response.statusCode(SC_CREATED);

        String responseBody = extractResponseBody();

        Link self = linkDiscoverer.findLinkWithRel("self", responseBody);

        assertThat(self.getHref(),
                equalTo(format("%s/orders/%s", env.orderingServiceUrl(), trackingId)));
    }

    @Then("^it should suggest making a payment$")
    public void it_should_suggest_making_a_payment() throws Throwable {
        String responseBody = extractResponseBody();

        Link payment = linkDiscoverer.findLinkWithRel("payment", responseBody);

        assertThat(payment.getHref(),
                equalTo(format("http://www.restfriedchicken.com/online-txn/%s", trackingId)));
    }

    private String extractResponseBody() {
        return response.extract().body().asString();
    }

    @Then("^it should suggest cancelling the order$")
    public void it_should_suggest_cancelling_the_order() throws Throwable {
        String responseBody = extractResponseBody();

        Link cancel = linkDiscoverer.findLinkWithRel("cancel", responseBody);

        assertThat(cancel.getHref(),
                equalTo(format("%s/orders/%s", env.orderingServiceUrl(), trackingId)));
    }

    @Configuration
    @EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
    static class HalConfiguration {

        @Autowired
        private RelProvider relProvider;

        @Bean
        public ObjectMapper halObjectMapper() {
            ObjectMapper halObjectMapper = new ObjectMapper();
            halObjectMapper.registerModule(new Jackson2HalModule());
            halObjectMapper
                    .setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(relProvider, null));
            return halObjectMapper;
        }
    }
}
