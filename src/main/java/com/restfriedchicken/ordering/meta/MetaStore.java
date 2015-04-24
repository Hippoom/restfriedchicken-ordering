package com.restfriedchicken.ordering.meta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfriedchicken.ordering.rest.ApplicationMeta;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.lang.String.format;

@Component
public class MetaStore implements ApplicationContextAware {
    @Autowired
    private ObjectMapper objectMapper;
    private ApplicationContext applicationContext;
    private String location = "classpath:meta.json";

    public void setLocation(String location) {
        this.location = location;
    }

    public ApplicationMeta load() {
        try {
            Resource resource = applicationContext.getResource(location);
            return objectMapper.readValue(resource.getInputStream(), ApplicationMeta.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(format("Cannot load meta from %s", location));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
