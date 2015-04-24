package com.restfriedchicken.ordering.rest;

import com.restfriedchicken.ordering.core.Order;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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

        OrderResource resource = new OrderResource(model.getTrackingId(), model.getStatus().getCode(), toResources(model.getItems()));
        resource.add(orderResourceHref(model).withSelfRel());
        resource.add(orderResourceHref(model).withRel("cancel"));

        resource.add(new Link("http://www.restfriedchicken.com/online-txn/" + model.getTrackingId(), "payment"));


        return resource;
    }

    private ControllerLinkBuilder orderResourceHref(Order model) {
        return linkTo(methodOn(OrderingController.class).
                getByTrackingId(model.getTrackingId()));
    }

    private List<OrderResource.Item> toResources(List<Order.Item> models) {
        return models.stream().map(m -> new OrderResource.Item(m.getName(), m.getQuantity()))
                .collect(Collectors.toList());
    }
}
