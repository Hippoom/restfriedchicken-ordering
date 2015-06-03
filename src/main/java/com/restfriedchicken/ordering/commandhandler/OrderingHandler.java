package com.restfriedchicken.ordering.commandhandler;

import com.restfriedchicken.ordering.core.Order;
import com.restfriedchicken.ordering.core.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class OrderingHandler {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order cancel(String trackingId) {
        Optional<Order> order = orderRepository.findByTrackingId(trackingId);
        if (order.isPresent()) {
            order.get().cancel();
            order.get().getItems().size();//fire lazy loading
            return order.get();
        }
        return null;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
