package com.restfriedchicken.smoke;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.restfriedchicken.env.EnvConfig;
import com.restfriedchicken.env.EnvVars;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {EnvConfig.class, EnvVars.class}
)
public class ApplicationAvailabilitySmokeTest {

    @Autowired
    private EnvVars env;

    @Before
    public void config_rest_assured() {
        RestAssured.baseURI = env.getOrderingServiceBaseUri();
        RestAssured.port = env.getOrderingServicePort();
    }


    @Test(timeout = 60000) // wait max 1min
    public void polls_until_app_is_ready() throws Throwable {

        Response response = null;
        while (true) {

            try {
                response = when().get("/meta").
                        then().log().everything().extract().response();

                break;
            } catch (Exception e) {
                continue;
            }

        }
        String responseBody = (response == null? "{}": response.asString());
        assertThat(JsonPath.read(responseBody, "$.version"), equalTo(env.getApplicationVersion()));


    }
}
