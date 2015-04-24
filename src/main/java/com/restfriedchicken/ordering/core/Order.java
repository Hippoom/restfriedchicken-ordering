package com.restfriedchicken.ordering.core;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String trackingId;
    private Status status;
    private List<Item> items = new ArrayList<>();

    public Order(String trackingId) {
        this.trackingId = trackingId;
        this.status = Status.WAIT_PAYMENT;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Status getStatus() {
        return status;
    }

    public void append(String name, int quantity) {
        this.items.add(new Item(name, quantity));
    }

    public List<Item> getItems() {
        return items;
    }


    public enum Status {
        WAIT_PAYMENT("WAIT_PAYMENT");

        private String code;

        Status(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public class Item {
        private String name;
        private int quantity;

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
