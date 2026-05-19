package com.example.craft;

import com.example.craft.domain.Customer;
import com.example.craft.domain.CustomerType;
import com.example.craft.domain.Order;
import com.example.craft.domain.OrderItem;

/*
 * Deliberately bad-but-working starter code.
 *
 * It mixes validation, pricing, discount rules, delivery rules, payment,
 * persistence, notification and receipt generation in one class.
 */
public class OrderProcessor {

    public String process(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }

        if (order.getCustomer() == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Customer customer = order.getCustomer();

        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Customer email is invalid");
        }

        int subtotal = 0;
        for (OrderItem item : order.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be positive");
            }

            if (item.getUnitPricePence() <= 0) {
                throw new IllegalArgumentException("Item price must be positive");
            }

            subtotal = subtotal + item.getQuantity() * item.getUnitPricePence();
        }

        int discount = 0;
        if (customer.getType() == CustomerType.STUDENT) {
            discount = (int) (subtotal * 0.15);
        } else if (customer.getType() == CustomerType.PREMIUM) {
            discount = (int) (subtotal * 0.10);
        } else if (customer.getType() == CustomerType.STAFF) {
            discount = (int) (subtotal * 0.20);
        } else {
            discount = 0;
        }

        int deliveryFee = 0;
        if (order.getDeliveryType().equalsIgnoreCase("STANDARD")) {
            deliveryFee = 399;
        } else if (order.getDeliveryType().equalsIgnoreCase("NEXT_DAY")) {
            deliveryFee = 799;
        } else if (order.getDeliveryType().equalsIgnoreCase("COLLECTION")) {
            deliveryFee = 0;
        } else {
            throw new IllegalArgumentException("Unknown delivery type: " + order.getDeliveryType());
        }

        int total = subtotal - discount + deliveryFee;

        if (total <= 0) {
            throw new IllegalStateException("Order total must be positive");
        }

        if (order.getPaymentType().equalsIgnoreCase("CARD")) {
            System.out.println("Taking card payment for £" + formatPounds(total));
        } else if (order.getPaymentType().equalsIgnoreCase("PAYPAL")) {
            System.out.println("Taking PayPal payment for £" + formatPounds(total));
        } else if (order.getPaymentType().equalsIgnoreCase("BANK_TRANSFER")) {
            System.out.println("Creating bank transfer request for £" + formatPounds(total));
        } else {
            throw new IllegalArgumentException("Unknown payment type: " + order.getPaymentType());
        }

        System.out.println("Saving order " + order.getOrderId());
        System.out.println("Sending email to " + customer.getEmail());

        if (customer.getPhoneNumber() != null && customer.getPhoneNumber().startsWith("07")) {
            System.out.println("Sending SMS to " + customer.getPhoneNumber());
        }

        String receipt = "Receipt\n"
                + "-------\n"
                + "Order: " + order.getOrderId() + "\n"
                + "Customer: " + customer.getName() + "\n"
                + "Subtotal: £" + formatPounds(subtotal) + "\n"
                + "Discount: £" + formatPounds(discount) + "\n"
                + "Delivery: £" + formatPounds(deliveryFee) + "\n"
                + "Total: £" + formatPounds(total) + "\n";

        System.out.println(receipt);
        return receipt;
    }

    private String formatPounds(int pence) {
        return String.format("%.2f", pence / 100.0);
    }
}
