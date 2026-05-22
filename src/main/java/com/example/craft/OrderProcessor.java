package com.example.craft;

import com.example.craft.discount.DiscountStrategy;
import com.example.craft.discount.DiscountStrategyFactory;
import com.example.craft.domain.Customer;
import com.example.craft.domain.CustomerType;
import com.example.craft.domain.Order;
import com.example.craft.domain.OrderItem;

public class OrderProcessor {

    private final DiscountStrategyFactory discountStrategyFactory = new DiscountStrategyFactory();

    public String process(Order order) {
        validateOrder(order);

        int subtotal = calculateSubtotal(order);
        int discount = calculateDiscount(order.getCustomer(), order, subtotal);

        int deliveryFee = 0;
        Customer customer = order.getCustomer();

        if (order.getDeliveryType().equalsIgnoreCase("STANDARD")) {
            deliveryFee = 399;

            if (subtotal > 5000) {
                deliveryFee = 0;
            }

            System.out.println("Standard delivery selected");
        } else if (order.getDeliveryType().equalsIgnoreCase("NEXT_DAY")) {
            deliveryFee = 799;

            if (subtotal > 15000) {
                deliveryFee = 499;
            }

            System.out.println("Next day delivery selected");
        } else if (order.getDeliveryType().equalsIgnoreCase("COLLECTION")) {
            deliveryFee = 0;

            if (customer.getPhoneNumber() == null) {
                System.out.println("Collection selected but no phone number was provided");
            }

            System.out.println("Collection selected");
        } else {
            throw new IllegalArgumentException("Unknown delivery type: " + order.getDeliveryType());
        }

        int total = subtotal - discount + deliveryFee;

        if (total <= 0) {
            throw new IllegalStateException("Order total must be positive");
        }

        if (order.getPaymentType().equalsIgnoreCase("CARD")) {
            System.out.println("Taking card payment for £" + formatPounds(total));

            if (total > 100000) {
                System.out.println("Large card payment requires manual review");
            }
        } else if (order.getPaymentType().equalsIgnoreCase("PAYPAL")) {
            System.out.println("Taking PayPal payment for £" + formatPounds(total));

            if (customer.getEmail().endsWith("@example.com")) {
                System.out.println("PayPal payment using test-like email address");
            }
        } else if (order.getPaymentType().equalsIgnoreCase("BANK_TRANSFER")) {
            System.out.println("Creating bank transfer request for £" + formatPounds(total));

            if (total < 1000) {
                System.out.println("Bank transfer for low value order may not be worth processing");
            }
        } else {
            throw new IllegalArgumentException("Unknown payment type: " + order.getPaymentType());
        }

        System.out.println("Saving order " + order.getOrderId());
        System.out.println("Saving order " + order.getOrderId() + " for customer " + customer.getName());

        System.out.println("Sending email to " + customer.getEmail());
        System.out.println("Dear " + customer.getName() + ", your order has been processed.");
        System.out.println("Order " + order.getOrderId() + " total was £" + formatPounds(total));

        if (customer.getPhoneNumber() != null && customer.getPhoneNumber().startsWith("07")) {
            System.out.println("Sending SMS to " + customer.getPhoneNumber());
            System.out.println("Order " + order.getOrderId() + " confirmed by SMS");
        }

        if (customer.getType() == CustomerType.PREMIUM && total > 5000) {
            System.out.println("Sending premium customer follow-up email");
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

    private boolean isLargeOrder(int total) {
        return total > 50000;
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }

        if (order.getCustomer() == null) {
            throw new IllegalArgumentException("Order must have a customer");
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        Customer customer = order.getCustomer();

        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Customer must have a name");
        }

        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Customer email is invalid");
        }

        if (customer.getType() == null) {
            throw new IllegalArgumentException("Customer must have a type");
        }

        for (OrderItem item : order.getItems()) {
            if (item == null) {
                throw new IllegalArgumentException("Order item must not be null");
            }

            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be positive");
            }

            if (item.getUnitPricePence() <= 0) {
                throw new IllegalArgumentException("Item price must be positive");
            }
        }

        if (order.getDeliveryType() == null || order.getDeliveryType().isBlank()) {
            throw new IllegalArgumentException("Order must have a delivery type");
        }

        if (order.getPaymentType() == null || order.getPaymentType().isBlank()) {
            throw new IllegalArgumentException("Order must have a payment type");
        }

        for (OrderItem item : order.getItems()) {
            if (item == null) {
                throw new IllegalArgumentException("Order items must not be null");
            }

            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Order item must have a positive quantity");
            }

            if (item.getUnitPricePence() <= 0) {
                throw new IllegalArgumentException("Order item must have a positive unit price");
            }
        }

        // if (order.getDeliveryType() == null || order.getDeliveryType().isBlank()) {
        // throw new IllegalArgumentException("Order must have a delivery type");
        // }

        // if (order.getPaymentType() == null || order.getPaymentType().isBlank()) {
        // throw new IllegalArgumentException("Order must have a payment type");
        // }
    }

    private int calculateSubtotal(Order order) {
        int subtotal = 0;

        for (OrderItem item : order.getItems()) {
            subtotal = subtotal + item.getQuantity() * item.getUnitPricePence();
        }
        return subtotal;
    }

    private int calculateDiscount(Customer customer, Order order, int subtotal) {
        DiscountStrategy strategy = discountStrategyFactory.getStrategy(customer.getType());
        return strategy.calculateDiscount(order, subtotal);
    }
}