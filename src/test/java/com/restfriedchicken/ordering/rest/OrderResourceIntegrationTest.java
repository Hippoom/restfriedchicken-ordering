package com.restfriedchicken.ordering.rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.restfriedchicken.ordering.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.restfriedchicken.ordering.rest.HateoasUtils.selfLinkOf;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")//random port used
public class OrderResourceIntegrationTest {
    @Value("${local.server.port}")
    private int port;

    protected String getBaseUri() {
        return RestAssured.DEFAULT_URI;
    }

    protected int getPort() {
        return port;
    }

    @Before
    public void config_rest_assured() {
        RestAssured.baseURI = getBaseUri();
        RestAssured.port = getPort();
    }


    @Test
    public void should_returns_status_code_with_accepted_and_self_link() throws Exception {

        final String command = "{" +
                "\"tracking_id\": \"123456\"," +
                "\"items\": [{" +
                "\"name\": \"potato\"," +
                "\"quantity\": \"1\"" +
                "}]}";

        Response response = given().contentType(JSON).content(command).
                when().
                post("/order").
                then().log().everything().
                statusCode(SC_ACCEPTED)
                .extract().response();

        String json = response.asString();

        assertThat(selfLinkOf(json).get(),
                equalTo(getResourceUri("/order/123456")));
    }

    private String getResourceUri(String path) {
        return getBaseUri() + ":" + getPort() + path;
    }

}
