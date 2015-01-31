package com.restfriedchicken.ordering.rest;

import com.restfriedchicken.ordering.command.PlaceOrderCommand;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/order")
public class OrderResource {

    @RequestMapping(method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    protected HttpEntity<OrderRepresentation> placeOrder(@RequestBody PlaceOrderCommand command) {

        final String trackingId = command.getTrackingId();

        final OrderRepresentation orderRep = new OrderRepresentation(trackingId, "WAIT_PAYMENT");

        orderRep.add(linkTo(methodOn(OrderResource.class).
                getByTrackingId(trackingId)).withSelfRel());

        orderRep.add(new Link("http://www.restfriedchicken.com/online-txn/" + trackingId, "payment").
                expand("method", "POST"));

        return new ResponseEntity<>(orderRep, ACCEPTED);
    }

    @RequestMapping(value = "/{tracking_id}", method = GET,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    protected HttpEntity<OrderRepresentation> getByTrackingId(@PathVariable("tracking_id") String trackingId) {
        return null;
    }
}
