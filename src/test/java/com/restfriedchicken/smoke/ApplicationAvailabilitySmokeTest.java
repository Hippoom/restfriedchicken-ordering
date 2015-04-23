package com.restfriedchicken.smoke;

import com.jayway.restassured.RestAssured;
import com.restfriedchicken.env.EnvConfig;
import com.restfriedchicken.env.EnvVars;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.ConnectException;

import static com.jayway.restassured.RestAssured.when;

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

        while(true) {
            try {
                when().get("/").
                        then().log().everything();
                break;
            } catch (Exception e) {
                continue;
            }
        }


    }
}
