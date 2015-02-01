package com.restfriedchicken.ordering.core;

public class Order {
    private String trackingId;
    private Status status;

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
}
