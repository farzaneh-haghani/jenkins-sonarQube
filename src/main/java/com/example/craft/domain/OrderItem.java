package com.example.craft.domain;

public class OrderItem {
    private final String name;
    private final int quantity;
    private final int unitPricePence;

    public OrderItem(String name, int quantity, int unitPricePence) {
        this.name = name;
        this.quantity = quantity;
        this.unitPricePence = unitPricePence;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getUnitPricePence() { return unitPricePence; }
}
