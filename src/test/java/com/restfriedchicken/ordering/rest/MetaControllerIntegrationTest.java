package com.restfriedchicken.ordering.rest;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.restfriedchicken.ordering.Application;
import com.restfriedchicken.ordering.meta.MetaStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")//random port used
public class MetaControllerIntegrationTest {
    @Value("${local.server.port}")
    private int port;

    protected String getBaseUri() {
        return RestAssured.DEFAULT_URI;
    }

    protected int getPort() {
        return port;
    }

    @Autowired
    private MetaStore metaStore;

    @Before
    public void config_rest_assured() {
        RestAssured.baseURI = getBaseUri();
        RestAssured.port = getPort();
    }

    @DirtiesContext
    @Before
    public void config_test_meta_store() {
        metaStore.setLocation("classpath:test_meta.json");
    }


    @Test
    public void should_returns_version() throws Exception {

        Response response = given().
                when().
                get("/meta").
                then().log().everything().
                statusCode(SC_OK)
                .extract().response();

        String responseBody = response.asString();

        assertThat(JsonPath.read(responseBody, "$.version"), equalTo("1.0"));
    }


}
