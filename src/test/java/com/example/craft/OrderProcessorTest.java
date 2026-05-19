package com.example.craft;

import com.example.craft.domain.Customer;
import com.example.craft.domain.CustomerType;
import com.example.craft.domain.Order;
import com.example.craft.domain.OrderItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessorTest {

    private final OrderProcessor processor = new OrderProcessor();

    @Test
    void student_customer_gets_discount_and_receipt_contains_total() {
        Customer customer = new Customer("Ada", "ada@example.com", "07123456789", CustomerType.STUDENT);
        Order order = new Order("ORD-1", customer, "STANDARD", "CARD");
        order.addItem(new OrderItem("Book", 1, 1000));

        String receipt = processor.process(order);

        assertTrue(receipt.contains("Order: ORD-1"));
        assertTrue(receipt.contains("Discount: £1.50"));
        assertTrue(receipt.contains("Delivery: £3.99"));
        assertTrue(receipt.contains("Total: £12.49"));
    }

    @Test
    void empty_order_is_rejected() {
        Customer customer = new Customer("Ada", "ada@example.com", null, CustomerType.STANDARD);
        Order order = new Order("ORD-2", customer, "STANDARD", "CARD");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> processor.process(order)
        );

        assertEquals("Order must contain at least one item", ex.getMessage());
    }

    @Test
    void invalid_email_is_rejected() {
        Customer customer = new Customer("Ada", "not-an-email", null, CustomerType.STANDARD);
        Order order = new Order("ORD-3", customer, "STANDARD", "CARD");
        order.addItem(new OrderItem("Book", 1, 1000));

        assertThrows(IllegalArgumentException.class, () -> processor.process(order));
    }

    @Test
    void unknown_delivery_type_is_rejected() {
        Customer customer = new Customer("Ada", "ada@example.com", null, CustomerType.STANDARD);
        Order order = new Order("ORD-4", customer, "DRONE", "CARD");
        order.addItem(new OrderItem("Book", 1, 1000));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> processor.process(order)
        );

        assertTrue(ex.getMessage().contains("Unknown delivery type"));
    }
}
