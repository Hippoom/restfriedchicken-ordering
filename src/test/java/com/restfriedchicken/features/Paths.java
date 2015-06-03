package com.restfriedchicken.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfriedchicken.env.EnvVars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.LinkDiscoverer;

@Configuration
public class Paths {

    @Autowired
    @Bean
    public OrderPath placeOrderPath(LinkDiscoverer linkDiscoverer, EnvVars env, ObjectMapper halObjectMapper) {
        return new OrderPath(env, linkDiscoverer, halObjectMapper);
    }
}
