package com.restfriedchicken.ordering.command;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderItem {
    private String name;
    private int quantity;
}
