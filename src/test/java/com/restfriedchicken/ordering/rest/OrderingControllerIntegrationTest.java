package com.restfriedchicken.ordering.rest;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.restfriedchicken.ordering.Application;
import com.restfriedchicken.ordering.core.Order;
import com.restfriedchicken.ordering.core.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")//random port used
public class OrderingControllerIntegrationTest {
    @Value("${local.server.port}")
    private int port;

    protected String getBaseUri() {
        return RestAssured.DEFAULT_URI;
    }

    protected int getPort() {
        return port;
    }

    @Autowired
    private LinkDiscoverer linkDiscoverer;

    @Autowired
    private OrderRepository orderRepository;


    @Before
    public void config_rest_assured() {
        RestAssured.baseURI = getBaseUri();
        RestAssured.port = getPort();
    }

    @Before
    public void reset_test_doubles() {
        orderRepository.clear();
    }


    @Test
    public void should_returns_accepted_and_self_link_when_places_an_order() throws Exception {

        final String command = "{" +
                "\"tracking_id\": \"123456\"," +
                "\"items\": [{" +
                "\"name\": \"potato\"," +
                "\"quantity\": \"1\"" +
                "}]}";


        Response response = given().contentType(JSON).content(command).
                when().
                post("/orders").
                then().log().everything().
                statusCode(SC_ACCEPTED)
                .extract().response();

        String responseBody = response.asString();

        Link self = linkDiscoverer.findLinkWithRel("self", responseBody);
        Link payment = linkDiscoverer.findLinkWithRel("payment", responseBody);

        assertThat(self.getHref(),
                equalTo(getResourceUri("/orders/123456")));


        assertThat(payment.getHref(),
                equalTo("http://www.restfriedchicken.com/online-txn/123456"));
    }

    @Test
    public void should_returns_ok_and_resource_when_gets_an_order() throws Exception {

        Order order = new Order("123456");
        order.item("Fried Chicken", 1);
        orderRepository.store(order);

        Response response = given().contentType(JSON).
                when().
                get("/orders/" + order.getTrackingId()).
                then().log().everything().
                statusCode(SC_OK)
                .extract().response();

        String responseBody = response.asString();

        Link self = linkDiscoverer.findLinkWithRel("self", responseBody);
        Link payment = linkDiscoverer.findLinkWithRel("payment", responseBody);

        assertThat(self.getHref(),
                equalTo(getResourceUri("/orders/" + order.getTrackingId())));


        assertThat(payment.getHref(),
                equalTo("http://www.restfriedchicken.com/online-txn/" + order.getTrackingId()));

        assertThat(JsonPath.read(responseBody, "$.tracking_id"),
                equalTo(order.getTrackingId()));

        assertThat(JsonPath.read(responseBody, "$.status"),
                equalTo(order.getStatus().getCode()));

        final Order.Item item = order.getItems().stream().findFirst().get();

        assertThat(JsonPath.read(responseBody, "$.items[0].name"),
                equalTo(item.getName()));
        assertThat(JsonPath.read(responseBody, "$.items[0].quantity"),
                equalTo(item.getQuantity()));
    }

    private String getResourceUri(String path) {
        return getBaseUri() + ":" + getPort() + path;
    }

}
