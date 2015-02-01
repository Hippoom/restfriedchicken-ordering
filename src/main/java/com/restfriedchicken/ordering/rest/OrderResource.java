package com.restfriedchicken.ordering.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderResource extends ResourceSupport {
    @JsonProperty("tracking_id")
    private String trackingId;

    private String status;


    public OrderResource(String trackingId, String status) {
        this.trackingId = trackingId;
        this.status = status;
    }
}
