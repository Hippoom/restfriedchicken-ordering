package com.restfriedchicken.ordering.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderResource extends ResourceSupport {
    @JsonProperty("tracking_id")
    private String trackingId;

    private String status;

    private List<Item> items;

    public OrderResource(String trackingId, String status, List<Item> items) {
        this.trackingId = trackingId;
        this.status = status;
        this.items = items;
    }

    //for frameworks only
    private OrderResource() {

    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class Item {
        private String name;
        private int quantity;

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        // for frameworks only
        private Item() {

        }
    }
}
