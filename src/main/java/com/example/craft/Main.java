package com.example.craft;

import com.example.craft.domain.Customer;
import com.example.craft.domain.CustomerType;
import com.example.craft.domain.Order;
import com.example.craft.domain.OrderItem;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer(
                "Ada Lovelace",
                "ada@example.com",
                "07123456789",
                CustomerType.STUDENT
        );

        Order order = new Order("ORD-1001", customer, "NEXT_DAY", "CARD");
        order.addItem(new OrderItem("Keyboard", 1, 4999));
        order.addItem(new OrderItem("Mouse", 2, 1299));

        OrderProcessor processor = new OrderProcessor();
        processor.process(order);
    }
}
