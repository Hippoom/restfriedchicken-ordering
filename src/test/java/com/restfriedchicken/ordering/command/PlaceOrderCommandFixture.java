package com.restfriedchicken.ordering.command;

import cucumber.api.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaceOrderCommandFixture {

    private String trackingId = UUID.randomUUID().toString();
    private List<PlaceOrderCommand.Item> items = defaultItems();

    private List<PlaceOrderCommand.Item> defaultItems() {
        List<PlaceOrderCommand.Item> items = new ArrayList<>();
        items.add(new PlaceOrderCommand.Item("Fried Chicken", 1));
        items.add(new PlaceOrderCommand.Item("Fried Octopus", 2));
        return items;
    }

    public PlaceOrderCommandFixture withItems(DataTable items) {
        this.items = items.asList(PlaceOrderCommand.Item.class);
        return this;
    }

    public PlaceOrderCommand build() {
        return new PlaceOrderCommand(
                trackingId, items);
    }
}
