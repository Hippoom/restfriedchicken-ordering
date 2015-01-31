package com.restfriedchicken.ordering.command;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PlaceOrderCommand {
    @JsonProperty("tracking_id")
    private String trackingId;

    private List<OrderItem> items;

    public String getTrackingId() {
        return trackingId;
    }
}
