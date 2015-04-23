package com.restfriedchicken.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import com.restfriedchicken.env.EnvConfig;
import com.restfriedchicken.env.EnvVars;
import com.restfriedchicken.ordering.command.PlaceOrderCommand;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
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

    @When("^I place an order with$")
    public void i_place_an_order_with() throws Throwable {

        RestAssured.baseURI = env.getOrderingServiceBaseUri();
        RestAssured.port = env.getOrderingServicePort();

        trackingId = UUID.randomUUID().toString();
        final PlaceOrderCommand command = new PlaceOrderCommand(
                trackingId, new PlaceOrderCommand.Item("fried chicken", 1));

        response = given().contentType(JSON).content(command).
                when().
                post("/orders").
                then().log().everything();

    }

    @Then("^it should accept the order$")
    public void it_should_accept_the_order() throws Throwable {
        response.statusCode(SC_ACCEPTED);
    }

    @Then("^it should suggest making a payment or cancelling the order$")
    public void it_should_suggest_making_a_payment_or_cancelling_the_order() throws Throwable {
        String responseBody = response.extract().body().asString();

        Link payment = linkDiscoverer.findLinkWithRel("payment", responseBody);

        assertThat(payment.getHref(),
                equalTo(format("http://www.restfriedchicken.com/online-txn/%s", trackingId)));
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
