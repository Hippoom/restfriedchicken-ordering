package com.restfriedchicken.ordering.rest;

import org.junit.Test;

import static com.restfriedchicken.ordering.rest.HateoasUtils.selfLinkOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HateoasUtilsTest {


    @Test
    public void should_return_self_ref() {
        final String json = "{" +
                "\"links\": [" +
                "{" +
                "\"rel\": \"self\"," +
                "\"href\": \"http://localhost:50733/order\"" +
                "}" +
                "]" +
                "}";

        assertThat(selfLinkOf(json, "href"), equalTo("http://localhost:50733/order"));
    }

}
