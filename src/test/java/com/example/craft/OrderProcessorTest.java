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

    // @Test
    // void throws_exception_when_order_is_null() {
    // IllegalArgumentException exception =
    // assertThrows(IllegalArgumentException.class, () -> processor.process(null));
    // assertEquals("Order must not be null", exception.getMessage());
    // }

    @Test
    void throws_exception_for_invalid_customer_email() {
        Customer customer = new Customer("Ada", "invalid-email", "07123456789", CustomerType.STANDARD);
        Order order = new Order("ORD-2", customer, "STANDARD", "CARD");
        order.addItem(new OrderItem("Pen", 1, 500));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> processor.process(order));
        assertEquals("Customer email is invalid", exception.getMessage());
    }

    @Test
    void throws_exception_for_unknown_delivery_type() {
        Customer customer = new Customer("Ada", "ada@example.com", "07123456789", CustomerType.STANDARD);
        Order order = new Order("ORD-3", customer, "SPACE_SHIP", "CARD");
        order.addItem(new OrderItem("Notebook", 1, 500));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> processor.process(order));
        assertEquals("Unknown delivery type: SPACE_SHIP", exception.getMessage());
    }

    @Test
    void throws_exception_for_unknown_payment_type() {
        Customer customer = new Customer("Ada", "ada@example.com", "07123456789", CustomerType.STANDARD);
        Order order = new Order("ORD-4", customer, "STANDARD", "CASH");
        order.addItem(new OrderItem("Notebook", 1, 500));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> processor.process(order));
        assertEquals("Unknown payment type: CASH", exception.getMessage());
    }

    @Test
    void premium_customer_with_many_items_gets_extra_discount() {
        Customer customer = new Customer("Ada", "ada@example.com", "07123456789", CustomerType.PREMIUM);
        Order order = new Order("ORD-5", customer, "NEXT_DAY", "CARD");
        order.addItem(new OrderItem("Sticker", 3, 399));
        order.addItem(new OrderItem("Mug", 4, 1599));

        String receipt = processor.process(order);

        assertTrue(receipt.contains("Discount: £10.59"));
        assertTrue(receipt.contains("Delivery: £7.99"));
        assertTrue(receipt.contains("Total: £73.33"));
    }

    @Test
    void collection_order_without_phone_number_has_zero_delivery() {
        Customer customer = new Customer("Ada", "ada@example.com", null, CustomerType.STANDARD);
        Order order = new Order("ORD-6", customer, "COLLECTION", "BANK_TRANSFER");
        order.addItem(new OrderItem("Poster", 1, 1500));

        String receipt = processor.process(order);

        assertTrue(receipt.contains("Delivery: £0.00"));
        assertTrue(receipt.contains("Total: £15.00"));
    }

    @Test
    void bank_transfer_low_value_order_receipt_total() {
        Customer customer = new Customer("Ada", "ada@example.com", "07123456789", CustomerType.STANDARD);
        Order order = new Order("ORD-7", customer, "STANDARD", "BANK_TRANSFER");
        order.addItem(new OrderItem("Sticker", 1, 300));

        String receipt = processor.process(order);

        assertTrue(receipt.contains("Delivery: £3.99"));
        assertTrue(receipt.contains("Total: £6.99"));
    }
}
