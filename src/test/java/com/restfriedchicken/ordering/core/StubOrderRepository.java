package com.restfriedchicken.ordering.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StubOrderRepository implements OrderRepository {

    private List<Order> orders = new ArrayList<>();

    @Override
    public void store(Order order) {
        orders.add(order);
    }

    @Override
    public Optional<Order> findByTrackingId(String trackingId) {
        return orders.stream().filter(o -> o.getTrackingId().equals(trackingId)).distinct().findFirst();
    }

    public void clear() {
        this.orders.clear();
    }
}
