package com.restfriedchicken.ordering.command;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PlaceOrderCommand {

    @JsonProperty("tracking_id")
    private String trackingId;

    private List<Item> items;

    public PlaceOrderCommand(String trackingId, Item... items) {
        this(trackingId, Arrays.asList(items));
    }

    public PlaceOrderCommand(String trackingId, List<Item> items) {
        this.trackingId = trackingId;
        this.items = items;
    }

    //for frameworks only
    private PlaceOrderCommand() {

    }

    public String getTrackingId() {
        return trackingId;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class Item {
        private String name;
        private int quantity;

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        //for frameworks only
        private Item() {
        }
    }
}
