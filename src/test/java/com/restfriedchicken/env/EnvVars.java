package com.restfriedchicken.env;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@PropertySource(value = "classpath:env.properties", ignoreResourceNotFound = true)
public class EnvVars {
    @Value("${orderingServiceHost}")
    private String orderingServiceHost;
    @Value("${orderingServicePort}")
    private int orderingServicePort;
    @Value("${orderingServiceVersion}")
    private String applicationVersion;

    public String orderingServiceUrl() {
        return format("http://%s:%d", orderingServiceHost, orderingServicePort);
    }

    public String getOrderingServiceBaseUri() {
        return format("http://%s", orderingServiceHost);
    }

    public int getOrderingServicePort() {
        return orderingServicePort;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }
}
