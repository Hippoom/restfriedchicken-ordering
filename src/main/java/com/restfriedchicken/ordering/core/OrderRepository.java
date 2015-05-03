package com.restfriedchicken.ordering.core;

import java.util.Optional;

/**
 * Created by twer on 5/3/15.
 */
public interface OrderRepository {
    void store(Order order);

    Optional<Order> findByTrackingId(String trackingId);
}
