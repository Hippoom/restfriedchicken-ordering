package com.restfriedchicken.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import com.restfriedchicken.env.EnvVars;
import com.restfriedchicken.ordering.command.PlaceOrderCommand;
import com.restfriedchicken.ordering.command.PlaceOrderCommandFixture;
import com.restfriedchicken.ordering.rest.OrderResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderPath {

    private LinkDiscoverer linkDiscoverer;

    private ObjectMapper halObjectMapper;

    private EnvVars env;

    private String trackingId;
    private ValidatableResponse currentResponse;
    private ValidatableResponse placeOrderResponse;
    private ValidatableResponse cancelOrderResponse;

    public OrderPath(EnvVars env, LinkDiscoverer linkDiscoverer, ObjectMapper halObjectMapper) {
        this.env = env;
        this.linkDiscoverer = linkDiscoverer;
        this.halObjectMapper = halObjectMapper;
        RestAssured.baseURI = env.getOrderingServiceBaseUri();
        RestAssured.port = env.getOrderingServicePort();
    }

    public OrderPath placeOrderWith(PlaceOrderCommand command) {
        trackingId = command.getTrackingId();
        placeOrderResponse = given().contentType(JSON).content(command).
                when().
                post("/orders").
                then().log().everything();
        setCurrentReponse(placeOrderResponse);
        return this;
    }

    public OrderPath placeOrder() {
        return placeOrderWith(new PlaceOrderCommandFixture().build());
    }

    public OrderPath assertPlaced() {
        placeOrderResponse.statusCode(SC_CREATED);

        Link self = linkDiscoverer.findLinkWithRel("self", getRepresentation());

        assertThat(self.getHref(),
                equalTo(format("%s/orders/%s", env.orderingServiceUrl(), trackingId)));
        return this;
    }

    public OrderPath cancelOrder() {
        cancelOrderResponse = given().when().
                delete("/orders/" + trackingId).
                then().log().everything();
        setCurrentReponse(cancelOrderResponse);
        return this;
    }

    public OrderPath assertCanceled() {
        cancelOrderResponse.statusCode(SC_OK);

        Link self = linkDiscoverer.findLinkWithRel("self", getRepresentation());

        assertThat(self.getHref(),
                equalTo(format("%s/orders/%s", env.orderingServiceUrl(), trackingId)));
        return this;
    }

    public String getRepresentation() {
        return currentResponse.extract().body().asString();
    }


    public OrderPath assertSuggestPayment() {

        Link payment = linkDiscoverer.findLinkWithRel("payment", getRepresentation());

        assertThat(payment.getHref(),
                equalTo(format("http://www.restfriedchicken.com/online-txn/%s", trackingId)));
        return this;
    }

    public OrderPath assertSuggestCancellation() {

        Link cancel = linkDiscoverer.findLinkWithRel("cancel", getRepresentation());

        assertThat(cancel.getHref(),
                equalTo(format("%s/orders/%s", env.orderingServiceUrl(), trackingId)));
        return this;
    }

    public OrderPath assertContainsSelfLinkeOnly() throws IOException {
        Link self = linkDiscoverer.findLinkWithRel("self", getRepresentation());

        assertThat(self, is(not(nullValue())));

        String representation = getRepresentation();

        OrderResource resource = halObjectMapper.readValue(representation, OrderResource.class);

        assertThat(resource.getLinks().size(), is(1));
        return this;
    }

    public void setCurrentReponse(ValidatableResponse currentReponse) {
        this.currentResponse = currentReponse;
    }
}
