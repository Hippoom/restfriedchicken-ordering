package com.restfriedchicken.ordering.core;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    private List<Order> orders = new ArrayList<>();

    public void store(Order order) {
        orders.add(order);
    }

    public Optional<Order> findByTrackingId(String trackingId) {
        return orders.stream().filter(o -> o.getTrackingId().equals(trackingId)).distinct().findFirst();
    }

    public void clear() {
        this.orders.clear();
    }
}
