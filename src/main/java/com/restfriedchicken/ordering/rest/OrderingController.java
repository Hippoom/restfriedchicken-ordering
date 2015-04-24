package com.restfriedchicken.ordering.rest;

import com.restfriedchicken.ordering.command.PlaceOrderCommand;
import com.restfriedchicken.ordering.core.Order;
import com.restfriedchicken.ordering.core.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/orders")
public class OrderingController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderResourceAssembler orderResourceAssembler;

    @RequestMapping(method = POST)
    @ResponseBody
    protected HttpEntity<OrderResource> placeOrder(@RequestBody PlaceOrderCommand command) {

        final String trackingId = command.getTrackingId();

        Order order = new Order(trackingId);
        for (PlaceOrderCommand.Item item: command.getItems()) {
            order.append(item.getName(), item.getQuantity());
        }

        orderRepository.store(order);

        final OrderResource orderResource = orderResourceAssembler.toResource(order);

        return new ResponseEntity<>(orderResource, CREATED);
    }

    @RequestMapping(value = "/{tracking_id}", method = GET)
    @ResponseBody
    protected HttpEntity<OrderResource> getByTrackingId(@PathVariable("tracking_id") String trackingId) {
        Optional<Order> order = orderRepository.findByTrackingId(trackingId);
        if (order.isPresent()) {
            final OrderResource orderResource = orderResourceAssembler.toResource(order.get());
            return new ResponseEntity<>(orderResource, OK);
        }
        return null;
    }
}
