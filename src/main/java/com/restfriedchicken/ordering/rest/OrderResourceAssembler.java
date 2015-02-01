package com.restfriedchicken.ordering.rest;

import com.restfriedchicken.ordering.core.Order;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OrderResourceAssembler extends ResourceAssemblerSupport<Order, OrderResource> {

    public OrderResourceAssembler() {
        super(OrderingController.class, OrderResource.class);
    }

    @Override
    public OrderResource toResource(Order model) {

        OrderResource resource = new OrderResource(model.getTrackingId(), model.getStatus().getCode(), to_resource(model.getItems()));
        resource.add(linkTo(methodOn(OrderingController.class).
                getByTrackingId(model.getTrackingId())).withSelfRel());

        resource.add(new Link("http://www.restfriedchicken.com/online-txn/" + model.getTrackingId(), "payment").
                expand("method", "POST"));
        return resource;
    }

    private List<OrderResource.Item> to_resource(List<Order.Item> models) {
        return models.stream().map(m -> new OrderResource.Item(m.getName(), m.getQuantity()))
                .collect(Collectors.toList());
    }
}
